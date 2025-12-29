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

@WebServlet("/confirmCommunication")
public class ConfirmCommunicationControllerGrafico extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String testo = request.getParameter("message");

        MessageBean mess = new MessageBean();
        mess.setMessage(testo);
        mess.setDate(new Timestamp(System.currentTimeMillis()));

        try {
            ConfirmCommunicationControllerApplicativo service = new ConfirmCommunicationControllerApplicativo();
            service.communication(mess);

            request.getSession().setAttribute("alertMessage", "Comunicazione inviata con successo!");

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("alertMessage", "Errore nell'invio della comunicazione.");
        }

        response.sendRedirect(request.getContextPath() + "/indexAdmin.jsp");
    }

}