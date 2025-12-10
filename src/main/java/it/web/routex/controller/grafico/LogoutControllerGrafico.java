package it.web.routex.controller.grafico;
import it.web.routex.model.domain.LoggedHttpServlet;
import it.web.routex.model.domain.Ruolo;
import it.web.routex.utility.factory.ConnectionFactory;
import it.web.routex.utility.singleton.Credentials;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutControllerGrafico extends LoggedHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 1. Svuota il singleton delle credenziali
            Credentials.getInstanceSingleton().clear();

            // 2. Invalida anche la sessione (per sicurezza)
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            // 3. Torna alla home / login
            response.sendRedirect("index.jsp");
            logger.info("Logout avvenuto correttamente");
            ConnectionFactory.cambioDiRuolo(Ruolo.LOGIN);


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
