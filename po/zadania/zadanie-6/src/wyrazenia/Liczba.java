package wyrazenia;

public class Liczba extends Wyrazenie {
    private final double wartosc;

    public Liczba(double wartosc) {
        super(3);
        this.wartosc = wartosc;
    }

    public double getWartosc() {
        return wartosc;
    }

    @Override
    public double ewaluuj(double x) {
        return wartosc;
    }

    @Override
    public Wyrazenie pochodna() {
        return new Liczba(0);
    }

    @Override
    public String toString() {
        return Double.toString(wartosc);
    }
}
