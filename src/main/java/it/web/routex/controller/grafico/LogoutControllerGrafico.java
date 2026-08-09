package it.web.routex.controller.grafico;
import it.web.routex.domain.LoggedHttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            response.sendRedirect("index.jsp");
            logger.info("Logout avvenuto correttamente");


        } catch (Exception e) {
            logger.error("Logout non avvenuto correttamente", e);
            request.setAttribute("errore", "Errore durante il logout. Riprova.");
            try {
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            } catch (Exception ex) {
                logger.error("Errore anche durante il forward alla pagina di errore", ex);
            }
        }
    }
}
