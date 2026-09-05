# 04 — Worki

[← All WDP assignments](../README.md)

**Nested bags · C++23**

Linked structures and dynamic container operations.

This assignment implements the API in [`worki.h`](worki.h); it is a library and does not contain a standalone `main` function.

## Build

From this assignment directory:

```sh
make -C .. task-4
```

This creates `../build/worki.o`. A caller should include `worki.h` and link this object, or compile together with `worki.cpp`. For example, if your caller is named `main.cpp`:

```sh
c++ -std=c++23 main.cpp worki.cpp -o ../build/worki-example
../build/worki-example
```

**Missing attachment:** the original `main.cpp` example mentioned in the task description is not included in the available files. The library can be compiled independently; the example command above requires a caller.

## Task description

**Otwarto:** wtorek, 2 grudnia 2025, 10:00  
**Wymagane do:** środa, 17 grudnia 2025, 16:00

**Uwaga:** Od tego zadania programy należy pisać w języku C++ (dopuszczamy standard C++23).

Postanowiłeś uporządkować przedmioty znajdujące się na Twoim biurku, wkładając je do worków. W trakcie porządkowania wkładasz przedmioty do worków, potem jedne worki do innych worków, wyjmujesz je, przekładasz, itd. Napisz program, który pomoże Ci śledzić aktualną konfigurację worków na biurku.

`przedmiot *nowy_przedmiot()` - Wywołanie funkcji informuje o kolejnym przedmiocie znajdującym się na biurku. Funkcja powinna stworzyć nowy element typu `przedmiot` i dać w wyniku wskaźnik na ten element.

`worek *nowy_worek()` - Kładziesz na biurku nowy worek. Worki otrzymują kolejny numer. Numery zaczynają się od 0 i są kolejnymi liczbami całkowitymi. Funkcja powinna stworzyć nowy element typu `worek` i dać w wyniku wskaźnik na ten element.

`void wloz(przedmiot *co, worek *gdzie)` - wkłada przedmiot `co` do worka `gdzie`. Możesz założyć, że w tym momencie zarówno przedmiot, jak i worek leżą bezpośrednio na biurku.

`void wloz(worek *co, worek *gdzie)` - wkłada worek `co` do worka `gdzie`. Możesz założyć, że w tym momencie oba worki leżą bezpośrednio na biurku.

`void wyjmij(przedmiot *p)` - Wyjmuje przedmiot `p` z worka, w którym się obecnie znajduje, i kładzie go na biurku. Możesz założyć, że w momencie wywołania funkcji przedmiot `p` znajdował się w worku, który leżał bezpośrednio na biurku.

`void wyjmij(worek *w)` - Wyjmuje worek `w` z worka, w którym się obecnie znajduje, i kładzie go na biurku. Możesz założyć, że w momencie wywołania funkcji worek `w` znajdował się w worku, który leżał bezpośrednio na biurku.

`int w_ktorym_worku(przedmiot *p)` - Wynikiem funkcji jest numer worka, w którym bezpośrednio znajduje się przedmiot `p`. Jeśli przedmiot `p` znajduje się bezpośrednio na biurku, wynikiem funkcji powinno być `-1`.

`int w_ktorym_worku(worek *w)` - Wynikiem funkcji jest numer worka, w którym bezpośrednio znajduje się worek `w`. Jeśli worek `w` znajduje się bezpośrednio na biurku, wynikiem funkcji powinno być `-1`.

`int ile_przedmiotow(worek *w)` - Wynikiem funkcji jest łączna liczba przedmiotów, które w danym momencie znajdują się bezpośrednio lub pośrednio wewnątrz worka `w`.

`void na_odwrot(worek *w)` - Funkcja może być wywołana tylko wtedy, gdy worek `w` znajduje się bezpośrednio na biurku. Następuje wielka zamiana: cała zawartość worka `w` ląduje na biurku, a wszystko, co poza workiem `w` znajdowało się bezpośrednio na biurku, ląduje wewnątrz worka `w`. Podczas zamiany nie rozpakowujemy zawartości innych worków niż worek `w`.

`void gotowe()` - Funkcja kończy porządki. Jest wywoływana jako ostatnia. Powinieneś w niej zwolnić całą pamięć, wliczając wszystkie przedmioty i worki.

Deklaracje podanych funkcji znajdują się w pliku `worki.h`. Twoim zadaniem jest uzupełnić w pliku definicje typów `struct przedmiot` i `struct worek` (nie zmieniając nic więcej w tym pliku) oraz zaimplementować podane funkcje w pliku `worki.cpp`.

Komenda kompilacji:

```bash
g++ @opcjeCpp main.cpp worki.cpp -o main.e
```

Różnice w pliku `opcjeCpp` w stosunku do pliku `opcje` z C są następujące: usunięte zostały opcje kompilacji: `-Wvla` (która sprawiała, że użycie tablic zmiennej długości jest uznawane za usterkę), `-Wjump-misses-init` (opcja właściwa dla C) i `-std=c17`, a w zamian została dodana opcja kompilacji `-std=c++23`.

Aby Twoje rozwiązanie uzyskało maksymalną punktację, koszt czasowy każdej funkcji musi być stały (poza techniczną funkcją `gotowe`). Jeśli będzie to koszt zamortyzowany stały, możesz stracić za to zadanie 0.5 pkt.

Twoje rozwiązanie zostanie także uruchomione za pomocą narzędzia `valgrind`, które pozwala sprawdzać m.in., czy program nie miał wycieków pamięci (nieco więcej o tym narzędziu dowiesz się w scenariuszu z tygodnia). Zakładamy, że pamięć po interesantach, którzy wyszli z urzędu, zostanie zwolniona przez użytkownika. W przypadku wykrycia wycieków pamięci za pomocą komendy:

```bash
valgrind --tool=memcheck --leak-check=yes ./main.e
```

możesz stracić od 1 do 2 punktów za zadanie.

### Przykład

W załączniku: `main.cpp`.

- `main.cpp` (24 listopada 2025, 21:26)
- `opcjeCpp` (6 listopada 2025, 23:05)
- `worki.h` (24 listopada 2025, 21:26)
