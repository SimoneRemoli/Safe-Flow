package it.web.safeflow.controller.grafico;

import it.web.safeflow.controller.applicativo.UserProfileControllerApplicativo;
import it.web.safeflow.dao.SocialDataRepository;
import it.web.safeflow.domain.LoggedHttpServlet;
import it.web.safeflow.domain.SessionAuthUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
            SocialDataRepository.StoredFile avatar = controller.getAvatarFile(codiceFiscale);
            response.setContentType(avatar.contentType() != null ? avatar.contentType() : "application/octet-stream");
            response.setHeader("Cache-Control", "private, max-age=60");
            response.getOutputStream().write(avatar.data());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
