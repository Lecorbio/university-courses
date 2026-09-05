public abstract class Element {
    protected Katalog rodzic;

    protected Element(Katalog rodzic) {
        this.rodzic = rodzic;
    }

    protected void dolaczDoRodzica() {
        if (rodzic != null) {
            rodzic.dodajElement(this);
        }
    }

    public Katalog getRodzic() {
        return rodzic;
    }

    protected void przeniesDo(Katalog nowyRodzic) {
        if (rodzic != null) {
            rodzic.usunElement(this);
        }

        rodzic = nowyRodzic;
        rodzic.dodajElement(this);
    }

    protected String sciezkaRodzica() {
        if (rodzic == null) {
            return "/";
        }

        return rodzic.sciezkaDoZawartosci();
    }

    public abstract void usun();
}
