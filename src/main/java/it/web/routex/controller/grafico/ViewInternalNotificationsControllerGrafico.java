package it.web.routex.controller.grafico;

import it.web.routex.bean.MessageBean;
import it.web.routex.controller.applicativo.ViewInternalNotificationsControllerApplicativo;
import it.web.routex.domain.LoggedHttpServlet;
import it.web.routex.domain.SessionAuthUtil;
import it.web.routex.exception.BrondiException;

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
        if (!isTraveler(session)) {
            redirectToLogin(response);
            return;
        }

        String codiceFiscale = session != null && session.getAttribute("codiceFiscale") != null
                ? session.getAttribute("codiceFiscale").toString()
                : null;

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

    private boolean isTraveler(HttpSession session) {
        return SessionAuthUtil.isLoggedIn(session)
                && session.getAttribute("ruolo") != null
                && "TRAVELER".equalsIgnoreCase(session.getAttribute("ruolo").toString());
    }

    private void redirectToLogin(HttpServletResponse response) {
        try {
            response.sendRedirect("login.jsp");
        } catch (Exception e) {
            logger.error("Internal notifications redirect error", e);
        }
    }
}
