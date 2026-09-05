# Homework 3: Hello World and Git

A minimal Java program used for a Git workflow assignment. The submitted program prints `Hello world!`.

[All homework](../README.md)

## Build and run

Requires JDK 21 or newer. Run from this directory:

```sh
javac --release 21 -encoding UTF-8 -d out Main.java
java -cp out Main
```

## Task description

Source: [Moodle — Zadanie 3 (Git)](https://moodle.mimuw.edu.pl/mod/assign/view.php?id=176302).

Przygotuj repozytorium Gita na małe zadania labowe.

Instrukcja:

1. Utwórz nowe lokalne repozytorium Gita.
2. Utwórz plik `README.md` z krótką informacją o repozytorium (wystarczy jedno zdanie o tym, czyje to repozytorium). Utwórz z tego pliku commita (na gałęzi `main`).
3. Utwórz katalog `zadanie3`, a w tym katalogu plik `Main.java` z programem wypisującym `Hello world!`. Zrób z tych zmian commita.
4. Utwórz projekt na wydziałowym GitLabie, np. o nazwie `PO Zadania`.
5. Wyślij commity z lokalnego repozytorium do zdalnego repozytorium na GitLabie.
6. Sprawdź, że w GitLabie widać plik `README.md` i plik `zadanie3/Main.java`.
7. Dodaj użytkownika Marek Zbysiński (`mzbysinski`) do projektu na GitLabie. Wejdź w menu *Manage > Members* i kliknij przycisk *Invite members*. Wpisz użytkownika `mzbysinski`. Uwaga: w wydziałowych systemach konto ma też inny Marek Zbysiński, który pewnie nie jest zainteresowany naszymi zadaniami z PO. Jako rolę wybierz *Maintainer*.

Jako rozwiązanie wyślij link do GitLabowego repozytorium w komentarzu pod tym zadaniem w Moodle'u. Spakuj też katalog z lokalnym repozytorium (ten zawierający `README.md`, katalog `zadanie3` i ukryty katalog `.git`) do formatu `.zip` i wyślij go w tym zadaniu na Moodle'u (na wszelki wypadek).

Wskazówki:

- Jeśli coś się nie uda (np. zapomni się o katalogu `zadanie3`), można tworzyć dodatkowe commity, żeby to skorygować. Czyli nie chodzi o to, żeby były dokładnie dwa commity, tylko żeby ostatecznie dojść do właściwego stanu. Z jednym zastrzeżeniem: w pierwszym commit'cie powinien znaleźć się tylko plik `README.md` (ułatwi mi to sprawdzanie) — a dopiero w kolejnym commit'cie (lub commitach) katalog `zadanie3` z zawartością.
- Nic nie stoi też na przeszkodzie, żeby dodać np. plik `.gitignore` (ale nie jest wymagany).
