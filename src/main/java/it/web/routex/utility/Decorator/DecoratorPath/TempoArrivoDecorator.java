package it.web.routex.utility.Decorator.DecoratorPath;

import it.web.routex.bean.RouteBean;
import it.web.routex.model.domain.Route;

public class TempoArrivoDecorator extends Decorator
{
    public TempoArrivoDecorator(Component component) {
        super(component);
    }
    @Override
    public RouteBean update(RouteBean rb, Route r)
    {
        rb = super.update(rb,r);
        if(rb.getTempoDiArrivo()==0.0)
        {
            rb.setPercTerrenoUtilizzato(0.0);
        }
        return rb;
    }
}
