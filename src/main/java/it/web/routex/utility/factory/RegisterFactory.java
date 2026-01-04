package it.web.routex.utility.factory;


import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.dao.RegisterAllUsersDAO;
import it.web.routex.dao.RegistrazioneLavoratoreDAO;
import it.web.routex.dao.RegistrazioneUtenteDAO;
import it.web.routex.domain.CredentialsDTO;

public class RegisterFactory {
    public static void create(CredentialsDTO cred) throws DAOExceptionRemoli {
        switch (cred.getRuolo()) {
            case TRAVELER: //ho tolto disabled traveler
                new RegisterAllUsersDAO().save(cred);
                new RegistrazioneUtenteDAO().save(cred);
                break;
            case WORKER:
                new RegisterAllUsersDAO().save(cred);
                new RegistrazioneLavoratoreDAO().save(cred);
                break;
            default:
                throw new IllegalArgumentException("[Errore nella factory] - Unknown role: " + cred.getRuolo());
        }
    }
}