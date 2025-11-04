package Controller.Grafico;

import Bean.CityBean;
import Bean.PrezzoTotaleBean;
import Controller.Applicativo.CityController;
import Exception.DAOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller grafico per la gestione del flusso "Buy Ticket".
 * Gestisce sia la visualizzazione della pagina di acquisto (GET)
 * che l'elaborazione dei dati di acquisto (POST).
 * @SimoneRemoli
 */
@WebServlet("/buyTicket")
public class BuyTicketControllerGrafico extends HttpServlet {

    /**
     * Mostra la pagina di acquisto dei biglietti con la lista delle città disponibili.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Recupero delle città tramite il controller applicativo
            CityController cityController = new CityController();
            List<CityBean> cities = cityController.getAllCities();

            // Passo la lista alla view
            request.setAttribute("cities", cities);

            // Mostro la pagina JSP
            request.getRequestDispatcher("buyTicket.jsp").forward(request, response);

        } catch (DAOException e) {
            e.printStackTrace();
            request.setAttribute("errore", "Errore nel caricamento delle città: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    /**
     * Gestisce la richiesta di acquisto di uno o più biglietti.
     * Calcola il prezzo totale e inoltra alla pagina di conferma pagamento.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String city = request.getParameter("city");
        String quantity = request.getParameter("quantity");

        System.out.println("Richiesta acquisto: " + quantity + " biglietti per la città di " + city);

        try {
            // Delego al controller applicativo il calcolo del prezzo totale
            CityController cityController = new CityController();
            PrezzoTotaleBean prezzo = cityController.ottieni_prezzo_totale(city, quantity);

            // Passo i dati alla pagina di conferma
            request.setAttribute("city", city);
            request.setAttribute("quantity", quantity);
            request.setAttribute("prezzo", prezzo.getPrezzo_totale());
            // Mostro la pagina di conferma
            request.getRequestDispatcher("/confermaPagamento.jsp").forward(request, response);

        } catch (DAOException e) {
            e.printStackTrace();
            request.setAttribute("errore", "Errore durante l'elaborazione dell'acquisto: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
