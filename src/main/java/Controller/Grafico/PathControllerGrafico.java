package Controller.Grafico;
import Bean.InformazioniPercorsoBean;
import Controller.Applicativo.PathController;
import Model.Domain.*;
import Model.Extractor.RouteInputExtractor;
import Model.Record.RouteRecord;
import utility.Singleton.Credentials;
import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import Exception.DAOExceptionRemoli;
import Exception.FuoriRangeExceptionRemoli;
import Exception.UnreacheableNodeExceptionRemoli;
import Exception.InvalidRouteInputExceptionRemoli;
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
            pathCtrl.save_route(cred, request);
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

