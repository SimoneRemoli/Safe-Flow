package Controller.Applicativo;

import Bean.RouteBean;
import Bean.TicketBean;
import Model.DAO.RouteDAO;
import Model.DAO.TicketDAODB;
import Model.DAO.TicketDAOLayer;
import Model.Domain.Route;

import java.util.ArrayList;
import java.util.List;
import Exception.DAOExceptionRemoli;
import Exception.PathNotFoundExceptionRemoli;
import Model.Domain.TypesOfPersistenceLayer;
import utility.Decorator.DecoratorPath.*;
import utility.Factory.FactoryPersistence;

public class AreaRiservata
{
    public List<TicketBean> runTicket(String cf) throws DAOExceptionRemoli, PathNotFoundExceptionRemoli {
        //TicketDAODB ticketDAO = new TicketDAODB();
        //List<TicketBean> tickets = ticketDAO.getTicketByCF(cf);
        TicketDAOLayer daolayer = FactoryPersistence.createTicketDAO();
        List<TicketBean> tickets = daolayer.getTicketByCF(cf);
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
