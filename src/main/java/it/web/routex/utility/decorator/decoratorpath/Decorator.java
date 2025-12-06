package it.web.routex.utility.decorator.decoratorpath;

import it.web.routex.bean.RouteBean;
import it.web.routex.model.domain.Route;

public abstract class Decorator implements Component {
    protected Component component;

    protected Decorator(Component component) {
        this.component= component;
    }

    @Override
    public RouteBean update(RouteBean rb, Route r) {
        return component.update(rb, r);
    }
}
