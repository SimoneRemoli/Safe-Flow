package it.web.routex.controller.grafico;

import it.web.routex.domain.LoggedHttpServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/travelerHome")
public class TravelerHomeControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (!hasRole(request, "TRAVELER")) {
                redirectToLogin(request, response);
                return;
            }

            request.getRequestDispatcher("/WEB-INF/views/indexLogged.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Errore durante la visualizzazione della home traveler", e);
            try {
                request.setAttribute("errore", "Errore durante il caricamento della home traveler.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            } catch (Exception ex) {
                logger.error("Errore durante il forward alla pagina di errore", ex);
            }
        }
    }
}
