package it.web.routex.dao;

import it.web.routex.demo.*;
import it.web.routex.enumerator.Ruolo;
import it.web.routex.exception.*;
import it.web.routex.model.*;
import it.web.routex.utility.builder.UserBuilder;
import it.web.routex.utility.builder.RouteBuilder;
import it.web.routex.utility.singleton.Credentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class LayerPersistenzaDemo extends LayerPersistenza{
    @Override
    public Credentials login(String email, String password)
            throws DAOExceptionRemoli, LoginNotFoundRemoli {

        try {
            for (User u : DemoStorage.getUsers()) {

                if (u.getEmail().equals(email) && u.getPassword().equals(password)) {

                    Credentials cred = new Credentials(); // OGGETTO NORMALE

                    cred.setCodiceFiscale(u.getCodiceFiscale());
                    cred.setNome(u.getNome());
                    cred.setCognome(u.getCognome());
                    cred.setDataDiNascita(u.getDataDiNascita());
                    cred.setDisabile(u.isDisabile());
                    cred.setRuolo(u.getRuolo());
                    cred.setEmail(email);
                    cred.setPassword(password);

                    return cred;
                }
            }

            throw new LoginNotFoundRemoli("Invalid credentials.", email, password);

        } catch (LoginNotFoundRemoli e) {
            throw e;
        } catch (Exception e) {
            throw new DAOExceptionRemoli(
                    "Errore durante il login in modalità DEMO: " + e.getMessage(),
                    e
            );
        }
    }

    @Override
    public void registerTraveler(String nome,
                                 String cognome,
                                 String codiceFiscale,
                                 String email,
                                 String password,
                                 java.sql.Date dataDiNascita,
                                 boolean disabile) throws DAOExceptionRemoli {

        try {
            for (User user : DemoStorage.getUsers()) {
                if (user.getEmail().equalsIgnoreCase(email)) {
                    throw new DAOExceptionRemoli("An account with this email already exists.");
                }
                if (user.getCodiceFiscale().equalsIgnoreCase(codiceFiscale)) {
                    throw new DAOExceptionRemoli("An account with this tax code already exists.");
                }
            }

            User newTraveler = new UserBuilder(codiceFiscale)
                    .nome(nome)
                    .cognome(cognome)
                    .dataDiNascita(dataDiNascita)
                    .disabile(disabile)
                    .ruolo(Ruolo.TRAVELER)
                    .email(email)
                    .password(password)
                    .build();

            DemoStorage.getUsers().add(newTraveler);

        } catch (DAOExceptionRemoli e) {
            throw e;
        } catch (Exception e) {
            throw new DAOExceptionRemoli("Error while creating the traveler account.", e);
        }
    }

    @Override
    public Mastercard getPaymentMastercard(String nC, String sc, String cvv)
            throws DAOExceptionRemoli {

        try {
            for (Mastercard m : DemoStorage.getMastercards()) {

                if (m.getNumeroCarta().equals(nC)
                        && m.getDataScadenza().equals(sc)
                        && m.getCvv().equals(cvv)) {

                    Mastercard found = new Mastercard();
                    found.setNumeroCarta(m.getNumeroCarta());
                    found.setDataScadenza(m.getDataScadenza());
                    found.setCvv(m.getCvv());

                    return found;
                }
            }

            //  nessun risultato : NON errore DAO
            return null;

        } catch (Exception e) {
            throw new DAOExceptionRemoli(
                    "Errore interno alla persistenza DEMO: " + e.getMessage(),
                    e
            );
        }
    }

    @Override
    public Paypal getPaymentPaypal(String email, String codice)
            throws DAOExceptionRemoli {

        try {
            for (Paypal p : DemoStorage.getPaypals()) {

                if (p.getEmail().equals(email)
                        && p.getCodice().equals(codice)) {

                    Paypal found = new Paypal();
                    found.setEmail(p.getEmail());
                    found.setCodice(p.getCodice());
                    return found;
                }
            }

            // nessun pagamento trovato decisione applicativa
            return null;

        } catch (Exception e) {
            throw new DAOExceptionRemoli(
                    "Errore nel recupero del pagamento Paypal in modalità DEMO: " + e.getMessage(),
                    e
            );
        }
    }


    @Override
    public List<City> listCities() throws DAOExceptionRemoli {

        try {
            List<City> informazioni = new ArrayList<>();

            for (City c : DemoStorage.getCities()) {
                informazioni.add(
                        new City(
                                c.getName(),
                                c.getCostoBiglietto(),
                                c.getNumeroStazioni()
                        )
                );
            }

            // può essere vuota, decisione rimandata al controller applicativo
            return informazioni;

        } catch (Exception e) {
            throw new DAOExceptionRemoli(
                    "Errore nella comunicazione con il database: " + e.getMessage(),
                    e
            );
        }
    }



    @Override
    public List<Fermata> getFermateByIds(List<Integer> ids, String city) throws SQLException {

        List<Fermata> fermateAll = new ArrayList<>();

        try {
            for (int id : ids) {

                for (Fermate f : DemoStorage.getFermate()) {

                    if (id == f.getId()) {
                        fermateAll.add(
                                new Fermata(
                                        f.getNome(),
                                        f.getLinea()
                                )
                        );
                    }
                }
            }

            return fermateAll;

        } catch (Exception e) {
            // equivalente concettuale della SQLException
            throw new SQLException("Errore durante il recupero delle fermate (DEMO)", e);
        }
    }


    @Override
    public List<Station> restituisciIdStazioni(String startStation, String endStation, String city) throws SQLException {

        List<Station> stations = new ArrayList<>();

        try {
            Integer startId = null;
            Integer endId = null;

            // simulazione della stored procedure
            for (Fermate f : DemoStorage.getFermate()) {

                if (startId == null && f.getNome().equalsIgnoreCase(startStation)) {
                    startId = f.getId();
                }

                if (endId == null && f.getNome().equalsIgnoreCase(endStation)) {
                    endId = f.getId();
                }
            }

            // primo result set
            if (startId != null) {
                stations.add(new Station(startId));
            }

            // secondo result set
            if (endId != null) {
                stations.add(new Station(endId));
            }

            return stations;

        } catch (Exception e) {
            throw new SQLException(
                    "Errore durante la simulazione della stored procedure RestituisciStazioni",
                    e
            );
        }
    }


    @Override
    public List<Notification> getMessages() throws DAOExceptionRemoli {

        List<Notification> result = new ArrayList<>();

        try {
            // simulazione del ResultSet
            for (Notification n : DemoStorage.getNotifications()) {

                Notification copy = new Notification(
                        n.getMessage(),
                        n.getDate(),
                        n.isRisolto(),
                        n.isApprovato(),
                        n.isLetto(),
                        n.getStatus(),
                        n.getSenderRole(),
                        n.getSenderCf(),
                        n.getRecipientCf(),
                        n.getCity(),
                        n.isPickpocketAlert(),
                        n.isFightAlert(),
                        n.isCrowdAlert(),
                        n.isGeneralAlert(),
                        n.getStationName(),
                        n.getSuspectClothing()
                );

                result.add(copy);
            }

            //  NESSUN controllo su lista vuota
            return result;

        } catch (Exception e) {
            throw new DAOExceptionRemoli(
                    "Errore nel recupero delle notifiche (DEMO)",
                    e
            );
        }
    }

    @Override
    public List<Credentials> listAdmins() throws DAOExceptionRemoli {
        try {
            List<Credentials> admins = new ArrayList<>();

            for (User user : DemoStorage.getUsers()) {
                if (user.getRuolo() == Ruolo.ADMIN) {
                    Credentials admin = new Credentials();
                    admin.setNome(user.getNome());
                    admin.setCognome(user.getCognome());
                    admin.setEmail(user.getEmail());
                    admin.setCodiceFiscale(user.getCodiceFiscale());
                    admin.setPassword(user.getPassword());
                    admin.setRuolo(user.getRuolo());
                    admins.add(admin);
                }
            }

            return admins;
        } catch (Exception e) {
            throw new DAOExceptionRemoli("Error while loading admin accounts in demo mode: " + e.getMessage(), e);
        }
    }

    @Override
    public void createAdmin(String nome,
                            String cognome,
                            String email,
                            String password,
                            String codiceFiscale) throws DAOExceptionRemoli {
        try {
            DemoStorage.getUsers().add(
                    new UserBuilder(codiceFiscale)
                            .nome(nome)
                            .cognome(cognome)
                            .dataDiNascita(java.sql.Date.valueOf("2000-01-01"))
                            .disabile(false)
                            .ruolo(Ruolo.ADMIN)
                            .email(email)
                            .password(password)
                            .build()
            );
        } catch (Exception e) {
            throw new DAOExceptionRemoli("Error while creating the admin account in demo mode: " + e.getMessage(), e);
        }
    }

    @Override
    public int deleteAdmins(List<String> codiceFiscali) throws DAOExceptionRemoli {
        try {
            int deleted = 0;
            Iterator<User> iterator = DemoStorage.getUsers().iterator();

            while (iterator.hasNext()) {
                User user = iterator.next();
                if (user.getRuolo() == Ruolo.ADMIN && codiceFiscali.contains(user.getCodiceFiscale().toUpperCase())) {
                    iterator.remove();
                    deleted++;
                }
            }

            return deleted;
        } catch (Exception e) {
            throw new DAOExceptionRemoli("Error while deleting admin accounts in demo mode: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Credentials> listWorkers() throws DAOExceptionRemoli {
        return listUsersByRole(Ruolo.WORKER);
    }

    @Override
    public int deleteWorkers(List<String> codiceFiscali) throws DAOExceptionRemoli {
        return deleteUsersByRole(codiceFiscali, Ruolo.WORKER, "worker");
    }

    @Override
    public List<Credentials> listTravelers() throws DAOExceptionRemoli {
        return listUsersByRole(Ruolo.TRAVELER);
    }

    @Override
    public int deleteTravelers(List<String> codiceFiscali) throws DAOExceptionRemoli {
        return deleteUsersByRole(codiceFiscali, Ruolo.TRAVELER, "traveler");
    }

    private List<Credentials> listUsersByRole(Ruolo ruolo) throws DAOExceptionRemoli {
        try {
            List<Credentials> users = new ArrayList<>();

            for (User user : DemoStorage.getUsers()) {
                if (user.getRuolo() == ruolo) {
                    Credentials cred = new Credentials();
                    cred.setNome(user.getNome());
                    cred.setCognome(user.getCognome());
                    cred.setEmail(user.getEmail());
                    cred.setCodiceFiscale(user.getCodiceFiscale());
                    cred.setPassword(user.getPassword());
                    cred.setRuolo(user.getRuolo());
                    users.add(cred);
                }
            }

            return users;
        } catch (Exception e) {
            throw new DAOExceptionRemoli("Error while loading demo accounts: " + e.getMessage(), e);
        }
    }

    private int deleteUsersByRole(List<String> codiceFiscali,
                                  Ruolo ruolo,
                                  String label) throws DAOExceptionRemoli {
        try {
            int deleted = 0;
            Iterator<User> iterator = DemoStorage.getUsers().iterator();

            while (iterator.hasNext()) {
                User user = iterator.next();
                if (user.getRuolo() == ruolo && codiceFiscali.contains(user.getCodiceFiscale().toUpperCase())) {
                    iterator.remove();
                    deleted++;
                }
            }

            return deleted;
        } catch (Exception e) {
            throw new DAOExceptionRemoli("Error while deleting " + label + " accounts in demo mode: " + e.getMessage(), e);
        }
    }



    @Override
    public void save(Route route) throws DAOExceptionRemoli {

        try {
            if (route == null) {
                throw new DAOExceptionRemoli(
                        "Errore durante la registrazione del percorso: route null"
                );
            }

            // simulazione INSERT INTO Route
            Route copy = new RouteBuilder(route.getPartenza()).endStation(route.getArrivo())
                            .city(route.getCitta()).tipoViaggiatore(route.getTipoViaggiatore())
                            .nCambi(route.getnCambi()).listaCambi(route.getListaCambi())
                            .stazioneDiInterscambio(route.getStazInterscambio()).nStazioniAttraversate(route.getnStazAttraversate())
                            .tempoDiArrivo(route.getTempoDiArrivo()).nStazioniCitta(route.getnStazioniCitta())
                            .percTerrenoUtilizzato(route.getPercTerrenoUtilizzato()).utente(route.getUtente())
                            .build();

            DemoStorage.getRoutes().add(copy);

        } catch (DAOExceptionRemoli e) {
            throw e;

        } catch (Exception e) {
            throw new DAOExceptionRemoli(
                    "Errore durante la registrazione del percorso: " + e.getMessage()
            );
        }
    }


    @Override
    public WorkerSchedule getWorkerSchedule(String codiceFiscale)
            throws DAOExceptionRemoli {

        try {
            for (WorkerScheduleRecord r : DemoStorage.getWorkerSchedules()) {

                if (r.getCodiceFiscale().equals(codiceFiscale)) {
                    return new WorkerSchedule(
                            r.getOraInizio(),
                            r.getOraFine(),
                            r.getLuogoDiLavoro()
                    );
                }
            }
            return null;

        } catch (Exception e) {
            throw new DAOExceptionRemoli(
                    "Errore durante il recupero degli orari di lavoro in modalità DEMO",
                    e
            );
        }
    }



    @Override
    public void sendMessage(Notification n) throws DAOExceptionRemoli {

        try {
            if (n == null) {
                throw new DAOExceptionRemoli("Errore durante l'invio della comunicazione");
            }

            // simulazione INSERT INTO communication
            Notification copy = new Notification(
                    n.getMessage(),
                    n.getDate(),
                    n.isRisolto(),
                    n.isApprovato(),
                    n.isLetto(),
                    n.getStatus(),
                    n.getSenderRole(),
                    n.getSenderCf(),
                    n.getRecipientCf(),
                    n.getCity(),
                    n.isPickpocketAlert(),
                    n.isFightAlert(),
                    n.isCrowdAlert(),
                    n.isGeneralAlert(),
                    n.getStationName(),
                    n.getSuspectClothing()
            );

            DemoStorage.getNotifications().add(copy);
            invalidateNotificationsCache();

        } catch (DAOExceptionRemoli e) {
            throw e;

        } catch (Exception e) {
            throw new DAOExceptionRemoli(
                    "Errore durante l'invio della comunicazione",
                    e
            );
        }
    }


    @Override
    public void solvedNotification(Notification n) throws DAOExceptionRemoli {

        try {
            if (n == null) {
                throw new DAOExceptionRemoli(
                        "Errore durante l'aggiornamento della notifica"
                );
            }

            boolean updated = false;

            for (Notification stored : DemoStorage.getNotifications()) {

                // criterio di matching: stesso messaggio + stessa data
                if (stored.getMessage().equals(n.getMessage()) && stored.getDate().equals(n.getDate())) {

                    stored.setRisolto(n.isRisolto());
                    updated = true;
                    break;
                }
            }

            if (!updated) {
                // simulazione: UPDATE che non trova righe
                throw new DAOExceptionRemoli(
                        "Errore durante l'aggiornamento della notifica"
                );
            }

            invalidateNotificationsCache();

        } catch (DAOExceptionRemoli e) {
            throw e;

        } catch (Exception e) {
            throw new DAOExceptionRemoli(
                    "Errore durante l'aggiornamento della notifica",
                    e
            );
        }
    }

    @Override
    public void approveNotification(Notification n) throws DAOExceptionRemoli {

        try {
            if (n == null) {
                throw new DAOExceptionRemoli(
                        "Error while approving the notification"
                );
            }

            boolean updated = false;

            for (Notification stored : DemoStorage.getNotifications()) {
                if (stored.getMessage().equals(n.getMessage()) && stored.getDate().equals(n.getDate())) {
                    stored.setApprovato(true);
                    updated = true;
                    break;
                }
            }

            if (!updated) {
                throw new DAOExceptionRemoli(
                        "Error while approving the notification"
                );
            }

            invalidateNotificationsCache();

        } catch (DAOExceptionRemoli e) {
            throw e;

        } catch (Exception e) {
            throw new DAOExceptionRemoli(
                    "Error while approving the notification in demo mode",
                    e
            );
        }
    }

    @Override
    public void deleteNotification(Notification n) throws DAOExceptionRemoli {

        try {
            if (n == null) {
                throw new DAOExceptionRemoli("Error while deleting the notification");
            }

            boolean deleted = DemoStorage.getNotifications().removeIf(
                    stored -> stored.getMessage().equals(n.getMessage()) && stored.getDate().equals(n.getDate())
            );

            if (!deleted) {
                throw new DAOExceptionRemoli("Error while deleting the notification");
            }

            invalidateNotificationsCache();

        } catch (DAOExceptionRemoli e) {
            throw e;
        } catch (Exception e) {
            throw new DAOExceptionRemoli(
                    "Error while deleting the notification in demo mode",
                    e
            );
        }
    }

    @Override
    public void markNotificationAsRead(Notification n) throws DAOExceptionRemoli {
        try {
            if (n == null) {
                throw new DAOExceptionRemoli("Error while marking the notification as read");
            }

            boolean updated = false;
            for (Notification stored : DemoStorage.getNotifications()) {
                if (stored.getMessage().equals(n.getMessage()) && stored.getDate().equals(n.getDate())) {
                    stored.setLetto(true);
                    updated = true;
                    break;
                }
            }

            if (!updated) {
                throw new DAOExceptionRemoli("Error while marking the notification as read");
            }

            invalidateNotificationsCache();
        } catch (DAOExceptionRemoli e) {
            throw e;
        } catch (Exception e) {
            throw new DAOExceptionRemoli("Error while marking the notification as read in demo mode", e);
        }
    }

    @Override
    public boolean approvePendingTravelerNotification(Notification n) throws DAOExceptionRemoli {
        try {
            if (n == null) {
                throw new DAOExceptionRemoli("Error while approving the traveler report");
            }

            for (Notification stored : DemoStorage.getNotifications()) {
                if (stored.getMessage().equals(n.getMessage()) && stored.getDate().equals(n.getDate())) {
                    if (!"PENDING".equalsIgnoreCase(stored.getStatus())) {
                        return false;
                    }
                    stored.setStatus("APPROVED");
                    stored.setApprovato(true);
                    invalidateNotificationsCache();
                    return true;
                }
            }

            return false;
        } catch (DAOExceptionRemoli e) {
            throw e;
        } catch (Exception e) {
            throw new DAOExceptionRemoli("Error while approving the traveler report in demo mode", e);
        }
    }

    @Override
    public boolean rejectPendingTravelerNotification(Notification n) throws DAOExceptionRemoli {
        try {
            if (n == null) {
                throw new DAOExceptionRemoli("Error while rejecting the traveler report");
            }

            for (Notification stored : DemoStorage.getNotifications()) {
                if (stored.getMessage().equals(n.getMessage()) && stored.getDate().equals(n.getDate())) {
                    if (!"PENDING".equalsIgnoreCase(stored.getStatus())) {
                        return false;
                    }
                    stored.setStatus("REJECTED");
                    stored.setApprovato(false);
                    invalidateNotificationsCache();
                    return true;
                }
            }

            return false;
        } catch (DAOExceptionRemoli e) {
            throw e;
        } catch (Exception e) {
            throw new DAOExceptionRemoli("Error while rejecting the traveler report in demo mode", e);
        }
    }


    @Override
    public List<Route> getAllPathInfo() throws DAOExceptionRemoli {

        List<Route> resultList = new ArrayList<>();

        try {
            for (Route r : DemoStorage.getRoutes()) {

                // simulazione ResultSet -> nuova riga
                Route copy = new RouteBuilder(r.getPartenza()).endStation(r.getArrivo())
                        .city(r.getCitta()).tipoViaggiatore(r.getTipoViaggiatore())
                        .nCambi(r.getnCambi()).listaCambi(r.getListaCambi())
                        .stazioneDiInterscambio(r.getStazInterscambio()).nStazioniAttraversate(r.getnStazAttraversate())
                        .tempoDiArrivo(r.getTempoDiArrivo()).nStazioniCitta(r.getnStazioniCitta())
                        .percTerrenoUtilizzato(r.getPercTerrenoUtilizzato()).utente(r.getUtente())
                        .build();

                resultList.add(copy);
            }

            return resultList;

        } catch (Exception e) {
            throw new DAOExceptionRemoli(
                    "Errore nel recupero delle statistiche PathInfo",
                    e
            );
        }
    }
    @Override
    public void salvataggio(Credentials cred, List<String> codiciBiglietti, String metodopayment, String city) throws CredentialsExceptionRemoli
    {
        final Logger logger = LoggerFactory.getLogger(getClass());
        logger.info("Modalità DEMO: il pagamento non viene salvato in persistenza.");
        try {

            String bigliettiConcatenati = String.join(",", codiciBiglietti);

            // simulazione INSERT SavePayment
            PaymentReg records = new PaymentReg(
                    cred.getCodiceFiscale(),
                    cred.getNome(),
                    cred.getCognome(),
                    cred.getDisabile(),
                    metodopayment,
                    bigliettiConcatenati,
                    city
            );

            DemoStorage.getPayments().add(records);

        } catch (Exception e) {
            throw new CredentialsExceptionRemoli(
                    "Nessun salvataggio del percorso nel livello di persistenza " + e.getMessage(),
                    "Errore in SalvaPagamentoDAO.java"
            );
        }
    }
    public List<Ticket> getTicketByCFDemo(String cf) throws DAOExceptionRemoli, PathNotFoundExceptionRemoli {

        try {
            List<Ticket> result = new ArrayList<>();

            for (PaymentReg p : DemoStorage.getPayments()) {

                if (p.getCodiceFiscale().equals(cf)) {

                    // simulazione dei ticket salvati nel pagamento
                    String[] codici = p.getBiglietti().split(",");

                    for (String codice : codici) {
                        Ticket t = new Ticket(
                                codice.trim(),
                                p.getCity(),
                                LocalDateTime.now() // simulazione data_pagamento
                        );
                        result.add(t);
                    }
                }
            }

            if (result.isEmpty()) {
                throw new PathNotFoundExceptionRemoli(
                        "Nessun biglietto trovato",
                        cf,
                        404,
                        "TicketDAODemo.getTicketByCF"
                );
            }

            return result;

        } catch (PathNotFoundExceptionRemoli e) {
            throw e;

        } catch (Exception e) {
            throw new DAOExceptionRemoli(
                    "Errore DEMO durante il recupero dei biglietti",
                    e
            );
        }
    }
    @Override
    public List<Route> getData(String cf) throws DAOExceptionRemoli {

        try {
            List<Route> result = new ArrayList<>();

            for (Route r : DemoStorage.getRoutes()) {

                if (r.getUtente() != null && r.getUtente().equals(cf)) {
                    Route copy = new Route();
                    copy.setPartenza(r.getPartenza());
                    copy.setArrivo(r.getArrivo());
                    copy.setCitta(r.getCitta());
                    copy.setTipoViaggiatore(r.getTipoViaggiatore());
                    copy.setnCambi(r.getnCambi());
                    copy.setnStazAttraversate(r.getnStazAttraversate());
                    copy.setTempoDiArrivo(r.getTempoDiArrivo());
                    copy.setnStazioniCitta(r.getnStazioniCitta());
                    copy.setPercTerrenoUtilizzato(r.getPercTerrenoUtilizzato());
                    copy.setUtente(cf);
                    copy.setListaCambi(r.getListaCambi());
                    copy.setStazInterscambio(r.getStazInterscambio());

                    result.add(copy);
                }
            }

            // SEMPRE ritorna la lista (anche vuota)
            return result;

        } catch (Exception e) {
            throw new DAOExceptionRemoli(
                    "Errore durante il recupero dei percorsi in modalità DEMO: " + e.getMessage()
            );
        }
    }

    @Override
    public int deleteRoutesBySignatures(String cf, List<String> routeSignatures) throws DAOExceptionRemoli {
        int deleted = 0;

        try {
            for (String signature : routeSignatures) {
                Iterator<Route> iterator = DemoStorage.getRoutes().iterator();
                while (iterator.hasNext()) {
                    Route route = iterator.next();
                    if (cf.equals(route.getUtente()) && signature.equals(buildRouteSignature(route))) {
                        iterator.remove();
                        deleted++;
                        break;
                    }
                }
            }
            return deleted;
        } catch (Exception e) {
            throw new DAOExceptionRemoli("Errore durante l'eliminazione dei percorsi in modalità DEMO: " + e.getMessage(), e);
        }
    }

    @Override
    public int deleteAllRoutes(String cf) throws DAOExceptionRemoli {
        int deleted = 0;

        try {
            Iterator<Route> iterator = DemoStorage.getRoutes().iterator();
            while (iterator.hasNext()) {
                Route route = iterator.next();
                if (cf.equals(route.getUtente())) {
                    iterator.remove();
                    deleted++;
                }
            }
            return deleted;
        } catch (Exception e) {
            throw new DAOExceptionRemoli("Errore durante l'eliminazione completa dei percorsi in modalità DEMO: " + e.getMessage(), e);
        }
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
