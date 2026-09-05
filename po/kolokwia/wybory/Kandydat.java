public class Kandydat {
    private String nazwisko;
    private int liczbaGlosow;
    private boolean powiadomiony;
    private boolean uzyskalMandat;

    public Kandydat(String nazwisko, int liczbaGlosow) {
        this.nazwisko = nazwisko;
        this.liczbaGlosow = liczbaGlosow;
        this.powiadomiony = false;
        this.uzyskalMandat = false;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public int getLiczbaGlosow() {
        return liczbaGlosow;
    }

    public boolean czyPowiadomiony() {
        return powiadomiony;
    }

    public boolean czyUzyskalMandat() {
        return uzyskalMandat;
    }

    public void powiadomOUzyskaniuMandatu() {
        powiadomiony = true;
        uzyskalMandat = true;
    }

    public void powiadomONieuzyskaniuMandatu() {
        powiadomiony = true;
        uzyskalMandat = false;
    }

    @Override
    public String toString() {
        return nazwisko + " (" + liczbaGlosow + ")";
    }
}
