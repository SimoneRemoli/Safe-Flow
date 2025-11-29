package Controller.Grafico;
import Bean.InformazioniPercorsoBean;
import Controller.Applicativo.PathController;
import Model.Domain.*;
import Model.Extractor.RouteInputExtractor;
import Model.Record.RouteRecord;
import utility.Singleton.Credentials;
import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import Exception.DAOExceptionRemoli;
import Exception.FuoriRangeExceptionRemoli;
import Exception.UnreacheableNodeExceptionRemoli;
import Exception.InvalidRouteInputExceptionRemoli;

import java.sql.SQLException;

@WebServlet("/PathControllerGrafico")
public class PathControllerGrafico extends HttpServlet {

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
                System.out.println("Errore nell'input del percorso: " + e.getMessage());
                request.getRequestDispatcher("search.jsp").forward(request, response);
                return;
            }
            String status = UserStatusResolver.resolve(cred);

            if(!RouteValidator.isValid(route))
            {
                response.sendRedirect("datiPercorsoAssenti.jsp");
                return;
            }
            System.out.println("City: " + route.city());
            System.out.println("Start Station: " + route.start());
            System.out.println("End Station: " + route.end());

            InformazioniPercorsoBean dto = new InformazioniPercorsoBean();

            try {
                PathController path = new PathController();
                dto = path.run(route.start(), route.end(), route.city()); //controller applicativo
            } catch (IllegalArgumentException | UnreacheableNodeExceptionRemoli |
                     FuoriRangeExceptionRemoli | DAOExceptionRemoli | SQLException e) {
                System.out.println("Errore: " + e.getMessage());
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


            //inoltro la richiesta al jsp
            RequestDispatcher dispatcher = request.getRequestDispatcher("PathNOREG.jsp");
            dispatcher.forward(request, response);


            //  logica per calcolare il percorso o qualsiasi altra logica
            String result = "Route from " + route.start() + " to " + route.end() + " in " + route.city();
            System.out.println(result);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

