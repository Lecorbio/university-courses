# Homework 4: Fractions

A rational-number class with normalization, arithmetic, reciprocal calculation, and comparisons.

[All homework](../README.md)

## Build and run

Requires JDK 21 or newer. Run these commands from this directory:

```sh
javac --release 21 -encoding UTF-8 -d out *.java
java -ea -cp out Main
```

The demonstration prints arithmetic and comparison results for a few fractions.

## Task description

Source: [Moodle — Zadanie 4 (Ułamek)](https://moodle.mimuw.edu.pl/mod/assign/view.php?id=176615).

Proszę dokończyć zadanie Ułamek z dzisiejszych zajęć.

Metody do zaimplementowania:

- `toString`: `"licznik/mianownik"` w postaci nieskracalnej;
- gettery do licznika i mianownika;
- odwracanie ułamka;
- dodawanie, odejmowanie, mnożenie, dzielenie;
- porównania: równość i mniejszość.

W metodzie `main` proszę utworzyć kilka ułamków i wypróbować zaimplementowane metody.

Od tego zadania można (i do tego zachęcam) wysyłać rozwiązania także na GitLabie — więcej informacji jest w [instrukcji wysyłania rozwiązań](https://moodle.mimuw.edu.pl/mod/page/view.php?id=176602).
