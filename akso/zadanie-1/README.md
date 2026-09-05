# 01 — Rekurencyjne stosy

[← All AKSO assignments](../README.md)

**Recursive stacks · C23**

A shared library for stacks containing unsigned integers or references to other stacks. It supports shared and cyclic structures, recursive lookup, and reading and writing numbers in text files.

## Build and run

From this assignment directory, on Linux with GNU Make, GCC supporting C23, and GNU binutils:

```sh
make
make check
```

`make` builds `librstack.so`. `make check` builds the provided example program and runs its seven cases, including allocation-failure handling, in a temporary directory. It compares the generated files with the included expected outputs, leaving those fixtures intact.

To run one example directly:

```sh
make example
./rstack_example one
```

The available cases are `zero`, `one`, `two`, `three`, `four`, `five`, and `memory`. A successful case exits with status 0. Direct runs write `file_*.out` in the current directory; prefer `make check` to preserve the expected fixtures. Use `make clean` to remove compiled files.

## Files

| File | Purpose |
| --- | --- |
| `rstack.c` | Selected library implementation |
| `rstack.h` | Assignment-provided public interface |
| `memory_tests.c`, `memory_tests.h` | Assignment-provided allocation-failure test support |
| `rstack_example.c` | Assignment-provided example tests |
| `file_four.in`, `file_*.out` | Example input and expected file outputs |
| `Makefile`, `run-examples.sh` | Build and example verification |

## Task description

Transcribed from the saved Moodle assignment page. The specification is preserved in Polish; course-page navigation and personal submission information are omitted.

**Otwarto:** piątek, 27 marca 2026, 00:00  
**Wymagane do:** poniedziałek, 20 kwietnia 2026, 23:59

### Rekurencyjne stosy

Zadanie polega na zaimplementowaniu w języku C dynamicznie ładowanej biblioteki obsługującej rekurencyjne stosy, nazywane dalej w skrócie stosami. Elementami stosu są liczby całkowite z zakresu reprezentowanego przez typ `uint64_t` lub inne stosy. Stos identyfikowany jest przez referencję do struktury reprezentującej ten stos. W języku C referencję implementujemy za pomocą wskaźnika i licznika odwołań do tego wskaźnika. Tuż po utworzeniu stosu licznik referencji ma wartość jeden. Stos może być odłożony na wielu stosach. Odłożenie stosu na stos nie powoduje jego skopiowania, a tylko odłożenie referencji do stosu. Zmiany zawartości stosu są widoczne wszędzie, gdzie odłożona jest referencja do tego stosu. Każde odłożenie stosu na stos zwiększa o jeden wartość licznika referencji odkładanego stosu. Każde zdjęcie stosu ze stosu zmniejsza o jeden wartość licznika referencji zdejmowanego stosu. Kasowanie stosu polega na zmniejszeniu o jeden wartości licznika referencji. Stos jest usuwany z pamięci, gdy wartość licznika referencji zmniejszyła się do zera. Struktura stosów może tworzyć cykle.

### Interfejs biblioteki

Interfejs biblioteki znajduje się w załączonym do treści zadania pliku [rstack.h](rstack.h). Dodatkowe szczegóły działania biblioteki należy wywnioskować z załączonych poniżej przykładów użycia.

#### Tworzenie nowego pustego stosu

```c
rstack_t* rstack_new();
```

Wynik funkcji:

- wskaźnik na strukturę reprezentującą stos;
- `nullptr` - jeśli wystąpił błąd przydzielania pamięci; funkcja ustawia wtedy `errno` odpowiednio na `ENOMEM`.

#### Kasowanie stosu

```c
void rstack_delete(rstack_t *rs);
```

Parametr `rs` to wskaźnik na strukturę reprezentującą usuwany stos.

Jeśli wartością `rs` jest `nullptr`, funkcja nic nie robi. Po skasowaniu stosu nie należy używać wskaźnika `rs`.

#### Odkładanie liczby na stos

```c
int rstack_push_value(rstack_t *rs, uint64_t value);
```

Parametry funkcji:

- `rs` - wskaźnik na strukturę reprezentującą stos;
- `value` - liczba odkładana na stos.

Wynik funkcji:

- `0` - jeśli operacja zakończyła się sukcesem;
- `-1` - jeśli wskaźnik `rs` ma wartość `nullptr` lub wystąpił błąd przydzielania pamięci; funkcja ustawia wtedy `errno` odpowiednio na `EINVAL` lub `ENOMEM`.

#### Odkładanie stosu na stos

```c
int rstack_push_rstack(rstack_t *rs1, rstack_t *rs2);
```

Parametry funkcji:

- `rs1` - wskaźnik na strukturę reprezentującą stos, na który odkładany jest stos;
- `rs2` - wskaźnik na strukturę reprezentującą stos, który jest odkładany.

Wynik funkcji:

- `0` - jeśli operacja zakończyła się sukcesem;
- `-1` - jeśli wskaźnik `rs1` lub `rs2` ma wartość `nullptr` lub wystąpił błąd przydzielania pamięci; funkcja ustawia wtedy `errno` odpowiednio na `EINVAL` lub `ENOMEM`.

#### Zdejmowanie nierekurencyjnie wierzchołka stosu

```c
void rstack_pop(rstack_t *rs);
```

Parametr `rs` to wskaźnik na strukturę reprezentującą stos.

Jeśli wartością `rs` jest `nullptr` lub stos jest pusty, funkcja nic nie robi.

#### Sprawdzanie rekurencyjnie, czy stos zawiera liczbę

```c
bool rstack_empty(rstack_t *rs);
```

Parametr `rs` to wskaźnik na strukturę reprezentującą stos.

Wynik funkcji:

- `true` - jeśli wskaźnik `rs` ma wartość `nullptr` lub stos nie zawiera liczby;
- `false` - jeśli stos zawiera liczbę.

#### Znajdowanie rekurencyjnie liczby najbliższej wierzchołkowi stosu

```c
result_t rstack_front(rstack_t *rs);
```

gdzie:

```c
typedef struct {
  bool     flag;  // To pole mówi, czy pole value zawiera wynik.
  uint64_t value; // W tym polu jest właściwy wynik.
} result_t;
```

Parametr `rs` to wskaźnik na strukturę reprezentującą stos.

Wynik funkcji:

- `flag == true` oznacza, że pole `value` zawiera znalezioną liczbę;
- `flag == false` oznacza, że wskaźnik `rs` ma wartość `nullptr`, stos jest pusty lub nie ma takiej liczby.

#### Tworzenie nowego stosu z liczb podanych w pliku

```c
rstack_t* rstack_read(char const *path);
```

Parametr `path` to nazwa pliku, ścieżka wskazująca plik. Na nowym stosie odkładane są kolejno liczby podane w pliku.

Wynik funkcji:

- wskaźnik na strukturę reprezentującą stos;
- `nullptr` - jeśli wskaźnik `path` ma wartość `nullptr` lub wystąpił błąd; funkcja ustawia wtedy odpowiednio `errno`; dobranie wartości `errno` jest częścią zadania.

Liczby w pliku podane są w zapisie przy podstawie 10. Liczby w pliku oddzielone są białymi znakami. Między dwoma liczbami może być więcej niż jeden biały znak. Na początku i końcu pliku może być dowolna liczba białych znaków. Funkcja dokładnie sprawdza poprawność zawartości pliku.

#### Zapisywanie do pliku liczb odłożonych na stosie

```c
int rstack_write(char const *path, rstack_t *rs);
```

Parametry funkcji:

- `path` - nazwa pliku, ścieżka wskazująca plik;
- `rs` - wskaźnik na strukturę reprezentującą stos.

Wynik funkcji:

- `0` - jeśli operacja zakończyła się sukcesem;
- `-1` - jeśli wskaźnik `path` lub `rs` ma wartość `nullptr` albo jeśli wystąpił błąd; funkcja ustawia wtedy odpowiednio `errno`; dobranie wartości `errno` jest częścią zadania.

Każda liczba zapisywana jest w osobnej linii w reprezentacji przy podstawie 10 bez zer wiodących. Każda linia kończy się znakiem nowej linii. Nie ma innych białych znaków. Jeśli przy zapisywaniu zostanie wykryty cykl, zapisywanie zostaje przerwane.

### Wymagania funkcjonalne i formalne

Biblioteka powinna zapewniać silną gwarancję odporności na niepowodzenie przydzielania pamięci, czyli nie powinna gubić pamięci, a obserwowalny stan wszystkich struktur danych nie powinien się zmienić.

Jako rozwiązanie zadania należy wstawić w Moodle archiwum zawierające plik `rstack.c` oraz opcjonalnie inne pliki `*.h` i `*.c` z implementacją biblioteki, oraz plik `makefile` lub `Makefile`. Archiwum nie powinno zawierać innych plików ani podkatalogów, w szczególności nie powinno zawierać plików binarnych. Do stworzenia archiwum należy użyć programu `zip`, `rar`, `7z` lub pary programów `tar` i `gzip`. Archiwum powinno mieć odpowiednio rozszerzenie `.zip`, `.rar`, `.7z` lub `.tgz` i format zgodny z tym rozszerzeniem. Po rozpakowaniu archiwum wszystkie pliki powinny się znaleźć we wspólnym podkatalogu.

Dostarczony w rozwiązaniu plik `makefile` lub `Makefile` powinien zawierać cel `librstack.so`, tak aby polecenie `make librstack.so` uruchamiało kompilowanie biblioteki i aby w bieżącym katalogu powstał plik `librstack.so`. Polecenie to powinno również kompilować i dołączać do biblioteki załączony do treści zadania plik `memory_tests.c`. Należy opisać zależności między plikami i zapewnić, że kompilowane są tylko pliki, które zostały zmienione lub pliki, które od nich zależą. Wywołanie `make clean` powinno usuwać wszystkie pliki utworzone przez polecenie `make`. Plik `makefile` lub `Makefile` powinien zawierać pseudocel `.PHONY`. Może zawierać też inne cele, na przykład cel kompilujący i linkujący z biblioteką dołączony do treści zadania przykład jej użycia bądź cel uruchamiający testy.

Do kompilowania należy użyć `gcc`. Biblioteka powinna się kompilować w laboratorium komputerowym pod Linuksem. Pliki źródłowe z implementacją biblioteki należy kompilować z opcjami:

```text
-Wall -Wextra -Wno-implicit-fallthrough -std=gnu23 -fPIC -O2
```

Aby umożliwić diagnozowanie problemów z zarządzaniem pamięcią, pliki powstałe w wyniku kompilowania plików źródłowych biblioteki należy linkować z opcjami:

```text
-shared -Wl,--wrap=malloc -Wl,--wrap=calloc -Wl,--wrap=realloc -Wl,--wrap=reallocarray -Wl,--wrap=free -Wl,--wrap=strdup -Wl,--wrap=strndup
```

Opcje `-Wl,--wrap=` sprawiają, że wywołania funkcji `malloc`, `calloc` itd. są przechwytywane odpowiednio przez funkcje `__wrap_malloc`, `__wrap_calloc` itd. Funkcje przechwytujące są zaimplementowane w załączonym do treści zadania pliku `memory_tests.c`.

Poprawność implementacji będzie sprawdzana za pomocą programu `valgrind`.

Implementacja nie może zawierać sztucznych ograniczeń na rozmiar przechowywanych danych - jedynymi ograniczeniami są rozmiar pamięci dostępnej w komputerze i rozmiar słowa maszynowego użytego komputera.

### Wskazówki

Warto zapoznać się z:

- [Nagłówek `stdint.h`](https://en.cppreference.com/w/c/header/stdint.html)
- [Nagłówek `inttypes.h`](https://en.cppreference.com/w/c/header/inttypes.html)
- [Funkcja `strtoul`](https://en.cppreference.com/w/c/string/byte/strtoul)
- [Funkcja `strtoimax`](https://en.cppreference.com/w/c/string/byte/strtoimax)

### Ocena

Za w pełni poprawne rozwiązanie zadania implementujące wszystkie wymagania można zdobyć 20 punktów, z tego 14 punktów zostanie wystawionych na podstawie testów automatycznych, a 6 punktów to ocena jakości kodu. Za problemy ze skompilowaniem rozwiązania lub niespełnienie wymogów formalnych można stracić wszystkie punkty. Za ostrzeżenia wypisywane przez kompilator może być odjęte do 2 punktów. Wystawienie oceny może być uzależnione od rozmowy z prowadzącym.

Rozwiązania należy implementować samodzielnie pod rygorem niezaliczenia przedmiotu. Zarówno korzystanie z cudzego kodu, jak i prywatne lub publiczne udostępnianie własnego kodu jest zabronione.

### Załączniki

Załącznikami do treści zadania są następujące pliki:

- [rstack.h](rstack.h) - deklaracja interfejsu biblioteki;
- [memory_tests.c](memory_tests.c) - implementacja modułu biblioteki służącego do testowania reakcji implementacji na niepowodzenie przydzielania pamięci;
- [memory_tests.h](memory_tests.h) - deklaracja interfejsu modułu biblioteki służącego do testowania reakcji implementacji na niepowodzenie przydzielania pamięci;
- [rstack_example.c](rstack_example.c) - przykładowe testy biblioteki;
- `*.in`, `*.out` - dane wejściowe i wyjściowe dla powyższych przykładów.

Nie wolno modyfikować pliku `rstack.h`. Zastrzegamy sobie możliwość zmiany testów oraz zawartości plików `memory_tests.h` i `memory_tests.c` podczas testowania rozwiązań.
