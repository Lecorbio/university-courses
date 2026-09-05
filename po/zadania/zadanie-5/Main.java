public class Main {
    private static void wypiszStan(String opis, KolejkaDwustronna kolejka) {
        System.out.println(opis);
        System.out.println("kolejka = " + kolejka);
        System.out.println("rozmiar = " + kolejka.rozmiar());
        System.out.println("pojemnosc = " + kolejka.pojemnosc());
        System.out.println("czy pusta? " + kolejka.czyPusta());
        System.out.println();
    }

    public static void main(String[] args) {
        KolejkaDwustronna kolejka = new KolejkaDwustronna();

        wypiszStan("Stan poczatkowy", kolejka);

        kolejka.dodajKoniec(10);
        kolejka.dodajKoniec(20);
        kolejka.dodajPoczatek(5);
        kolejka.dodajPoczatek(1);
        wypiszStan("Po dodaniu czterech elementow", kolejka);

        System.out.println("sprawdzPoczatek = " + kolejka.sprawdzPoczatek());
        System.out.println("sprawdzKoniec = " + kolejka.sprawdzKoniec());
        System.out.println();

        kolejka.dodajKoniec(30);
        wypiszStan("Po dodaniu piatego elementu, czyli po realokacji", kolejka);

        System.out.println("usunPoczatek = " + kolejka.usunPoczatek());
        System.out.println("usunKoniec = " + kolejka.usunKoniec());
        wypiszStan("Po usunieciu z poczatku i konca", kolejka);

        kolejka.dodajPoczatek(0);
        kolejka.dodajKoniec(40);
        wypiszStan("Po ponownym dodaniu na oba konce", kolejka);

        while (!kolejka.czyPusta()) {
            System.out.println("zdejmujemy z poczatku: " + kolejka.usunPoczatek());
        }

        wypiszStan("Po oproznieniu kolejki", kolejka);
    }
}
