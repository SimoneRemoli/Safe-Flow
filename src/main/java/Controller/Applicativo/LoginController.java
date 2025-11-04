package Controller.Applicativo;

import Bean.AutenticazioneBean;
import Bean.UtenteBeanGenerico;
import Model.DAO.LoginProcedureDAO;
import Model.Domain.Credentials;
import Exception.DAOException;

import java.sql.SQLException;

/**
 * Controller applicativo responsabile della logica di autenticazione utente.
 * Interagisce con il DAO per verificare le credenziali e costruisce il bean utente.
 */
public class LoginController {

    private final AutenticazioneBean autenticazione;

    /**
     * Costruttore che riceve il bean con email e password dell’utente.
     *
     * @param autenticazione Bean contenente le credenziali inserite dall’utente
     */
    public LoginController(AutenticazioneBean autenticazione) {
        this.autenticazione = autenticazione;
    }

    public UtenteBeanGenerico autenticaUtente() throws DAOException, SQLException {

        // Creazione dell’oggetto Credentials a partire dalle credenziali ricevute
        Credentials credenziali = new Credentials(
                "", "", "",
                autenticazione.getPassword(),
                autenticazione.getEmail(),
                null, false
        );

        // Interazione con il DAO per la verifica
        LoginProcedureDAO loginDAO = new LoginProcedureDAO();
        Credentials utenteVerificato = loginDAO.login(credenziali);

        // Costruzione del bean da restituire alla parte grafica
        UtenteBeanGenerico utente = new UtenteBeanGenerico();
        utente.setNome(utenteVerificato.getNome());
        utente.setCognome(utenteVerificato.getCognome());
        utente.setCodice_fiscale(utenteVerificato.getCodiceFiscale());
        utente.setDisable(utenteVerificato.getDisabile());
        utente.setRuolo(utenteVerificato.getRuolo());

        return utente;
    }
}
