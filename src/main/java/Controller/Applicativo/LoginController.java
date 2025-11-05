package Controller.Applicativo;

import Bean.AutenticazioneBean;
import Bean.UtenteBeanGenerico;
import Model.DAO.LoginProcedureDAO;
import Model.Domain.Credentials;
import Exception.DAOException;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

/**
 * Controller applicativo responsabile della logica di autenticazione utente.
 * Interagisce con il DAO per verificare le credenziali e costruisce il singleton Credentials.
 */
public class LoginController {

    private final AutenticazioneBean autenticazione;

    public LoginController(AutenticazioneBean autenticazione) {
        this.autenticazione = autenticazione;
    }

    /**
     * Esegue l’autenticazione dell’utente, crea (o aggiorna) il singleton `Credentials`
     * e restituisce un `UtenteBeanGenerico` per la parte grafica.
     */
    public UtenteBeanGenerico autenticaUtente(HttpSession session) throws DAOException, SQLException {

        // Crea oggetto per DAO
        Credentials credenziali = new Credentials();
        credenziali.setEmail(autenticazione.getEmail());
        credenziali.setPassword(autenticazione.getPassword());

        // Chiamata al DAO
        LoginProcedureDAO loginDAO = new LoginProcedureDAO();
        Credentials utenteVerificato = loginDAO.login(credenziali);

        //  Recupera o crea il singleton legato alla sessione
        Credentials sessionCred = Credentials.getInstance(session);

        //  Aggiorna i dati nella sessione
        sessionCred.setNome(utenteVerificato.getNome());
        sessionCred.setCognome(utenteVerificato.getCognome());
        sessionCred.setCodiceFiscale(utenteVerificato.getCodiceFiscale());
        sessionCred.setDisabile(utenteVerificato.getDisabile());
        sessionCred.setRuolo(utenteVerificato.getRuolo());
        sessionCred.setEmail(utenteVerificato.getEmail());

        // Popola anche il bean (solo per il layer grafico)
        UtenteBeanGenerico utente = new UtenteBeanGenerico();
        utente.setNome(sessionCred.getNome());
        utente.setCognome(sessionCred.getCognome());
        utente.setCodice_fiscale(sessionCred.getCodiceFiscale());
        utente.setDisable(sessionCred.getDisabile());
        utente.setRuolo(sessionCred.getRuolo());

        return utente;
    }
}
