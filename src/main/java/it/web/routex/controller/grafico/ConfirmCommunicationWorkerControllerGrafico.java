package it.web.routex.controller.grafico;

import it.web.routex.bean.MessageBean;
import it.web.routex.controller.applicativo.ConfirmCommunicationControllerApplicativo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/confirmCommunicationWorker")
public class ConfirmCommunicationWorkerControllerGrafico extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String testo = request.getParameter("message");

        // Creazione bean
        MessageBean mess = new MessageBean();
        mess.setMessage(testo);
        mess.setDate(new Timestamp(System.currentTimeMillis()));

        try {
            ConfirmCommunicationControllerApplicativo service = new ConfirmCommunicationControllerApplicativo();
            service.communication(mess);

            // Messaggio di successo in sessione (verrà mostrato dopo il redirect)
            request.getSession().setAttribute("alertMessage", "Comunicazione inviata con successo!");

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("alertMessage", "Errore nell'invio della comunicazione.");
        }

        // *** MODIFICA QUI ***
        // Non puntare a .jsp, ma alla Servlet che carica la lista!
        // (Assumendo che la servlet di visualizzazione sia mappata su "/viewNotifications")
        response.sendRedirect(request.getContextPath() + "/viewNotifications");
    }

}