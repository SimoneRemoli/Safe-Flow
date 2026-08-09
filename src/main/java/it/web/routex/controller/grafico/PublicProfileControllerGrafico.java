package it.web.routex.controller.grafico;

import it.web.routex.controller.applicativo.UserProfileControllerApplicativo;
import it.web.routex.domain.LoggedHttpServlet;
import it.web.routex.domain.SessionAuthUtil;
import it.web.routex.exception.BrondiException;
import it.web.routex.model.UserProfileSummary;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/publicProfile")
public class PublicProfileControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (!SessionAuthUtil.isLoggedIn(session)) {
            redirectToLogin(request, response);
            return;
        }

        String requestedCf = request.getParameter("cf");
        String currentCf = SessionAuthUtil.codiceFiscale(session).orElse(null);
        if (requestedCf != null && currentCf != null && requestedCf.equalsIgnoreCase(currentCf)) {
            try {
                response.sendRedirect(request.getContextPath() + "/profile");
            } catch (Exception e) {
                logger.error("Error while redirecting to own profile", e);
            }
            return;
        }

        try {
            UserProfileControllerApplicativo controller = new UserProfileControllerApplicativo();
            UserProfileSummary profile = controller.getPublicProfile(requestedCf);
            request.setAttribute("publicProfile", profile);
            forward(request, response, "/WEB-INF/views/publicProfile.jsp");
        } catch (BrondiException e) {
            logger.warn("Public profile not available: {}", e.getDetails());
            forwardError(request, response, e.getMessage(), "/errorLogged.jsp");
        }
    }
}
