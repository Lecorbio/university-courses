public class Rabatka {
    private int liczbaWierszy;
    private int liczbaKolumn;
    private Kwiat[][] pola;

    public Rabatka(int liczbaWierszy, int liczbaKolumn) {
        this.liczbaWierszy = liczbaWierszy;
        this.liczbaKolumn = liczbaKolumn;
        this.pola = new Kwiat[liczbaWierszy][liczbaKolumn];
    }

    public Rabatka(Kwiat[][] pola) {
        this.liczbaWierszy = pola.length;
        this.liczbaKolumn = pola[0].length;
        this.pola = pola;
    }

    public int liczbaWierszy() {
        return liczbaWierszy;
    }

    public int liczbaKolumn() {
        return liczbaKolumn;
    }

    public boolean pustePole(int wiersz, int kolumna) {
        return pola[wiersz][kolumna] == null;
    }

    public void posadz(Kwiat kwiat, int wiersz, int kolumna) {
        pola[wiersz][kolumna] = kwiat;
    }

    public void zamien(int wiersz1, int kolumna1, int wiersz2, int kolumna2) {
        Kwiat pom = pola[wiersz1][kolumna1];
        pola[wiersz1][kolumna1] = pola[wiersz2][kolumna2];
        pola[wiersz2][kolumna2] = pom;
    }

    public void podlej() {
        for (int i = 0; i < liczbaWierszy; i++) {
            for (int j = 0; j < liczbaKolumn; j++) {
                if (pola[i][j] != null) {
                    pola[i][j].podlej(this, i, j);
                }
            }
        }
    }

    public void tancz() {
        if (liczbaKolumn <= 1) {
            return;
        }

        for (int i = 0; i < liczbaWierszy; i++) {
            int przesuniecie = (i + 1) % liczbaKolumn;
            if (przesuniecie != 0) {
                przesunWierszNaWschod(i, przesuniecie);
            }
        }
    }

    private void przesunWierszNaWschod(int wiersz, int przesuniecie) {
        odwrocFragmentWiersza(wiersz, 0, liczbaKolumn - 1);
        odwrocFragmentWiersza(wiersz, 0, przesuniecie - 1);
        odwrocFragmentWiersza(wiersz, przesuniecie, liczbaKolumn - 1);
    }

    private void odwrocFragmentWiersza(int wiersz, int lewy, int prawy) {
        while (lewy < prawy) {
            Kwiat pom = pola[wiersz][lewy];
            pola[wiersz][lewy] = pola[wiersz][prawy];
            pola[wiersz][prawy] = pom;
            lewy++;
            prawy--;
        }
    }

    @Override
    public String toString() {
        String wynik = "";
        for (int i = 0; i < liczbaWierszy; i++) {
            for (int j = 0; j < liczbaKolumn; j++) {
                if (pola[i][j] == null) {
                    wynik += ".";
                } else {
                    wynik += pola[i][j].toString();
                }
            }
            if (i + 1 < liczbaWierszy) {
                wynik += "\n";
            }
        }
        return wynik;
    }
}
