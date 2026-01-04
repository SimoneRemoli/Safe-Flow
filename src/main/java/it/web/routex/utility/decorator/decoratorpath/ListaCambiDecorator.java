package it.web.routex.utility.decorator.decoratorpath;

import it.web.routex.bean.RouteBean;
import it.web.routex.domain.Route;

public class ListaCambiDecorator extends Decorator
{
    public ListaCambiDecorator(Component component) {
        super(component);
    }

    @Override
    public RouteBean update(RouteBean rb, Route r)
    {
        rb = super.update(rb, r);
        int numeroCambi = r.getnCambi();
        if(numeroCambi == 0) rb.setListaCambi("Diretto");
        return  rb;
    }
}
