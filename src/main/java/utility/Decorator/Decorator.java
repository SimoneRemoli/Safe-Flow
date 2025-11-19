package utility.Decorator;

public abstract class Decorator implements Component {

    protected Component component;

    public Decorator(Component component) {
        this.component = component;
    }
    public String genera()
    {
        String message = this.component.genera();
        return message;
    }
}