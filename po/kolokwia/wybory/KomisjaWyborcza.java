public class KomisjaWyborcza {
    private KomitetWyborczy[] komitety;

    public KomisjaWyborcza(KomitetWyborczy[] komitety) {
        this.komitety = komitety;
    }

    public void podzielMandaty(int liczbaMandatow, double progProcentowy) {
        int wszystkieGlosy = policzGlosyKomitetow();
        przygotujKomitety(wszystkieGlosy, progProcentowy);
        przydzielMandatyKomitetom(liczbaMandatow);
        przydzielMandatyKandydatom();
    }

    private int policzGlosyKomitetow() {
        int wszystkieGlosy = 0;

        for (int i = 0; i < komitety.length; i++) {
            komitety[i].policzGlosy();
            wszystkieGlosy += komitety[i].getLiczbaGlosow();
        }

        return wszystkieGlosy;
    }

    private void przygotujKomitety(int wszystkieGlosy, double progProcentowy) {
        for (int i = 0; i < komitety.length; i++) {
            boolean osiagnalProg = czyOsiagnalProg(komitety[i], wszystkieGlosy, progProcentowy);
            komitety[i].przygotujDoPodzialuMandatow(osiagnalProg);
        }
    }

    private boolean czyOsiagnalProg(KomitetWyborczy komitet, int wszystkieGlosy, double progProcentowy) {
        if (wszystkieGlosy == 0) {
            return false;
        }

        return komitet.getLiczbaGlosow() * 100.0 >= wszystkieGlosy * progProcentowy;
    }

    private void przydzielMandatyKomitetom(int liczbaMandatow) {
        for (int i = 0; i < liczbaMandatow; i++) {
            int najlepszy = znajdzKomitetZNajwiekszymIlorazem();

            if (najlepszy == -1) {
                return;
            }

            komitety[najlepszy].dodajMandat();
        }
    }

    private int znajdzKomitetZNajwiekszymIlorazem() {
        int najlepszy = -1;

        for (int i = 0; i < komitety.length; i++) {
            if (komitety[i].czyOsiagnalProg() && czyLepszyIloraz(i, najlepszy)) {
                najlepszy = i;
            }
        }

        return najlepszy;
    }

    private boolean czyLepszyIloraz(int komitet, int najlepszy) {
        if (najlepszy == -1) {
            return true;
        }

        long glosyKomitetu = komitety[komitet].getLiczbaGlosow();
        long dzielnikKomitetu = komitety[komitet].getLiczbaMandatow() + 1;
        long glosyNajlepszego = komitety[najlepszy].getLiczbaGlosow();
        long dzielnikNajlepszego = komitety[najlepszy].getLiczbaMandatow() + 1;

        return glosyKomitetu * dzielnikNajlepszego > glosyNajlepszego * dzielnikKomitetu;
    }

    private void przydzielMandatyKandydatom() {
        for (int i = 0; i < komitety.length; i++) {
            komitety[i].przydzielMandatyKandydatom();
        }
    }
}
