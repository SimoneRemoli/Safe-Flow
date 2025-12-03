package it.web.routex.utility.Decorator.DecoratorTicket;

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
        preliminary = this.ApplyFoo(preliminary);
        return preliminary;
    }

    private String ApplyFoo(String input)
    {
        String out = citta.toUpperCase() + "-" + input;
        return out;
    }
}
