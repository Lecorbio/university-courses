public abstract class NazwanyElement extends Element {
    private static final int POCZATKOWA_POJEMNOSC = 4;
    private static final int MNOZNIK_REALOKACJI = 2;

    protected String nazwa;
    private Dowiazanie[] dowiazania;
    private int liczbaDowiazan;

    protected NazwanyElement(String nazwa, Katalog rodzic) {
        super(rodzic);
        this.nazwa = nazwa;
        dowiazania = new Dowiazanie[POCZATKOWA_POJEMNOSC];
        liczbaDowiazan = 0;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void zmienNazwe(String nowaNazwa) {
        nazwa = nowaNazwa;
    }

    void dodajDowiazanie(Dowiazanie dowiazanie) {
        zapewnijMiejsceNaDowiazanie();
        dowiazania[liczbaDowiazan] = dowiazanie;
        liczbaDowiazan++;
    }

    void usunDowiazanie(Dowiazanie dowiazanie) {
        int indeks = znajdzDowiazanie(dowiazanie);

        for (int i = indeks; i < liczbaDowiazan - 1; i++) {
            dowiazania[i] = dowiazania[i + 1];
        }

        liczbaDowiazan--;
        dowiazania[liczbaDowiazan] = null;
    }

    protected void usunWszystkieDowiazaniaDoMnie() {
        while (liczbaDowiazan > 0) {
            dowiazania[0].usun();
        }
    }

    private int znajdzDowiazanie(Dowiazanie dowiazanie) {
        for (int i = 0; i < liczbaDowiazan; i++) {
            if (dowiazania[i] == dowiazanie) {
                return i;
            }
        }

        return -1;
    }

    private void zapewnijMiejsceNaDowiazanie() {
        if (liczbaDowiazan == dowiazania.length) {
            Dowiazanie[] noweDowiazania = new Dowiazanie[dowiazania.length * MNOZNIK_REALOKACJI];

            for (int i = 0; i < dowiazania.length; i++) {
                noweDowiazania[i] = dowiazania[i];
            }

            dowiazania = noweDowiazania;
        }
    }
}
