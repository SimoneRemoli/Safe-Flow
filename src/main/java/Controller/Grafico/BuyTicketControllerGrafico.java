package Controller.Grafico;

import Bean.CityBean;
import Bean.PrezzoTotaleBean;
import Controller.Applicativo.CityController;
import Exception.DAOExceptionRemoli;
import Model.Extractor.BuyTicketExtractor;
import Model.Record.BuyTicketRecord;
import Exception.InvalidBuyTicketInputExceptionRemoli;
import Exception.InvalidPriceCalculationExceptionRemoli;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
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
    {
        try {
            try {
                // Recupero delle città tramite il controller applicativo
                CityController cityController = new CityController();
                List<CityBean> cities = cityController.getAllCities();

                // Passo la lista alla view
                request.setAttribute("cities", cities);

                // Mostro la pagina JSP
                request.getRequestDispatcher("buyTicket.jsp").forward(request, response);

            } catch (DAOExceptionRemoli e) {
                e.printStackTrace();
                request.setAttribute("errore", "Errore nel caricamento delle città: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gestisce la richiesta di acquisto di uno o più biglietti.
     * Calcola il prezzo totale e inoltra alla pagina di conferma pagamento.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            BuyTicketRecord buyTicket;
            final HttpSession session = request.getSession(false);
            if (session == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            try {
                buyTicket = BuyTicketExtractor.from(request);
            } catch (InvalidBuyTicketInputExceptionRemoli e) {
                System.out.println("Errore di validazione input: " + e.getMessage());
                request.setAttribute("errore", e.getUserMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            System.out.println("Richiesta acquisto: " + buyTicket.quantity() + " biglietti per la città di " + buyTicket.city());

            try {
                // Delego al controller applicativo il calcolo del prezzo totale
                CityController cityController = new CityController();
                PrezzoTotaleBean prezzo = cityController.ottieni_prezzo_totale(buyTicket.city(), buyTicket.quantity());

                // Passo i dati alla pagina di conferma
                request.setAttribute("city", buyTicket.city());
                request.setAttribute("quantity", String.valueOf(buyTicket.quantity()));
                request.setAttribute("prezzo", prezzo.getPrezzo_totale());
                // Mostro la pagina di conferma
                request.getRequestDispatcher("/confermaPagamento.jsp").forward(request, response);

            } catch (DAOExceptionRemoli e) {
                e.printStackTrace();
                request.setAttribute("errore", "Errore durante l'elaborazione dell'acquisto: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            } catch (InvalidPriceCalculationExceptionRemoli e) {
                System.out.println("Errore nel calcolo del prezzo: " + e.getMessage());
                request.setAttribute("errore", e.getUserMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
