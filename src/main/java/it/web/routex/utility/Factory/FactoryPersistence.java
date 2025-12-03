package it.web.routex.utility.Factory;
import it.web.routex.model.dao.TicketDAODB;
import it.web.routex.model.dao.TicketDAOFile;
import it.web.routex.model.dao.TicketDAOLayer;
import it.web.routex.model.domain.TypesOfPersistenceLayer;
import it.web.routex.utility.Singleton.PersistenceMode;
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