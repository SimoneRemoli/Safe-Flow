package it.web.routex.controller.applicativo;

import it.web.routex.bean.AutenticazioneBean;
import it.web.routex.bean.UtenteBeanGenerico;
import it.web.routex.model.dao.LoginProcedureDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import it.web.routex.utility.singleton.Credentials;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.exception.LoginNotFoundRemoli;


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
    public UtenteBeanGenerico autenticaUtente() throws DAOExceptionRemoli, LoginNotFoundRemoli {

        final Logger logger = LoggerFactory.getLogger(getClass());

        // Chiamata al DAO
        LoginProcedureDAO loginDAO = new LoginProcedureDAO();
        loginDAO.login(autenticazione.getEmail(), autenticazione.getPassword());

        // Ottieni l'istanza singleton delle credenziali
        Credentials sessionCred = Credentials.getInstanceSingleton();


        logger.info("Funzione autenticaUtente() dentro LoginController.java con autenticazione {} e {}", sessionCred.getNome(), sessionCred.getCognome());

        // Popola anche il bean (solo per il layer grafico)
        UtenteBeanGenerico utente = new UtenteBeanGenerico();
        utente.setNome(sessionCred.getNome());
        utente.setCognome(sessionCred.getCognome());
        utente.setCodiceFiscale(sessionCred.getCodiceFiscale());
        utente.setDisable(sessionCred.getDisabile());
        utente.setRuolo(sessionCred.getRuolo());

        return utente;
    }
}
