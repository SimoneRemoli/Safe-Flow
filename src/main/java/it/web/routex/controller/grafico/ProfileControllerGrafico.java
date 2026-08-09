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
            logger.error("Errore durante il caricamento del profilo", e);
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
            Part avatarPart = request.getPart("avatar");
            UserProfileControllerApplicativo controller = new UserProfileControllerApplicativo();
            controller.saveProfile(codiceFiscale, request.getParameter("bio"), avatarPart);
            response.sendRedirect(request.getContextPath() + "/profile?saved=1");
        } catch (BrondiException e) {
            logger.warn("Profilo non valido: {}", e.getDetails());
            request.setAttribute("errore", e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            logger.error("Errore durante il salvataggio del profilo", e);
            forwardError(request, response, "Impossibile salvare il profilo.", "/errorLogged.jsp");
        }
    }
}
