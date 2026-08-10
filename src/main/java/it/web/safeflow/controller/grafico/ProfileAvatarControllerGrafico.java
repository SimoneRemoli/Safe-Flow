package it.web.safeflow.controller.grafico;

import it.web.safeflow.controller.applicativo.UserProfileControllerApplicativo;
import it.web.safeflow.domain.LoggedHttpServlet;
import it.web.safeflow.domain.SessionAuthUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Files;
import java.nio.file.Path;

@WebServlet("/profileAvatar")
public class ProfileAvatarControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String codiceFiscale = SessionAuthUtil.codiceFiscale(request.getSession(false)).orElse(null);
        if (codiceFiscale == null) {
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
