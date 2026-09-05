# Szybkość programów w Javie

Source: [Moodle assignment](https://moodle.mimuw.edu.pl/mod/forum/view.php?id=170644).

Proszę o zamieszczanie na tym forum wyników Państwa eksperymentów polegających na porównaniu szybkości programów w Javie i drugim, dowolnie wybranym języku (programowania). Na forum należy wpisać — każdy w **nowym**, osobnym wątku — następujące informacje:

- nazwę porównywanego języka (np. *C++*),
- środowisko (system operacyjny, Windows/Linux/..., wersja systemu, 32/64bity — np. pod Linuxem wynik polecenia `uname -mrs`, który może wyglądać tak: `Linux 3.7.10-8 x86_64`),
- wersję Javy (`javac --version`), ewentualne opcje kompilacji/wykonania (np. `javac 24`),
- wersję implementacji drugiego języka programowania (o ile dostępna), ewentualne opcje kompilacji/wykonania (np. dla c++ wynik polecenia `c++ --version`, który może wyglądać tak: `c++ (PLD-Linux) 4.8.2 20131104 (release)`),
- czas działania programu w Javie w ms (np. *Java: 500 ms*),
- czas działania programu w drugim języku w ms (np. *C++: 500 ms*),
- ewentualne uwagi.

Ponadto proszę o wgranie kodu źródłowego obu programów i (jeśli dotyczy) danych (jako spakowanego archiwum). Chodzi o możliwość odtworzenia wyników Państwa eksperymentu.

Proszę również o przeprowadzenie eksperymentu zarówno z kompilacją w locie (Just in Time, tak jest domyślnie), z kompilacją przy pierwszym wywołaniu, jak i z wyłączeniem kompilacji w JVMie. W standardowej JVM (zarówno pod Windows, jak i pod Linuxem na students) działają następujące opcje:

- `-X` wyświetla dostępne opcje,
- `-Xdiag` wyświetla dodatkowe informacje,
- `-Xint` wymusza tryb pracy JVM bez kompilowania bajtkodu,
- `-Xcomp` wymusza (częstszą) kompilację przy pierwszym wywołaniu (tu dokumentacja nie jest jednoznaczna),
- `-Xmixed` (tryb domyślny) kompilacja w locie.

Zatem proszę przetestować swój program w Javie zarówno tak:

```sh
java Program
```

jak i tak:

```sh
java -Xcomp -Xdiag Program
```

jak i tak:

```sh
java -Xint -Xdiag Program
```

Algorytm nie musi być wyrafinowany, ale powinien być zorientowany na obliczenia (a nie np. na operacje we/wy). Na ile to możliwe, należy zadbać o uruchomienie obu programów w wersji zoptymalizowanej czasowo i dodatkowo w Javie z `-Xint` i `-Xcomp`. Zarówno algorytmy, jak i języki programowania, mogą się powtarzać (choć oczywiście zachęcam do wybierania jak najbardziej różnorodnych).

Czas można mierzyć zarówno z poziomu systemu operacyjnego, jak i wewnątrz programów (w Javie czas podaje `System.nanoTime()`), proszę to tylko wyraźnie zaznaczyć w opisie.

Zwróć uwagę, aby tak dobrać dane, by testowany czas wykonania programu był istotnie większy od czasu ładowania maszyny wirtualnej (zwłaszcza przy mierzeniu instrukcją time). Czasy w stylu 50 ms nie są w ogóle miarodajne. Najlepiej przeprowadź testy dla kilku "rozmiarów" danych (być może dla niektórych rozmiarów czasy z niektórymi opcjami będą nie do doczekania się — wtedy można je oczywiście pominąć, być może z pomocą polecenia timeout).

Testowane programy nie powinny też wypisywać zbyt dużo na wyjście, gdyż nie chcemy mierzyć sprawności i ustawień bibliotek współpracy z terminalem, tylko sprawność samego języka programowania.

Jednym słowem, zadbaj, by Twój eksperyment był sensowny!

Można podać kilka wyników (np. ten sam algorytm dla różnych rozmiarów danych).

Po wpisaniu się na forum należy powiadomić prowadzącego zajęcia w grupie laboratoryjnej (żeby mógł dodać punkt), najwygodniej to zrobić podczas zajęć.

Termin oddawania (liczy się termin wpisania na forum; prowadzącemu wpisanie można zgłosić później — podczas kolejnych zajęć): 4 IV 2026, 23:59.
