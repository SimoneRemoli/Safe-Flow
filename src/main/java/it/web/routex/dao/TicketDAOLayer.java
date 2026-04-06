package it.web.routex.dao;
import java.util.List;
import it.web.routex.exception.PathNotFoundExceptionRemoli;
import it.web.routex.exception.CredentialsExceptionRemoli;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.model.Ticket;
import it.web.routex.utility.singleton.Credentials;

public abstract class TicketDAOLayer {
    public abstract List<Ticket> getTicketByCF(String cf) throws DAOExceptionRemoli, PathNotFoundExceptionRemoli;
    public abstract int deleteTicketsByCodes(String cf, List<String> ticketCodes) throws DAOExceptionRemoli;
    public abstract int deleteAllTickets(String cf) throws DAOExceptionRemoli;
    public abstract void salvataggio(Credentials cred, List<String> codiciBiglietti, String metodopayment, String city) throws CredentialsExceptionRemoli;
}
