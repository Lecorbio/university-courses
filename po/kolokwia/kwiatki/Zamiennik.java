public class Zamiennik extends Kwiat {
    @Override
    public void podlej(Rabatka rabatka, int wiersz, int kolumna) {
        int zachodniaKolumna = kolumna - 1;
        if (zachodniaKolumna >= 0 && !rabatka.pustePole(wiersz, zachodniaKolumna)) {
            rabatka.zamien(wiersz, kolumna, wiersz, zachodniaKolumna);
        }
    }

    @Override
    public String toString() {
        return "Z";
    }
}
