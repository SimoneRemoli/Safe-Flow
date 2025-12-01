package Controller.Grafico;

import Bean.CityBean;
import Bean.PrezzoTotaleBean;
import Controller.Applicativo.CityController;
import Exception.DAOExceptionRemoli;
import Model.Domain.LoggedHttpServlet;
import Model.Extractor.BuyTicketExtractor;
import Model.Record.BuyTicketRecord;
import Exception.InvalidBuyTicketInputExceptionRemoli;
import Exception.InvalidPriceCalculationExceptionRemoli;
import Exception.InvalidCityDataExceptionRemoli;
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
public class BuyTicketControllerGrafico extends LoggedHttpServlet {

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
                logger.info("Visualizzata la pagina di acquisto biglietti con size={} città disponibili.", cities.size());

            } catch (DAOExceptionRemoli e) {
                e.printStackTrace();
                request.setAttribute("errore", "Errore nel caricamento delle città: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
                logger.error("Errore nella presentazione della view il caricamento delle città: {}", e.toString());
            } catch (InvalidCityDataExceptionRemoli e) {
                request.setAttribute("errore", e.getUserMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
                logger.error("Errore nei dati delle città: {}", e.toString());
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
                logger.error("Errore di validazione input nell'acquisto biglietti: {}", e.toString());
                request.setAttribute("errore", e.getUserMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            logger.info("Elaborazione richiesta acquisto biglietti per città='{}', quantità={}", buyTicket.city(), buyTicket.quantity());

            try {
                // Delego al controller applicativo il calcolo del prezzo totale
                CityController cityController = new CityController();
                PrezzoTotaleBean prezzo = cityController.ottieni_prezzo_totale(buyTicket.city(), buyTicket.quantity());
                logger.info("Elaborazione prezzo: prezzo={}", buyTicket.city(),prezzo.getPrezzo_totale());


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
                logger.error("Errore nella DAO. ", e.toString());
            } catch (InvalidPriceCalculationExceptionRemoli e) {
                logger.error("Errore nei dati inseriti: {}", e.toString());
                request.setAttribute("errore", e.getUserMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
