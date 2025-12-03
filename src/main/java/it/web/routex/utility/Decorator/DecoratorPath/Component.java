package it.web.routex.utility.Decorator.DecoratorPath;

import it.web.routex.bean.RouteBean;
import it.web.routex.model.domain.Route;

public interface Component {
    RouteBean update(RouteBean rb, Route r);
}

