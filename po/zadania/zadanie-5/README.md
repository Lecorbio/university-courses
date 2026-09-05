# Homework 5: Double-ended queue

An array-backed integer deque with a circular buffer and automatic capacity growth.

[All homework](../README.md)

## Build and run

Requires JDK 21 or newer. Run these commands from this directory:

```sh
javac --release 21 -encoding UTF-8 -d out *.java
java -ea -cp out Main
```

The demonstration inserts and removes elements at both ends and shows how the capacity changes.

## Task description

Source: [Moodle — Zadanie 5 (Kolejka dwustronna)](https://moodle.mimuw.edu.pl/mod/assign/view.php?id=177093).

Proszę dokończyć zadanie Kolejka dwustronna (na buforze cyklicznym) z zajęć.

Wymagane operacje:

- dodawanie/sprawdzanie/usuwanie elementu z początku/końca kolejki (iloczyn kartezjański);
- czy pusta.

Przy próbie dodania elementu do pełnego bufora, bufor powinien się powiększyć (dotychczasowy rozmiar razy stała `> 1`; np. razy 2).

W metodzie `main` proszę utworzyć kolejkę i wypróbować zaimplementowane metody. W szczególności należy dodać co najmniej tyle elementów, żeby realokacja wykonała się przynajmniej raz.

Od tego zadania można (i do tego zachęcam) wysyłać rozwiązania na GitLabie również bardziej zaawansowanym (i wyżej punktowanym) sposobem — opisanym jako *Sposób 2* w [instrukcji](https://moodle.mimuw.edu.pl/mod/page/view.php?id=176602) (naturalnie *Sposób 1* dalej jest też w porządku — można wybrać).
