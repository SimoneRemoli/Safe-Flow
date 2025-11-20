package utility.DecoratorPath;

import Bean.RouteBean;
import Model.Domain.Route;

public class ListaCambiDecorator extends Decorator
{
    public ListaCambiDecorator(Component component) {
        super(component);
    }

    @Override
    public RouteBean update(RouteBean rb, Route r)
    {
        rb = super.update(rb, r);
        int numero_cambi = r.getnCambi();
        if(numero_cambi == 0) rb.setListaCambi("Diretto");
        return  rb;
    }
}
