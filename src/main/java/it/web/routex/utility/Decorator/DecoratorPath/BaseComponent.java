package it.web.routex.utility.Decorator.DecoratorPath;

import it.web.routex.bean.RouteBean;
import it.web.routex.model.domain.Route;


public class BaseComponent implements Component
{
    @Override
    public RouteBean update(RouteBean rb, Route r)
    {
        return rb;
    }
}

