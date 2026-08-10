package it.web.safeflow.controller.applicativo;

import it.web.safeflow.bean.AutenticazioneBean;
import it.web.safeflow.bean.UtenteBeanGenerico;
import it.web.safeflow.dao.LayerPersistenza;
import it.web.safeflow.utility.factory.FactoryLayerPersistenza;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import it.web.safeflow.model.Credentials;
import it.web.safeflow.exception.DAOExceptionRemoli;
import it.web.safeflow.exception.LoginNotFoundRemoli;


/**
 * Controller applicativo responsabile della logica di autenticazione utente.
 * Interagisce con il DAO per verificare le credenziali e costruisce il bean utente di sessione.
 */
public class LoginController {

    private final AutenticazioneBean autenticazione;

    public LoginController(AutenticazioneBean autenticazione) {
        this.autenticazione = autenticazione;
    }

    /**
     * Esegue l’autenticazione dell’utente e costruisce il bean usato dal livello grafico.
     * e restituisce un `UtenteBeanGenerico` per la parte grafica.
     */
    public UtenteBeanGenerico autenticaUtente() throws DAOExceptionRemoli, LoginNotFoundRemoli {

        final Logger logger = LoggerFactory.getLogger(getClass());

        // Chiamata al DAO
        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        Credentials credFromDb = layer.login(autenticazione.getEmail(), autenticazione.getPassword());
        
        logger.info("Funzione autenticaUtente() dentro LoginController.java con autenticazione {} e {}", credFromDb.getNome(), credFromDb.getCognome());

        // Popola anche il bean (solo per il layer grafico)
        UtenteBeanGenerico utente = new UtenteBeanGenerico();
        utente.setNome(credFromDb.getNome());
        utente.setCognome(credFromDb.getCognome());
        utente.setCodiceFiscale(credFromDb.getCodiceFiscale());
        utente.setDisable(credFromDb.getDisabile());
        utente.setRuolo(credFromDb.getRuolo());

        return utente;
    }
}
