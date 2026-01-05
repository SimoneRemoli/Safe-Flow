package it.web.routex.controller.grafico;

import it.web.routex.bean.PathInfoBean;
import it.web.routex.bean.ReportsStatsBean;
import it.web.routex.controller.applicativo.ReportsControllerApplicativo;
import it.web.routex.domain.LoggedHttpServlet;
import it.web.routex.exception.DAOExceptionRemoli;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/PathInfoRAS")
public class ReportsControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            ReportsControllerApplicativo service = new ReportsControllerApplicativo();
            ReportsStatsBean stats = service.recuperaStatistiche();

            request.setAttribute("stats", stats);
            request.getRequestDispatcher("/viewRAS.jsp").forward(request, response);

        } catch (DAOExceptionRemoli e) {
            logger.error("Errore statistiche admin", e);
            request.setAttribute("errore", "Errore nel recupero statistiche.");
            request.getRequestDispatcher("/adminError.jsp").forward(request, response);
        }
    }
}
