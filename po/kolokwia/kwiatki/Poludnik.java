public class Poludnik extends Kwiat {
    @Override
    public void podlej(Rabatka rabatka, int wiersz, int kolumna) {
        int i = wiersz - 1;
        while (i >= 0 && rabatka.pustePole(i, kolumna)) {
            rabatka.posadz(new Poludnik(), i, kolumna);
            i--;
        }

        i = wiersz + 1;
        while (i < rabatka.liczbaWierszy() && rabatka.pustePole(i, kolumna)) {
            rabatka.posadz(new Poludnik(), i, kolumna);
            i++;
        }
    }

    @Override
    public String toString() {
        return "P";
    }
}
