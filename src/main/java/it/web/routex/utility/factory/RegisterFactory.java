package it.web.routex.utility.factory;


import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.model.dao.RegisterAllUsersDAO;
import it.web.routex.model.dao.RegistrazioneLavoratoreDAO;
import it.web.routex.model.dao.RegistrazioneUtenteDAO;
import it.web.routex.model.domain.CredentialsDTO;

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