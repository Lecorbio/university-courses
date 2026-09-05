public class Main {
    private static void sprawdz(boolean warunek, String opis) {
        if (!warunek) {
            throw new AssertionError(opis);
        }
    }

    public static void main(String[] args) {
        sprawdzPrzykladZTresci();
        sprawdzProgWyborczyIRemisy();

        System.out.println("Wszystkie testy przeszly.");
    }

    private static void sprawdzPrzykladZTresci() {
        KomitetWyborczy wolnaBajtocja = new KomitetWyborczy(
                "Wolna Bajtocja",
                new Kandydat[] {
                        new Kandydat("A", 300),
                        new Kandydat("B", 200),
                        new Kandydat("C", 120),
                        new Kandydat("D", 100),
                        new Kandydat("E", 0),
                        new Kandydat("F", 0),
                        new Kandydat("G", 0),
                        new Kandydat("H", 0)
                });
        KomitetWyborczy bajtek = new KomitetWyborczy(
                "Bajtek i przyjaciele",
                new Kandydat[] {
                        new Kandydat("I", 300),
                        new Kandydat("J", 0),
                        new Kandydat("K", 0),
                        new Kandydat("L", 0),
                        new Kandydat("M", 0),
                        new Kandydat("N", 0),
                        new Kandydat("O", 0),
                        new Kandydat("P", 0)
                });
        KomitetWyborczy precz = new KomitetWyborczy(
                "Precz z kompilacja",
                new Kandydat[] {
                        new Kandydat("R", 200),
                        new Kandydat("S", 150),
                        new Kandydat("T", 130),
                        new Kandydat("U", 0),
                        new Kandydat("V", 0),
                        new Kandydat("W", 0),
                        new Kandydat("X", 0),
                        new Kandydat("Y", 0)
                });

        KomisjaWyborcza komisja = new KomisjaWyborcza(
                new KomitetWyborczy[] {wolnaBajtocja, bajtek, precz});
        komisja.podzielMandaty(8, 0);

        sprawdz(wolnaBajtocja.getLiczbaMandatow() == 4, "Wolna Bajtocja ma 4 mandaty");
        sprawdz(bajtek.getLiczbaMandatow() == 1, "Bajtek ma 1 mandat");
        sprawdz(precz.getLiczbaMandatow() == 3, "Precz z kompilacja ma 3 mandaty");

        sprawdz(wolnaBajtocja.getKandydaciZMandatem()[0].getNazwisko().equals("A"), "pierwszy mandat WB");
        sprawdz(wolnaBajtocja.getKandydaciZMandatem()[3].getNazwisko().equals("D"), "czwarty mandat WB");
        sprawdz(!wolnaBajtocja.getKandydaci()[4].czyUzyskalMandat(), "piaty kandydat WB bez mandatu");
        sprawdz(wolnaBajtocja.getKandydaci()[4].czyPowiadomiony(), "piaty kandydat WB powiadomiony");
    }

    private static void sprawdzProgWyborczyIRemisy() {
        KomitetWyborczy pierwszy = new KomitetWyborczy(
                "Pierwszy",
                new Kandydat[] {
                        new Kandydat("A1", 80),
                        new Kandydat("A2", 20),
                        new Kandydat("A3", 0)
                });
        KomitetWyborczy drugi = new KomitetWyborczy(
                "Drugi",
                new Kandydat[] {
                        new Kandydat("B1", 60),
                        new Kandydat("B2", 40),
                        new Kandydat("B3", 0)
                });
        KomitetWyborczy podProgiem = new KomitetWyborczy(
                "Pod progiem",
                new Kandydat[] {
                        new Kandydat("C1", 9),
                        new Kandydat("C2", 0),
                        new Kandydat("C3", 0)
                });

        KomisjaWyborcza komisja = new KomisjaWyborcza(
                new KomitetWyborczy[] {pierwszy, drugi, podProgiem});
        komisja.podzielMandaty(3, 10);

        sprawdz(pierwszy.getLiczbaMandatow() == 2, "remis ilorazow wygrywa wczesniejszy komitet");
        sprawdz(drugi.getLiczbaMandatow() == 1, "drugi komitet dostaje jeden mandat");
        sprawdz(podProgiem.getLiczbaMandatow() == 0, "komitet pod progiem bez mandatu");
        sprawdz(!podProgiem.getKandydaci()[0].czyUzyskalMandat(), "kandydat komitetu pod progiem bez mandatu");

        sprawdz(pierwszy.getKandydaciZMandatem()[0].getNazwisko().equals("A1"), "najlepszy kandydat pierwszy");
        sprawdz(pierwszy.getKandydaciZMandatem()[1].getNazwisko().equals("A2"), "drugi kandydat pierwszy");
    }
}
