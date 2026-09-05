public class KomitetWyborczy {
    private String nazwa;
    private Kandydat[] kandydaci;
    private Kandydat[] kandydaciZMandatem;
    private int liczbaGlosow;
    private int liczbaMandatow;
    private boolean osiagnalProg;

    public KomitetWyborczy(String nazwa, Kandydat[] kandydaci) {
        this.nazwa = nazwa;
        this.kandydaci = kandydaci;
        this.kandydaciZMandatem = new Kandydat[0];
        this.liczbaGlosow = 0;
        this.liczbaMandatow = 0;
        this.osiagnalProg = false;
    }

    public String getNazwa() {
        return nazwa;
    }

    public Kandydat[] getKandydaci() {
        return kandydaci;
    }

    public Kandydat[] getKandydaciZMandatem() {
        return kandydaciZMandatem;
    }

    public int getLiczbaGlosow() {
        return liczbaGlosow;
    }

    public int getLiczbaMandatow() {
        return liczbaMandatow;
    }

    public boolean czyOsiagnalProg() {
        return osiagnalProg;
    }

    public void policzGlosy() {
        liczbaGlosow = 0;

        for (int i = 0; i < kandydaci.length; i++) {
            liczbaGlosow += kandydaci[i].getLiczbaGlosow();
        }
    }

    public void przygotujDoPodzialuMandatow(boolean osiagnalProg) {
        this.osiagnalProg = osiagnalProg;
        this.liczbaMandatow = 0;
        this.kandydaciZMandatem = new Kandydat[0];
    }

    public void dodajMandat() {
        liczbaMandatow++;
    }

    public void przydzielMandatyKandydatom() {
        kandydaciZMandatem = new Kandydat[liczbaMandatow];
        boolean[] wybrani = new boolean[kandydaci.length];

        for (int i = 0; i < liczbaMandatow; i++) {
            int najlepszy = znajdzNajlepszegoNiewybranegoKandydata(wybrani);
            wybrani[najlepszy] = true;
            kandydaciZMandatem[i] = kandydaci[najlepszy];
        }

        powiadomKandydatow(wybrani);
    }

    private int znajdzNajlepszegoNiewybranegoKandydata(boolean[] wybrani) {
        int najlepszy = -1;

        for (int i = 0; i < kandydaci.length; i++) {
            if (!wybrani[i] && czyLepszyKandydat(i, najlepszy)) {
                najlepszy = i;
            }
        }

        return najlepszy;
    }

    private boolean czyLepszyKandydat(int kandydat, int najlepszy) {
        if (najlepszy == -1) {
            return true;
        }

        return kandydaci[kandydat].getLiczbaGlosow() > kandydaci[najlepszy].getLiczbaGlosow();
    }

    private void powiadomKandydatow(boolean[] wybrani) {
        for (int i = 0; i < kandydaci.length; i++) {
            if (wybrani[i]) {
                kandydaci[i].powiadomOUzyskaniuMandatu();
            } else {
                kandydaci[i].powiadomONieuzyskaniuMandatu();
            }
        }
    }
}
