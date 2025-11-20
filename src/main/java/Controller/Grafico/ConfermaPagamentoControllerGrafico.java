package Controller.Grafico;

import Controller.Applicativo.PagamentoMastercard;
import Controller.Applicativo.PagamentoPaypal;
import Controller.Applicativo.RegistrazionePagamentoController;
import utility.Singleton.Credentials;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/confermaPagamento")
public class ConfermaPagamentoControllerGrafico extends HttpServlet {

    List<String> codiciBiglietti;


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        //  Recupera la sessione senza crearne una nuova se scaduta
        final HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        //  Recupera credenziali dell’utente autenticato
        final Credentials cred = Credentials.getInstance(session);
        if (cred == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        System.out.printf("[PAGAMENTO] Utente loggato: %s %s (%s)%n",
                cred.getNome(), cred.getCognome(), cred.getRuolo());

        //  Parametri dal form
        final String city = request.getParameter("city");
        final String quantityParam = request.getParameter("quantity");
        final String totaleParam = request.getParameter("totale");
        final String metodo = request.getParameter("metodoPagamento");

        if (city == null || quantityParam == null || totaleParam == null || metodo == null) {
            response.sendRedirect("errorePagamento.jsp");
            return;
        }

        //  Parsing valori numerici
        final int quantita;
        final double totale;
        try {
            quantita = Integer.parseInt(quantityParam);
            totale = Double.parseDouble(totaleParam);
        } catch (NumberFormatException e) {
            response.sendRedirect("errorePagamento.jsp");
            return;
        }

        //  Selezione metodo di pagamento ----------------------------------------------
        RegistrazionePagamentoController controllerPagamento = null;
        String messaggio;

        switch (metodo.toLowerCase()) {
            case "mastercard" -> {
                final String numeroCarta = request.getParameter("numeroCarta");
                final String scadenza = request.getParameter("scadenza");
                final String cvv = request.getParameter("cvv");

                if (numeroCarta == null || scadenza == null || cvv == null) {
                    response.sendRedirect("errorePagamento.jsp");
                    return;
                }

                controllerPagamento = new PagamentoMastercard(numeroCarta, scadenza, cvv, cred, totale, quantita, city);
                messaggio = "Pagamento con Mastercard completato (" + numeroCarta + ").";
            }

            case "paypal" -> {
                final String emailPaypal = request.getParameter("emailPaypal");
                final String codiceTransazione = request.getParameter("codiceTransazione");

                if (emailPaypal == null || codiceTransazione == null) {
                    response.sendRedirect("errorePagamento.jsp");
                    return;
                }

                controllerPagamento = new PagamentoPaypal(emailPaypal, codiceTransazione, cred, totale, quantita, city);
                messaggio = "Pagamento con PayPal completato per " + emailPaypal + ".";
            }

            default -> {
                response.sendRedirect("errorePagamento.jsp");
                return;
            }
        }

        try {
            codiciBiglietti = controllerPagamento.run(); // se la vede poi chi viene chiamato
        } catch (Exception e) {
            request.getRequestDispatcher("/errorePagamento.jsp").forward(request, response);
            return;
        }

        // Passaggio dati alla view di successo
        request.setAttribute("city", city);
        request.setAttribute("quantity", quantityParam);
        request.setAttribute("totale", totale);
        request.setAttribute("metodo", metodo);
        request.setAttribute("messaggio", messaggio);
        request.setAttribute("codiciBiglietti", codiciBiglietti);

        request.getRequestDispatcher("/successoPagamento.jsp").forward(request, response);
    }
}
