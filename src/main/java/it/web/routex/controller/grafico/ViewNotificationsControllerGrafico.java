package it.web.routex.controller.grafico;
import it.web.routex.bean.MessageBean;
import it.web.routex.controller.applicativo.ViewNotificationsControllerApplicativo;
import it.web.routex.domain.LoggedHttpServlet;
import it.web.routex.exception.BrondiException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@WebServlet("/viewNotifications")
public class ViewNotificationsControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        try {
            HttpSession session = request.getSession(false);
            String ruolo = session != null && session.getAttribute("ruolo") != null
                    ? session.getAttribute("ruolo").toString()
                    : "";
            String codiceFiscale = session != null && session.getAttribute("codiceFiscale") != null
                    ? session.getAttribute("codiceFiscale").toString()
                    : null;
            ViewNotificationsControllerApplicativo notifications = new ViewNotificationsControllerApplicativo();
            List<MessageBean> notifiche = notifications.messages(ruolo, codiceFiscale);
            request.setAttribute("notifiche", notifiche);
            request.setAttribute("isWorkerView", "WORKER".equalsIgnoreCase(ruolo));
            request.setAttribute("isTravelerView", "TRAVELER".equalsIgnoreCase(ruolo));
            request.getRequestDispatcher("/viewNotifications.jsp").forward(request, response);

        } catch (BrondiException e) {
            logger.error(
                    "Errore applicativo durante il recupero delle notifiche. Codice={} Dettagli={}",
                    e.getCodiceDiErrore(),
                    e.getDetails(),
                    e
            );
            forwardError(request, response, e.getMessage());

        } catch (Exception e) {
            logger.error("Errore imprevisto nella visualizzazione delle notifiche", e);
            forwardError(request, response, "Errore imprevisto");
        }
    }

    private void forwardError(HttpServletRequest request,
                              HttpServletResponse response,
                              String message) {
        try {
            request.setAttribute("errore", message);
            request.getRequestDispatcher("/error.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            logger.error("Errore durante il forward alla pagina di errore", e);
        }
    }
}
