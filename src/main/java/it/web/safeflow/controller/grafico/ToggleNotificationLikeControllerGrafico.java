package it.web.safeflow.controller.grafico;

import it.web.safeflow.controller.applicativo.NotificationLikeControllerApplicativo;
import it.web.safeflow.domain.LoggedHttpServlet;
import it.web.safeflow.domain.SessionAuthUtil;
import it.web.safeflow.exception.BrondiException;
import it.web.safeflow.model.NotificationLikeState;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/toggleNotificationLike")
public class ToggleNotificationLikeControllerGrafico extends LoggedHttpServlet {

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
            NotificationLikeState state = new NotificationLikeControllerApplicativo()
                    .toggleTravelerLike(notificationKey, codiceFiscale);
            writeJson(response, HttpServletResponse.SC_OK,
                    "{\"liked\":" + state.isLikedByCurrentUser()
                            + ",\"likeCount\":" + state.getLikeCount()
                            + "}");
        } catch (BrondiException e) {
            logger.warn("Invalid notification like request: {}", e.getDetails());
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"" + jsonEscape(e.getMessage()) + "\"}");
        } catch (Exception e) {
            logger.error("Unexpected error while toggling notification like", e);
            writeJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\":\"Unable to update like\"}");
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
