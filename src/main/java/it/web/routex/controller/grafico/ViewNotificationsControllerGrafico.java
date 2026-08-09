package it.web.routex.controller.grafico;
import it.web.routex.bean.MessageBean;
import it.web.routex.controller.applicativo.ViewNotificationsControllerApplicativo;
import it.web.routex.domain.LoggedHttpServlet;
import it.web.routex.domain.SessionAuthUtil;
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
            if (!SessionAuthUtil.hasRole(session, "TRAVELER")) {
                redirectToLogin(request, response);
                return;
            }

            String ruolo = SessionAuthUtil.ruolo(session).orElse("");
            String codiceFiscale = SessionAuthUtil.codiceFiscale(session).orElse(null);
            ViewNotificationsControllerApplicativo notifications = new ViewNotificationsControllerApplicativo();
            List<MessageBean> notifiche = notifications.messages(ruolo, codiceFiscale);
            request.setAttribute("notifiche", notifiche);
            request.setAttribute("isTravelerView", "TRAVELER".equalsIgnoreCase(ruolo));
            forward(request, response, "/WEB-INF/views/viewNotifications.jsp");

        } catch (BrondiException e) {
            logger.error(
                    "Errore applicativo durante il recupero delle notifiche. Codice={} Dettagli={}",
                    e.getCodiceDiErrore(),
                    e.getDetails(),
                    e
            );
            forwardError(request, response, e.getMessage(), "/error.jsp");

        } catch (Exception e) {
            logger.error("Errore imprevisto nella visualizzazione delle notifiche", e);
            forwardError(request, response, "Errore imprevisto", "/error.jsp");
        }
    }
}
