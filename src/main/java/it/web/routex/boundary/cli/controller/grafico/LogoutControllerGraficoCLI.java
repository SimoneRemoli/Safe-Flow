package it.web.routex.boundary.cli.controller.grafico;
import it.web.routex.boundary.cli.LoggedCLI;
import it.web.routex.boundary.cli.view.GenericErrorCLI;
import it.web.routex.boundary.cli.view.HomePrincipaleCLI;
import it.web.routex.model.domain.Ruolo;
import it.web.routex.utility.factory.ConnectionFactory;
import it.web.routex.utility.singleton.Credentials;

public class LogoutControllerGraficoCLI extends LoggedCLI {

    public void doGet()
    {
        try {
            // 1. Svuota il singleton delle credenziali
            Credentials.getInstanceSingleton().clear();

            logger.info("Logout avvenuto correttamente");
            ConnectionFactory.cambioDiRuolo(Ruolo.LOGIN);

            HomePrincipaleCLI index = new HomePrincipaleCLI();
            index.home();


        } catch (Exception e) {
            logger.error("Logout non avvenuto correttamente", e);
            GenericErrorCLI.mostraErrore("Errore durante il logout. Riprova.");
        }
    }
}
