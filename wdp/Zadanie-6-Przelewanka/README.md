# 06 — Przelewanka

[← All WDP assignments](../README.md)

**Water pouring · C++23**

Breadth-first search over states and reachability checks.

## Build and run

From this assignment directory:

```sh
make -C .. task-6
../build/przelewanka < input.txt
```

Create `input.txt` using the input format in the task description below. The program writes its answer to standard output.

For a small usage example (one empty glass with capacity 5 that should end full):

```sh
printf '1\n5 5\n' | ../build/przelewanka
```

Expected output: `1`.

## Task description

**Otwarto:** wtorek, 13 stycznia 2026, 10:00  
**Wymagane do:** środa, 21 stycznia 2026, 16:00

**Uwaga:** Aby punkty za to zadanie miały pozwolić ubiegać się o zwolnienie z egzaminu na podstawie punktów, należy rozwiązanie oddać do czasu ostatniego laboratorium.

Masz dane `n` szklanek, ponumerowanych od `1` do `n`, o pojemnościach odpowiednio `x1`, `x2`, ..., `xn`. Początkowo wszystkie szklanki są puste. Możesz wykonywać następujące czynności:

- nalać do wybranej szklanki do pełna wody z kranu,
- wylać całą wodę z wybranej szklanki do zlewu,
- przelać wodę z jednej szklanki do drugiej - jeżeli się zmieści, to przelewasz całą wodę, a jeżeli nie, to tyle żeby druga szklanka była pełna.

Twoim celem jest uzyskanie takiej sytuacji, że w każdej szklance jest określona ilość wody, odpowiednio `y1`, `y2`, ..., `yn`.

Napisz program, który mając dane liczby `x1`, `x2`, ..., `xn` i `y1`, `y2`, ..., `yn` wyznaczy minimalną liczbę czynności potrzebnych do uzyskania opisanej przez nie sytuacji. Jeżeli jej uzyskanie nie jest możliwe, to poprawnym wynikiem jest `-1`. Dane należy wczytać ze standardowego wejścia w formacie:

```text
n
x1 y1
x2 y2
...
xn yn
```

Możesz założyć, że `0 <= n` oraz `0 <= yi <= xi` dla `i = 1, 2, ..., n`. Wszystkie liczby na wejściu są całkowite. Liczby `n` oraz `x1+x2+...+xn` i `y1+y2+...+yn` mieszczą się w typie `int`. Limity są celowo niedospecyfikowane - należy spróbować napisać możliwie najlepsze rozwiązanie.
