package it.web.routex.controller.grafico;

import it.web.routex.domain.LoggedHttpServlet;
import it.web.routex.domain.SessionAuthUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/travelerReport")
public class TravelerReportPageControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (!hasRole(session, "TRAVELER")) {
            redirectToLogin(response);
            return;
        }
        try {
            request.getRequestDispatcher("/WEB-INF/views/sendTravelerCommunication.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Error while opening traveler report form", e);
        }
    }

    private boolean hasRole(HttpSession session, String role) {
        return SessionAuthUtil.isLoggedIn(session)
                && session.getAttribute("ruolo") != null
                && role.equalsIgnoreCase(session.getAttribute("ruolo").toString());
    }

    private void redirectToLogin(HttpServletResponse response) {
        try {
            response.sendRedirect("login.jsp");
        } catch (Exception e) {
            logger.error("Traveler report page redirect error", e);
        }
    }
}
