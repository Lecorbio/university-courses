package wyrazenia;

public class Zmienna extends Wyrazenie {
    public Zmienna() {
        super(3);
    }

    @Override
    public double ewaluuj(double x) {
        return x;
    }

    @Override
    public Wyrazenie pochodna() {
        return new Liczba(1);
    }

    @Override
    public String toString() {
        return "x";
    }
}
