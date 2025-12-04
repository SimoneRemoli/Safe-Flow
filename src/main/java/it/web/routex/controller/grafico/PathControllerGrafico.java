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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    {
        try {
            RouteRecord route;
            final HttpSession session = request.getSession(false);
            if (session == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            final Credentials cred = Credentials.getInstanceSingleton();

            try {
                route = RouteInputExtractor.from(request);
            } catch (InvalidRouteInputExceptionRemoli e)
            {
                logger.error("Errore nell'input del percorso {}", e.getMessage());
                request.getRequestDispatcher("search.jsp").forward(request, response);
                return;
            }
            String status = UserStatusResolver.resolve(cred);

            if(!RouteValidator.isValid(route))
            {
                response.sendRedirect("datiPercorsoAssenti.jsp");
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
                request.getRequestDispatcher("search.jsp").forward(request, response);
            }

            RouteDecoratorService.decorate(dto, request);
            request.setAttribute("status", status);
            request.setAttribute("inizio", route.start());
            request.setAttribute("fine", route.end());
            request.setAttribute("city", route.city());


            PathController pathCtrl = new PathController();
            pathCtrl.saveRoute(cred, request);
            logger.info("Percorso salvato correttamente per l'utente {} {} {} relativo alla città {}.", cred.getNome(), cred.getCognome(), cred.getRuolo(), route.city());


            //inoltro la richiesta al jsp
            RequestDispatcher dispatcher = request.getRequestDispatcher("PathNOREG.jsp");
            dispatcher.forward(request, response);

            //  logica per calcolare il percorso o qualsiasi altra logica
            String result = "Route from " + route.start() + " to " + route.end() + " in " + route.city();
            logger.info(result);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

