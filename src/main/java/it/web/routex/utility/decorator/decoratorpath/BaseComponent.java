package it.web.routex.utility.decorator.decoratorpath;

import it.web.routex.bean.RouteBean;
import it.web.routex.domain.Route;


public class BaseComponent implements Component
{
    @Override
    public RouteBean update(RouteBean rb, Route r)
    {
        return rb;
    }
}

