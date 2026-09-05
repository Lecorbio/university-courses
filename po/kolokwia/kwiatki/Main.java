public class Main {
    public static void main(String[] args) {
        Rabatka rabatka = new Rabatka(5, 10);

        rabatka.posadz(new Poludnik(), 0, 1);
        rabatka.posadz(new Poludnik(), 0, 5);
        rabatka.posadz(new Poludnik(), 1, 3);
        rabatka.posadz(new Poludnik(), 1, 7);
        rabatka.posadz(new Ekspansor(7), 2, 2);
        rabatka.posadz(new Zamiennik(), 2, 7);
        rabatka.posadz(new Ekspansor(9), 3, 3);
        rabatka.posadz(new Zamiennik(), 4, 6);

        System.out.println(rabatka);
        System.out.println();

        rabatka.podlej();
        System.out.println(rabatka);
        System.out.println();

        rabatka.tancz();
        System.out.println(rabatka);
    }
}
