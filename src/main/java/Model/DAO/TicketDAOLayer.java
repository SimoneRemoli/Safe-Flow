package Model.DAO;

import Bean.TicketBean;

import java.io.IOException;
import java.util.List;
import Exception.PathNotFoundExceptionRemoli;
import Exception.CredentialsExceptionRemoli;
import Exception.DAOExceptionRemoli;
import utility.Singleton.Credentials;

public abstract class TicketDAOLayer {
    public abstract List<TicketBean> getTicketByCF(String cf) throws DAOExceptionRemoli, PathNotFoundExceptionRemoli;
    public abstract void salvataggio(Credentials cred, List<String> codiciBiglietti, String metodopayment, String city) throws CredentialsExceptionRemoli;
}
