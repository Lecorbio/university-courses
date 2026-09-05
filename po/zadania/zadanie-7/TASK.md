# Zadanie 7 — ONP

Źródło: [Moodle — Zadanie 7 (ONP)](https://moodle.mimuw.edu.pl/mod/assign/view.php?id=177588), kurs PO 2025/2026, grupa 11. Treść pobrana 5 września 2026 r.

[Rozwiązanie i uruchomienie](README.md)

## Treść zadania

Proszę dokończyć zadanie ONP z zajęć.

Program powinien:

- zapytać użytkownika o podanie wyrażenia w ONP
- wczytać podane wyrażenie ze standardowego wejścia (za pomocą klasy `Scanner`; metody `hasNextLine` i `nextLine` mogą się przydać); każde wyrażenie jest w osobnej linii
- wypisać wartość wyrażenia
- i tak w kółko, aż użytkownik poda pustą linię.

Poprawne wyrażenia składają się z operatorów `+ - * /` oraz liczb (wszystko oddzielane spacjami). Do podziału linii na składowe wyrażeń również przyda się `Scanner` (skonstruowany np. tak `Scanner scannerLinii = new Scanner(linia)`).

W przypadku podania niepoprawnego wyrażenia metoda licząca powinna zgłosić wyjątek. Natomiast niepoprawne wyrażenie nie powinno kończyć działania programu – użytkownik powinien móc dalej wpisywać kolejne wyrażenia.

Jako część rozwiązania zaimplementuj klasę `Stos` (również sygnalizującą niepożądane sytuacje wyjątkami).

Do rozwiązania dołącz także (w komentarzu w pliku z metodą `main`) co najmniej dwa przykładowe wyrażenia w ONP (jedno poprawne i jedno niepoprawne), na których można przetestować program.

Dokumentacja klasy `Scanner`: [Java SE 21 — Scanner](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Scanner.html).

Przykładowe użycie Scannera do czytania kolejnych linii:

```java
Scanner scanner = new Scanner(System.in);
if (scanner.hasNextLine()) {
    String line = scanner.nextLine();
    // do stuff...
}
```
