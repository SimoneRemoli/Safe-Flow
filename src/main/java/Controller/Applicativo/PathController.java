package Controller.Applicativo;
import Bean.InformazioniPercorsoBean;
import Bean.RoutingRequestBean;
import Model.DAO.RestituisciIdStazioniPartenzaArrivoDAO;
import Model.DAO.RouteDAO;
import Model.Domain.Route;
import utility.Facade.FacadePath;
import Exception.DAOExceptionRemoli;
import utility.Singleton.Credentials;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import Exception.CFIsNullRemoli;

public class PathController
{
    public InformazioniPercorsoBean run(String startStation, String endStation, String city) throws Exception
    {
        RestituisciIdStazioniPartenzaArrivoDAO dao = new RestituisciIdStazioniPartenzaArrivoDAO();
        dao.restituisciIdStazioni(startStation, endStation, city);

        int codeStartStation=9999;
        int codeFinishStation=9999;

        codeStartStation = dao.getStazioneDiPartenza();
        codeFinishStation = dao.getStazioneDiArrivo();

        if (codeFinishStation == 9999 || codeStartStation == 9999) {
            throw new DAOExceptionRemoli("Stazioni non trovate nella città selezionata.");
        }

        System.out.println("Id partenza = " + codeStartStation);
        System.out.println("Id arrivo = " + codeFinishStation);


        RoutingRequestBean route = new RoutingRequestBean();
        route.setCity(city);
        route.setStartId(codeStartStation);
        route.setEndId(codeFinishStation);

        InformazioniPercorsoBean trasferimento = new FacadePath().compute(route);
        return trasferimento;
    }
    public void save_route(Credentials cred, HttpServletRequest request) throws DAOExceptionRemoli, SQLException, CFIsNullRemoli {
        String cf = cred.getCodiceFiscale();

        if (cf!= null) {
            Route info = new Route(request);
            RouteDAO saveRoute = new RouteDAO();
            saveRoute.save(info); //uso route per salvare il percorso. Poi RouteBean è diverso, non ha utente
        }
        else
        {
            System.out.println("Utente non autenticato, impossibile salvare il percorso.");
            throw new CFIsNullRemoli("Devi effettuare il login per salvare il percorso.",
                    "CF nullo: richiesta di salvataggio senza autenticazione.",
                    "ERR-CF-NULL",
                    CFIsNullRemoli.Severity.CRITICAL);
            //throw new DAOExceptionRemoli("Utente non autenticato, impossibile salvare il percorso.");
        }
    }
}
