public class Klasa {
    private SzkolaSrednia szkola;
    private int profil;
    private int limit;
    private int wolne;
    private int liczbaUczniow;
    private Uczen[] uczniowie;

    public Klasa(SzkolaSrednia szkola, int limit) {
        this(szkola, 0, limit);
    }

    public Klasa(SzkolaSrednia szkola, int profil, int limit) {
        this.szkola = szkola;
        this.profil = profil;
        this.limit = limit;
        this.wolne = limit;
        this.liczbaUczniow = 0;
        this.uczniowie = new Uczen[limit];
    }

    public SzkolaSrednia getSzkola() {
        return szkola;
    }

    public int getProfil() {
        return profil;
    }

    public int getLimit() {
        return limit;
    }

    public int getWolne() {
        return wolne;
    }

    public boolean czyWolne() {
        return wolne > 0;
    }

    public void decWolne() {
        if (wolne > 0) {
            wolne--;
        }
    }

    public void incWolne() {
        if (wolne < limit) {
            wolne++;
        }
    }

    public int getLiczbaUczniow() {
        return liczbaUczniow;
    }

    public Uczen[] getUczniowie() {
        return uczniowie;
    }

    public void dodajUcznia(Uczen uczen) {
        if (liczbaUczniow < uczniowie.length) {
            uczniowie[liczbaUczniow] = uczen;
            liczbaUczniow++;
        }
    }

    public void wyczyscListeUczniow() {
        for (int i = 0; i < liczbaUczniow; i++) {
            uczniowie[i] = null;
        }
        liczbaUczniow = 0;
    }

    public void wyczyscPrzydzial() {
        wolne = limit;
        wyczyscListeUczniow();
    }
}
