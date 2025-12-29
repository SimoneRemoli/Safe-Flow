package it.web.routex.controller.grafico;

import it.web.routex.controller.applicativo.UpdateNotificationsControllerApplicativo;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/updateNotifications")
public class UpdateNotificationsControllerGrafico extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            String[] risolte = request.getParameterValues("risolte");

            UpdateNotificationsControllerApplicativo service = new UpdateNotificationsControllerApplicativo();

            service.aggiornaStatoNotifiche(risolte);

            request.getSession().setAttribute("solved", "Segnalazione aggiornata.");

            response.sendRedirect(request.getContextPath() + "/viewNotifications");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
