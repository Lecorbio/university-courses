public final class Katalog extends NazwanyElement {
    private static final int POCZATKOWA_POJEMNOSC = 4;
    private static final int MNOZNIK_REALOKACJI = 2;

    private Element[] zawartosc;
    private int liczbaElementow;

    public Katalog() {
        this("", null);
    }

    public Katalog(String nazwa, Katalog rodzic) {
        super(nazwa, rodzic);
        zawartosc = new Element[POCZATKOWA_POJEMNOSC];
        liczbaElementow = 0;
        dolaczDoRodzica();
    }

    public boolean czyPusty() {
        return liczbaElementow == 0;
    }

    public int liczbaElementow() {
        return liczbaElementow;
    }

    void dodajElement(Element element) {
        zapewnijMiejsceNaElement();
        zawartosc[liczbaElementow] = element;
        liczbaElementow++;
    }

    void usunElement(Element element) {
        int indeks = znajdzElement(element);

        for (int i = indeks; i < liczbaElementow - 1; i++) {
            zawartosc[i] = zawartosc[i + 1];
        }

        liczbaElementow--;
        zawartosc[liczbaElementow] = null;
    }

    @Override
    public void usun() {
        if (!czyPusty()) {
            return;
        }

        usunWszystkieDowiazaniaDoMnie();

        if (rodzic != null) {
            rodzic.usunElement(this);
            rodzic = null;
        }
    }

    public void wypiszZawartosc() {
        System.out.println(this);

        for (int i = 0; i < liczbaElementow; i++) {
            if (zawartosc[i] instanceof Katalog) {
                ((Katalog) zawartosc[i]).wypiszZawartosc();
            } else {
                System.out.println(zawartosc[i]);
            }
        }
    }

    String sciezkaDoZawartosci() {
        if (rodzic == null) {
            return "/";
        }

        return sciezkaRodzica() + nazwa + "/";
    }

    @Override
    public String toString() {
        if (rodzic == null) {
            return "/";
        }

        return sciezkaRodzica() + nazwa;
    }

    private int znajdzElement(Element element) {
        for (int i = 0; i < liczbaElementow; i++) {
            if (zawartosc[i] == element) {
                return i;
            }
        }

        return -1;
    }

    private void zapewnijMiejsceNaElement() {
        if (liczbaElementow == zawartosc.length) {
            Element[] nowaZawartosc = new Element[zawartosc.length * MNOZNIK_REALOKACJI];

            for (int i = 0; i < zawartosc.length; i++) {
                nowaZawartosc[i] = zawartosc[i];
            }

            zawartosc = nowaZawartosc;
        }
    }
}
