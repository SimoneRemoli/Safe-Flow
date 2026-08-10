package it.web.safeflow.controller.grafico;

import it.web.safeflow.domain.LoggedHttpServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/reportGuide")
public class ReportGuideControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        if (!hasRole(request, "TRAVELER")) {
            redirectToLogin(request, response);
            return;
        }
        try {
            request.getRequestDispatcher("/WEB-INF/views/reportGuide.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Error while opening report guide", e);
        }
    }

}
