public class DrzewoBST<T extends Comparable<T>> {
    private Wezel<T> korzen;
    private int rozmiar;

    public boolean wstaw(T wartosc) {
        if (wartosc == null) {
            throw new IllegalArgumentException("Nie mozna wstawic null");
        }

        if (korzen == null) {
            korzen = new Wezel<>(wartosc);
            rozmiar = 1;
            return true;
        }

        Wezel<T> aktualny = korzen;

        while (true) {
            int porownanie = wartosc.compareTo(aktualny.wartosc);

            if (porownanie == 0) {
                return false;
            }

            if (porownanie < 0) {
                if (aktualny.lewy == null) {
                    aktualny.lewy = new Wezel<>(wartosc);
                    rozmiar++;
                    return true;
                }
                aktualny = aktualny.lewy;
            } else {
                if (aktualny.prawy == null) {
                    aktualny.prawy = new Wezel<>(wartosc);
                    rozmiar++;
                    return true;
                }
                aktualny = aktualny.prawy;
            }
        }
    }

    public int rozmiar() {
        return rozmiar;
    }

    public boolean czyPuste() {
        return rozmiar == 0;
    }

    @Override
    public String toString() {
        StringBuilder wynik = new StringBuilder("[");
        dopiszWPorzadkuRosnacym(korzen, wynik);
        wynik.append("]");
        return wynik.toString();
    }

    private void dopiszWPorzadkuRosnacym(Wezel<T> wezel, StringBuilder wynik) {
        if (wezel == null) {
            return;
        }

        dopiszWPorzadkuRosnacym(wezel.lewy, wynik);

        if (wynik.length() > 1) {
            wynik.append(", ");
        }
        wynik.append(wezel.wartosc);

        dopiszWPorzadkuRosnacym(wezel.prawy, wynik);
    }

    private static class Wezel<T> {
        private final T wartosc;
        private Wezel<T> lewy;
        private Wezel<T> prawy;

        private Wezel(T wartosc) {
            this.wartosc = wartosc;
        }
    }
}
