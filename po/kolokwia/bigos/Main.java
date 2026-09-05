public class Main {
    private static void sprawdz(boolean warunek, String opis) {
        if (!warunek) {
            throw new AssertionError(opis);
        }
    }

    public static void main(String[] args) {
        Katalog root = new Katalog();
        Plik plikWRoocie = new Plik("plik", root, 100);
        sprawdz("/plik".equals(plikWRoocie.toString()), "plik w katalogu glownym");

        Katalog a = new Katalog("a", root);
        Plik x = new Plik("x", a, 200, false, true);
        sprawdz("/a".equals(a.toString()), "katalog /a");
        sprawdz("/a/x".equals(x.toString()), "plik /a/x");
        sprawdz(x.czyMoznaCzytac(), "plik modyfikowalny mozna czytac");

        Katalog b = new Katalog("b", root);
        Dowiazanie dowiazanieDoX = new Dowiazanie(x, b);
        sprawdz("/b/(/a/x)".equals(dowiazanieDoX.toString()), "dowiazanie do pliku");
        sprawdz(dowiazanieDoX.pobierzElement() == x, "pobranie celu dowiazania");

        Plik drugiX = new Plik("x", a, 300);
        sprawdz(a.liczbaElementow() == 2, "dwa pliki o tej samej nazwie sa dozwolone");

        dowiazanieDoX.zmienKatalog(root);
        sprawdz("/(/a/x)".equals(dowiazanieDoX.toString()), "przeniesienie dowiazania");

        x.usun();
        sprawdz(a.liczbaElementow() == 1, "usuniecie pliku z katalogu");
        sprawdz(root.liczbaElementow() == 3, "usuniecie dowiazan do pliku");

        a.usun();
        sprawdz(root.liczbaElementow() == 3, "niepusty katalog nie zostal usuniety");

        drugiX.usun();
        a.usun();
        sprawdz(root.liczbaElementow() == 2, "pusty katalog zostal usuniety");

        System.out.println("Wszystkie testy przeszly.");
        System.out.println();
        root.wypiszZawartosc();
    }
}
