package it.web.routex.controller.grafico;

import it.web.routex.bean.MessageBean;
import it.web.routex.controller.applicativo.UpdateNotificationsControllerApplicativo;
import it.web.routex.exception.DAOExceptionRemoli;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.Timestamp;@WebServlet("/updateNotifications")
public class UpdateNotificationsControllerGrafico extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        try {
            String[] risolte = request.getParameterValues("risolte");

            UpdateNotificationsControllerApplicativo service = new UpdateNotificationsControllerApplicativo();

            if (risolte != null) {
                for (String r : risolte) {

                    // PARSING TECNICO (boundary)
                    String[] parts = r.split("\\|", 2);
                    long timestamp = Long.parseLong(parts[0]);
                    String message = parts[1];

                    // BEAN di trasporto
                    MessageBean bean = new MessageBean(
                            message,
                            new Timestamp(timestamp)
                    );

                    service.aggiornaStatoNotifica(bean);
                }
            }

            response.sendRedirect(
                    request.getContextPath() + "/viewNotifications"
            );

        } catch (DAOExceptionRemoli e) {

            // ERRORE TECNICO DI PERSISTENZA
            request.setAttribute(
                    "errore",
                    "Impossibile aggiornare le notifiche. Riprovare più tardi."
            );

            try {
                request.getRequestDispatcher("/error.jsp")
                        .forward(request, response);
            } catch (Exception ex) {
                ex.printStackTrace(); // fallback estremo
            }

        } catch (Exception e) {

            // ERRORE IMPREVISTO
            request.setAttribute(
                    "errore",
                    "Errore imprevisto durante l'operazione."
            );

            try {
                request.getRequestDispatcher("/error.jsp")
                        .forward(request, response);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
