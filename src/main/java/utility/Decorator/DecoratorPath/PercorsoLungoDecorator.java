package utility.Decorator.DecoratorPath;

import Bean.RouteBean;
import Model.Domain.Route;

public class PercorsoLungoDecorator extends Decorator
{
    public PercorsoLungoDecorator(Component component) {
        super(component);
    }

    public RouteBean update(RouteBean rb, Route r)
    {
        rb = super.update(rb,r);
        if(r.getnStazAttraversate()==1)
        {
            rb.setTempoDiArrivo(0.0);
        }
        return rb;
    }
}
