# 05 — Ścisłe przedziały

[← All WDP assignments](../README.md)

**Constrained intervals · C++23**

Sliding windows, monotonic queues, and interval selection.

## Build and run

From this assignment directory:

```sh
make -C .. task-5
../build/scisle-przedzialy < input.txt
```

Create `input.txt` using the input format in the task description below. The program writes its answer to standard output.

## Task description

**Otwarto:** poniedziałek, 15 grudnia 2025, 10:00  
**Wymagane do:** środa, 14 stycznia 2026, 16:05

Dany jest ciąg punktów `(x1, y1), ..., (xn, yn)` takich, że współrzędne `x` tworzą ciąg rosnący (`x1 < ... < xn`). Przedział indeksów `[l, r]` nazwiemy `U`-ścisłym jeśli `|yi - yj| <= U` dla każdych `i, j ∈ [l, r]`. Przedział `U`-ścisły `I` jest maksymalny, jeśli nie istnieje przedział `U`-ścisły `J` taki że `I ⊊ J`. Wreszcie jakość przedziału `U`-ścisłego `[l, r]` definiujemy jako `(x_r - x_l) / √(r - l + 1)`.

Dla każdego indeksu `i = 1, ..., n` chcemy wyznaczyć maksymalny przedział `U`-ścisły `[l_i, r_i]` zawierający `i` o największej jakości. Jeśli istnieje wiele takich przedziałów, to chcemy wybrać mniejszy z nich (taki, którego lewy koniec jest mniejszy).

Napisz program w C++, który wczyta z `stdin` liczby `n` (`1 <= n <= 3 · 10^6`) oraz `U` (`0 <= U <= 10^9`) i `n` par liczb całkowitych `(x1, y1), ..., (xn, yn)`, każda z zakresu od `0` do `10^9`, i wypisze na `stdout` wyznaczone przedziały, dla `i = 1, ..., n`, po jednym w wierszu.

Komenda kompilacji:

```bash
g++ @opcjeCpp prz.cpp -lm -o prz.e
```

gdzie `opcjeCpp` są opcjami kompilacji takimi jak w zadaniu Worki.

### Przykładowe wejście

```text
9 10
1 30
2 25
8 19
12 24
30 39
32 35
44 30
46 25
47 21
```

### Przykładowe wyjście

```text
1 2
2 4
2 4
2 4
5 7
5 7
5 7
6 8
7 9
```

Wyjaśnienie przykładu: Mamy 5 maksymalnych przedziałów `U`-ścisłych:

- `[1, 2]` o jakości `(2 - 1)/√2 ≈ 0.7071`,
- `[2, 4]` o jakości `(12 - 2)/√3 ≈ 5.7735`,
- `[5, 7]` o jakości `(44 - 30)/√3 ≈ 8.0829`,
- `[6, 8]` o jakości `(46 - 32)/√3 ≈ 8.0829`,
- `[7, 9]` o jakości `(47 - 44)/√3 ≈ 1.7321`.

Przykładowo dla indeksu `6` mamy trzy maksymalne przedziały `U`-ścisłe: `[5, 7]`, `[6, 8]` i `[7, 9]`. Największą jakość mają `[5, 7]` i `[6, 8]`, ale ten pierwszy ma mniejszy lewy koniec, więc wybieramy `[5, 7]`.

**Uwaga:** Na maksymalną punktację mogą liczyć tylko rozwiązania o liniowej złożoności czasowej. Za rozwiązanie o złożoności `Θ(n^3)` można uzyskać maksymalnie 1 punkt. Za rozwiązanie o złożoności `Θ(n^2)` można uzyskać maksymalnie 3 punkty. Za rozwiązanie o złożoności `Θ(n log n)` można uzyskać maksymalnie 4 punkty. Ocena za code review nie zależy od złożoności rozwiązania, tzn. za poprawne i stylowe rozwiązanie o gorszej złożoności recenzent może równie dobrze uzyskać 2 punkty.

### Załącznik

- `opcjeCpp` (15 grudnia 2025, 21:37)
