public class Ulamek {
    private int licznik;
    private int mianownik;

    public Ulamek(int licznik, int mianownik) {
        if (mianownik == 0) {
            throw new IllegalArgumentException("Mianownik nie moze byc 0");
        }

        if (licznik == 0) {
            this.licznik = 0;
            this.mianownik = 1;
            return;
        }

        if (mianownik < 0) {
            licznik = -licznik;
            mianownik = -mianownik;
        }

        int d = nwd(licznik, mianownik);
        this.licznik = licznik / d;
        this.mianownik = mianownik / d;
    }

    private static int nwd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);

        while (b != 0) {
            int r = a % b;
            a = b;
            b = r;
        }

        return a;
    }

    public int getLicznik() {
        return licznik;
    }

    public int getMianownik() {
        return mianownik;
    }

    public Ulamek odwroc() {
        if (licznik == 0) {
            throw new ArithmeticException("Nie mozna odwrocic zera");
        }

        return new Ulamek(mianownik, licznik);
    }

    public Ulamek dodaj(Ulamek other) {
        int nowyLicznik = licznik * other.mianownik + other.licznik * mianownik;
        int nowyMianownik = mianownik * other.mianownik;
        return new Ulamek(nowyLicznik, nowyMianownik);
    }

    public Ulamek odejmij(Ulamek other) {
        int nowyLicznik = licznik * other.mianownik - other.licznik * mianownik;
        int nowyMianownik = mianownik * other.mianownik;
        return new Ulamek(nowyLicznik, nowyMianownik);
    }

    public Ulamek pomnoz(Ulamek other) {
        int nowyLicznik = licznik * other.licznik;
        int nowyMianownik = mianownik * other.mianownik;
        return new Ulamek(nowyLicznik, nowyMianownik);
    }

    public Ulamek podziel(Ulamek other) {
        return pomnoz(other.odwroc());
    }

    public boolean czyMniejsze(Ulamek other) {
        return (long) licznik * other.mianownik < (long) other.licznik * mianownik;
    }

    public boolean czyRowne(Ulamek other) {
        return licznik == other.licznik && mianownik == other.mianownik;
    }

    @Override
    public String toString() {
        return licznik + "/" + mianownik;
    }
}
