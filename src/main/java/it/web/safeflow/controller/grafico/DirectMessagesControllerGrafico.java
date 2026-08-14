package it.web.safeflow.controller.grafico;

import it.web.safeflow.controller.applicativo.PrivateTravelerChatControllerApplicativo;
import it.web.safeflow.domain.LoggedHttpServlet;
import it.web.safeflow.domain.SessionAuthUtil;
import it.web.safeflow.exception.BrondiException;
import it.web.safeflow.model.PrivateChatThread;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@WebServlet("/directMessages")
public class DirectMessagesControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (!SessionAuthUtil.hasRole(session, "TRAVELER")) {
            redirectToLogin(request, response);
            return;
        }

        String codiceFiscale = SessionAuthUtil.codiceFiscale(session).orElse(null);

        try {
            PrivateTravelerChatControllerApplicativo controller = new PrivateTravelerChatControllerApplicativo();
            List<PrivateChatThread> threads = controller.threads(codiceFiscale);
            int unreadCount = controller.unreadCount(codiceFiscale);
            request.setAttribute("threads", threads);
            request.setAttribute("unreadDirectMessagesCount", unreadCount);
            request.getRequestDispatcher("/WEB-INF/views/directMessages.jsp").forward(request, response);
        } catch (BrondiException e) {
            forwardError(request, response, e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while loading direct messages", e);
            forwardError(request, response, "Unexpected error");
        }
    }

    private void forwardError(HttpServletRequest request, HttpServletResponse response, String message) {
        try {
            request.setAttribute("errore", message);
            request.getRequestDispatcher("/errorLogged.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Direct messages forward error", e);
        }
    }
}
