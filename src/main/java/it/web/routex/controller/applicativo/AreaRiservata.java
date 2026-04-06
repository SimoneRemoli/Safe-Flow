package it.web.routex.controller.applicativo;

import it.web.routex.bean.RouteBean;
import it.web.routex.bean.TicketBean;
import it.web.routex.dao.LayerPersistenza;
import it.web.routex.dao.LayerPersistenzaDemo;
import it.web.routex.dao.TicketDAOLayer;
import it.web.routex.demo.DemoStorage;
import it.web.routex.demo.PaymentReg;
import it.web.routex.model.Route;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.exception.InvalidTicketExceptionRemoli;
import it.web.routex.exception.PathNotFoundExceptionRemoli;
import it.web.routex.model.Ticket;
import it.web.routex.utility.decorator.decoratorpath.*;
import it.web.routex.utility.factory.FactoryLayerPersistenza;
import it.web.routex.utility.factory.FactoryPersistence;
import it.web.routex.utility.singleton.ApplicationModeManager;

public class AreaRiservata
{
    public List<TicketBean> runTicket(String cf)
            throws DAOExceptionRemoli, PathNotFoundExceptionRemoli {

        List<TicketBean> beans = new ArrayList<>();
        List<Ticket> tickets;

        try {
            if(ApplicationModeManager.getSingletonInstance().getMode().toString().equals("DEMO")){

                LayerPersistenzaDemo layer = new LayerPersistenzaDemo();
                tickets = layer.getTicketByCFDemo(cf);

            }
            else {

                TicketDAOLayer dao = FactoryPersistence.createTicketDAO();
                tickets = dao.getTicketByCF(cf);
            }
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

        if (ApplicationModeManager.getSingletonInstance().getMode().toString().equals("DEMO")) {
            return deleteSelectedTicketsDemo(cf, ticketCodes);
        }

        TicketDAOLayer dao = FactoryPersistence.createTicketDAO();
        return dao.deleteTicketsByCodes(cf, ticketCodes);
    }

    public int deleteAllTickets(String cf) throws DAOExceptionRemoli {
        if (ApplicationModeManager.getSingletonInstance().getMode().toString().equals("DEMO")) {
            return deleteAllTicketsDemo(cf);
        }

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

    private int deleteSelectedTicketsDemo(String cf, List<String> ticketCodes) {
        Set<String> selectedCodes = ticketCodes.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(code -> !code.isEmpty())
                .collect(Collectors.toSet());

        int deleted = 0;
        ListIterator<PaymentReg> iterator = DemoStorage.getPayments().listIterator();
        while (iterator.hasNext()) {
            PaymentReg payment = iterator.next();
            if (!payment.getCodiceFiscale().equals(cf)) {
                continue;
            }

            List<String> remainingCodes = Arrays.stream(payment.getBiglietti().split(","))
                    .map(String::trim)
                    .filter(code -> !code.isEmpty())
                    .collect(Collectors.toCollection(ArrayList::new));

            int before = remainingCodes.size();
            remainingCodes.removeIf(selectedCodes::contains);
            deleted += before - remainingCodes.size();

            if (remainingCodes.isEmpty()) {
                iterator.remove();
            } else if (before != remainingCodes.size()) {
                iterator.set(new PaymentReg(
                        payment.getCodiceFiscale(),
                        payment.getNome(),
                        payment.getCognome(),
                        payment.isDisabile(),
                        payment.getMetodoPagamento(),
                        String.join(",", remainingCodes),
                        payment.getCity()
                ));
            }
        }
        return deleted;
    }

    private int deleteAllTicketsDemo(String cf) {
        int deleted = 0;
        ListIterator<PaymentReg> iterator = DemoStorage.getPayments().listIterator();
        while (iterator.hasNext()) {
            PaymentReg payment = iterator.next();
            if (!payment.getCodiceFiscale().equals(cf)) {
                continue;
            }

            deleted += (int) Arrays.stream(payment.getBiglietti().split(","))
                    .map(String::trim)
                    .filter(code -> !code.isEmpty())
                    .count();
            iterator.remove();
        }
        return deleted;
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
