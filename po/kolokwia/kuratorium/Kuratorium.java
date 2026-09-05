public class Kuratorium {
    private Zyczenie[] listaZyczen;
    private SzkolaSrednia[] szkoly;
    private Uczen[] uczniowie;
    private Uczen[] nieprzyjeci;
    private int liczbaNieprzyjetych;

    public Kuratorium(Zyczenie[] listaZyczen, SzkolaSrednia[] szkoly, Uczen[] uczniowie) {
        this.listaZyczen = listaZyczen;
        this.szkoly = szkoly;
        this.uczniowie = uczniowie;
        this.nieprzyjeci = new Uczen[uczniowie.length];
        this.liczbaNieprzyjetych = 0;
    }

    public void algorytmPrzydzialu() {
        przygotujAlgorytm();

        int i = 0;
        while (i < listaZyczen.length) {
            Zyczenie zyczenie = listaZyczen[i];

            if (zyczenie.getStatus() == Zyczenie.Status.PRZYJETE) {
                i++;
            } else {
                int nastepnaPozycja = rozpatrzZyczenie(zyczenie, i);
                if (nastepnaPozycja < i) {
                    i = nastepnaPozycja;
                } else {
                    i++;
                }
            }
        }

        stworzListy();
    }

    public Uczen[] getNieprzyjeci() {
        return nieprzyjeci;
    }

    public int getLiczbaNieprzyjetych() {
        return liczbaNieprzyjetych;
    }

    private void przygotujAlgorytm() {
        for (int i = 0; i < listaZyczen.length; i++) {
            listaZyczen[i].setPozycja(i);
            listaZyczen[i].setStatus(Zyczenie.Status.NIE_ROZPATRZONE);
        }

        for (int i = 0; i < uczniowie.length; i++) {
            uczniowie[i].setSpelnioneZyczenie(null);
        }

        for (int i = 0; i < szkoly.length; i++) {
            Klasa[] klasy = szkoly[i].getKlasy();
            for (int j = 0; j < klasy.length; j++) {
                klasy[j].wyczyscPrzydzial();
            }
        }
    }

    private int rozpatrzZyczenie(Zyczenie zyczenie, int biezacaPozycja) {
        if (!zyczenie.getKlasa().czyWolne()) {
            zyczenie.setStatus(Zyczenie.Status.ODRZUCONE);
            return biezacaPozycja;
        }

        Uczen uczen = zyczenie.getUczen();
        Zyczenie poprzednie = uczen.getSpelnioneZyczenie();

        if (poprzednie == null) {
            zaakceptuj(zyczenie);
            return biezacaPozycja;
        }

        if (poprzednie.getNumer() < zyczenie.getNumer()) {
            zyczenie.setStatus(Zyczenie.Status.ODRZUCONE);
            return biezacaPozycja;
        }

        odrzucPrzyjete(poprzednie);
        zaakceptuj(zyczenie);

        if (poprzednie.getPozycja() < zyczenie.getPozycja()) {
            return poprzednie.getPozycja() + 1;
        }

        return biezacaPozycja;
    }

    private void zaakceptuj(Zyczenie zyczenie) {
        zyczenie.setStatus(Zyczenie.Status.PRZYJETE);
        zyczenie.getUczen().setSpelnioneZyczenie(zyczenie);
        zyczenie.getKlasa().decWolne();
    }

    private void odrzucPrzyjete(Zyczenie zyczenie) {
        zyczenie.setStatus(Zyczenie.Status.ODRZUCONE);
        zyczenie.getKlasa().incWolne();

        if (zyczenie.getUczen().getSpelnioneZyczenie() == zyczenie) {
            zyczenie.getUczen().setSpelnioneZyczenie(null);
        }
    }

    private void stworzListy() {
        wyczyscListyKlas();
        liczbaNieprzyjetych = 0;

        for (int i = 0; i < uczniowie.length; i++) {
            Zyczenie spelnione = uczniowie[i].getSpelnioneZyczenie();

            if (spelnione == null) {
                nieprzyjeci[liczbaNieprzyjetych] = uczniowie[i];
                liczbaNieprzyjetych++;
            } else {
                spelnione.getKlasa().dodajUcznia(uczniowie[i]);
            }
        }

        for (int i = liczbaNieprzyjetych; i < nieprzyjeci.length; i++) {
            nieprzyjeci[i] = null;
        }
    }

    private void wyczyscListyKlas() {
        for (int i = 0; i < szkoly.length; i++) {
            Klasa[] klasy = szkoly[i].getKlasy();
            for (int j = 0; j < klasy.length; j++) {
                klasy[j].wyczyscListeUczniow();
            }
        }
    }
}
