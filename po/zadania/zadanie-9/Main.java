public class Main {
    public static void main(String[] args) {
        DrzewoBST<Integer> liczby = new DrzewoBST<>();
        liczby.wstaw(8);
        liczby.wstaw(3);
        liczby.wstaw(10);
        liczby.wstaw(1);
        liczby.wstaw(6);
        liczby.wstaw(14);
        liczby.wstaw(4);
        liczby.wstaw(7);
        liczby.wstaw(13);

        boolean dodanoDuplikatLiczby = liczby.wstaw(6);

        System.out.println("Drzewo liczb calkowitych:");
        System.out.println(liczby);
        System.out.println("rozmiar = " + liczby.rozmiar());
        System.out.println("Czy dodano drugi raz liczbe 6? " + dodanoDuplikatLiczby);
        System.out.println();

        DrzewoBST<Godzina> godziny = new DrzewoBST<>();
        godziny.wstaw(new Godzina(12, 30, 0));
        godziny.wstaw(new Godzina(8, 15, 42));
        godziny.wstaw(new Godzina(23, 59, 59));
        godziny.wstaw(new Godzina(6, 0, 0));
        godziny.wstaw(new Godzina(18, 45, 5));
        godziny.wstaw(new Godzina(8, 15, 43));

        boolean dodanoDuplikatGodziny = godziny.wstaw(new Godzina(12, 30, 0));

        System.out.println("Drzewo godzin:");
        System.out.println(godziny);
        System.out.println("rozmiar = " + godziny.rozmiar());
        System.out.println("Czy dodano drugi raz godzine 12:30:00? " + dodanoDuplikatGodziny);
    }
}
