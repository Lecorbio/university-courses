public class Main {
    public static void main(String[] args) {
        Ulamek a = new Ulamek(3, 5);
        Ulamek b = new Ulamek(2, 7);
        Ulamek c = new Ulamek(4, -6);
        Ulamek d = new Ulamek(6, 10);

        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("c = " + c);
        System.out.println("d = " + d);

        System.out.println("a + b = " + a.dodaj(b));
        System.out.println("a - b = " + a.odejmij(b));
        System.out.println("a * b = " + a.pomnoz(b));
        System.out.println("a / b = " + a.podziel(b));
        System.out.println("odwrotnosc a = " + a.odwroc());
        System.out.println("a < b ? " + a.czyMniejsze(b));
        System.out.println("c < b ? " + c.czyMniejsze(b));
        System.out.println("a == d ? " + a.czyRowne(d));
    }
}
