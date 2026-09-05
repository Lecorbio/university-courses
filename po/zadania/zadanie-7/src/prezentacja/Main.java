package prezentacja;

import java.util.Scanner;
import onp.KalkulatorONP;

public class Main {
    public static void main(String[] args) {
        // Przyklady do testowania:
        // 3 4 + 2 *        wynik: 14
        // 10 2 / 7 -       wynik: -2
        // -3 5 +           wynik: 2
        // 2.5 1.5 +        wynik: 4
        // 3 +              blad: za malo argumentow dla operatora +
        // 1 2              blad: po obliczeniu zostaja dwie liczby na stosie
        // 10 0 /           blad: dzielenie przez zero
        // abc              blad: nieznany token
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Podaj wyrazenie ONP: ");

            if (!scanner.hasNextLine()) {
                break;
            }

            String linia = scanner.nextLine();

            if (linia.trim().isEmpty()) {
                break;
            }

            try {
                double wynik = KalkulatorONP.oblicz(linia);
                System.out.println("Wartosc: " + wynik);
            } catch (IllegalArgumentException wyjatek) {
                System.out.println("Blad: " + wyjatek.getMessage());
            }
        }

        scanner.close();
    }
}
