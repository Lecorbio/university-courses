package wyrazenia;

public abstract class Wyrazenie {
    private final int priorytet;

    public Wyrazenie(int priorytet) {
        this.priorytet = priorytet;
    }

    public abstract double ewaluuj(double x);

    public abstract Wyrazenie pochodna();

    public int getPriorytet() {
        return priorytet;
    }

    public static Wyrazenie dodaj(Wyrazenie lewe, Wyrazenie prawe) {
        if (czyLiczba(lewe, 0)) {
            return prawe;
        }

        if (czyLiczba(prawe, 0)) {
            return lewe;
        }

        if (lewe instanceof Liczba && prawe instanceof Liczba) {
            Liczba lewaLiczba = (Liczba) lewe;
            Liczba prawaLiczba = (Liczba) prawe;

            return new Liczba(lewaLiczba.getWartosc() + prawaLiczba.getWartosc());
        }

        return new Dodawanie(lewe, prawe);
    }

    public static Wyrazenie pomnoz(Wyrazenie lewe, Wyrazenie prawe) {
        if (czyLiczba(lewe, 0) || czyLiczba(prawe, 0)) {
            return new Liczba(0);
        }

        if (czyLiczba(lewe, 1)) {
            return prawe;
        }

        if (czyLiczba(prawe, 1)) {
            return lewe;
        }

        if (lewe instanceof Liczba && prawe instanceof Liczba) {
            Liczba lewaLiczba = (Liczba) lewe;
            Liczba prawaLiczba = (Liczba) prawe;

            return new Liczba(lewaLiczba.getWartosc() * prawaLiczba.getWartosc());
        }

        return new Mnozenie(lewe, prawe);
    }

    private static boolean czyLiczba(Wyrazenie wyrazenie, double wartosc) {
        if (!(wyrazenie instanceof Liczba)) {
            return false;
        }

        Liczba liczba = (Liczba) wyrazenie;
        return liczba.getWartosc() == wartosc;
    }

    public double calkuj(double a, double b, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Liczba przedzialow musi byc dodatnia");
        }

        double suma = 0;
        double dx = (b - a) / n;

        for (int i = 0; i < n; i++) {
            double x1 = a + i * dx;
            double x2 = a + (i + 1) * dx;
            double y1 = ewaluuj(x1);
            double y2 = ewaluuj(x2);

            suma += (y1 + y2) * dx / 2;
        }

        return suma;
    }
}
