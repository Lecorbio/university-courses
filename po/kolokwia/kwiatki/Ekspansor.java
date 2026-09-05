public class Ekspansor extends Kwiat {
    private int liczbaPlatkow;

    public Ekspansor(int liczbaPlatkow) {
        this.liczbaPlatkow = liczbaPlatkow;
    }

    @Override
    public void podlej(Rabatka rabatka, int wiersz, int kolumna) {
        for (int i = wiersz - 1; i <= wiersz + 1; i++) {
            for (int j = kolumna - 1; j <= kolumna + 1; j++) {
                if ((i != wiersz || j != kolumna)
                        && i >= 0 && i < rabatka.liczbaWierszy()
                        && j >= 0 && j < rabatka.liczbaKolumn()
                        && !rabatka.pustePole(i, j)) {
                    rabatka.posadz(new Ekspansor(liczbaPlatkow), i, j);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "" + liczbaPlatkow;
    }
}
