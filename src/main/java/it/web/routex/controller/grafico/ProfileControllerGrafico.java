package it.web.routex.controller.grafico;

import it.web.routex.controller.applicativo.UserProfileControllerApplicativo;
import it.web.routex.domain.LoggedHttpServlet;
import it.web.routex.domain.SessionAuthUtil;
import it.web.routex.exception.BrondiException;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@WebServlet("/profile")
@MultipartConfig(maxFileSize = 2 * 1024 * 1024, maxRequestSize = 3 * 1024 * 1024)
public class ProfileControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String codiceFiscale = SessionAuthUtil.codiceFiscale(session).orElse(null);
        if (codiceFiscale == null) {
            redirectToLogin(request, response);
            return;
        }

        try {
            UserProfileControllerApplicativo controller = new UserProfileControllerApplicativo();
            request.setAttribute("profile", controller.getProfile(codiceFiscale));
            forward(request, response, "/WEB-INF/views/profile.jsp");
        } catch (BrondiException e) {
            logger.error("Error while loading the profile", e);
            forwardError(request, response, e.getMessage(), "/errorLogged.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String codiceFiscale = SessionAuthUtil.codiceFiscale(session).orElse(null);
        if (codiceFiscale == null) {
            redirectToLogin(request, response);
            return;
        }

        try {
            UserProfileControllerApplicativo controller = new UserProfileControllerApplicativo();
            if ("removeAvatar".equals(request.getParameter("action"))) {
                controller.removeAvatar(codiceFiscale);
                response.sendRedirect(request.getContextPath() + "/profile?imageRemoved=1");
                return;
            }

            Part avatarPart = request.getPart("avatar");
            controller.saveProfile(codiceFiscale, request.getParameter("bio"), avatarPart);
            response.sendRedirect(request.getContextPath() + "/profile?saved=1");
        } catch (BrondiException e) {
            logger.warn("Invalid profile data: {}", e.getDetails());
            request.setAttribute("profileError", e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            logger.error("Error while saving the profile", e);
            forwardError(request, response, "Unable to save the profile.", "/errorLogged.jsp");
        }
    }
}
