package wyrazenia;

public abstract class Operacja2Arg extends Wyrazenie {
    private final Wyrazenie lewe;
    private final Wyrazenie prawe;

    public Operacja2Arg(Wyrazenie lewe, Wyrazenie prawe, int priorytet) {
        super(priorytet);
        this.lewe = lewe;
        this.prawe = prawe;
    }

    public Wyrazenie getLewe() {
        return lewe;
    }

    public Wyrazenie getPrawe() {
        return prawe;
    }
}
