package it.web.routex.controller.grafico;
import it.web.routex.bean.ReportsStatsBean;
import it.web.routex.controller.applicativo.ReportsControllerApplicativo;
import it.web.routex.domain.LoggedHttpServlet;
import it.web.routex.exception.DAOExceptionRemoli;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/PathInfoRAS")
public class ReportsControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        try {
            ReportsControllerApplicativo service = new ReportsControllerApplicativo();
            ReportsStatsBean stats = service.recuperaStatistiche();

            request.setAttribute("stats", stats);
            try {
                request.getRequestDispatcher("/viewRAS.jsp").forward(request, response);
            }catch(Exception e)
            {
                logger.error("Errore nel forwarding verso viewRAS.jsp. {}", e.getMessage());
            }

        } catch (DAOExceptionRemoli e) {
            logger.error("Errore statistiche admin", e);
            request.setAttribute("errore", "Errore nel recupero statistiche.");
            try {
                request.getRequestDispatcher("/adminError.jsp").forward(request, response);
            }catch(Exception d)
            {
                logger.error("Errore nel forwarding verso adminError.jsp.jsp. {}", d.getMessage());
            }
        }
    }
}
