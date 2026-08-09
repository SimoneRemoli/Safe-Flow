package it.web.routex.controller.grafico;

import it.web.routex.controller.applicativo.UserProfileControllerApplicativo;
import it.web.routex.domain.LoggedHttpServlet;
import it.web.routex.domain.SessionAuthUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Files;
import java.nio.file.Path;

@WebServlet("/publicProfileAvatar")
public class PublicProfileAvatarControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        if (!SessionAuthUtil.isLoggedIn(request.getSession(false))) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String codiceFiscale = request.getParameter("cf");
        if (codiceFiscale == null || codiceFiscale.isBlank()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            UserProfileControllerApplicativo controller = new UserProfileControllerApplicativo();
            Path avatarPath = controller.getAvatarPath(codiceFiscale);
            String contentType = Files.probeContentType(avatarPath);
            response.setContentType(contentType != null ? contentType : "application/octet-stream");
            response.setHeader("Cache-Control", "private, max-age=60");
            Files.copy(avatarPath, response.getOutputStream());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
