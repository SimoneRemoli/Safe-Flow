package it.web.safeflow.controller.grafico;

import it.web.safeflow.controller.applicativo.NotificationCommentControllerApplicativo;
import it.web.safeflow.domain.LoggedHttpServlet;
import it.web.safeflow.domain.SessionAuthUtil;
import it.web.safeflow.exception.BrondiException;
import it.web.safeflow.model.NotificationComment;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

@WebServlet("/addNotificationComment")
public class AddNotificationCommentControllerGrafico extends LoggedHttpServlet {

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
        String commentText = request.getParameter("commentText");
        String parentCommentId = request.getParameter("parentCommentId");

        try {
            NotificationComment comment = new NotificationCommentControllerApplicativo()
                    .addTravelerComment(notificationKey, codiceFiscale, commentText, parentCommentId);
            writeJson(response, HttpServletResponse.SC_OK, toJson(comment, request));
        } catch (BrondiException e) {
            logger.warn("Invalid notification comment request: {}", e.getDetails());
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"" + jsonEscape(e.getMessage()) + "\"}");
        } catch (Exception e) {
            logger.error("Unexpected error while adding notification comment", e);
            writeJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\":\"Unable to add comment\"}");
        }
    }

    private String toJson(NotificationComment comment, HttpServletRequest request) {
        String createdAt = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(comment.getCreatedAt());
        String encodedCf = URLEncoder.encode(comment.getAuthorCf(), StandardCharsets.UTF_8);
        String profileUrl = comment.isCurrentUserAuthor()
                ? request.getContextPath() + "/profile"
                : request.getContextPath() + "/publicProfile?cf=" + encodedCf;
        String avatarUrl = comment.isCurrentUserAuthor()
                ? request.getContextPath() + "/profileAvatar?t=" + System.currentTimeMillis()
                : request.getContextPath() + "/publicProfileAvatar?cf=" + encodedCf + "&t=" + System.currentTimeMillis();

        return "{"
                + "\"id\":\"" + jsonEscape(comment.getId()) + "\","
                + "\"notificationKey\":\"" + jsonEscape(comment.getNotificationKey()) + "\","
                + "\"parentCommentId\":\"" + jsonEscape(comment.getParentCommentId()) + "\","
                + "\"replyToDisplayName\":\"" + jsonEscape(comment.getReplyToDisplayName()) + "\","
                + "\"text\":\"" + jsonEscape(comment.getText()) + "\","
                + "\"createdAt\":\"" + jsonEscape(createdAt) + "\","
                + "\"authorDisplayName\":\"" + jsonEscape(comment.getAuthorDisplayName()) + "\","
                + "\"authorInitials\":\"" + jsonEscape(comment.getAuthorInitials()) + "\","
                + "\"likeCount\":" + comment.getLikeCount() + ","
                + "\"likedByCurrentUser\":" + comment.isLikedByCurrentUser() + ","
                + "\"authorAvatarPresent\":" + comment.isAuthorAvatarPresent() + ","
                + "\"authorProfileUrl\":\"" + jsonEscape(profileUrl) + "\","
                + "\"authorAvatarUrl\":\"" + jsonEscape(avatarUrl) + "\""
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
