public final class Dowiazanie extends Element {
    private NazwanyElement cel;

    public Dowiazanie(NazwanyElement cel, Katalog rodzic) {
        super(rodzic);
        this.cel = cel;
        cel.dodajDowiazanie(this);
        dolaczDoRodzica();
    }

    public NazwanyElement pobierzElement() {
        return cel;
    }

    public void zmienKatalog(Katalog nowyRodzic) {
        przeniesDo(nowyRodzic);
    }

    @Override
    public void usun() {
        if (rodzic != null) {
            rodzic.usunElement(this);
            rodzic = null;
        }

        cel.usunDowiazanie(this);
    }

    @Override
    public String toString() {
        return sciezkaRodzica() + "(" + cel + ")";
    }
}
