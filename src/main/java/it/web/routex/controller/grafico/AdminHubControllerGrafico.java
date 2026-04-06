package it.web.routex.controller.grafico;

import it.web.routex.domain.LoggedHttpServlet;
import it.web.routex.domain.SessionAuthUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/adminHub")
public class AdminHubControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (!hasRole(session, "ADMIN")) {
            redirectToLogin(response);
            return;
        }
        try {
            request.getRequestDispatcher("/WEB-INF/views/indexAdmin.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Error while opening admin hub", e);
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
            logger.error("Admin hub redirect error", e);
        }
    }
}
