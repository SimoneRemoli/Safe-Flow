package Controller.Grafico;
import Bean.AutenticazioneBean;
import Bean.UtenteBeanGenerico;
import Controller.Applicativo.LoginController;
import Exception.DAOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginControllerGrafico extends HttpServlet {

    private AutenticazioneBean creaBeanAutenticazione(HttpServletRequest request) {
        AutenticazioneBean aut = new AutenticazioneBean();
        aut.setEmail(request.getParameter("Email"));
        aut.setPassword(request.getParameter("Password"));
        return aut;
    }
    private UtenteBeanGenerico autentica(AutenticazioneBean aut, HttpSession session) throws DAOException, SQLException {
        LoginController login = new LoginController(aut);
        UtenteBeanGenerico utente = login.autenticaUtente();
        session.setAttribute("utenteLoggato", utente);
        return utente;
    }
    private void gestisciSessione(HttpSession session, UtenteBeanGenerico utente) {
        session.setAttribute("nome", utente.getNome());
        session.setAttribute("cognome", utente.getCognome());
        session.setAttribute("cf", utente.getCodicefiscale());
        session.setAttribute("ruolo", utente.getRuolo());
        if (utente.isDisable()) {
            session.setAttribute("disabile", "yes");
        }
        System.out.println("Utente loggato: " + utente.getNome() + " " + utente.getCognome() + " ruolo: " + utente.getRuolo());
    }
    private void gestisciReindirizzamento(UtenteBeanGenerico utente, HttpServletResponse response) throws IOException {
        switch (utente.getRuolo().toString().toUpperCase()) {
            case "TRAVELER" -> response.sendRedirect("index.jsp");
            case "WORKER", "ADMIN" -> response.sendRedirect("dashboardWorker.jsp");
            default -> response.sendRedirect("erroreLogin.jsp");
        }
    }
    private void gestisciErroreLogin(HttpServletRequest request, HttpServletResponse response, DAOException ex)
            throws ServletException, IOException {
        ex.printStackTrace();
        request.setAttribute("messaggioErrore", "Credenziali non valide. Riprova.");
        request.getRequestDispatcher("erroreLogin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(180);
            //Così l’utente, se resta inattivo per 3 minuti, verrà automaticamente disconnesso.
            Class.forName("com.mysql.cj.jdbc.Driver");

            AutenticazioneBean credenziali = creaBeanAutenticazione(request);
            UtenteBeanGenerico utente = autentica(credenziali, session);

            gestisciSessione(session, utente);
            gestisciReindirizzamento(utente, response);

        } catch (DAOException ex) {
            //credenziali non valide
            gestisciErroreLogin(request, response, ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}