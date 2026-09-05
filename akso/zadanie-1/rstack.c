#include "rstack.h"

#include <assert.h>
#include <ctype.h>
#include <errno.h>
#include <inttypes.h>
#include <stdio.h>
#include <stdlib.h>

typedef enum {
  RSTACK_ITEM_VALUE,
  RSTACK_ITEM_CHILD
} rstack_item_type_t;

typedef struct rstack_item rstack_item_t;

// Element listy reprezentujacej zawartosc jednego stosu.
struct rstack_item {
  rstack_item_type_t type;
  rstack_item_t *prev; // Poprzedni element blizej dna stosu.
  rstack_item_t *next; // Nastepny element blizej wierzcholka.

  union {
    uint64_t value; // Liczba odlozona bezposrednio na stos.
    rstack_t *child; // Referencja do innego stosu.
  } data;
};

// Jeden wierzcholek grafu rekurencyjnych stosow.
struct rstack {
  rstack_item_t *bottom; // Pierwszy element, od ktorego zaczyna sie zapis.
  rstack_item_t *top; // Ostatni element, z ktorego korzysta `pop` i `front`.

  size_t ref_count; // Wszystkie referencje do stosu.
  size_t external_refs; // Tylko uchwyty trzymane jeszcze przez uzytkownika.

  unsigned long mark_token; // Znacznik uzywany przez faze mark w GC.
  unsigned long active_token; // Stos jest na biezacej sciezce DFS.
  unsigned long front_done_token; // Wynik `front` juz policzono.
  result_t front_cached_result; // Zapamietany wynik `front`.

  rstack_t *registered_prev; // Poprzedni stos na globalnej liscie.
  rstack_t *registered_next; // Nastepny stos na globalnej liscie.
};

typedef enum {
  WRITE_STATUS_OK,
  WRITE_STATUS_CYCLE,
  WRITE_STATUS_ERROR
} write_status_t;

// Globalny stan biblioteki potrzebny do GC i tokenow DFS.
typedef struct {
  rstack_t *registered_head; // Poczatek listy wszystkich stosow.
  unsigned long token_counter; // Kolejny numer przejscia dla DFS i GC.
} rstack_manager_t;

// Globalny rejestr wszystkich stosow. Sam refcount nie wystarcza
// do usuwania cykli, wiec GC musi przejsc po calym grafie obiektow.
static rstack_manager_t library_manager;

static rstack_item_t *allocate_value_item(uint64_t value);
static rstack_item_t *allocate_child_item(rstack_t *child);
static void attach_item_to_top(rstack_t *rs, rstack_item_t *item);
static rstack_item_t *detach_top_item(rstack_t *rs);
static void free_item(rstack_item_t *item);

static void register_stack(rstack_t *rs);
static void unregister_stack(rstack_t *rs);

static void reset_traversal_tokens(void);
static unsigned long acquire_traversal_token(void);

static void decrement_ref_count(rstack_t *rs);
static void decrement_external_reference(rstack_t *rs);

static void mark_reachable_stack(rstack_t *rs, unsigned long token);
static void mark_reachable_roots(unsigned long token);
static void drop_dead_edges(unsigned long token);
static void free_stack_items(rstack_t *rs);
static void sweep_dead_stacks(unsigned long token);
static void collect_unreachable_stacks(void);

static result_t make_missing_result(void);
static result_t make_value_result(uint64_t value);
static result_t find_front_value(rstack_t *rs, unsigned long token);

static write_status_t write_stack(FILE *file, rstack_t *rs,
                                  unsigned long token);
static int get_io_errno(void);
static int close_stream(FILE *file);

static int update_decimal_value(uint64_t *value, int digit);
static rstack_t *fail_read(FILE *file, rstack_t *rs, int error_code);
static bool is_decimal_digit(int character);

// `push` alokuje nowy element przed zmiana stosu.
// Gdy alokacja sie nie uda, zawartosc stosu pozostaje bez zmian.
static rstack_item_t *allocate_value_item(uint64_t value) {
  rstack_item_t *item = malloc(sizeof(*item));

  if (item == nullptr) {
    return nullptr;
  }

  item->type = RSTACK_ITEM_VALUE;
  item->prev = nullptr;
  item->next = nullptr;
  item->data.value = value;

  return item;
}

// Tworzy element przechowujacy referencje do innego stosu.
static rstack_item_t *allocate_child_item(rstack_t *child) {
  rstack_item_t *item = malloc(sizeof(*item));

  if (item == nullptr) {
    return nullptr;
  }

  item->type = RSTACK_ITEM_CHILD;
  item->prev = nullptr;
  item->next = nullptr;
  item->data.child = child;

  return item;
}

// Dopina nowy element na wierzch stosu.
static void attach_item_to_top(rstack_t *rs, rstack_item_t *item) {
  if (rs->top == nullptr) {
    rs->bottom = item;
    rs->top = item;
    return;
  }

  item->prev = rs->top;
  rs->top->next = item;
  rs->top = item;
}

// Odpina wierzcholek stosu i zwraca go bez zwalniania pamieci.
static rstack_item_t *detach_top_item(rstack_t *rs) {
  rstack_item_t *item = rs->top;

  if (item == nullptr) {
    return nullptr;
  }

  rs->top = item->prev;
  if (rs->top == nullptr) {
    rs->bottom = nullptr;
  }
  else {
    rs->top->next = nullptr;
  }

  item->prev = nullptr;
  item->next = nullptr;

  return item;
}

static void free_item(rstack_item_t *item) {
  free(item);
}

// Wstawia stos do globalnego rejestru wszystkich istniejacych stosow.
static void register_stack(rstack_t *rs) {
  rs->registered_prev = nullptr;
  rs->registered_next = library_manager.registered_head;

  if (library_manager.registered_head != nullptr) {
    library_manager.registered_head->registered_prev = rs;
  }

  library_manager.registered_head = rs;
}

// Usuwa stos z globalnego rejestru.
static void unregister_stack(rstack_t *rs) {
  if (rs->registered_prev != nullptr) {
    rs->registered_prev->registered_next = rs->registered_next;
  }
  else {
    library_manager.registered_head = rs->registered_next;
  }

  if (rs->registered_next != nullptr) {
    rs->registered_next->registered_prev = rs->registered_prev;
  }

  rs->registered_prev = nullptr;
  rs->registered_next = nullptr;
}

// Kolejne przejscia DFS dostaja rozne tokeny.
// Pola znacznikow sa zerowane dopiero po przepelnieniu licznika.
static void reset_traversal_tokens(void) {
  for (rstack_t *current = library_manager.registered_head;
       current != nullptr;
       current = current->registered_next) {
    current->mark_token = 0;
    current->active_token = 0;
    current->front_done_token = 0;
  }
}

// Zwraca nowy token dla kolejnego przejscia DFS lub GC.
static unsigned long acquire_traversal_token(void) {
  library_manager.token_counter++;

  if (library_manager.token_counter == 0) {
    reset_traversal_tokens();
    library_manager.token_counter = 1;
  }

  return library_manager.token_counter;
}

// `ref_count` obejmuje wszystkie krawedzie i uchwyty prowadzace do stosu.
static void decrement_ref_count(rstack_t *rs) {
  assert(rs->ref_count > 0);
  rs->ref_count--;
}

// Usuwa jedna referencje zewnetrzna i aktualizuje pelny licznik.
static void decrement_external_reference(rstack_t *rs) {
  assert(rs->external_refs > 0);

  rs->external_refs--;
  decrement_ref_count(rs);
}

// DFS po grafie stosow uzywany przez faze mark.
static void mark_reachable_stack(rstack_t *rs, unsigned long token) {
  if (rs == nullptr || rs->mark_token == token) {
    return;
  }

  rs->mark_token = token;

  for (rstack_item_t *item = rs->bottom; item != nullptr; item = item->next) {
    if (item->type == RSTACK_ITEM_CHILD) {
      mark_reachable_stack(item->data.child, token);
    }
  }
}

// Uruchamia faze mark od wszystkich korzeni zewnetrznych.
static void mark_reachable_roots(unsigned long token) {
  // Korzeniami sa tylko stosy, do ktorych uzytkownik nadal trzyma uchwyt.
  for (rstack_t *current = library_manager.registered_head;
       current != nullptr;
       current = current->registered_next) {
    if (current->external_refs > 0) {
      mark_reachable_stack(current, token);
    }
  }
}

// Aktualizuje liczniki referencji wychodzacych z martwych stosow.
// Ta faza jest wykonywana przed sweepem.
static void drop_dead_edges(unsigned long token) {
  for (rstack_t *current = library_manager.registered_head;
       current != nullptr;
       current = current->registered_next) {
    if (current->mark_token == token) {
      continue;
    }

    // Usuniecie martwego stosu usuwa tez jego krawedzie, wiec dzieci
    // musza stracic odpowiadajace im referencje.
    for (rstack_item_t *item = current->bottom; item != nullptr;
         item = item->next) {
      if (item->type == RSTACK_ITEM_CHILD) {
        decrement_ref_count(item->data.child);
      }
    }
  }
}

// Zwalnia wszystkie elementy nalezace do jednego stosu.
static void free_stack_items(rstack_t *rs) {
  rstack_item_t *item = rs->bottom;

  while (item != nullptr) {
    rstack_item_t *next_item = item->next;

    free_item(item);
    item = next_item;
  }

  rs->bottom = nullptr;
  rs->top = nullptr;
}

// Faza sweep zwalnia wylacznie stosy nieosiagalne od korzeni.
static void sweep_dead_stacks(unsigned long token) {
  rstack_t *current = library_manager.registered_head;

  while (current != nullptr) {
    rstack_t *next_stack = current->registered_next;

    if (current->mark_token != token) {
      unregister_stack(current);
      free_stack_items(current);
      free(current);
    }

    current = next_stack;
  }
}

// Oznacza stosy osiagalne od zewnetrznych uchwytow i zwalnia pozostale.
static void collect_unreachable_stacks(void) {
  unsigned long token = acquire_traversal_token();

  mark_reachable_roots(token);
  drop_dead_edges(token);
  sweep_dead_stacks(token);
}

static result_t make_missing_result(void) {
  return (result_t) {
    .flag = false,
    .value = 0
  };
}

static result_t make_value_result(uint64_t value) {
  return (result_t) {
    .flag = true,
    .value = value
  };
}

// Dla jednego tokenu wynik `front` dla danego stosu jest liczony raz
// i potem zwracany z zapamietanego pola wyniku.
static result_t find_front_value(rstack_t *rs, unsigned long token) {
  if (rs == nullptr) {
    return make_missing_result();
  }

  // `front_done_token` oznacza, ze wynik tego stosu zostal juz policzony
  // dla biezacego zapytania i moze zostac zwrocony od razu.
  if (rs->front_done_token == token) {
    return rs->front_cached_result;
  }

  // Ponowne wejscie do stosu aktywnego na tej sciezce DFS oznacza cykl.
  // W `front` taka galaz daje brak wyniku.
  if (rs->active_token == token) {
    return make_missing_result();
  }

  rs->active_token = token;

  // Petla idzie od wierzcholka ku dnu.
  // Pierwsza znaleziona liczba albo pierwszy wynik dziecka konczy szukanie.
  for (rstack_item_t *item = rs->top; item != nullptr; item = item->prev) {
    if (item->type == RSTACK_ITEM_VALUE) {
      rs->front_cached_result = make_value_result(item->data.value);
      rs->front_done_token = token;
      rs->active_token = 0;
      return rs->front_cached_result;
    }

    result_t child_result = find_front_value(item->data.child, token);

    if (child_result.flag) {
      rs->front_cached_result = child_result;
      rs->front_done_token = token;
      rs->active_token = 0;
      return rs->front_cached_result;
    }
  }

  rs->front_cached_result = make_missing_result();
  rs->front_done_token = token;
  rs->active_token = 0;
  return rs->front_cached_result;
}

// Zapisuje zawartosc stosu od dna do gory.
// Dla elementu-dziecka najpierw zapisuje caly wskazywany stos.
static write_status_t write_stack(FILE *file, rstack_t *rs,
                                  unsigned long token) {
  if (rs->active_token == token) {
    // Ponowne wejscie do aktywnego stosu oznacza cykl.
    return WRITE_STATUS_CYCLE;
  }

  rs->active_token = token;

  for (rstack_item_t *item = rs->bottom; item != nullptr; item = item->next) {
    if (item->type == RSTACK_ITEM_VALUE) {
      if (fprintf(file, "%" PRIu64 "\n", item->data.value) < 0) {
        if (errno == 0) {
          errno = EIO;
        }

        rs->active_token = 0;
        return WRITE_STATUS_ERROR;
      }

      continue;
    }

    write_status_t child_status = write_stack(file, item->data.child, token);

    if (child_status != WRITE_STATUS_OK) {
      rs->active_token = 0;
      return child_status;
    }
  }

  rs->active_token = 0;
  return WRITE_STATUS_OK;
}

// Zwraca kod bledu I/O, nawet jesli strumien nie ustawil `errno`.
static int get_io_errno(void) {
  if (errno != 0) {
    return errno;
  }

  return EIO;
}

// Zamyka plik i mapuje blad zamkniecia na kod zgodny z API.
static int close_stream(FILE *file) {
  if (fclose(file) == 0) {
    return 0;
  }

  return get_io_errno();
}

// Sprawdza, czy dopisanie kolejnej cyfry miesci sie w `uint64_t`.
static int update_decimal_value(uint64_t *value, int digit) {
  uint64_t digit_value = (uint64_t)digit;
  uint64_t limit = UINT64_MAX / 10;
  uint64_t remainder = UINT64_MAX % 10;

  if (*value > limit || (*value == limit && digit_value > remainder)) {
    errno = ERANGE;
    return -1;
  }

  *value = *value * 10 + digit_value;
  return 0;
}

// Zamyka plik, usuwa czesciowo zbudowany stos i ustawia koncowe `errno`.
static rstack_t *fail_read(FILE *file, rstack_t *rs, int error_code) {
  if (file != nullptr) {
    fclose(file);
  }

  if (rs != nullptr) {
    rstack_delete(rs);
  }

  errno = error_code;
  return nullptr;
}

static bool is_decimal_digit(int character) {
  return isdigit((unsigned char)character) != 0;
}

rstack_t *rstack_new() {
  rstack_t *rs = malloc(sizeof(*rs));

  if (rs == nullptr) {
    errno = ENOMEM;
    return nullptr;
  }

  rs->bottom = nullptr;
  rs->top = nullptr;
  rs->ref_count = 1;
  rs->external_refs = 1;
  rs->mark_token = 0;
  rs->active_token = 0;
  rs->front_done_token = 0;
  rs->front_cached_result = make_missing_result();
  rs->registered_prev = nullptr;
  rs->registered_next = nullptr;

  // Nowy stos od razu trafia do globalnego rejestru wszystkich stosow.
  register_stack(rs);

  return rs;
}

void rstack_delete(rstack_t *rs) {
  if (rs == nullptr) {
    return;
  }

  // Po usunieciu zewnetrznej referencji czesc grafu moze stac sie martwa.
  decrement_external_reference(rs);
  collect_unreachable_stacks();
}

int rstack_push_value(rstack_t *rs, uint64_t value) {
  if (rs == nullptr) {
    errno = EINVAL;
    return -1;
  }

  rstack_item_t *item = allocate_value_item(value);

  if (item == nullptr) {
    errno = ENOMEM;
    return -1;
  }

  attach_item_to_top(rs, item);
  return 0;
}

int rstack_push_rstack(rstack_t *rs1, rstack_t *rs2) {
  if (rs1 == nullptr || rs2 == nullptr) {
    errno = EINVAL;
    return -1;
  }

  rstack_item_t *item = allocate_child_item(rs2);

  if (item == nullptr) {
    errno = ENOMEM;
    return -1;
  }

  attach_item_to_top(rs1, item);
  rs2->ref_count++;

  return 0;
}

void rstack_pop(rstack_t *rs) {
  if (rs == nullptr || rs->top == nullptr) {
    return;
  }

  rstack_item_t *item = detach_top_item(rs);

  if (item->type == RSTACK_ITEM_CHILD) {
    decrement_ref_count(item->data.child);
  }

  free_item(item);
  // Po zdjeciu wierzcholka czesc grafu moze przestac byc osiagalna.
  collect_unreachable_stacks();
}

bool rstack_empty(rstack_t *rs) {
  if (rs == nullptr) {
    return true;
  }

  // Stos jest pusty rekurencyjnie wtedy, gdy `front` nie znajduje liczby.
  return !find_front_value(rs, acquire_traversal_token()).flag;
}

result_t rstack_front(rstack_t *rs) {
  if (rs == nullptr) {
    return make_missing_result();
  }

  return find_front_value(rs, acquire_traversal_token());
}

rstack_t *rstack_read(char const *path) {
  if (path == nullptr) {
    errno = EINVAL;
    return nullptr;
  }

  FILE *file = fopen(path, "r");

  if (file == nullptr) {
    return nullptr;
  }

  rstack_t *rs = rstack_new();

  if (rs == nullptr) {
    int error_code = errno;

    fclose(file);
    errno = error_code;
    return nullptr;
  }

  bool in_number = false;
  uint64_t value = 0;

  // Petla czyta plik znak po znaku.
  // Cyfry buduja liczbe, bialy znak ja konczy, a inny znak oznacza blad.
  while (true) {
    int character = fgetc(file);

    if (character == EOF) {
      break;
    }

    if (isspace((unsigned char)character) != 0) {
      if (in_number) {
        // Konczy biezaca liczbe i odklada ja na stos.
        if (rstack_push_value(rs, value) != 0) {
          return fail_read(file, rs, errno);
        }

        in_number = false;
        value = 0;
      }

      continue;
    }

    if (!is_decimal_digit(character)) {
      return fail_read(file, rs, EINVAL);
    }

    if (!in_number) {
      in_number = true;
      value = 0;
    }

    if (update_decimal_value(&value, character - '0') != 0) {
      return fail_read(file, rs, errno);
    }
  }

  if (ferror(file)) {
    return fail_read(file, rs, get_io_errno());
  }

  if (in_number && rstack_push_value(rs, value) != 0) {
    return fail_read(file, rs, errno);
  }

  int close_error = close_stream(file);

  if (close_error != 0) {
    return fail_read(nullptr, rs, close_error);
  }

  return rs;
}

int rstack_write(char const *path, rstack_t *rs) {
  if (path == nullptr || rs == nullptr) {
    errno = EINVAL;
    return -1;
  }

  FILE *file = fopen(path, "w");

  if (file == nullptr) {
    return -1;
  }

  // Cykl przerywa dalszy zapis, ale nie zamienia operacji w blad I/O.
  write_status_t write_status =
      write_stack(file, rs, acquire_traversal_token());

  if (write_status == WRITE_STATUS_ERROR) {
    int error_code = get_io_errno();

    fclose(file);
    errno = error_code;
    return -1;
  }

  int close_error = close_stream(file);

  if (close_error != 0) {
    errno = close_error;
    return -1;
  }

  return 0;
}
