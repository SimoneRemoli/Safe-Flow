package utility.Factory;
import Model.DAO.TicketDAODB;
import Model.DAO.TicketDAOFile;
import Model.DAO.TicketDAOLayer;
import Model.Domain.TypesOfPersistenceLayer;
import utility.Singleton.PersistenceMode;

import static Model.Domain.TypesOfPersistenceLayer.FileSystem;
import static Model.Domain.TypesOfPersistenceLayer.JDBC;

public class FactoryPersistence {

    public static TicketDAOLayer createTicketDAO() {

        TypesOfPersistenceLayer typesPer = PersistenceMode.getInstance().getTipo();
        switch (typesPer) {
            case JDBC:
                return new TicketDAODB();
            case FileSystem:
                return new TicketDAOFile();
            default:
                throw new IllegalArgumentException("Tipo di persistenza non supportato: " + typesPer);
        }
    }
}









/* versione piu bellla
public class PersistenceFactory {

    public static TicketDAOLayer createTicketDAO(TypesOfPersistenceLayer type) {
        return switch (type) {
            case JDBC -> new TicketDAODB();
            case FileSystem -> new TicketDAOFile();
        };
    }

    public static LoginDAOLayer createLoginDAO(TypesOfPersistenceLayer type) {
        return switch (type) {
            case JDBC -> new LoginDAODB();
            case FileSystem -> new LoginDAOFile();
        };
    }

    // ...e così via per gli altri DAO
}

 */

