# 02 — Trzy różne

[← All WDP assignments](../README.md)

**Three different motel chains · C17**

Finding closest and farthest triples along a highway.

## Build and run

From this assignment directory:

```sh
make -C .. task-2
../build/trz < input.txt
```

Create `input.txt` using the input format in the task description below. The program writes its answer to standard output.

## Task description

**Otwarto:** wtorek, 4 listopada 2025, 08:00  
**Wymagane do:** wtorek, 25 listopada 2025, 16:00

Wzdłuż autostrady, którą możemy sobie wyobrazić jako linię prostą, rozmieszczonych jest `n` moteli. Motele numerujemy od 1 do `n` kolejno wzdłuż autostrady. Każdy z moteli należy do jakiejś sieci moteli, którą opisujemy liczbą z zakresu od 1 do `n`.

Bajtek powiedział Bitkowi, że w trakcie jazdy autostradą trzy razy zatrzymał się na noc w motelu, przy czym za każdym razem nocował w motelu innej sieci. Bitek zastanawia się, w jak dużych odstępach musiały znajdować się te trzy motele. Interesuje go najbliższa i najdalsza trójka moteli.

Formalnie, Bitek chciałby wyznaczyć trzy motele `A`, `B`, `C` położone w tej kolejności wzdłuż autostrady i należące do różnych sieci, dla których:

1. maksimum z odległości od `A` do `B` i od `B` do `C` jest jak najmniejsze (najbliższa trójka),
2. minimum z odległości od `A` do `B` i od `B` do `C` jest jak największe (najdalsza trójka).

Napisz program, który wczyta z `stdin` liczbę `n` moteli, a następnie opisy moteli w kolejności wzdłuż autostrady - dla każdego motelu jego numer sieci i odległość od początku autostrady - i wypisze na `stdout` dwie liczby: jak powyżej, maksimum z minimów odległości i minimum z maksimów odległości trzech moteli należących do różnych sieci. Jeśli Bajtek pomylił się i żadna trójka moteli różnych sieci nie występuje wzdłuż autostrady, obie liczby powinny być równe zero. Dokładny format wejścia i wyjścia opisuje przykład poniżej.

Możesz założyć, że liczba moteli będzie dodatnia i nie przekroczy miliona, a wszystkie liczby na wejściu będą nieujemne i zmieszczą się w typie `int`. Może się tak zdarzyć, że kilka moteli znajduje się w tym samym punkcie autostrady, a nawet że Bajtek nocował w dwóch lub trzech takich motelach.

Rozwiązanie, które tylko jedną z dwóch wymaganych liczb wypisuje poprawnie, może uzyskać połowę punktów za zadanie.

Komenda kompilacji:

```bash
gcc @opcje trz.c -o trz.e
```

### Wejście

```text
9
1 2
2 6
2 9
1 13
1 17
3 20
1 26
3 27
1 30
```

### Wyjście

```text
7 10
```

Wyjaśnienie: Motele 3, 4 i 6 (te w odległościach 9, 13 i 20 od początku autostrady) wszystkie należą do różnych sieci (odpowiednio 2, 1 i 3) i minimalizują maksimum z odległości. Motele 2, 6 i 9 (te w odległościach 6, 20 i 30 od początku autostrady) wszystkie należą do różnych sieci (odpowiednio 2, 3 i 1) i maksymalizują minimum z odległości.

**Uwaga:** Na maksymalną punktację mogą liczyć tylko rozwiązania o liniowej złożoności czasowej. Za rozwiązanie o złożoności `O(n^3)` można uzyskać maksymalnie 1 punkt. Za rozwiązanie o złożoności `O(n^2)` można uzyskać maksymalnie 3 punkty. Za rozwiązanie o złożoności `O(n log n)` można uzyskać maksymalnie 4 punkty. Ocena za code review nie zależy od złożoności rozwiązania, tzn. za poprawne i stylowe rozwiązanie o gorszej złożoności recenzent może równie dobrze uzyskać 2 punkty.
