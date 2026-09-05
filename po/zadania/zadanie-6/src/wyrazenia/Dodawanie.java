package wyrazenia;

public class Dodawanie extends Operacja2Arg {
    public Dodawanie(Wyrazenie lewe, Wyrazenie prawe) {
        super(lewe, prawe, 1);
    }

    @Override
    public double ewaluuj(double x) {
        return getLewe().ewaluuj(x) + getPrawe().ewaluuj(x);
    }

    @Override
    public Wyrazenie pochodna() {
        return Wyrazenie.dodaj(getLewe().pochodna(), getPrawe().pochodna());
    }

    @Override
    public String toString() {
        return argumentToString(getLewe()) + " + " + argumentToString(getPrawe());
    }

    private String argumentToString(Wyrazenie wyrazenie) {
        if (wyrazenie.getPriorytet() < getPriorytet()) {
            return "(" + wyrazenie + ")";
        }

        return wyrazenie.toString();
    }
}
