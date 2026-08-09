package it.web.routex.controller.grafico;

import it.web.routex.domain.LoggedHttpServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/travelerReport")
public class TravelerReportPageControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        if (!hasRole(request, "TRAVELER")) {
            redirectToLogin(request, response);
            return;
        }
        try {
            request.getRequestDispatcher("/WEB-INF/views/sendTravelerCommunication.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Error while opening traveler report form", e);
        }
    }

}
