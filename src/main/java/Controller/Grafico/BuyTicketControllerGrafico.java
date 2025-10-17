package Controller.Grafico;

import Bean.AutenticazioneBean;
import Bean.PrezzoTotaleBean;
import Controller.Applicativo.AcquistoController;
import Controller.Applicativo.CityController;
import Factory.ConnectionFactory;
import Model.Domain.City;
import Exception.DAOException;
import Model.Domain.Ruolo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/buyTicket")
public class BuyTicketControllerGrafico extends HttpServlet {

    List<City> cities = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            ConnectionFactory.Cambio_Di_Ruolo(Ruolo.TRAVELER);//commenta e da errore (giustamente)
            CityController cityController = new CityController();
            cities = cityController.getAllCities();

            // passo la lista delle città alla JSP
            request.setAttribute("cities", cities);

            // mostro la pagina
            request.getRequestDispatcher("buyTicket.jsp").forward(request, response);

        } catch (DAOException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("errore", "Errore nel caricamento delle città: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // qui gestisci l'acquisto del biglietto
        try {
            ConnectionFactory.Cambio_Di_Ruolo(Ruolo.TRAVELER);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String city = request.getParameter("city");
        String quantity = request.getParameter("quantity");

        System.out.println("Acquisto di " + quantity + " biglietti per " + city);

        AcquistoController buy = new AcquistoController();
        PrezzoTotaleBean prezzo =  buy.ottieni_prezzo_totale(city,cities, quantity);
        //il controller applcativo ha restitutito la bean del prezzo totale al controller grafico

        request.setAttribute("city", city);
        request.setAttribute("quantity", quantity);
        request.setAttribute("prezzo", prezzo.getPrezzo_totale());
        request.getRequestDispatcher("/confermaPagamento.jsp").forward(request, response);


        //response.sendRedirect("index.jsp");
    }
}
