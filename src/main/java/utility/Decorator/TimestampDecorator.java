package utility.Decorator;

public class TimestampDecorator extends Decorator {

    public TimestampDecorator(Component component) {
        super(component);
    }

    @Override
    public String genera() {
        return System.currentTimeMillis() + "-" + component.genera();
    }
}


