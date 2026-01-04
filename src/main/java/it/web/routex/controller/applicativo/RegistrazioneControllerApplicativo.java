package it.web.routex.controller.applicativo;

import it.web.routex.domain.CredentialsDTO;
import it.web.routex.utility.factory.RegisterFactory;

public class RegistrazioneControllerApplicativo {

    public boolean register(CredentialsDTO cred) {

        try {

            RegisterFactory.create(cred);

            return true;

        } catch (Exception e) {
            System.err.println("[RegistrazioneUtenteController] Errore durante la registrazione:");
            e.printStackTrace();
            return false;
        }
    }
}