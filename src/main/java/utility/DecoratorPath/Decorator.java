package utility.DecoratorPath;

import Bean.RouteBean;
import Model.Domain.Route;

public abstract class Decorator implements Component {
    protected Component component;

    public Decorator(Component component) {
        this.component= component;
    }

    @Override
    public RouteBean update(RouteBean rb, Route r) {
        return component.update(rb, r);
    }
}
