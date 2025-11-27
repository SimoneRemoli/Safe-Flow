package Controller.Applicativo;

import Bean.AutenticazioneBean;
import Bean.UtenteBeanGenerico;
import Model.DAO.LoginProcedureDAO;
import utility.Singleton.Credentials;
import Exception.DAOExceptionRemoli;
import Exception.LoginNotFoundRemoli;

import javax.servlet.http.HttpSession;

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

        // Chiamata al DAO
        LoginProcedureDAO loginDAO = new LoginProcedureDAO();
        loginDAO.login(autenticazione.getEmail(), autenticazione.getPassword());

        // Ottieni l'istanza singleton delle credenziali
        Credentials sessionCred = Credentials.getInstanceSingleton();

        System.out.println("Utente autenticato: " + sessionCred.getNome() + " " + sessionCred.getCognome());

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
