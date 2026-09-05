public class Godzina implements Comparable<Godzina> {
    private final int godzina;
    private final int minuta;
    private final int sekunda;

    public Godzina(int godzina, int minuta, int sekunda) {
        sprawdzZakres("Godzina", godzina, 0, 23);
        sprawdzZakres("Minuta", minuta, 0, 59);
        sprawdzZakres("Sekunda", sekunda, 0, 59);

        this.godzina = godzina;
        this.minuta = minuta;
        this.sekunda = sekunda;
    }

    @Override
    public int compareTo(Godzina inna) {
        return Integer.compare(liczbaSekundOdPolnocy(), inna.liczbaSekundOdPolnocy());
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", godzina, minuta, sekunda);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Godzina)) {
            return false;
        }

        Godzina inna = (Godzina) obj;
        return godzina == inna.godzina && minuta == inna.minuta && sekunda == inna.sekunda;
    }

    @Override
    public int hashCode() {
        return liczbaSekundOdPolnocy();
    }

    private int liczbaSekundOdPolnocy() {
        return godzina * 3600 + minuta * 60 + sekunda;
    }

    private static void sprawdzZakres(String nazwa, int wartosc, int minimum, int maksimum) {
        if (wartosc < minimum || wartosc > maksimum) {
            throw new IllegalArgumentException(nazwa + " musi byc z zakresu " + minimum + "-" + maksimum);
        }
    }
}
