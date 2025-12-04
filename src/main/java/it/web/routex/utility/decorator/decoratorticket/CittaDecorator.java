package it.web.routex.utility.decorator.decoratorticket;

public class CittaDecorator extends Decorator {

    private final String citta;

    public CittaDecorator(Component component, String citta) {
        super(component);
        this.citta = citta;
    }

    @Override
    public String genera()
    {
        String preliminary = super.genera();
        preliminary = this.applyFoo(preliminary);
        return preliminary;
    }

    private String applyFoo(String input)
    {
        return citta.toUpperCase() + "-" + input;
    }
}
