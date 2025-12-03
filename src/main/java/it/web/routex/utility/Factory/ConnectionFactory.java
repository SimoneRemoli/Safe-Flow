package it.web.routex.utility.Factory;

import it.web.routex.model.domain.Ruolo;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static String connectionUrl;
    private static String currentUser;
    private static String currentPass;

    static {
        try (InputStream input = ConnectionFactory.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null)
                throw new RuntimeException("Impossibile trovare il file db.properties nel classpath!");

            Class.forName("com.mysql.cj.jdbc.Driver");

            Properties properties = new Properties();
            properties.load(input);

            connectionUrl = properties.getProperty("CONNECTION_URL");
            currentUser = properties.getProperty("LOGIN_USER");
            currentPass = properties.getProperty("LOGIN_PASS");

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Errore durante il caricamento delle impostazioni DB: " + e.getMessage());
        }
    }

    /**
     * 🔹 Restituisce una nuova connessione viva ogni volta.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionUrl, currentUser, currentPass);
    }

    /**
     * 🔹 Cambia ruolo (utente DB) aggiornando le credenziali per le connessioni future.
     *     Non chiude connessioni usate da altri thread.
     */
    public static void Cambio_Di_Ruolo(Ruolo ruolo) throws SQLException {
        try (InputStream input = ConnectionFactory.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null)
                throw new RuntimeException("Impossibile trovare il file db.properties nel classpath!");

            Properties properties = new Properties();
            properties.load(input);

            currentUser = properties.getProperty(ruolo.name() + "_USER");
            currentPass = properties.getProperty(ruolo.name() + "_PASS");

            // Log diagnostico
            System.out.println("[ConnectionFactory] Cambio ruolo a " + ruolo + " (user: " + currentUser + ")");
        } catch (IOException e) {
            throw new SQLException("Errore durante il cambio di ruolo: " + e.getMessage());
        }
    }
}

