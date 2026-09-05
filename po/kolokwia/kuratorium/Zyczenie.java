public class Zyczenie {
    public enum Status {
        NIE_ROZPATRZONE,
        PRZYJETE,
        ODRZUCONE
    }

    private Klasa klasa;
    private Uczen uczen;
    private int numer;
    private int pozycja;
    private Status status;
    private int punkty;

    public Zyczenie(Klasa klasa, Uczen uczen, int numer) {
        this(klasa, uczen, numer, 0);
    }

    public Zyczenie(Klasa klasa, Uczen uczen, int numer, int punkty) {
        this.klasa = klasa;
        this.uczen = uczen;
        this.numer = numer;
        this.punkty = punkty;
        this.pozycja = -1;
        this.status = Status.NIE_ROZPATRZONE;
    }

    public Klasa getKlasa() {
        return klasa;
    }

    public Uczen getUczen() {
        return uczen;
    }

    public int getNumer() {
        return numer;
    }

    public int getPozycja() {
        return pozycja;
    }

    public Status getStatus() {
        return status;
    }

    public int getPunkty() {
        return punkty;
    }

    public void setPozycja(int pozycja) {
        this.pozycja = pozycja;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
