package it.web.routex.dao;

import it.web.routex.demo.*;
import it.web.routex.enumerator.PaymentMethod;
import it.web.routex.exception.*;
import it.web.routex.model.*;
import it.web.routex.utility.builder.RouteBuilder;
import it.web.routex.utility.singleton.Credentials;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LayerPersistenzaDemo extends LayerPersistenza{
    @Override
    public void login(String email, String password) throws DAOExceptionRemoli, LoginNotFoundRemoli
    {
        for (User u : DemoStorage.getUsers()) {
            if (u.getEmail().equals(email) && u.getPassword().equals(password))
            {
                Credentials cred = Credentials.getInstanceSingleton();
                cred.setCodiceFiscale(u.getCodiceFiscale());
                cred.setNome(u.getNome());
                cred.setCognome(u.getCognome());
                cred.setDataDiNascita(u.getDataDiNascita());
                cred.setDisabile(u.isDisabile());
                cred.setRuolo(u.getRuolo());
                cred.setEmail(email);
                cred.setPassword(password);
                return;
            }
        }
        throw new LoginNotFoundRemoli("Credenziali non valide.", email, password);

    }

    @Override
    public Mastercard getPaymentMastercard(String nC, String sc, String cvv)
            throws DAOExceptionRemoli, PaymentValidationExceptionRemoli {

        try {
            for (Mastercard m : DemoStorage.getMastercards()) {

                if (m.getNumeroCarta().equals(nC) && m.getDataScadenza().equals(sc) && m.getCvv().equals(cvv)) {

                    Mastercard found = new Mastercard();
                    found.setNumeroCarta(m.getNumeroCarta());
                    found.setDataScadenza(m.getDataScadenza());
                    found.setCvv(m.getCvv());

                    return found;
                }
            }

            throw new PaymentValidationExceptionRemoli(
                    "Nessun pagamento trovato per la carta indicata.",
                    PaymentMethod.MASTERCARD,
                    "MastercardDAO.java ha fallito"
            );

        } catch (PaymentValidationExceptionRemoli e) {
            throw e; // stesso comportamento della FULL
        } catch (Exception e) {
            throw new DAOExceptionRemoli(
                    "Errore interno alla connessione: " + e.getMessage()
            );
        }
    }



    @Override
    public Paypal getPaymentPaypal(String email, String codice)
            throws DAOExceptionRemoli, PaymentValidationExceptionRemoli {

        try {
            for (Paypal p : DemoStorage.getPaypals()) {

                if (p.getEmail().equals(email) && p.getCodice().equals(codice))
                {

                    Paypal found = new Paypal();
                    found.setEmail(p.getEmail());
                    found.setCodice(p.getCodice());

                    return found;
                }
            }

            throw new PaymentValidationExceptionRemoli(
                    "Nessun pagamento trovato per i dati Paypal inseriti.",
                    PaymentMethod.PAYPAL,
                    "PaypalDAO.java ha fallito"
            );

        } catch (PaymentValidationExceptionRemoli e) {
            throw e;
        } catch (Exception e) {
            throw new DAOExceptionRemoli(
                    "Errore in GetPaymentMastercard: " + e.getMessage()
            );
        }
    }


    @Override
    public List<City> listCities() throws DAOExceptionRemoli {

        try {
            if (DemoStorage.getCities().isEmpty()) {
                throw new DAOExceptionRemoli(
                        "Il database non ha restituito nessuna città. "
                                + "Possibile errore nella stored procedure o nel caricamento dati."
                );
            }

            List<City> informazioni = new ArrayList<>();

            for (City c : DemoStorage.getCities()) {
                informazioni.add(new City(c.getName(), c.getCostoBiglietto(), c.getNumeroStazioni()));
            }

            return informazioni;

        } catch (DAOExceptionRemoli e) {
            throw e; // stesso comportamento della FULL
        } catch (Exception e) {
            throw new DAOExceptionRemoli(
                    "Errore nella comunicazione con il database: " + e.getMessage()
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
                        n.isRisolto()
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
                    n.isRisolto()
            );

            DemoStorage.getNotifications().add(copy);

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

    public void salvataggio(Credentials cred, List<String> codiciBiglietti, String metodopayment, String city) throws CredentialsExceptionRemoli
    {
        try {
            if (cred == null || codiciBiglietti == null) {
                throw new CredentialsExceptionRemoli(
                        "Nessun salvataggio del percorso nel livello di persistenza",
                        "Errore in SalvaPagamentoDAO.java"
                );
            }

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

        } catch (CredentialsExceptionRemoli e) {
            throw e;

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
}
