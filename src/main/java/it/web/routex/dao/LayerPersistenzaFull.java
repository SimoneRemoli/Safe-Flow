package it.web.routex.dao;
import it.web.routex.enumerator.Ruolo;
import it.web.routex.exception.CredentialsExceptionRemoli;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.exception.LoginNotFoundRemoli;
import it.web.routex.exception.PathNotFoundExceptionRemoli;
import it.web.routex.model.*;
import it.web.routex.utility.builder.RouteBuilder;
import it.web.routex.utility.factory.ConnectionFactory;
import it.web.routex.utility.singleton.Credentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LayerPersistenzaFull extends LayerPersistenza
{
    public Credentials login(String email, String password)
            throws DAOExceptionRemoli, LoginNotFoundRemoli {

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.login_user(?, ?) }")) {

            cs.setString(1, email);
            cs.setString(2, password);

            ResultSet rs = cs.executeQuery();

            if (!rs.next()) {
                throw new LoginNotFoundRemoli("Invalid credentials.", email, password);
            }

            Ruolo ruolo = Ruolo.fromint(rs.getInt("ruolo"));
            if (ruolo == null) {
                throw new LoginNotFoundRemoli(
                        "This account type is no longer supported.",
                        email,
                        password
                );
            }

            Credentials c = new Credentials();
            c.setCodiceFiscale(rs.getString("p_codiceFiscale"));
            c.setNome(rs.getString("p_nome"));
            c.setCognome(rs.getString("p_cognome"));
            c.setDataDiNascita(rs.getDate("p_dataDiNascita"));
            c.setDisabile(rs.getBoolean("p_disabile"));
            c.setRuolo(ruolo);
            c.setEmail(email);
            c.setPassword(password);

            return c;

        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Errore durante il login: " + e.getMessage(), e);
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

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);

            try (CallableStatement registerRegistry = conn.prepareCall("{ CALL RouteX_Update.register(?, ?) }");
                 CallableStatement registerUser = conn.prepareCall("{ CALL RouteX_Update.register_User(?, ?, ?, ?, ?, ?, ?, ?) }")) {

                registerRegistry.setString(1, codiceFiscale);
                registerRegistry.setString(2, email);
                registerRegistry.execute();

                registerUser.setString(1, nome);
                registerUser.setString(2, cognome);
                registerUser.setString(3, codiceFiscale);
                registerUser.setString(4, password);
                registerUser.setString(5, email);
                registerUser.setDate(6, dataDiNascita);
                registerUser.setBoolean(7, disabile);
                registerUser.setInt(8, Ruolo.TRAVELER.getId());
                registerUser.execute();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Error while creating the traveler account: " + e.getMessage(), e);
        }
    }


    public Mastercard getPaymentMastercard(String nC, String sc, String cvv)
            throws DAOExceptionRemoli {

        final String query = "{ CALL RouteX_Update.getMastercardPayment(?,?,?) }";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(query)) {

            cs.setString(1, nC);
            cs.setString(2, sc);
            cs.setString(3, cvv);

            try (ResultSet rs = cs.executeQuery()) {

                if (rs.next()) {
                    Mastercard found = new Mastercard();
                    found.setNumeroCarta(rs.getString("numero_carta"));
                    found.setDataScadenza(rs.getString("data_scadenza"));
                    found.setCvv(rs.getString("cvv"));
                    return found;
                }

                // nessun risultato → NON è errore DAO
                return null;
            }

        } catch (SQLException e) {
            throw new DAOExceptionRemoli(
                    "Errore interno alla connessione: " + e.getMessage(),
                    e
            );
        }
    }

    @Override
    public Paypal getPaymentPaypal(String email, String codice)
            throws DAOExceptionRemoli {

        final String query = "{ CALL RouteX_Update.getPaypalPayment(?,?) }";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(query)) {

            cs.setString(1, email);
            cs.setString(2, codice);

            try (ResultSet rs = cs.executeQuery()) {

                if (rs.next()) {
                    Paypal found = new Paypal();
                    found.setEmail(rs.getString("email_paypal"));
                    found.setCodice(rs.getString("codice_transazione"));
                    return found;
                }

                //  nessun risultato semantica neutra
                return null;
            }

        } catch (SQLException e) {
            throw new DAOExceptionRemoli(
                    "Errore durante il recupero del pagamento Paypal: " + e.getMessage(),
                    e
            );
        }
    }


    @Override
    public List<City> listCities() throws DAOExceptionRemoli {

        final List<City> informazioni = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.getAllCity() }");
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                String nome = rs.getString("nome_citta");
                double costo = rs.getDouble("prezzo_ticket");
                long numero = rs.getLong("numero_stazioni");

                informazioni.add(new City(nome, costo, numero));
            }

            // può essere vuota, è lecito
            return informazioni;

        } catch (SQLException e) {
            throw new DAOExceptionRemoli(
                    "Errore nella comunicazione con il database: " + e.getMessage(),
                    e
            );
        }
    }


    @Override
    public List<Fermata> getFermateByIds(List<Integer> ids, String city) throws SQLException {
        List<Fermata> fermate = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.GetFermataById(?, ?) }")) {

            cs.setString(1, city);

            for (int id : ids) {
                cs.setInt(2, id);

                try (ResultSet rs = cs.executeQuery()) {
                    while (rs.next()) {
                        fermate.add(
                                new Fermata(
                                        rs.getString("nome"),
                                        rs.getString("linea")
                                )
                        );
                    }
                }
            }
        }
        return fermate;
    }

    @Override
    public List<Station> restituisciIdStazioni(String startStation, String endStation, String city) throws SQLException
    {
        String procedure = "{CALL RouteX_Update.RestituisciStazioni(?,?,?)}";
        List<Station> stations = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(procedure))
        {
            cs.setString(1, startStation);
            cs.setString(2, endStation);
            cs.setString(3, city);

            boolean hasResults = cs.execute();
            int resultIndex = 0;

            while (hasResults) {
                try (ResultSet rs = cs.getResultSet()) {
                    resultIndex++;
                    if (rs != null) {
                        if (resultIndex == 1 && rs.next()) {
                            stations.add(new Station(rs.getInt("id")));
                        }

                        if (resultIndex == 2 && rs.next()) {
                            stations.add(new Station(rs.getInt("id")));
                        }
                    }
                }
                hasResults = cs.getMoreResults();
            }

        } catch (SQLException e) {
            throw new SQLException("Errore durante l'esecuzione della stored procedure RestituisciStazioni", e);
        }
        return stations;
    }

    @Override
    public List<Notification> getMessages() throws DAOExceptionRemoli {

        List<Notification> result = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.getMessages() }")) {

            if (cs.execute()) {
                try (ResultSet rs = cs.getResultSet()) {
                    while (rs.next()) {
                        Notification notification = new Notification(
                                rs.getString("testo"),
                                rs.getTimestamp("data"),
                                rs.getBoolean("risolto"),
                                rs.getBoolean("approvato"),
                                rs.getBoolean("letto"),
                                rs.getString("status"),
                                rs.getString("sender_role"),
                                rs.getString("sender_cf"),
                                rs.getString("recipient_cf"),
                                rs.getString("city"),
                                rs.getBoolean("pickpocket_alert"),
                                rs.getBoolean("fight_alert"),
                                rs.getBoolean("crowd_alert"),
                                rs.getBoolean("general_alert"),
                                rs.getString("station_name"),
                                rs.getString("suspect_clothing")
                        );
                        result.add(notification);
                    }
                }
            }
            //  NESSUN controllo isEmpty()
            return result;

        } catch (SQLException e) {
            throw new DAOExceptionRemoli(
                    "Errore nel recupero delle notifiche",
                    e
            );
        }
    }

    @Override
    public void save(Route route) throws DAOExceptionRemoli {
        try (Connection conn = ConnectionFactory.getConnection()){
            String sp = "{ CALL RouteX_Update.saveRoute(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
            CallableStatement cs = conn.prepareCall(sp);

            cs.setString(1, route.getPartenza());
            cs.setString(2, route.getArrivo());
            cs.setString(3, route.getCitta());
            cs.setString(4, route.getTipoViaggiatore());
            cs.setInt(5, route.getnCambi());
            cs.setString(6, route.getListaCambi());
            cs.setString(7, route.getStazInterscambio());
            cs.setDouble(8, route.getnStazAttraversate());
            cs.setDouble(9, route.getTempoDiArrivo());
            cs.setInt(10, route.getnStazioniCitta());
            cs.setDouble(11, route.getPercTerrenoUtilizzato());
            cs.setString(12, route.getUtente()); // qui salvo utente

            cs.execute();

        } catch (Exception e) {
            throw new DAOExceptionRemoli("Errore durante la registrazione del percorso: " + e.getMessage());
        }
    }

    @Override
    public void sendMessage(Notification n) throws DAOExceptionRemoli {

        try (Connection conn = ConnectionFactory.getConnection()) {

            CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.spCommunication(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");

            cs.setString(1, n.getMessage());
            cs.setTimestamp(2, n.getDate());
            cs.setBoolean(3, n.isRisolto());
            cs.setBoolean(4, n.isApprovato());
            cs.setBoolean(5, n.isLetto());
            cs.setString(6, n.getStatus());
            cs.setString(7, n.getSenderRole());
            cs.setString(8, n.getSenderCf());
            cs.setString(9, n.getRecipientCf());
            cs.setString(10, n.getCity());
            cs.setBoolean(11, n.isPickpocketAlert());
            cs.setBoolean(12, n.isFightAlert());
            cs.setBoolean(13, n.isCrowdAlert());
            cs.setBoolean(14, n.isGeneralAlert());
            cs.setString(15, n.getStationName());
            cs.setString(16, n.getSuspectClothing());

            cs.execute();
            invalidateNotificationsCache();

        } catch (Exception e) {
            throw new DAOExceptionRemoli(
                    "Errore durante l'invio della comunicazione",
                    e
            );
        }
    }

    @Override
    public void solvedNotification(Notification n) throws DAOExceptionRemoli {

        try (Connection conn = ConnectionFactory.getConnection()) {

            CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.spSolvedMessage(?, ?, ?) }");

            cs.setString(1, n.getMessage());
            cs.setTimestamp(2, n.getDate());
            cs.setBoolean(3, n.isRisolto());

            cs.execute();
            invalidateNotificationsCache();

        } catch (Exception e) {
            throw new DAOExceptionRemoli(
                    "Errore durante l'aggiornamento della notifica",
                    e
            );
        }
    }

    @Override
    public void approveNotification(Notification n) throws DAOExceptionRemoli {

        try (Connection conn = ConnectionFactory.getConnection()) {

            CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.ApproveCommunication(?, ?) }");

            cs.setString(1, n.getMessage());
            cs.setTimestamp(2, n.getDate());

            cs.execute();
            invalidateNotificationsCache();

        } catch (Exception e) {
            throw new DAOExceptionRemoli(
                    "Error while approving the notification",
                    e
            );
        }
    }

    @Override
    public void deleteNotification(Notification n) throws DAOExceptionRemoli {

        try (Connection conn = ConnectionFactory.getConnection()) {

            CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.DeleteCommunication(?, ?) }");

            cs.setString(1, n.getMessage());
            cs.setTimestamp(2, n.getDate());

            cs.execute();
            invalidateNotificationsCache();

        } catch (Exception e) {
            throw new DAOExceptionRemoli(
                    "Error while deleting the notification",
                    e
            );
        }
    }

    @Override
    public void markNotificationAsRead(Notification n) throws DAOExceptionRemoli {

        try (Connection conn = ConnectionFactory.getConnection()) {

            CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.MarkCommunicationAsRead(?, ?) }");

            cs.setString(1, n.getMessage());
            cs.setTimestamp(2, n.getDate());

            cs.execute();
            invalidateNotificationsCache();

        } catch (Exception e) {
            throw new DAOExceptionRemoli(
                    "Error while marking the notification as read",
                    e
            );
        }
    }

    @Override
    public boolean approvePendingTravelerNotification(Notification n) throws DAOExceptionRemoli {
        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.ApproveTravelerCommunication(?, ?) }")) {

            cs.setString(1, n.getMessage());
            cs.setTimestamp(2, n.getDate());

            if (cs.execute()) {
                try (ResultSet rs = cs.getResultSet()) {
                    boolean updated = rs.next() && rs.getInt("updated_rows") > 0;
                    if (updated) {
                        invalidateNotificationsCache();
                    }
                    return updated;
                }
            }
            return false;
        } catch (Exception e) {
            throw new DAOExceptionRemoli("Error while approving the traveler report", e);
        }
    }

    @Override
    public boolean rejectPendingTravelerNotification(Notification n) throws DAOExceptionRemoli {
        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.RejectTravelerCommunication(?, ?) }")) {

            cs.setString(1, n.getMessage());
            cs.setTimestamp(2, n.getDate());

            if (cs.execute()) {
                try (ResultSet rs = cs.getResultSet()) {
                    boolean updated = rs.next() && rs.getInt("updated_rows") > 0;
                    if (updated) {
                        invalidateNotificationsCache();
                    }
                    return updated;
                }
            }
            return false;
        } catch (Exception e) {
            throw new DAOExceptionRemoli("Error while rejecting the traveler report", e);
        }
    }
    @Override
    public List<Route> getAllPathInfo() throws DAOExceptionRemoli {

        List<Route> resultList = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection()) {

            CallableStatement cs =
                    conn.prepareCall("{ CALL RouteX_Update.getAllPathInfo() }");

            if (cs.execute()) {
                try (ResultSet rs = cs.getResultSet()) {
                    while (rs.next()) {

                        Route info = new RouteBuilder(rs.getString("StartStation"))
                                .endStation(rs.getString("EndStation"))
                                .city(rs.getString("City"))
                                .tipoViaggiatore(rs.getString("TipoViaggiatore"))
                                .nCambi(rs.getInt("NCambi"))
                                .listaCambi(rs.getString("ListaCambi"))
                                .stazioneDiInterscambio(rs.getString("StazioneDiInterscambio"))
                                .nStazioniAttraversate(rs.getInt("NStazioniAttraversate"))
                                .tempoDiArrivo(rs.getDouble("TempoDiArrivo"))
                                .nStazioniCitta(rs.getInt("NStazioniCitta"))
                                .percTerrenoUtilizzato(rs.getDouble("PercTerrenoUtilizzato"))
                                .utente(rs.getString("Utente"))
                                .build();


                        resultList.add(info);
                    }
                }
            }

        } catch (SQLException e) {
            throw new DAOExceptionRemoli(
                    "Errore nel recupero delle statistiche PathInfo",
                    e
            );
        }

        return resultList;
    }

    @Override
    public List<Credentials> listAdmins() throws DAOExceptionRemoli {
        List<Credentials> admins = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.ListAdmins() }");
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                Credentials admin = new Credentials();
                admin.setNome(rs.getString("nome"));
                admin.setCognome(rs.getString("cognome"));
                admin.setEmail(rs.getString("email"));
                admin.setCodiceFiscale(rs.getString("codice_fiscale"));
                admin.setRuolo(Ruolo.ADMIN);
                admins.add(admin);
            }

            return admins;
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Error while loading admin accounts: " + e.getMessage(), e);
        }
    }

    @Override
    public void createAdmin(String nome,
                            String cognome,
                            String email,
                            String password,
                            String codiceFiscale) throws DAOExceptionRemoli {
        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.CreateAdmin(?, ?, ?, ?, ?) }")) {

            cs.setString(1, nome);
            cs.setString(2, cognome);
            cs.setString(3, email);
            cs.setString(4, password);
            cs.setString(5, codiceFiscale);
            cs.execute();
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Error while creating the admin account: " + e.getMessage(), e);
        }
    }

    @Override
    public int deleteAdmins(List<String> codiceFiscali) throws DAOExceptionRemoli {
        int deleted = 0;

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.DeleteAdminByCodiceFiscale(?) }")) {

            for (String codiceFiscale : codiceFiscali) {
                cs.setString(1, codiceFiscale);
                try (ResultSet rs = cs.executeQuery()) {
                    if (rs.next()) {
                        deleted += rs.getInt("deleted_rows");
                    }
                }
            }

            return deleted;
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Error while deleting admin accounts: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Credentials> listTravelers() throws DAOExceptionRemoli {
        List<Credentials> travelers = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.ListTravelers() }");
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                Credentials traveler = new Credentials();
                traveler.setNome(rs.getString("nome"));
                traveler.setCognome(rs.getString("cognome"));
                traveler.setEmail(rs.getString("email"));
                traveler.setCodiceFiscale(rs.getString("codice_fiscale"));
                traveler.setRuolo(Ruolo.TRAVELER);
                travelers.add(traveler);
            }

            return travelers;
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Error while loading traveler accounts: " + e.getMessage(), e);
        }
    }

    @Override
    public int deleteTravelers(List<String> codiceFiscali) throws DAOExceptionRemoli {
        int deleted = 0;

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.DeleteTravelerByCodiceFiscale(?) }")) {

            for (String codiceFiscale : codiceFiscali) {
                cs.setString(1, codiceFiscale);
                try (ResultSet rs = cs.executeQuery()) {
                    if (rs.next()) {
                        deleted += rs.getInt("deleted_rows");
                    }
                }
            }

            return deleted;
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Error while deleting traveler accounts: " + e.getMessage(), e);
        }
    }

    private List<Credentials> listPermessiByRole(String procedureName) throws DAOExceptionRemoli {
        List<Credentials> users = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update." + procedureName + "() }");
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                Credentials user = new Credentials();
                user.setNome(rs.getString("nome"));
                user.setCognome(rs.getString("cognome"));
                user.setEmail(rs.getString("email"));
                user.setCodiceFiscale(rs.getString("codice_fiscale"));
                users.add(user);
            }

            return users;
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Error while loading accounts: " + e.getMessage(), e);
        }
    }

    private int deletePermessiByProcedure(List<String> codiceFiscali,
                                          String procedureName,
                                          String accountLabel) throws DAOExceptionRemoli {
        int deleted = 0;

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update." + procedureName + "(?) }")) {

            for (String codiceFiscale : codiceFiscali) {
                cs.setString(1, codiceFiscale);
                try (ResultSet rs = cs.executeQuery()) {
                    if (rs.next()) {
                        deleted += rs.getInt("deleted_rows");
                    }
                }
            }

            return deleted;
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Error while deleting " + accountLabel + " accounts: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Route> getData(String cf)
            throws PathNotFoundExceptionRemoli, DAOExceptionRemoli {

        try (Connection conn = ConnectionFactory.getConnection())
        {

            CallableStatement cs =
                    conn.prepareCall("{ CALL RouteX_Update.getPath(?) }");

            cs.setString(1, cf);
            cs.execute();

            ResultSet rs = cs.getResultSet();
            List<Route> lista = new ArrayList<>();

            while (rs.next()) {
                Route r = new Route();
                r.setPartenza(rs.getString("StartStation"));
                r.setArrivo(rs.getString("EndStation"));
                r.setCitta(rs.getString("City"));
                r.setTipoViaggiatore(rs.getString("TipoViaggiatore"));
                r.setnCambi(rs.getInt("NCambi"));
                r.setnStazAttraversate(rs.getInt("NStazioniAttraversate"));
                r.setTempoDiArrivo(rs.getDouble("TempoDiArrivo"));
                r.setnStazioniCitta(rs.getInt("NStazioniCitta"));
                r.setPercTerrenoUtilizzato(rs.getDouble("PercTerrenoUtilizzato"));
                r.setListaCambi(rs.getString("ListaCambi"));
                r.setStazInterscambio(rs.getString("StazioneDiInterscambio"));
                r.setUtente(cf);

                lista.add(r);
            }

            if (lista.isEmpty())
            {
                throw new PathNotFoundExceptionRemoli(
                        "Nessun percorso trovato per l’utente.",
                        cf,
                        404,
                        "RouteDAO.getData"
                );
            }

            return lista;
        } catch (SQLException e) {
            // Errore tecnico
            throw new DAOExceptionRemoli(
                    "Errore durante la connessione al database",
                    e
            );
        }
    }

    @Override
    public int deleteRoutesBySignatures(String cf, List<String> routeSignatures) throws DAOExceptionRemoli {
        int deleted = 0;
        String procedure = "{ CALL RouteX_Update.DeleteRouteBySignature(?,?,?,?,?,?,?,?,?,?,?,?,?) }";

        try (Connection conn = ConnectionFactory.getConnection()) {
            for (String signature : routeSignatures) {
                String[] parts = signature.split("\\Q||\\E", -1);
                if (parts.length != 11) {
                    continue;
                }

                try (CallableStatement cs = conn.prepareCall(procedure)) {
                    cs.setString(1, cf);
                    cs.setString(2, parts[0]);
                    cs.setString(3, parts[1]);
                    cs.setString(4, parts[2]);
                    cs.setString(5, parts[3]);
                    cs.setInt(6, Integer.parseInt(parts[4]));
                    cs.setString(7, parts[5]);
                    cs.setString(8, parts[6]);
                    cs.setInt(9, Integer.parseInt(parts[7]));
                    cs.setDouble(10, Double.parseDouble(parts[8]));
                    cs.setInt(11, Integer.parseInt(parts[9]));
                    cs.setDouble(12, Double.parseDouble(parts[10]));
                    cs.registerOutParameter(13, java.sql.Types.INTEGER);
                    cs.execute();
                    deleted += cs.getInt(13);
                }
            }

            return deleted;
        } catch (SQLException | NumberFormatException ex) {
            throw new DAOExceptionRemoli("Errore durante l'eliminazione dei percorsi", ex);
        }
    }

    @Override
    public int deleteAllRoutes(String cf) throws DAOExceptionRemoli {
        String procedure = "{ CALL RouteX_Update.DeleteAllRoutes(?, ?) }";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(procedure)) {
            cs.setString(1, cf);
            cs.registerOutParameter(2, java.sql.Types.INTEGER);
            cs.execute();
            return cs.getInt(2);
        } catch (SQLException ex) {
            throw new DAOExceptionRemoli("Errore durante l'eliminazione completa dei percorsi", ex);
        }
    }

    @Override
    public void salvataggio(Credentials cred, List<String> codiciBiglietti, String metodoPagamento, String city) throws CredentialsExceptionRemoli {
        TicketDAODB dao = new TicketDAODB();
        dao.salvataggio(cred, codiciBiglietti, metodoPagamento, city);
        final Logger logger = LoggerFactory.getLogger(getClass());
        logger.info("Modalità FULL: il pagamento  viene salvato in persistenza.");

    }

}
