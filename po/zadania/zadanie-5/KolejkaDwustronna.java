public class KolejkaDwustronna {
    private static final int POCZATKOWA_POJEMNOSC = 4;
    private static final int MNOZNIK_REALOKACJI = 2;

    private int[] liczby;
    private int poczatek;
    private int koniec;
    private int rozmiar;

    public KolejkaDwustronna() {
        liczby = new int[POCZATKOWA_POJEMNOSC];
        poczatek = 0;
        koniec = 0;
        rozmiar = 0;
    }

    public void dodajPoczatek(int liczba) {
        zapewnijMiejsce();
        poczatek = poprzedniIndeks(poczatek);
        liczby[poczatek] = liczba;
        rozmiar++;
    }

    public void dodajKoniec(int liczba) {
        zapewnijMiejsce();
        liczby[koniec] = liczba;
        koniec = nastepnyIndeks(koniec);
        rozmiar++;
    }

    public int sprawdzPoczatek() {
        sprawdzCzyNiepusta();
        return liczby[poczatek];
    }

    public int sprawdzKoniec() {
        sprawdzCzyNiepusta();
        return liczby[poprzedniIndeks(koniec)];
    }

    public int usunPoczatek() {
        int wynik = sprawdzPoczatek();
        poczatek = nastepnyIndeks(poczatek);
        rozmiar--;
        return wynik;
    }

    public int usunKoniec() {
        int wynik = sprawdzKoniec();
        koniec = poprzedniIndeks(koniec);
        rozmiar--;
        return wynik;
    }

    public boolean czyPusta() {
        return rozmiar == 0;
    }

    public int rozmiar() {
        return rozmiar;
    }

    public int pojemnosc() {
        return liczby.length;
    }

    private void zapewnijMiejsce() {
        if (rozmiar == liczby.length) {
            realokuj();
        }
    }

    private void realokuj() {
        int[] noweLiczby = new int[liczby.length * MNOZNIK_REALOKACJI];

        for (int i = 0; i < rozmiar; i++) {
            noweLiczby[i] = liczby[(poczatek + i) % liczby.length];
        }

        liczby = noweLiczby;
        poczatek = 0;
        koniec = rozmiar;
    }

    private int nastepnyIndeks(int indeks) {
        return (indeks + 1) % liczby.length;
    }

    private int poprzedniIndeks(int indeks) {
        return (indeks - 1 + liczby.length) % liczby.length;
    }

    private void sprawdzCzyNiepusta() {
        if (czyPusta()) {
            throw new IllegalStateException("Kolejka jest pusta");
        }
    }

    @Override
    public String toString() {
        StringBuilder wynik = new StringBuilder("[");

        for (int i = 0; i < rozmiar; i++) {
            if (i > 0) {
                wynik.append(", ");
            }
            wynik.append(liczby[(poczatek + i) % liczby.length]);
        }

        wynik.append("]");
        return wynik.toString();
    }
}
