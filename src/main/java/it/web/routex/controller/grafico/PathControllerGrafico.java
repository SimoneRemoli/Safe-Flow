package it.web.routex.controller.grafico;
import it.web.routex.bean.InformazioniPercorsoBean;
import it.web.routex.controller.applicativo.PathController;
import it.web.routex.model.domain.LoggedHttpServlet;
import it.web.routex.model.domain.RouteDecoratorService;
import it.web.routex.model.domain.RouteValidator;
import it.web.routex.model.domain.UserStatusResolver;
import it.web.routex.model.extractor.RouteInputExtractor;
import it.web.routex.model.record.RouteRecord;
import it.web.routex.utility.singleton.Credentials;
import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.exception.FuoriRangeExceptionRemoli;
import it.web.routex.exception.UnreacheableNodeExceptionRemoli;
import it.web.routex.exception.InvalidRouteInputExceptionRemoli;
import java.sql.SQLException;

@WebServlet("/PathControllerGrafico")
public class PathControllerGrafico extends LoggedHttpServlet {
    private static final String FORWARDING = "Errore nel forwarding";


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    {

            RouteRecord route;
            final HttpSession session = request.getSession(false);
            if (session == null) {
                try {
                    response.sendRedirect("login.jsp");
                    return;
                }catch(Exception e){
                    logger.info(FORWARDING,e);
                }
            }

            final Credentials cred = Credentials.getInstanceSingleton();

            route = estrattorePercorso(request, response);
            String status = UserStatusResolver.resolve(cred);

            if(!RouteValidator.isValid(route))
            {
                try {
                    response.sendRedirect("datiPercorsoAssenti.jsp");
                }catch(Exception e){
                    logger.info(FORWARDING,e);
                }
                return;
            }

            logger.info("Dati per il percorso acquisiti correttamente. Città={}, StazPart={}, StazArr={}", route.city(), route.start(), route.end());
            InformazioniPercorsoBean dto = new InformazioniPercorsoBean();

            try {
                PathController path = new PathController();
                dto = path.run(route.start(), route.end(), route.city()); //controller applicativo
            } catch (IllegalArgumentException | UnreacheableNodeExceptionRemoli |
                     FuoriRangeExceptionRemoli | DAOExceptionRemoli | SQLException e) {
                logger.error("Errore processamento dati percorso {}", e.toString());
                request.setAttribute("stazioniNonValide", true);
                try {
                    request.getRequestDispatcher("search.jsp").forward(request, response);
                }catch(Exception ex){
                    logger.info(FORWARDING,ex);
                }
            }

            RouteDecoratorService.decorate(dto, request);
            request.setAttribute("status", status);
            request.setAttribute("inizio", route.start());
            request.setAttribute("fine", route.end());
            request.setAttribute("city", route.city());


            PathController pathCtrl = new PathController();
            Boolean salvato = pathCtrl.saveRoute(cred, request);
            if(salvato)
                logger.info("Percorso salvato correttamente per l'utente {} {} {} relativo alla città {}.", cred.getNome(), cred.getCognome(), cred.getRuolo(), route.city());
            else
                logger.info("Percorso non salvato per l'utente {} {} {} relativo alla città {}.", cred.getNome(), cred.getCognome(), cred.getRuolo(), route.city());



        //inoltro la richiesta al jsp
            RequestDispatcher dispatcher = request.getRequestDispatcher("PathNOREG.jsp");
            try {
                dispatcher.forward(request, response);
            }catch(Exception e){
                logger.info(FORWARDING, e);
            }
            //  logica per calcolare il percorso o qualsiasi altra logica
            String result = "Route from " + route.start() + " to " + route.end() + " in " + route.city();
            logger.info(result);


    }
    private RouteRecord estrattorePercorso(HttpServletRequest request, HttpServletResponse response)
    {
        try {
            return RouteInputExtractor.from(request);
        } catch (InvalidRouteInputExceptionRemoli e)
        {
            logger.error("Errore nell'input del percorso {}", e.getMessage());
            try {
                request.getRequestDispatcher("search.jsp").forward(request, response);
            }catch(Exception ex){
                logger.error(FORWARDING,ex);
            }
            return null;
        }
    }
}

