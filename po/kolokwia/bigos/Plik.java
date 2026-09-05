public final class Plik extends NazwanyElement {
    private long polozenieNaDysku;
    private boolean moznaCzytac;
    private boolean moznaModyfikowac;

    public Plik(String nazwa, Katalog rodzic, long polozenieNaDysku) {
        this(nazwa, rodzic, polozenieNaDysku, true, false);
    }

    public Plik(String nazwa, Katalog rodzic, long polozenieNaDysku,
            boolean moznaCzytac, boolean moznaModyfikowac) {
        super(nazwa, rodzic);
        this.polozenieNaDysku = polozenieNaDysku;
        this.moznaModyfikowac = moznaModyfikowac;
        this.moznaCzytac = moznaCzytac || moznaModyfikowac;
        dolaczDoRodzica();
    }

    public long getPolozenieNaDysku() {
        return polozenieNaDysku;
    }

    public boolean czyMoznaCzytac() {
        return moznaCzytac;
    }

    public boolean czyMoznaModyfikowac() {
        return moznaModyfikowac;
    }

    public void zmienKatalog(Katalog nowyRodzic) {
        przeniesDo(nowyRodzic);
    }

    public void zmienPrawaDostepu(boolean moznaCzytac, boolean moznaModyfikowac) {
        this.moznaModyfikowac = moznaModyfikowac;
        this.moznaCzytac = moznaCzytac || moznaModyfikowac;
    }

    @Override
    public void usun() {
        usunWszystkieDowiazaniaDoMnie();

        if (rodzic != null) {
            rodzic.usunElement(this);
            rodzic = null;
        }
    }

    @Override
    public String toString() {
        return sciezkaRodzica() + nazwa;
    }
}
