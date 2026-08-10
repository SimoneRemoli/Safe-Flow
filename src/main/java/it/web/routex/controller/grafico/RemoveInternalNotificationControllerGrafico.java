package it.web.routex.controller.grafico;

import it.web.routex.controller.applicativo.ViewInternalNotificationsControllerApplicativo;
import it.web.routex.domain.LoggedHttpServlet;
import it.web.routex.domain.SessionAuthUtil;
import it.web.routex.exception.BrondiException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/removeInternalNotification")
public class RemoveInternalNotificationControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (!SessionAuthUtil.hasRole(session, "TRAVELER")) {
            writeJson(response, HttpServletResponse.SC_UNAUTHORIZED, "{\"error\":\"Unauthorized\"}");
            return;
        }

        String codiceFiscale = SessionAuthUtil.codiceFiscale(session).orElse(null);
        String notificationKey = request.getParameter("notificationKey");

        try {
            new ViewInternalNotificationsControllerApplicativo()
                    .dismissNotification(codiceFiscale, notificationKey);
            writeJson(response, HttpServletResponse.SC_OK, "{\"removed\":true}");
        } catch (BrondiException e) {
            logger.warn("Invalid internal notification remove request: {}", e.getDetails());
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"" + jsonEscape(e.getMessage()) + "\"}");
        } catch (Exception e) {
            logger.error("Unexpected error while removing internal notification", e);
            writeJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\":\"Unable to remove notification\"}");
        }
    }

    private void writeJson(HttpServletResponse response, int status, String json) {
        response.setStatus(status);
        try {
            response.getWriter().write(json);
        } catch (IOException e) {
            logger.error("Unable to write JSON response", e);
        }
    }

    private String jsonEscape(String value) {
        return value == null
                ? ""
                : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
