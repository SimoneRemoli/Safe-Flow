package it.web.routex.controller.grafico;

import it.web.routex.domain.LoggedHttpServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/adminReport")
public class AdminReportPageControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        if (!hasRole(request, "ADMIN")) {
            redirectToLogin(request, response);
            return;
        }

        try {
            request.getRequestDispatcher("/WEB-INF/views/sendCommunicationn.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Error while opening admin report page", e);
        }
    }

}
