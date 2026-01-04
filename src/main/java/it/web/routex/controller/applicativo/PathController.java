package it.web.routex.controller.applicativo;
import it.web.routex.bean.InformazioniPercorsoBean;
import it.web.routex.bean.RoutingRequestBean;
import it.web.routex.exception.*;
import it.web.routex.dao.RestituisciIdStazioniPartenzaArrivoDAO;
import it.web.routex.dao.RouteDAO;
import it.web.routex.domain.Route;
import it.web.routex.record.RouteRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import it.web.routex.utility.facade.FacadePath;
import it.web.routex.utility.singleton.Credentials;
import java.sql.SQLException;


public class PathController
{
    public InformazioniPercorsoBean run(String startStation, String endStation, String city) throws IllegalArgumentException, FuoriRangeExceptionRemoli, UnreacheableNodeExceptionRemoli, SQLException, DAOExceptionRemoli {
        RestituisciIdStazioniPartenzaArrivoDAO dao = new RestituisciIdStazioniPartenzaArrivoDAO();
        dao.restituisciIdStazioni(startStation, endStation, city);

        final Logger logger = LoggerFactory.getLogger(getClass());


        int codeStartStation=9999;
        int codeFinishStation=9999;

        codeStartStation = dao.getStazioneDiPartenza();
        codeFinishStation = dao.getStazioneDiArrivo();

        if (codeFinishStation == 9999 || codeStartStation == 9999) {
            throw new DAOExceptionRemoli("Stazioni non trovate nella città selezionata.");
        }

        logger.info("Stazioni trovate. Id stazione di partenza = {} e stazione di arrivo = {}", codeStartStation, codeFinishStation);

        RoutingRequestBean route = new RoutingRequestBean();
        route.setCity(city);
        route.setStartId(codeStartStation);
        route.setEndId(codeFinishStation);
        return new FacadePath().compute(route);
    }
    public boolean saveRoute(Credentials cred, InformazioniPercorsoBean dto, RouteRecord route, String status) {

        final Logger logger = LoggerFactory.getLogger(getClass());
        String cf = cred.getCodiceFiscale();
        try {

            if (cf != null) {
                Route info = new Route(dto,route,status);
                RouteDAO saveRoute = new RouteDAO();
                saveRoute.save(info); //uso route per salvare il percorso. Poi RouteBean è diverso, non ha utente
                return true;
            } else {
                throw new CFIsNullRemoli("Devi effettuare il login per salvare il percorso.",
                        "CF nullo: richiesta di salvataggio senza autenticazione.",
                        "ERR-CF-NULL",
                        CFIsNullRemoli.Severity.CRITICAL);
            }
        }catch (CFIsNullRemoli e)
        {
            logger.error("Utente non autenticato, impossibile salvare il percorso. {}", e.toString());
        }
        catch (DAOExceptionRemoli e)
        {
            logger.error("Errore nel salvataggio del percorso. {}", e.toString());
        } catch (InvalidRouteException e) {
            logger.error("Errore nella conversione dei dati nel percorso. {}", e.toString());
        }
        return false;
    }
}
