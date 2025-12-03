package it.web.routex.utility.Decorator.DecoratorTicket;

public class TimestampDecorator extends Decorator {

    public TimestampDecorator(Component component) {
        super(component);
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
        String out = System.currentTimeMillis() + "-" + input;
        return out;
    }
}


