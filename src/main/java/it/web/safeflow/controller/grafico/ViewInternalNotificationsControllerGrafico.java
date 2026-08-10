package it.web.safeflow.controller.grafico;

import it.web.safeflow.bean.MessageBean;
import it.web.safeflow.controller.applicativo.ViewInternalNotificationsControllerApplicativo;
import it.web.safeflow.domain.LoggedHttpServlet;
import it.web.safeflow.domain.SessionAuthUtil;
import it.web.safeflow.exception.BrondiException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@WebServlet("/internalNotifications")
public class ViewInternalNotificationsControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (!SessionAuthUtil.hasRole(session, "TRAVELER")) {
            redirectToLogin(request, response);
            return;
        }

        String codiceFiscale = SessionAuthUtil.codiceFiscale(session).orElse(null);

        try {
            ViewInternalNotificationsControllerApplicativo controller = new ViewInternalNotificationsControllerApplicativo();
            controller.markAllAsRead(codiceFiscale);
            List<MessageBean> notifiche = controller.messages(codiceFiscale);
            request.setAttribute("notifiche", notifiche);
            request.getRequestDispatcher("/WEB-INF/views/internalNotifications.jsp").forward(request, response);
        } catch (BrondiException e) {
            forwardError(request, response, e.getMessage());
        } catch (Exception e) {
            forwardError(request, response, "Unexpected error");
        }
    }

    private void forwardError(HttpServletRequest request, HttpServletResponse response, String message) {
        try {
            request.setAttribute("errore", message);
            request.getRequestDispatcher("/errorLogged.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Internal notifications forward error", e);
        }
    }

}
