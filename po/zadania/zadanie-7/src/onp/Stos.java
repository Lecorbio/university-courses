package onp;

public class Stos {
    private double[] elementy;
    private int rozmiar;

    public Stos() {
        elementy = new double[8];
        rozmiar = 0;
    }

    public void wloz(double wartosc) {
        if (rozmiar == elementy.length) {
            powieksz();
        }

        elementy[rozmiar] = wartosc;
        rozmiar++;
    }

    public double zdejmij() {
        if (czyPusty()) {
            throw new IllegalStateException("Nie mozna zdjac elementu z pustego stosu");
        }

        rozmiar--;
        return elementy[rozmiar];
    }

    public int rozmiar() {
        return rozmiar;
    }

    public boolean czyPusty() {
        return rozmiar == 0;
    }

    private void powieksz() {
        double[] wiekszeElementy = new double[elementy.length * 2];

        for (int i = 0; i < elementy.length; i++) {
            wiekszeElementy[i] = elementy[i];
        }

        elementy = wiekszeElementy;
    }
}
