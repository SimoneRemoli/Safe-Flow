package it.web.routex.utility.factory;
import it.web.routex.dao.TicketDAODB;
import it.web.routex.dao.TicketDAOLayer;
public class FactoryPersistence {

    private FactoryPersistence() {
        throw new UnsupportedOperationException("Classe di utility - non istanziabile");
    }

    public static TicketDAOLayer createTicketDAO() {
        return new TicketDAODB();
    }
}
