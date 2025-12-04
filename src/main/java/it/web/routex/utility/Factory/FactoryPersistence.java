package it.web.routex.utility.Factory;
import it.web.routex.model.dao.TicketDAODB;
import it.web.routex.model.dao.TicketDAOFile;
import it.web.routex.model.dao.TicketDAOLayer;
import it.web.routex.model.domain.TypesOfPersistenceLayer;
import it.web.routex.utility.Singleton.PersistenceMode;
public class FactoryPersistence {

    public static TicketDAOLayer createTicketDAO() {

        TypesOfPersistenceLayer typesPer = PersistenceMode.getSingletonInstance().getTipo();
        switch (typesPer) {
            case JDBC:
                return new TicketDAODB();
            case FILE_SYSTEM:
                return new TicketDAOFile();
            default:
                throw new IllegalArgumentException("Tipo di persistenza non supportato: " + typesPer);
        }
    }
}