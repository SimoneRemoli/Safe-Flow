package utility.Decorator.DecoratorPath;

import Bean.RouteBean;
import Model.Domain.Route;


public class BaseComponent implements Component
{
    @Override
    public RouteBean update(RouteBean rb, Route r)
    {
        return rb;
    }
}

