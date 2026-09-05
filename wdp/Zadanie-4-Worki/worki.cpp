#include "worki.h"

/** Struktura bucketa **/
/* Kazdy bucket opisuje liste rzeczy lezacych bezposrednio:
 * - albo na biurku (owner == nullptr),
 * - albo w konkretnym worku (owner != nullptr).
 * Zawiera on swojego ojca, liste przedmiotow i workow, liste bukctetow (do
 * usuniecia). */
struct przedmiot::Bucket {
    przedmiot *items_head;
    worek *owner, *bags_head;
    Bucket *next_all;
};
typedef przedmiot::Bucket Bucket;

/** Globalny stan. **/
static int next_bag_idx = 0;            // numer nastepnego worka
static int total_items = 0;             // ile przedmiotow utworzono
static przedmiot *all_items = nullptr;  // wszystkie przedmioty (do usuniecia)
static worek *all_bags = nullptr;       // wszystkie worki (do usuniecia)
static Bucket *desk_bucket = nullptr;   // aktualny bucket biurka
static Bucket *all_buckets = nullptr;   // wszystkie buckety (do usuniecia)

/** Pomocnicze funkcje. **/
/* Stworz nowy bucket. */
static Bucket *new_bucket(worek *owner) {
    Bucket *b = new Bucket{};
    b->owner = owner;
    b->items_head = nullptr;
    b->bags_head = nullptr;
    b->next_all = all_buckets;
    all_buckets = b;
    return b;
}

/* Zadbaj o to zeby istnial bucket biurka. */
static void ensure_initialized() {
    if (desk_bucket != nullptr) return;
    desk_bucket = new_bucket(nullptr);
}

/* Dodaj przedmiot do bucketa. */
static void attach_item(przedmiot *p, Bucket *b) {
    p->parent = b;
    p->prev = nullptr;
    p->next = b->items_head;
    if (b->items_head != nullptr) b->items_head->prev = p;
    b->items_head = p;
}

/* Zabierz przedmiot z miejsca gdzie sie znajduje. */
static void detach_item(przedmiot *p) {
    Bucket *b = p->parent;
    if (p->prev != nullptr)
        p->prev->next = p->next;
    else
        b->items_head = p->next;
    if (p->next != nullptr) p->next->prev = p->prev;
    p->prev = p->next = nullptr;
    p->parent = nullptr;
}

/* Dodaj worek do bucketa. */
static void attach_bag(worek *w, Bucket *b) {
    w->parent = b;
    w->prev = nullptr;
    w->next = b->bags_head;
    if (b->bags_head != nullptr) b->bags_head->prev = w;
    b->bags_head = w;
}

/* Zabierz worek z miejsca gdzie sie znajduje. */
static void detach_bag(worek *w) {
    Bucket *b = w->parent;
    if (w->prev != nullptr)
        w->prev->next = w->next;
    else
        b->bags_head = w->next;
    if (w->next != nullptr) w->next->prev = w->prev;
    w->prev = w->next = nullptr;
    w->parent = nullptr;
}

/** Funkcje z tresci zadania. **/
przedmiot *nowy_przedmiot() {
    ensure_initialized();
    przedmiot *p = new przedmiot{};
    p->next_all = all_items;
    all_items = p;
    total_items++;
    attach_item(p, desk_bucket);
    return p;
}

worek *nowy_worek() {
    ensure_initialized();
    worek *w = new worek{};
    w->idx = next_bag_idx++;
    w->item_count = 0;
    w->contents = new_bucket(w);
    w->next_all = all_bags;
    all_bags = w;
    attach_bag(w, desk_bucket);
    return w;
}

void wloz(przedmiot *co, worek *gdzie) {
    detach_item(co);
    attach_item(co, gdzie->contents);
    gdzie->item_count++;
}

void wloz(worek *co, worek *gdzie) {
    detach_bag(co);
    attach_bag(co, gdzie->contents);
    gdzie->item_count += co->item_count;
}

void wyjmij(przedmiot *p) {
    worek *owner = p->parent->owner;
    detach_item(p);
    attach_item(p, desk_bucket);
    owner->item_count--;
}

void wyjmij(worek *w) {
    worek *owner = w->parent->owner;
    detach_bag(w);
    attach_bag(w, desk_bucket);
    owner->item_count -= w->item_count;
}

int w_ktorym_worku(przedmiot *p) {
    worek *owner = p->parent->owner;
    return owner == nullptr ? -1 : owner->idx;
}

int w_ktorym_worku(worek *w) {
    worek *owner = w->parent->owner;
    return owner == nullptr ? -1 : owner->idx;
}

int ile_przedmiotow(worek *w) { return w->item_count; }

void na_odwrot(worek *w) {
    detach_bag(w);
    Bucket *old_desk_bucket = desk_bucket, *old_contents = w->contents;
    int old_count = w->item_count;
    desk_bucket = old_contents;
    desk_bucket->owner = nullptr;
    w->contents = old_desk_bucket;
    w->contents->owner = w;
    attach_bag(w, desk_bucket);
    w->item_count = total_items - old_count;
}

/* Kasujemy kolejno wszystkie przedmioty, worki i buckety. */
void gotowe() {
    while (all_items != nullptr) {
        przedmiot *p = all_items;
        all_items = p->next_all;
        delete p;
    }
    while (all_bags != nullptr) {
        worek *w = all_bags;
        all_bags = w->next_all;
        delete w;
    }
    while (all_buckets != nullptr) {
        Bucket *b = all_buckets;
        all_buckets = b->next_all;
        delete b;
    }
    desk_bucket = nullptr;
    next_bag_idx = total_items = 0;
}

/*

g++ @opcjeCpp main.cpp worki.cpp -o main.e
valgrind --tool=memcheck --leak-check=yes ./main.e

g++ -o interactor worki.cpp interactor.cpp
g++ @opcjeCpp -o interactor worki.cpp interactor.cpp
toster --io tiny interactor
toster --io small interactor
toster --io medium interactor
toster --io big interactor

g++ @opcjeCpp -g -o interactor worki.cpp interactor.cpp
chmod +x vg.sh
toster --io big ./vg.sh


*/
