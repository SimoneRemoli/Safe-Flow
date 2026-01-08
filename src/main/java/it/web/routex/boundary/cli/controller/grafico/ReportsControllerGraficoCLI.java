package it.web.routex.boundary.cli.controller.grafico;

import it.web.routex.bean.ReportsStatsBean;
import it.web.routex.boundary.cli.LoggedCLI;
import it.web.routex.boundary.cli.view.GenericErrorCLI;
import it.web.routex.boundary.cli.view.ViewReportsAndStatisticsCLI;
import it.web.routex.controller.applicativo.ReportsControllerApplicativo;
import it.web.routex.exception.DAOExceptionRemoli;

import java.io.IOException;

public class ReportsControllerGraficoCLI extends LoggedCLI {

    public void doGet() {

        try {
            ReportsControllerApplicativo service = new ReportsControllerApplicativo();
            ReportsStatsBean stats = service.recuperaStatistiche();

            ViewReportsAndStatisticsCLI.mostrareports(stats);



        } catch (DAOExceptionRemoli e) {
            logger.error("Errore statistiche admin", e);
            GenericErrorCLI.mostraErrore("Errore nel recupero statistiche.");

        }
    }
}
