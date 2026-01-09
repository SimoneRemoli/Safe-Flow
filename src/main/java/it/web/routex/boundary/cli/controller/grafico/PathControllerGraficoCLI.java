package it.web.routex.boundary.cli.controller.grafico;
import it.web.routex.bean.InformazioniPercorsoBean;
import it.web.routex.boundary.cli.LoggedCLI;
import it.web.routex.boundary.cli.domain.RouteDecoratorServiceCLI;
import it.web.routex.boundary.cli.extractor.RouteInputExtractorCLI;
import it.web.routex.boundary.cli.view.ErrorePath;
import it.web.routex.boundary.cli.view.PathNOREGCLI;
import it.web.routex.boundary.cli.view.StartExploringCLI;
import it.web.routex.controller.applicativo.PathController;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.exception.FuoriRangeExceptionRemoli;
import it.web.routex.exception.InvalidRouteInputExceptionRemoli;
import it.web.routex.exception.UnreacheableNodeExceptionRemoli;
import it.web.routex.domain.RouteValidator;
import it.web.routex.domain.UserStatusResolver;
import it.web.routex.record.RouteRecord;
import it.web.routex.utility.builder.PathNORegInitBuilder;
import it.web.routex.utility.singleton.Credentials;
import java.sql.SQLException;

public class PathControllerGraficoCLI extends LoggedCLI
{
    public void post()
    {
        final Credentials cred = Credentials.getInstanceSingleton();

        RouteRecord route = estrattorePercorso();
        String status = UserStatusResolver.resolve(cred);

        if(!RouteValidator.isValid(route)) ErrorePath.mostraErrore();

        logger.info("Dati per il percorso acquisiti correttamente. Città={}, StazPart={}, StazArr={}", route.city(), route.start(), route.end());
        InformazioniPercorsoBean dto = new InformazioniPercorsoBean();

        try {
            PathController path = new PathController();
            dto = path.run(route.start(), route.end(), route.city()); //controller applicativo
        } catch (IllegalArgumentException | UnreacheableNodeExceptionRemoli |
                 FuoriRangeExceptionRemoli | DAOExceptionRemoli | SQLException e) {
            logger.error("Errore processamento dati percorso {}", e.toString());
            StartExploringCLI.mostraExploring();
            return;
        }

        RouteDecoratorServiceCLI.decorate(dto);

        new PathNORegInitBuilder(status).start(route.start()).end(route.end()).city(route.city()).build();

        PathController pathCtrl = new PathController();
        boolean salvato = pathCtrl.saveRoute(cred,dto,route, status);
        if(salvato)
            logger.info("[CLI]Percorso salvato correttamente per l'utente {} {} {} relativo alla città {}.", cred.getNome(), cred.getCognome(), cred.getRuolo(), route.city());
        else
            logger.info("[CLI]Percorso non salvato per l'utente {} {} {} relativo alla città {}.", cred.getNome(), cred.getCognome(), cred.getRuolo(), route.city());


        //inoltro la richiesta al jsp
        PathNOREGCLI.stampa();
        //  logica per calcolare il percorso o qualsiasi altra logica
        String result = "[CLI]Route from " + route.start() + " to " + route.end() + " in " + route.city();
        logger.info(result);
    }
    private RouteRecord estrattorePercorso()
    {
        try {
            return RouteInputExtractorCLI.from();
        } catch (InvalidRouteInputExceptionRemoli e)
        {
            logger.error("Errore nell'input del percorso {}", e.getMessage());
            StartExploringCLI.mostraExploring();
            return null;
        }
    }
}