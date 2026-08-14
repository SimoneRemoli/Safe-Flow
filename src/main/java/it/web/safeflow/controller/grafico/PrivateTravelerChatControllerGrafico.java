package it.web.safeflow.controller.grafico;

import it.web.safeflow.controller.applicativo.PrivateTravelerChatControllerApplicativo;
import it.web.safeflow.domain.LoggedHttpServlet;
import it.web.safeflow.domain.SessionAuthUtil;
import it.web.safeflow.exception.BrondiException;
import it.web.safeflow.model.PrivateChatMessage;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet("/privateTravelerChat")
public class PrivateTravelerChatControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (!SessionAuthUtil.hasRole(session, "TRAVELER")) {
            writeJson(response, HttpServletResponse.SC_UNAUTHORIZED, "{\"error\":\"Unauthorized\"}");
            return;
        }

        String currentCf = SessionAuthUtil.codiceFiscale(session).orElse(null);
        String notificationKey = request.getParameter("notificationKey");
        String otherTravelerCf = request.getParameter("travelerCf");

        try {
            List<PrivateChatMessage> messages = new PrivateTravelerChatControllerApplicativo()
                    .messages(notificationKey, currentCf, otherTravelerCf);
            writeJson(response, HttpServletResponse.SC_OK, messagesJson(messages));
        } catch (BrondiException e) {
            logger.warn("Invalid private chat load request: {}", e.getDetails());
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"" + jsonEscape(e.getMessage()) + "\"}");
        } catch (Exception e) {
            logger.error("Unexpected error while loading private chat", e);
            writeJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\":\"Unable to load private chat\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (!SessionAuthUtil.hasRole(session, "TRAVELER")) {
            writeJson(response, HttpServletResponse.SC_UNAUTHORIZED, "{\"error\":\"Unauthorized\"}");
            return;
        }

        String currentCf = SessionAuthUtil.codiceFiscale(session).orElse(null);
        String notificationKey = request.getParameter("notificationKey");
        String recipientCf = request.getParameter("travelerCf");
        String text = request.getParameter("messageText");

        try {
            PrivateChatMessage message = new PrivateTravelerChatControllerApplicativo()
                    .send(notificationKey, currentCf, recipientCf, text);
            writeJson(response, HttpServletResponse.SC_OK, messageJson(message));
        } catch (BrondiException e) {
            logger.warn("Invalid private chat send request: {}", e.getDetails());
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"" + jsonEscape(e.getMessage()) + "\"}");
        } catch (Exception e) {
            logger.error("Unexpected error while sending private chat message", e);
            writeJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\":\"Unable to send private message\"}");
        }
    }

    private String messagesJson(List<PrivateChatMessage> messages) {
        StringBuilder json = new StringBuilder("{\"messages\":[");
        for (int i = 0; i < messages.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            json.append(messageJsonObject(messages.get(i)));
        }
        json.append("]}");
        return json.toString();
    }

    private String messageJson(PrivateChatMessage message) {
        return "{\"message\":" + messageJsonObject(message) + "}";
    }

    private String messageJsonObject(PrivateChatMessage message) {
        String createdAt = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(message.getCreatedAt());
        return "{"
                + "\"id\":" + message.getId() + ","
                + "\"notificationKey\":\"" + jsonEscape(message.getNotificationKey()) + "\","
                + "\"senderCf\":\"" + jsonEscape(message.getSenderCf()) + "\","
                + "\"recipientCf\":\"" + jsonEscape(message.getRecipientCf()) + "\","
                + "\"text\":\"" + jsonEscape(message.getText()) + "\","
                + "\"createdAt\":\"" + jsonEscape(createdAt) + "\","
                + "\"senderDisplayName\":\"" + jsonEscape(message.getSenderDisplayName()) + "\","
                + "\"senderInitials\":\"" + jsonEscape(message.getSenderInitials()) + "\","
                + "\"currentUserSender\":" + message.isCurrentUserSender()
                + "}";
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
                : value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
