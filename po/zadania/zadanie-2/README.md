# Homework 2: Majority voting

A two-pass majority-vote algorithm that returns a candidate with more than half the votes, or `0` when there is no majority.

[All homework](../README.md)

## Build and run

Requires JDK 21 or newer. Run these commands from this directory:

```sh
javac --release 21 -encoding UTF-8 -d out *.java
java -ea -cp out Glosowanie
```

The demonstration prints results for five built-in voting examples.

## Task description

Source: [Moodle — Zadanie 2 (Głosowanie)](https://moodle.mimuw.edu.pl/mod/assign/view.php?id=175691).

Zaimplementuj metodę statyczną (można myśleć o niej jako zwyczajnej funkcji jak w C) do wyznaczania zwycięzcy w głosowaniu większościowym. Metoda jako parametr przyjmuje niepustą tablicę `n` liczb całkowitych z przedziału `1..k`. `i`-ty element tablicy oznacza, na którego z `k` kandydatów `i`-ta osoba oddała głos. Zwycięzcą jest kandydat, który otrzymał ponad `n/2` głosów (może nie być żadnego zwycięzcy). Może się zdarzyć, że `k > n`.

Funkcja powinna dać w wyniku numer zwycięzcy lub `0`, jeśli nie ma zwycięzcy.

Można założyć, że `k` nie jest bardzo duże i można stworzyć `k`-elementową tablicę liczników głosów; przy czym `k` nie jest podane jako argument metody (trzeba je w razie potrzeby wyznaczyć). Metodę da się też zaimplementować bez tablicy liczników (jedno i drugie rozwiązanie jest w porządku).

Oprócz implementacji metody proszę w funkcji `main` zademonstrować działanie metody (wywołać ją i wypisać wynik) na 3 ciekawych przykładowych tablicach.

Na Moodle'a wysyłamy pojedynczy plik `.java` z kodem rozwiązania.

Szablon rozwiązania:

```java
public class Glosowanie {
    public static int wyznaczZwyciezce(int[] glosy) {
        // Tutaj Państwa implementacja:
        // ...
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(wyznaczZwyciezce(new int[]{5, 2, 1, 3}));
        // I trzeba uruchomić jeszcze dla trzech ciekawych tablic
        // pokazujących poprawność implemenacji.
        // ...
    }
}
```
