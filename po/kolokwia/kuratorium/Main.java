public class Main {
    private static void sprawdz(boolean warunek, String opis) {
        if (!warunek) {
            throw new AssertionError(opis);
        }
    }

    public static void main(String[] args) {
        SzkolaSrednia szkola = new SzkolaSrednia(null);
        Klasa klasaA = new Klasa(szkola, 0, 1);
        Klasa klasaB = new Klasa(szkola, 1, 1);
        szkola.setKlasy(new Klasa[] {klasaA, klasaB});

        Uczen uczen1 = new Uczen(1, null);
        Uczen uczen2 = new Uczen(2, null);

        Zyczenie uczen1DoB = new Zyczenie(klasaB, uczen1, 1, 100);
        Zyczenie uczen2DoB = new Zyczenie(klasaB, uczen2, 0, 90);
        Zyczenie uczen1DoA = new Zyczenie(klasaA, uczen1, 0, 80);

        uczen1.setZyczenia(new Zyczenie[] {uczen1DoA, uczen1DoB});
        uczen2.setZyczenia(new Zyczenie[] {uczen2DoB});

        Zyczenie[] listaZyczen = new Zyczenie[] {uczen1DoB, uczen2DoB, uczen1DoA};
        SzkolaSrednia[] szkoly = new SzkolaSrednia[] {szkola};
        Uczen[] uczniowie = new Uczen[] {uczen1, uczen2};

        Kuratorium kuratorium = new Kuratorium(listaZyczen, szkoly, uczniowie);
        kuratorium.algorytmPrzydzialu();

        sprawdz(uczen1.getSpelnioneZyczenie() == uczen1DoA, "uczen 1 dostal lepsze zyczenie");
        sprawdz(uczen2.getSpelnioneZyczenie() == uczen2DoB, "uczen 2 wszedl na zwolnione miejsce");
        sprawdz(klasaA.getLiczbaUczniow() == 1, "klasa A ma jednego ucznia");
        sprawdz(klasaB.getLiczbaUczniow() == 1, "klasa B ma jednego ucznia");
        sprawdz(kuratorium.getLiczbaNieprzyjetych() == 0, "wszyscy przyjeci");

        System.out.println("Test procedury przydzialu przeszedl.");
    }
}
