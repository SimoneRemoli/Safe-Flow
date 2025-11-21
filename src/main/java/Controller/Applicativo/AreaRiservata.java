package Controller.Applicativo;

import Bean.RouteBean;
import Bean.TicketBean;
import Model.DAO.RouteDAO;
import Model.DAO.TicketDAO;
import Model.Domain.Route;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Exception.DAOExceptionRemoli;
import Exception.PathNotFoundExceptionRemoli;
import utility.Decorator.DecoratorPath.*;

public class AreaRiservata
{
    public List<TicketBean> runTicket(String cf) throws DAOExceptionRemoli, PathNotFoundExceptionRemoli {
        TicketDAO ticketDAO = new TicketDAO();
        List<TicketBean> tickets = ticketDAO.getTicketByCF(cf);
        return tickets;
    }
    public List<RouteBean> runPath(String cf) throws PathNotFoundExceptionRemoli, DAOExceptionRemoli {
        RouteDAO routeDAO = new RouteDAO();
        // Ottieni la lista di percorsi dal DAO
        List<Route> listaPercorsi = routeDAO.getData(cf);
        List<RouteBean> listaPercorsiBean = new ArrayList<>();

        Component component = new TempoArrivoDecorator(new PercorsoLungoDecorator(new ListaCambiDecorator(new BaseComponent()))); //in più

        for(Route r: listaPercorsi)
        {
            RouteBean rb = new RouteBean();
            rb.setPartenza(r.getPartenza());
            rb.setArrivo(r.getArrivo());
            rb.setCitta(r.getCitta());
            rb.setnCambi(r.getnCambi());
            rb.setListaCambi(r.getListaCambi());
            rb.setStazInterscambio(r.getStazInterscambio());
            rb.setnStazAttraversate(r.getnStazAttraversate());
            rb.setTempoDiArrivo(r.getTempoDiArrivo());
            rb.setnStazioniCitta(r.getnStazioniCitta());
            rb.setPercTerrenoUtilizzato(r.getPercTerrenoUtilizzato());

            rb = component.update(rb,r); //in più


            listaPercorsiBean.add(rb);

        }
        return listaPercorsiBean;
    }
}
