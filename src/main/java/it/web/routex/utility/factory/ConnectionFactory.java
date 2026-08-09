package it.web.routex.utility.factory;

import it.web.routex.exception.ConfigurationException;
import it.web.routex.enumerator.Ruolo;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static final Properties DB_PROPERTIES = new Properties();
    private static final ThreadLocal<Ruolo> REQUEST_ROLE = ThreadLocal.withInitial(() -> Ruolo.LOGIN);
    private static String connectionUrl;

    private ConnectionFactory() {
        throw new UnsupportedOperationException("Classe di utility - non istanziabile");
    }


    static {
        try (InputStream input = ConnectionFactory.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null)
                throw new ConfigurationException("Impossibile trovare il file db.properties nel classpath!");

            Class.forName("com.mysql.cj.jdbc.Driver");

            DB_PROPERTIES.load(input);

            connectionUrl = DB_PROPERTIES.getProperty("CONNECTION_URL");

        } catch (IOException | ClassNotFoundException e) {
            throw new ConfigurationException("Errore durante il caricamento delle impostazioni DB: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        Ruolo ruolo = REQUEST_ROLE.get();
        String user = DB_PROPERTIES.getProperty(ruolo.name() + "_USER");
        String pass = DB_PROPERTIES.getProperty(ruolo.name() + "_PASS");

        if (user == null || pass == null) {
            throw new SQLException("Credenziali DB mancanti per il ruolo " + ruolo);
        }

        return DriverManager.getConnection(connectionUrl, user, pass);
    }

    public static void setRequestRole(Ruolo ruolo) {
        REQUEST_ROLE.set(ruolo == null ? Ruolo.LOGIN : ruolo);
    }

    public static void clearRequestRole() {
        REQUEST_ROLE.remove();
    }
}
