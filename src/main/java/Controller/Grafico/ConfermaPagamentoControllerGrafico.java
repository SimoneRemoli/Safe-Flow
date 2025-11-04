package Controller.Grafico;

import Bean.UtenteBeanGenerico;
import Controller.Applicativo.PagamentoMastercard;
import Controller.Applicativo.PagamentoPaypal;
import Controller.Applicativo.RegistrazionePagamentoController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Exception.DAOException;


@WebServlet("/confermaPagamento")
public class ConfermaPagamentoControllerGrafico extends HttpServlet {
    int esito = 0;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false); // non crea nuova sessione se scaduta
        UtenteBeanGenerico utente = (UtenteBeanGenerico) session.getAttribute("utenteLoggato");

        System.out.println("CHI SONO: "+ utente.getRuolo()+ " "+utente.getNome());

        String city = request.getParameter("city");
        String quantity = request.getParameter("quantity");
        String totale = request.getParameter("totale");
        String metodo = request.getParameter("metodoPagamento");

        String messaggio = "";
        System.out.println("ci siamoooo");
        // eventuale logica: salvataggio DB, invio email, ecc.
        RegistrazionePagamentoController reg = null;

        if ("mastercard".equals(metodo)) {
            String numeroCarta = request.getParameter("numeroCarta");
            String scadenza = request.getParameter("scadenza");
            String cvv = request.getParameter("cvv");
            messaggio = "Pagamento con Mastercard completato (" + numeroCarta + ").";
            reg = new PagamentoMastercard(numeroCarta, scadenza, cvv);

        } else if ("paypal".equals(metodo)) {
            String emailPaypal = request.getParameter("emailPaypal");
            String codiceTransazione = request.getParameter("codiceTransazione");
            messaggio = "Pagamento con PayPal completato per " + emailPaypal + ".";
            reg = new PagamentoPaypal();

        }
        try {
            esito = reg.registra_pagamento();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Salvataggio o logica aggiuntiva qui (DB, notifiche, ecc.)

        reg.setUtente(utente);
        List<String> codiciBiglietti = new ArrayList<>();
        if (esito == 1) {
            int quantita = Integer.parseInt(quantity);

            for (int i = 0; i < quantita; i++) {
                codiciBiglietti.add(UUID.randomUUID().toString());
            }


//prendere i dati di chi ha fatto il pagamento
            //System.out.println("Pagamento effettuato da: " + utente.getNome() + " " + utente.getCognome());

            try {
                reg.gestisciPagamento(Double.parseDouble(totale), codiciBiglietti, city, utente);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


            request.setAttribute("city", city);
            request.setAttribute("quantity", quantity);
            request.setAttribute("totale", Double.valueOf(totale));
            request.setAttribute("metodo", metodo);
            request.setAttribute("messaggio", messaggio);
            request.setAttribute("codiciBiglietti", codiciBiglietti);
            request.getRequestDispatcher("/successoPagamento.jsp").forward(request, response);



        }
        else
        {
            request.getRequestDispatcher("/errorePagamento.jsp").forward(request, response);
        }





    }
}
