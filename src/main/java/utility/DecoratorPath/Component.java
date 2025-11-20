package utility.DecoratorPath;

import Bean.RouteBean;
import Model.Domain.Route;

public interface Component {
    RouteBean update(RouteBean rb, Route r);
}

