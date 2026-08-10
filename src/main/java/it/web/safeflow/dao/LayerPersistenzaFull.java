package it.web.safeflow.dao;
import it.web.safeflow.enumerator.Ruolo;
import it.web.safeflow.exception.DAOExceptionRemoli;
import it.web.safeflow.exception.LoginNotFoundRemoli;
import it.web.safeflow.model.*;
import it.web.safeflow.utility.factory.ConnectionFactory;
import it.web.safeflow.model.Credentials;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LayerPersistenzaFull extends LayerPersistenza
{
    public Credentials login(String email, String password)
            throws DAOExceptionRemoli, LoginNotFoundRemoli {

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL SafeFlow_Update.login_User(?, ?) }")) {

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

            try (CallableStatement registerRegistry = conn.prepareCall("{ CALL SafeFlow_Update.register(?, ?) }");
                 CallableStatement registerUser = conn.prepareCall("{ CALL SafeFlow_Update.register_User(?, ?, ?, ?, ?, ?, ?, ?) }")) {

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

    @Override
    public List<City> listCities() throws DAOExceptionRemoli {

        final List<City> informazioni = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL SafeFlow_Update.getAllCity() }");
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                String nome = rs.getString("nome_citta");

                informazioni.add(new City(nome));
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
    public List<Notification> getMessages() throws DAOExceptionRemoli {

        List<Notification> result = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL SafeFlow_Update.getMessages() }")) {

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
    public void sendMessage(Notification n) throws DAOExceptionRemoli {

        try (Connection conn = ConnectionFactory.getConnection()) {

            CallableStatement cs = conn.prepareCall("{ CALL SafeFlow_Update.spCommunication(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");

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
    public void markNotificationAsRead(Notification n) throws DAOExceptionRemoli {

        try (Connection conn = ConnectionFactory.getConnection()) {

            CallableStatement cs = conn.prepareCall("{ CALL SafeFlow_Update.MarkCommunicationAsRead(?, ?) }");

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
             CallableStatement cs = conn.prepareCall("{ CALL SafeFlow_Update.ApproveTravelerCommunication(?, ?) }")) {

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
             CallableStatement cs = conn.prepareCall("{ CALL SafeFlow_Update.RejectTravelerCommunication(?, ?) }")) {

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
    public List<Credentials> listAdmins() throws DAOExceptionRemoli {
        List<Credentials> admins = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL SafeFlow_Update.ListAdmins() }");
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
             CallableStatement cs = conn.prepareCall("{ CALL SafeFlow_Update.CreateAdmin(?, ?, ?, ?, ?) }")) {

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
             CallableStatement cs = conn.prepareCall("{ CALL SafeFlow_Update.DeleteAdminByCodiceFiscale(?) }")) {

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
             CallableStatement cs = conn.prepareCall("{ CALL SafeFlow_Update.ListTravelers() }");
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
             CallableStatement cs = conn.prepareCall("{ CALL SafeFlow_Update.DeleteTravelerByCodiceFiscale(?) }")) {

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
             CallableStatement cs = conn.prepareCall("{ CALL SafeFlow_Update." + procedureName + "() }");
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
             CallableStatement cs = conn.prepareCall("{ CALL SafeFlow_Update." + procedureName + "(?) }")) {

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

}
