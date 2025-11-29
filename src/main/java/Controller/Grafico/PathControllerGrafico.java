package Controller.Grafico;
import Bean.InformazioniPercorsoBean;
import Controller.Applicativo.PathController;
import Model.Domain.*;
import utility.Decorator.DecoratorChange.BaseComponent;
import utility.Decorator.DecoratorChange.CheckCambiamentiDecorator;
import utility.Decorator.DecoratorChange.Component;
import utility.Singleton.Credentials;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/PathControllerGrafico")
public class PathControllerGrafico extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            final HttpSession session = request.getSession(false);
            if (session == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            final Credentials cred = Credentials.getInstanceSingleton();
            RouteInput route = RouteInputExtractor.from(request);
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
            } catch (Exception e) {
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


/*
 @WebServlet("/PathControllerGrafico")
public class PathControllerGrafico extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            RouteInput input = RouteInputExtractor.from(request);

            if (!RouteValidator.isValid(input)) {
                response.sendRedirect("datiPercorsoAssenti.jsp");
                return;
            }

            Credentials cred = Credentials.getInstanceSingleton();
            String status = UserStatusResolver.resolve(cred);

            InformazioniPercorsoBean dto = resolveRoute(input);

            RouteDecoratorService.decorate(dto, request);

            request.setAttribute("status", status);
            request.setAttribute("inizio", input.start());
            request.setAttribute("fine", input.end());
            request.setAttribute("city", input.city());

            saveUserRouteIfNeeded(cred, input, dto);

            request.getRequestDispatcher("PathNOREG.jsp").forward(request, response);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private InformazioniPercorsoBean resolveRoute(RouteInput input) throws Exception {
        PathController controller = new PathController();
        return controller.run(input.start(), input.end(), input.city());
    }

    private void saveUserRouteIfNeeded(Credentials cred, RouteInput input,
                                       InformazioniPercorsoBean dto) throws Exception {
        if (cred.getNome() != null) {
            PathController pathCtrl = new PathController();
            pathCtrl.save_route(cred, null); // puoi migliorare passando un bean
        }
    }
}


 */