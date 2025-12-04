package it.web.routex.utility.decorator.decoratorticket;

public abstract class Decorator implements Component {

    protected Component component;

    protected Decorator(Component component) {
        this.component = component;
    }
    public String genera()
    {
        return this.component.genera();
    }
}