#ifndef WORKI_H
#define WORKI_H

struct przedmiot {
    struct Bucket;  		// wewnetrzny typ zdefiniowany w worki.cpp
    przedmiot *prev, *next;	// poprzedni i nastepny przedmiot w tym samym bucket
    przedmiot *next_all;	// lista wszystkich przedmiotow (do usuniecia)
    Bucket *parent;			// gdzie lezy bezposrednio (biurko lub worek)
};

struct worek {
    int idx, item_count;  			// numer worka, ile przedmiotow w srodku
    worek *prev, *next;   			// poprzedni i nastepny worek w tym samym bucket
    worek *next_all;      			// lista wszystkich workow (do usuniecia)
    przedmiot::Bucket *parent;  	// gdzie lezy bezposrednio (biurko lub inny worek)
    przedmiot::Bucket *contents;	// zawartosc worka
};

// Nowy przedmiot na biurku
przedmiot *nowy_przedmiot();

// Nowy worek na biurku; otrzymuje kolejny numer, począwszy od 0.
worek *nowy_worek();

// Wkłada przedmiot co do worka gdzie.
// Założenie: co i gdzie leżą na biurku.
void wloz(przedmiot *co, worek *gdzie);

// Wkłada worek co do worka gdzie.
// Założenie: co i gdzie leżą na biurku.
void wloz(worek *co, worek *gdzie);

// Wyjmuje przedmiot p z worka i kładzie na biurku.
// Założenie: Przedmiot p był w worku leżącym na biurku.
void wyjmij(przedmiot *p);

// Wyjmuje worek w z worka i kładzie na biurku.
// Założenie: Worek w był w worku leżącym na biurku.
void wyjmij(worek *w);

// Wynik: numer worka, w którym znajduje się przedmiot p (-1 jeśli na biurku).
int w_ktorym_worku(przedmiot *p);

// Wynik: numer worka, w którym znajduje się worek w (-1 jeśli na biurku).
int w_ktorym_worku(worek *w);

// Wynik: liczba przedmiotów zawartych (bezpośrednio i pośrednio) w worku w
int ile_przedmiotow(worek *w);

// Cała zawartość worka w ląduje na biurku, a wszystko, co poza workiem w
// znajdowało się bezpośrednio na biurku, ląduje wewnątrz worka w.
void na_odwrot(worek *w);

// Kończy i zwalnia pamięć
void gotowe();

#endif
