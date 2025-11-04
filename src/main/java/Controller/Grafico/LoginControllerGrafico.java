package Controller.Grafico;

import Bean.AutenticazioneBean;
import Bean.UtenteBeanGenerico;
import Controller.Applicativo.LoginController;
import Exception.DAOException;
import Factory.ConnectionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller grafico per la gestione del login utente.
 * Si occupa di autenticare l’utente, gestire la sessione
 * e reindirizzare alla pagina corretta in base al ruolo.
 */
@WebServlet("/login")
public class LoginControllerGrafico extends HttpServlet {

    /** Crea il bean delle credenziali a partire dai parametri del form */
    private AutenticazioneBean creaBeanAutenticazione(HttpServletRequest request) {
        AutenticazioneBean aut = new AutenticazioneBean();
        aut.setEmail(request.getParameter("Email"));
        aut.setPassword(request.getParameter("Password"));
        return aut;
    }

    /** Esegue l’autenticazione dell’utente tramite il controller applicativo */
    private UtenteBeanGenerico autentica(AutenticazioneBean aut) throws DAOException, SQLException {
        LoginController login = new LoginController(aut);
        return login.autenticaUtente();
    }

    /** Popola la sessione con i dati utente */
    private void gestisciSessione(HttpSession session, UtenteBeanGenerico utente) {
        session.setAttribute("utenteLoggato", utente);
        session.setAttribute("nome", utente.getNome());
        session.setAttribute("cognome", utente.getCognome());
        session.setAttribute("cf", utente.getCodicefiscale());
        session.setAttribute("ruolo", utente.getRuolo());
        if (utente.isDisable()) {
            session.setAttribute("disabile", "yes");
        }
        System.out.println("Utente loggato: " + utente.getNome() + " " + utente.getCognome() +
                " | ruolo: " + utente.getRuolo());
    }

    /** Gestisce il reindirizzamento in base al ruolo */
    private void gestisciReindirizzamento(UtenteBeanGenerico utente, HttpServletResponse response)
            throws IOException {
        try {
            ConnectionFactory.Cambio_Di_Ruolo(utente.getRuolo());
            switch (utente.getRuolo().toString().toUpperCase()) {
                case "TRAVELER" -> response.sendRedirect("index.jsp");
                case "WORKER", "ADMIN" -> response.sendRedirect("dashboardWorker.jsp");
                default -> response.sendRedirect("erroreLogin.jsp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("erroreLogin.jsp");
        }
    }

    /** Gestisce eventuali errori di login */
    private void gestisciErroreLogin(HttpServletRequest request, HttpServletResponse response, DAOException ex)
            throws ServletException, IOException {
        ex.printStackTrace();
        request.setAttribute("messaggioErrore", "Credenziali non valide. Riprova.");
        request.getRequestDispatcher("erroreLogin.jsp").forward(request, response);
    }

    /** Metodo principale di gestione del login */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            //  Recupera e invalida eventuale vecchia sessione
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            //  Crea una nuova sessione pulita e imposta timeout
            HttpSession session = request.getSession(true);
            session.setMaxInactiveInterval(180); // 3 minuti di inattività

            //️ Controlla se l’app è appena stata avviata
            Boolean appStarted = (Boolean) getServletContext().getAttribute("appStarted");
            if (appStarted != null && appStarted) {
                getServletContext().setAttribute("appStarted", Boolean.FALSE);
                System.out.println(" Tutte le sessioni precedenti invalidate dopo il riavvio dell'app.");
            }

            //  Carica il driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            //  Recupera le credenziali dal form
            AutenticazioneBean credenziali = creaBeanAutenticazione(request);

            //  Autenticazione
            UtenteBeanGenerico utente = autentica(credenziali);

            //  Salvataggio dati in sessione
            gestisciSessione(session, utente);

            //  Reindirizzamento
            gestisciReindirizzamento(utente, response);

        } catch (DAOException ex) {
            gestisciErroreLogin(request, response, ex);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            request.setAttribute("messaggioErrore", "Errore interno: " + ex.getMessage());
            request.getRequestDispatcher("erroreLogin.jsp").forward(request, response);
        }
    }
}
