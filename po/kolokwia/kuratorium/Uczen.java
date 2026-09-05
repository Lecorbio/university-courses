public class Uczen {
    private int numer;
    private Zyczenie[] zyczenia;
    private Zyczenie spelnioneZyczenie;

    public Uczen(Zyczenie[] zyczenia) {
        this(0, zyczenia);
    }

    public Uczen(int numer, Zyczenie[] zyczenia) {
        this.numer = numer;
        this.zyczenia = zyczenia;
        this.spelnioneZyczenie = null;
    }

    public int getNumer() {
        return numer;
    }

    public Zyczenie[] getZyczenia() {
        return zyczenia;
    }

    public void setZyczenia(Zyczenie[] zyczenia) {
        this.zyczenia = zyczenia;
    }

    public Zyczenie getSpelnioneZyczenie() {
        return spelnioneZyczenie;
    }

    public void setSpelnioneZyczenie(Zyczenie spelnioneZyczenie) {
        this.spelnioneZyczenie = spelnioneZyczenie;
    }
}
