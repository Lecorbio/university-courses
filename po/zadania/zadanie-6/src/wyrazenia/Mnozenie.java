package wyrazenia;

public class Mnozenie extends Operacja2Arg {
    public Mnozenie(Wyrazenie lewe, Wyrazenie prawe) {
        super(lewe, prawe, 2);
    }

    @Override
    public double ewaluuj(double x) {
        return getLewe().ewaluuj(x) * getPrawe().ewaluuj(x);
    }

    @Override
    public Wyrazenie pochodna() {
        return Wyrazenie.dodaj(
            Wyrazenie.pomnoz(getLewe().pochodna(), getPrawe()),
            Wyrazenie.pomnoz(getLewe(), getPrawe().pochodna())
        );
    }

    @Override
    public String toString() {
        return argumentToString(getLewe()) + " * " + argumentToString(getPrawe());
    }

    private String argumentToString(Wyrazenie wyrazenie) {
        if (wyrazenie.getPriorytet() < getPriorytet()) {
            return "(" + wyrazenie + ")";
        }

        return wyrazenie.toString();
    }
}
