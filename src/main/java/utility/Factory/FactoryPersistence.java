package utility.Factory;
import Model.DAO.TicketDAODB;
import Model.DAO.TicketDAOFile;
import Model.DAO.TicketDAOLayer;
import Model.Domain.TypesOfPersistenceLayer;

public class FactoryPersistence {

    public static TicketDAOLayer createTicketDAO(TypesOfPersistenceLayer type) {
        switch (type) {
            case JDBC:
                return new TicketDAODB();
            case FileSystem:
                return new TicketDAOFile();
            default:
                throw new IllegalArgumentException("Tipo di persistenza non supportato: " + type);
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

