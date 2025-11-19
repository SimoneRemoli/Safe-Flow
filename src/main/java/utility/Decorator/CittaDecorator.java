package utility.Decorator;

public class CittaDecorator extends Decorator {

    private final String citta;

    public CittaDecorator(Component component, String citta) {
        super(component);
        this.citta = citta;
    }

    @Override
    public String genera() {
        return citta.toUpperCase() + "-" + component.genera();
    }
}
