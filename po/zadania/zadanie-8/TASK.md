# Zadanie 8 — Testy

Źródło: [Moodle — Zadanie 8 (Testy)](https://moodle.mimuw.edu.pl/mod/assign/view.php?id=178037), kurs PO 2025/2026, grupa 11. Treść pobrana 5 września 2026 r.

[Rozwiązanie i uruchomienie](README.md) · [Scenariusz JUnit na Moodle](https://moodle.mimuw.edu.pl/mod/resource/view.php?id=178019)

## Treść zadania

W ramach tego zadania proszę napisać testy jednostkowe w JUnit do uproszczonej klasy `Polynomial` (ze scenariusza z JUnit – jest w module z naszymi małymi zadaniami domowymi).

Modyfikacje klasy:

- Usuwamy sporą część metod – zostawiamy tylko konstruktor, `add`, `evaluate`, `toString`. Przy takim specjalnie okrojonym zestawie metod, postarajmy się o dość dokładne pokrycie różnych przypadków testami (szczegóły niżej).
- W konstruktorze dodajemy normalizację – jeśli wielomian ma wiodące zerowe współczynniki przy najwyższych potęgach, to je ucinamy i zachowujemy pomniejszoną tablicę. Można wybrać, czy zerowy wielomian reprezentujemy jako `new double[]{}` czy jako `new double[]{0}`.
- Jeśli na potrzeby testowania przydatne okaże się dopisanie jakichś nowych metod do klasy `Polynomial` (które mają sens z perspektywy użytkownika klasy), to można takie dodać (np. metoda `equals` może być dobrym pomysłem). Wygoda testowania to jeden z aspektów, o których warto pomyśleć przy projektowaniu klas (nie naruszając przy tym kapsułkowania, np. poprzez nadmierne eksponowanie wewnętrznej reprezentacji).

Zachowania klasy `Polynomial` do sprawdzenia w testach (domyślnie każde zachowanie w osobnym teście; ew. niektóre można pogrupować, jeśli ma to sens):

- Konstruktor ucina zera wiodące.
- Add dobrze sumuje dwa zwyczajne wielomiany tego samego stopnia (większego niż 1).
- Add dobrze sumuje dwa zwyczajne wielomiany różnych stopni (większych niż 1).
- Add radzi sobie z przypadkami brzegowymi – wielomian zerowego stopnia jako 1) lewy argument, 2) prawy argument, 3) oba argumenty.
- Wynik `add` nie ma zer wiodących, nawet jeśli najwyższe współczynniki się wyzerują.
- Evaluate dobrze wylicza zwyczajny wielomian stopnia > 1.
- Evaluate dobrze wylicza zwyczajny wielomian stopnia > 1 o niecałkowitych współczynnikach w niecałkowitym punkcie (może być potrzebny margines błędu przy assertach). To jedyny test z double'ami – pozostałe sugeruję zrobić na wartościach całkowitych – wtedy mamy gwarancję, że wynik jest w 100% dokładny.
- Evaluate radzi sobie z przypadkiem brzegowym – wielomianem stopnia zerowego.
- `toString` dobrze wypisuje zwyczajny wielomian stopnia > 1.
- `toString` pomija jednomiany z zerowymi współczynnikami.
- `toString` pomija współczynniki = 1.
- `toString` dobrze wypisuje wielomian zerowy.

Jeśli któryś z testów wykryje błąd w implementacji klasy `Polynomial`, to należy ten błąd poprawić (i najlepiej oznaczyć to miejsce komentarzem – na potrzeby mojego sprawdzania).
