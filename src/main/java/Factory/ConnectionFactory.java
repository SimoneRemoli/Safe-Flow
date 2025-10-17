package Factory;

import Model.Domain.Ruolo;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    private static Connection connection;

    static {
        try (InputStream input = ConnectionFactory.class.getClassLoader().getResourceAsStream("db.properties")) {

            if (input == null) {
                throw new RuntimeException("Impossibile trovare il file db.properties nel classpath!");
            }
            Class.forName("com.mysql.cj.jdbc.Driver");


            Properties properties = new Properties();
            properties.load(input);

            String connection_url = properties.getProperty("CONNECTION_URL");
            String user = properties.getProperty("LOGIN_USER");
            String pass = properties.getProperty("LOGIN_PASS");

            connection = DriverManager.getConnection(connection_url, user, pass);

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante la creazione della connessione al database: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return connection;
    }

    public static void Cambio_Di_Ruolo(Ruolo ruolo) throws SQLException {
        connection.close();

        try (InputStream input = ConnectionFactory.class.getClassLoader().getResourceAsStream("db.properties")) {

            if (input == null) {
                throw new RuntimeException("Impossibile trovare il file db.properties nel classpath!");
            }

            Properties properties = new Properties();
            properties.load(input);

            String connection_url = properties.getProperty("CONNECTION_URL");
            String user = properties.getProperty(ruolo.name() + "_USER");
            String pass = properties.getProperty(ruolo.name() + "_PASS");

            connection = DriverManager.getConnection(connection_url, user, pass);

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            throw new SQLException("Errore durante il cambio di ruolo: " + e.getMessage());
        }
    }
}
