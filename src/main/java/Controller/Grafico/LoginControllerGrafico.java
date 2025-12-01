package Controller.Grafico;
import Bean.AutenticazioneBean;
import Bean.UtenteBeanGenerico;
import Controller.Applicativo.LoginController;
import Exception.DAOExceptionRemoli;
import Model.Domain.LoggedHttpServlet;
import Model.Extractor.LoginExtractor;
import Model.Record.LoginRecord;
import utility.Factory.ConnectionFactory;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import Exception.LoginNotFoundRemoli;
import Exception.InvalidLoginInputExceptionRemoli;

/**
 * Controller grafico per la gestione del login utente.
 * Si occupa di ricevere i dati dal form, delegare la logica al controller applicativo
 * e reindirizzare l’utente alla pagina corretta in base al ruolo.
 */
@WebServlet("/login")
public class LoginControllerGrafico extends LoggedHttpServlet {

    /**
     * Crea il bean di autenticazione a partire dai parametri del form.
     */
    private AutenticazioneBean creaBeanAutenticazione(HttpServletRequest request, HttpServletResponse response) {
        AutenticazioneBean aut = new AutenticazioneBean();
        LoginRecord login = null;
        try {
            try {
                login = LoginExtractor.from(request);
            } catch (InvalidLoginInputExceptionRemoli e) {
                System.out.println("Errore di validazione input login: " + e.getMessage());
                request.setAttribute("messaggioErrore", e.getUserMessage());
                request.getRequestDispatcher("erroreLogin.jsp").forward(request, response);
                logger.error("Errore di validazione input login: {}", e.toString());
            }
            aut.setEmail(login.email());
            aut.setPassword(login.password());
            logger.info("Bean di autenticazione creato con email: email={}, NotCheckedPassword={}", login.email(), login.password());
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        return aut;
    }

    /**
     * Gestisce il reindirizzamento in base al ruolo dell’utente autenticato.
     */
    private void gestisciReindirizzamento(UtenteBeanGenerico utente, HttpServletResponse response)
            throws IOException {
        try {
            // Imposta la connessione corretta in base al ruolo
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

    /**
     * Gestisce eventuali errori di login (DAO o credenziali errate).
     */
    private void gestisciErroreLogin(HttpServletRequest request, HttpServletResponse response, DAOExceptionRemoli ex)
    {
                try {
                    ex.printStackTrace();
                    request.setAttribute("messaggioErrore", "Errore nella connessione al DB [500 internal error]");
                    request.getRequestDispatcher("erroreLogin.jsp").forward(request, response);
                }catch(Exception e) {
                    throw new RuntimeException(e);
                }
    }

    /**
     * Metodo principale di gestione del login.
     * Riceve i dati dal form, invoca il controller applicativo e imposta la sessione.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            try {
                //  Crea una nuova sessione e imposta il timeout
                HttpSession session = request.getSession(true);
                session.setMaxInactiveInterval(180); // 3 minuti di inattività

                //  Costruisce il bean con i dati del form
                AutenticazioneBean credenziali = creaBeanAutenticazione(request,response);

                //  Delegazione al controller applicativo
                LoginController loginController = new LoginController(credenziali);
                UtenteBeanGenerico utente = loginController.autenticaUtente();

                //  Dopo loginController.autenticaUtente(session);
                session.setAttribute("nome", utente.getNome());
                session.setAttribute("cognome", utente.getCognome());
                session.setAttribute("ruolo", utente.getRuolo());

                //  Stampa debug
                System.out.println("[LOGIN] Utente autenticato: " + utente.getNome() + " " + utente.getCognome()
                        + " (" + utente.getRuolo() + ")");

                //  Reindirizzamento in base al ruolo
                gestisciReindirizzamento(utente, response);

            } catch (DAOExceptionRemoli ex) {
                gestisciErroreLogin(request, response, ex);

            } catch (LoginNotFoundRemoli ex) {
                ex.printStackTrace();
                request.setAttribute("messaggioErrore", ex.getMessage());
                request.getRequestDispatcher("erroreLogin.jsp").forward(request, response);
                logger.error("Tentativo di login fallito: email={}, Maskedpassw={}, message={}", ex.getEmail(), ex.getMaskedPassword(), ex.getMessage());
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }
}

/*
Se la password deve stare nell'eccezione, allora la inserisco, ma sempre in modo sicuro, cioè MAI in chiaro,
perché una eccezione può finire nei log e i log sono consultabili da altri amministratori.
 */
