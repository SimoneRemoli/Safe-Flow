package it.web.safeflow.controller.grafico;

import it.web.safeflow.controller.applicativo.ReportImageControllerApplicativo;
import it.web.safeflow.dao.SocialDataRepository;
import it.web.safeflow.domain.LoggedHttpServlet;
import it.web.safeflow.domain.SessionAuthUtil;
import it.web.safeflow.exception.BrondiException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

@WebServlet("/reportImage")
public class ReportImageFileControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (!SessionAuthUtil.isLoggedIn(session)) {
            redirectToLogin(request, response);
            return;
        }

        String notificationKey = request.getParameter("notificationKey");
        String fileName = request.getParameter("file");
        String codiceFiscale = SessionAuthUtil.codiceFiscale(session).orElse(null);
        String ruolo = SessionAuthUtil.ruolo(session).orElse("");

        try {
            SocialDataRepository.StoredFile image = new ReportImageControllerApplicativo()
                    .viewableImage(notificationKey, fileName, codiceFiscale, ruolo);
            response.setContentType(image.contentType() == null ? contentTypeFor(fileName) : image.contentType());
            response.setHeader("Cache-Control", "private, max-age=300");
            response.getOutputStream().write(image.data());
        } catch (BrondiException e) {
            logger.warn("Report image not available: {}", e.getDetails());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error while streaming report image", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String contentTypeFor(String fileName) {
        String lower = fileName == null ? "" : fileName.toLowerCase(Locale.ROOT);
        if (lower.endsWith(".png")) {
            return "image/png";
        }
        if (lower.endsWith(".webp")) {
            return "image/webp";
        }
        if (lower.endsWith(".gif")) {
            return "image/gif";
        }
        return "image/jpeg";
    }
}
