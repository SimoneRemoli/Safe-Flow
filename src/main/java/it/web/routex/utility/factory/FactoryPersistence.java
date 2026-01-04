package it.web.routex.utility.factory;
import it.web.routex.dao.TicketDAODB;
import it.web.routex.dao.TicketDAOFile;
import it.web.routex.dao.TicketDAOLayer;
import it.web.routex.enumerator.TypesOfPersistenceLayer;
import it.web.routex.utility.singleton.PersistenceMode;
public class FactoryPersistence {

    private FactoryPersistence() {
        throw new UnsupportedOperationException("Classe di utility - non istanziabile");
    }

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