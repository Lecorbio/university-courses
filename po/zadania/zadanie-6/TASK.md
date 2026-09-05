# Zadanie 6 — Wyrażenia

Źródło: [Moodle — Zadanie 6 (Wyrażenia)](https://moodle.mimuw.edu.pl/mod/assign/view.php?id=177293), kurs PO 2025/2026, grupa 11. Treść pobrana 5 września 2026 r.

[Rozwiązanie i uruchomienie](README.md)

## Treść zadania

Dokończ zadanie o wyrażeniach z zajęć.

Wymagane składowe wyrażeń:

- Stałe
- Zmienna
- Dodawanie
- Mnożenie

Wymagane operacje:

- Wartość w punkcie
- Pochodna symboliczna
- Całka numeryczna
- `toString` (z unikaniem niepotrzebnych nawiasów w takim zakresie, jak na zajęciach)

Ponadto należy zaimplementować jeden z następujących punktów (do wyboru; w metodzie `main` proszę napisać komentarz, którą wersję się wybrało):

- Funkcje `sin` i `cos`.
- Upraszczanie `0 + wyr -> wyr; wyr + 0 -> wyr; liczba + liczba -> liczba` (i analogiczne reguły dla mnożenia)

Wszystkie podklasy wyrażeń (i samą klasę wyrażenie) umieszczamy w (jednym) pakiecie.

W osobnym pakiecie, w osobnej klasie (w jej metodzie `main`) prezentujemy użycie zaimplementowanych klas wyrażeń.

Przykładowa prezentacja:

- Tworzymy wyrażenie zawierające po jednym obiekcie każdej klasy. Wykonujemy na tym wyrażeniu wymienione wymagane operacje.
- (ponadto, w przypadku wariantu z upraszczaniem) Tworzymy drugie wyrażenie o takiej strukturze, która spowoduje użycie każdej z reguł upraszczania i wypisujemy `toString`. Alternatywnie można kilka mniejszych wyrażeń.
