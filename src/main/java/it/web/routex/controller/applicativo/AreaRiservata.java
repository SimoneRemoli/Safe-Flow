package it.web.routex.controller.applicativo;

import it.web.routex.bean.RouteBean;
import it.web.routex.bean.TicketBean;
import it.web.routex.dao.RouteDAO;
import it.web.routex.dao.TicketDAOLayer;
import it.web.routex.model.Route;
import java.util.ArrayList;
import java.util.List;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.exception.InvalidTicketExceptionRemoli;
import it.web.routex.exception.PathNotFoundExceptionRemoli;
import it.web.routex.model.Ticket;
import it.web.routex.utility.decorator.decoratorpath.*;
import it.web.routex.utility.factory.FactoryPersistence;

public class AreaRiservata
{
    public List<TicketBean> runTicket(String cf)
            throws DAOExceptionRemoli, PathNotFoundExceptionRemoli {

        TicketDAOLayer dao = FactoryPersistence.createTicketDAO();
        List<Ticket> tickets = dao.getTicketByCF(cf);

        List<TicketBean> beans = new ArrayList<>();

        for (Ticket t : tickets) {
            try {
                // VALIDAZIONE FORTE DI DOMINIO
                t.validate();

                TicketBean b = new TicketBean();
                b.setCodice(t.getCodice());
                b.setCitta(t.getCitta());
                b.setDataAcquisto(t.getDataAcquisto().toString());

                beans.add(b);

            } catch (InvalidTicketExceptionRemoli e) {

                // scelta progettuale
                throw new PathNotFoundExceptionRemoli(
                        "Sono stati trovati ticket non validi associati all’utente.",
                        cf,
                        500,
                        "Errore dominio Ticket: " + e.getMessage()
                );
            }
        }
        return beans;
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
