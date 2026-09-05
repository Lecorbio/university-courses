package onp;

import java.util.Locale;
import java.util.Scanner;

public class KalkulatorONP {
    public static double oblicz(String linia) {
        Stos stos = new Stos();
        try (Scanner scannerLinii = new Scanner(linia)) {
            scannerLinii.useLocale(Locale.US);

            while (scannerLinii.hasNext()) {
                if (scannerLinii.hasNextDouble()) {
                    double liczba = scannerLinii.nextDouble();
                    stos.wloz(liczba);
                } else {
                    wykonajOperator(scannerLinii.next(), stos);
                }
            }
        }

        if (stos.rozmiar() != 1) {
            throw new IllegalArgumentException("Wyrazenie powinno zostawic dokladnie jedna wartosc na stosie");
        }

        return stos.zdejmij();
    }

    private static void wykonajOperator(String token, Stos stos) {
        if (!czyOperator(token)) {
            throw new IllegalArgumentException("Nieznany token: " + token);
        }

        if (stos.rozmiar() < 2) {
            throw new IllegalArgumentException("Operator " + token + " wymaga dwoch argumentow");
        }

        // Przy odejmowaniu i dzieleniu kolejnosc argumentow ma znaczenie.
        double prawy = stos.zdejmij();
        double lewy = stos.zdejmij();

        if (token.equals("+")) {
            stos.wloz(lewy + prawy);
        } else if (token.equals("-")) {
            stos.wloz(lewy - prawy);
        } else if (token.equals("*")) {
            stos.wloz(lewy * prawy);
        } else {
            if (prawy == 0) {
                throw new IllegalArgumentException("Nie mozna dzielic przez zero");
            }

            stos.wloz(lewy / prawy);
        }
    }

    private static boolean czyOperator(String token) {
        return token.equals("+")
            || token.equals("-")
            || token.equals("*")
            || token.equals("/");
    }
}
