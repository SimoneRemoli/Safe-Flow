package Controller.Grafico;
import Bean.InformazioniPercorsoBean;
import Controller.Applicativo.PathController;
import Model.DAO.RouteDAO;
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
            String status="";

            // Recupera il valore della città selezionata dal form
            System.out.println(("ciao"));
            String city = request.getParameter("city");
            String startStation = request.getParameter("startStation");
            String endStation = request.getParameter("endStation");

            final HttpSession session = request.getSession(false);
            if (session == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            //  Recupera credenziali dell’utente autenticato
            final Credentials cred = Credentials.getInstanceSingleton();

            if (city == null || city.trim().isEmpty() || startStation == null || startStation.trim().isEmpty() || endStation == null || endStation.trim().isEmpty())
            {
                response.sendRedirect("datiPercorsoAssenti.jsp");
                return;
            }


            if(cred.getNome()!=null) {

                if (cred.getDisabile()) {
                    status = "Disabled Traveler";
                } else {
                    status = "Non-disabled Traveler";
                }
            }
            else {
                status = "NO REG Traveler";
            }

            System.out.println("City: " + city);
            System.out.println("Start Station: " + startStation);
            System.out.println("End Station: " + endStation);

            InformazioniPercorsoBean dto = new InformazioniPercorsoBean();
            try {

                PathController path = new PathController();
                dto = path.run(startStation, endStation, city); //parte il controller applicativo
            } catch (Exception e) {
                //e.printStackTrace(); // stampa la causa vera sul log Tomcat
                //throw new RuntimeException(e);
                request.setAttribute("stazioniNonValide", true);
                request.getRequestDispatcher("search.jsp").forward(request, response);

                //return;
            }

            Component c = new CheckCambiamentiDecorator(new BaseComponent()); //Decorator Pattern

            request.setAttribute("percorsi", dto.getCityLife().getPercorsi_Con_Nomi());
            request.setAttribute("numero_cambi", dto.getCityLife().getNumero_cambi());
            request.setAttribute("linee", dto.getCityLife().getLinee());
            request.setAttribute("numero", dto.getNumero_stazioni_usate());
            request.setAttribute("status", status);
            request.setAttribute("minutaggio", dto.getMinutaggio());
            request.setAttribute("inizio", startStation);
            request.setAttribute("fine", endStation);
            request.setAttribute("city", city);
            request.setAttribute("stazionitotali", dto.getCityLife().getNumero_stazioni_totali());
            request.setAttribute("suolometropolitano", dto.getPercentuale_stazioni_usate());


            ArrayList<String> cambi = c.getChanges(dto.getCityLife().getSequenze_di_cambiamento());//in più
            request.setAttribute("listacambi", cambi); //in più


            ArrayList<String> cambicruciali = c.getChanges(dto.getCityLife().getSequenze_nodi_cruciali());//in più
            request.setAttribute("nodicruciali", cambicruciali); //in più


            if (session != null) {
                PathController pathCtrl = new PathController();
                pathCtrl.save_route(cred, request);
            }



            //inoltro la richiesta al jsp
            RequestDispatcher dispatcher = request.getRequestDispatcher("PathNOREG.jsp");
            dispatcher.forward(request, response);


            //  logica per calcolare il percorso o qualsiasi altra logica
            String result = "Route from " + startStation + " to " + endStation + " in " + city;
            System.out.println(result);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}