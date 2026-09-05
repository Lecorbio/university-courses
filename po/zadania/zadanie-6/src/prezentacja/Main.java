package prezentacja;

import wyrazenia.*;

public class Main {
    public static void main(String[] args) {
        // Wybrany wariant dodatkowy: upraszczanie wyrazen
        Wyrazenie wyrazenie = Wyrazenie.dodaj(
            Wyrazenie.pomnoz(
                new Liczba(2),
                new Zmienna()
            ),
            new Liczba(3)
        );

        System.out.println("wyrazenie = " + wyrazenie);
        System.out.println("wartosc dla x = 0: " + wyrazenie.ewaluuj(0));
        System.out.println("wartosc dla x = 2: " + wyrazenie.ewaluuj(2));
        System.out.println("wartosc dla x = 5: " + wyrazenie.ewaluuj(5));
        System.out.println("pochodna = " + wyrazenie.pochodna());
        System.out.println("wartosc pochodnej dla x = 5: " + wyrazenie.pochodna().ewaluuj(5));
        System.out.println("calka od 0 do 10 = " + wyrazenie.calkuj(0, 10, 1000));

        Wyrazenie zNawiasami = new Mnozenie(
            new Dodawanie(
                new Zmienna(),
                new Liczba(2)
            ),
            new Liczba(3)
        );

        System.out.println("przyklad nawiasow = " + zNawiasami);

        System.out.println("0 + x = " + Wyrazenie.dodaj(new Liczba(0), new Zmienna()));
        System.out.println("x + 0 = " + Wyrazenie.dodaj(new Zmienna(), new Liczba(0)));
        System.out.println("2 + 3 = " + Wyrazenie.dodaj(new Liczba(2), new Liczba(3)));
        System.out.println("0 * x = " + Wyrazenie.pomnoz(new Liczba(0), new Zmienna()));
        System.out.println("1 * x = " + Wyrazenie.pomnoz(new Liczba(1), new Zmienna()));
        System.out.println("2 * 3 = " + Wyrazenie.pomnoz(new Liczba(2), new Liczba(3)));
    }
}
