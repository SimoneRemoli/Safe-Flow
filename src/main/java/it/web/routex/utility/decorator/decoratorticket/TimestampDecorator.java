package it.web.routex.utility.decorator.decoratorticket;

public class TimestampDecorator extends Decorator {

    public TimestampDecorator(Component component) {
        super(component);
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
        return System.currentTimeMillis() + "-" + input;
    }
}


