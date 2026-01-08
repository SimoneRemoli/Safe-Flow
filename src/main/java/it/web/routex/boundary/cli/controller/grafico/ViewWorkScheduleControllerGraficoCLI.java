package it.web.routex.boundary.cli.controller.grafico;


import it.web.routex.bean.WorkerScheduleBean;
import it.web.routex.boundary.cli.LoggedCLI;
import it.web.routex.boundary.cli.view.GenericErrorCLI;
import it.web.routex.boundary.cli.view.LoginViewCLI;
import it.web.routex.boundary.cli.view.ViewWorkScheduleCLI;
import it.web.routex.controller.applicativo.ViewWorkScheduleControllerApplicativo;
import it.web.routex.exception.BrondiException;
import it.web.routex.utility.singleton.Credentials;

public class ViewWorkScheduleControllerGraficoCLI extends LoggedCLI {

    public void doGet() {
        try {

            Credentials cred = Credentials.getInstanceSingleton();
            if (cred == null || cred.getCodiceFiscale() == null) {
                redirectToLogin();
                return;
            }

            ViewWorkScheduleControllerApplicativo service = new ViewWorkScheduleControllerApplicativo();
            WorkerScheduleBean schedule = service.getSchedule(cred.getCodiceFiscale());

            ViewWorkScheduleCLI.mostraOrario(schedule.getOraInizio(), schedule.getOraFine(), schedule.getLuogoDiLavoro(), schedule.getDurataTurno());


        } catch (BrondiException e) {
            logger.error(
                    "Errore Brondi. Messaggio={} CodiceErrore={} Dettagli={}",
                    e.getMessage(),
                    e.getCodiceDiErrore(),
                    e.getDetails()
            );

            forwardError(e.getMessage());
        } catch (Exception e) {
            logger.error("Errore generico", e);
            forwardError("Errore imprevisto");
        }
    }

    private void forwardError(String message) {
        GenericErrorCLI.mostraErrore(message);
    }

    private void redirectToLogin() {
        LoginViewCLI.mostraLogin();
    }
}
