package it.web.routex.controller.grafico;
import it.web.routex.bean.UtenteBeanGenerico;
import it.web.routex.bean.WorkerScheduleBean;
import it.web.routex.controller.applicativo.ViewWorkScheduleControllerApplicativo;
import it.web.routex.utility.singleton.Credentials;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import javax.servlet.ServletException; // Import necessario per il forward

@WebServlet("/viewWorkSchedule")
public class ViewWorkScheduleControllerGrafico extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null) {
                response.sendRedirect("index.jsp");
                return;
            }

            Credentials cred = Credentials.getInstanceSingleton();
            System.out.println(cred.getCodiceFiscale()+cred.getNome()+cred.getRuolo());

            if (cred == null) {
                response.sendRedirect("index.jsp");
                return;
            }


            ViewWorkScheduleControllerApplicativo service = new ViewWorkScheduleControllerApplicativo();
            WorkerScheduleBean schedule = service.getSchedule(cred.getCodiceFiscale());

            // 1. Usa request.setAttribute (non session)
            // Questo rende il dato disponibile SOLO per questa visualizzazione
            //request.setAttribute("workerSchedule", schedule);
            request.getSession().setAttribute("workerSchedule", schedule);
            /*
             * if null
             * errore
             * else dashboard*/
            // Se non sei loggato o cf è null, reindirizza a login
            response.sendRedirect(request.getContextPath() + "/dashboardWorker.jsp");
            // 2. Usa forward (non sendRedirect)
            // Questo passa il controllo alla JSP mantenendo i dati nella request
            //request.getRequestDispatcher("dashboardWorker.jsp").forward(request, response);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
