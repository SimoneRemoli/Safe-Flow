package it.web.routex.model.dao;
import it.web.routex.bean.TicketBean;
import java.util.List;
import it.web.routex.exception.PathNotFoundExceptionRemoli;
import it.web.routex.exception.CredentialsExceptionRemoli;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.utility.singleton.Credentials;

public abstract class TicketDAOLayer {
    public abstract List<TicketBean> getTicketByCF(String cf) throws DAOExceptionRemoli, PathNotFoundExceptionRemoli;
    public abstract void salvataggio(Credentials cred, List<String> codiciBiglietti, String metodopayment, String city) throws CredentialsExceptionRemoli;
}
