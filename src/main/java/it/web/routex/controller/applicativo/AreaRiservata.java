package it.web.routex.controller.applicativo;

import it.web.routex.bean.RouteBean;
import it.web.routex.bean.TicketBean;
import it.web.routex.dao.LayerPersistenza;
import it.web.routex.dao.TicketDAOLayer;
import it.web.routex.model.Route;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.exception.InvalidTicketExceptionRemoli;
import it.web.routex.exception.PathNotFoundExceptionRemoli;
import it.web.routex.model.Ticket;
import it.web.routex.utility.decorator.decoratorpath.*;
import it.web.routex.utility.factory.FactoryLayerPersistenza;
import it.web.routex.utility.factory.FactoryPersistence;

public class AreaRiservata
{
    public List<TicketBean> runTicket(String cf)
            throws DAOExceptionRemoli, PathNotFoundExceptionRemoli {

        List<TicketBean> beans = new ArrayList<>();
        List<Ticket> tickets;

        try {
            TicketDAOLayer dao = FactoryPersistence.createTicketDAO();
            tickets = dao.getTicketByCF(cf);
        } catch (PathNotFoundExceptionRemoli e) {
            return beans;
        }

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

    public int deleteSelectedTickets(String cf, List<String> ticketCodes) throws DAOExceptionRemoli {
        if (ticketCodes == null || ticketCodes.isEmpty()) {
            return 0;
        }

        TicketDAOLayer dao = FactoryPersistence.createTicketDAO();
        return dao.deleteTicketsByCodes(cf, ticketCodes);
    }

    public int deleteAllTickets(String cf) throws DAOExceptionRemoli {
        TicketDAOLayer dao = FactoryPersistence.createTicketDAO();
        return dao.deleteAllTickets(cf);
    }

    public int deleteSelectedRoutes(String cf, List<String> routeSignatures) throws DAOExceptionRemoli {
        if (routeSignatures == null || routeSignatures.isEmpty()) {
            return 0;
        }

        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        return layer.deleteRoutesBySignatures(cf, routeSignatures);
    }

    public int deleteAllRoutes(String cf) throws DAOExceptionRemoli {
        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        return layer.deleteAllRoutes(cf);
    }


    public List<RouteBean> runPath(String cf)
            throws PathNotFoundExceptionRemoli, DAOExceptionRemoli {

        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        List<Route> listaPercorsi;

        try {
            listaPercorsi = layer.getData(cf);
        } catch (PathNotFoundExceptionRemoli e) {
            return new ArrayList<>();
        }

        List<RouteBean> listaPercorsiBean = new ArrayList<>();

        Component component =
                new TempoArrivoDecorator(
                        new PercorsoLungoDecorator(
                                new ListaCambiDecorator(
                                        new BaseComponent()
                                )
                        )
                );

        for (Route r : listaPercorsi) {

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
            rb.setSignature(buildRouteSignature(r));

            // Decorator applicativo
            rb = component.update(rb, r);

            listaPercorsiBean.add(rb);
        }

        return listaPercorsiBean;
    }

    private String buildRouteSignature(Route route) {
        return String.join("||", Arrays.asList(
                route.getPartenza(),
                route.getArrivo(),
                route.getCitta(),
                route.getTipoViaggiatore(),
                String.valueOf(route.getnCambi()),
                route.getListaCambi(),
                route.getStazInterscambio(),
                String.valueOf(route.getnStazAttraversate()),
                String.valueOf(route.getTempoDiArrivo()),
                String.valueOf(route.getnStazioniCitta()),
                String.valueOf(route.getPercTerrenoUtilizzato())
        ));
    }

}
