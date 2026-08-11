package it.web.safeflow.controller.grafico;
import it.web.safeflow.bean.AutenticazioneBean;
import it.web.safeflow.bean.UtenteBeanGenerico;
import it.web.safeflow.controller.applicativo.LoginController;
import it.web.safeflow.exception.DAOExceptionRemoli;
import it.web.safeflow.domain.LoggedHttpServlet;
import it.web.safeflow.extractor.LoginExtractor;
import it.web.safeflow.record.LoginRecord;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import it.web.safeflow.exception.LoginNotFoundRemoli;
import it.web.safeflow.exception.InvalidLoginInputExceptionRemoli;

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

    private static final String ATTR_MESSAGGIO_ERRORE = "messaggioErrore";
    private static final String ATTR_TITOLO_ERRORE = "titoloErrore";
    private static final String PAGE_ERRORE_LOGIN = "/erroreLogin.jsp";
    private static final String FORWARDING = "Errore nel forwarding";
    private static final String INVALID_CREDENTIALS_MESSAGE =
            "The email or password you entered is not valid. Check your credentials and try again.";



    private AutenticazioneBean creaBeanAutenticazione(HttpServletRequest request)
            throws InvalidLoginInputExceptionRemoli {
        AutenticazioneBean aut = new AutenticazioneBean();
        LoginRecord login = LoginExtractor.from(request);
        aut.setEmail(login.email());
        aut.setPassword(login.password());
        logger.info(
                "Bean di autenticazione creato con email: {}, password presente={}",
                login.email(),
                login.password() != null
        );

        return aut;
    }

    /**
     * Gestisce il reindirizzamento in base al ruolo dell’utente autenticato.
     */
    private void gestisciReindirizzamento(UtenteBeanGenerico utente,
                                           HttpServletRequest request,
                                           HttpServletResponse response)
    {
        if (utente.getRuolo() == null) {
            forwardLoginError(
                    request,
                    response,
                    HttpServletResponse.SC_FORBIDDEN,
                    "Access cannot be completed",
                    "Your account role could not be verified. Please contact Safe Flow support."
            );
            return;
        }

        switch (utente.getRuolo().toString().toUpperCase()) {

            case "TRAVELER" -> safeRedirect(response, "travelerHome");

            case "ADMIN" -> safeRedirect(response, "adminHub");

            default -> forwardLoginError(
                    request,
                    response,
                    HttpServletResponse.SC_FORBIDDEN,
                    "Access cannot be completed",
                    "This account type is not allowed to access the reserved area."
            );
        }
    }
    private void safeRedirect(HttpServletResponse response, String pagina) {
        try {
            response.sendRedirect(pagina);
        } catch (IOException e) {
            logger.error("Errore nel redirect verso {}", pagina, e);
        }
    }


    /**
     * Gestisce eventuali errori di login (DAO o credenziali errate).
     */
    private void forwardLoginError(HttpServletRequest request,
                                   HttpServletResponse response,
                                   int statusCode,
                                   String title,
                                   String message) {
        try {
            response.setStatus(statusCode);
            request.setAttribute(ATTR_TITOLO_ERRORE, title);
            request.setAttribute(ATTR_MESSAGGIO_ERRORE, message);
            request.getRequestDispatcher(PAGE_ERRORE_LOGIN).forward(request, response);
        } catch (Exception e) {
            logger.error(FORWARDING, e);
        }
    }

    /**
     * Metodo principale di gestione del login.
     * Riceve i dati dal form, invoca il controller applicativo e imposta la sessione.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        try {
            //  Costruisce il bean con i dati del form
            AutenticazioneBean credenziali = creaBeanAutenticazione(request);

            //  Delegazione al controller applicativo
            LoginController loginController = new LoginController(credenziali);
            UtenteBeanGenerico utente = loginController.autenticaUtente();

            if (utente.getRuolo() == null) {
                forwardLoginError(
                        request,
                        response,
                        HttpServletResponse.SC_FORBIDDEN,
                        "Access cannot be completed",
                        "Your account role could not be verified. Please contact Safe Flow support."
                );
                return;
            }

            //  Crea una nuova sessione solo dopo autenticazione riuscita
            HttpSession session = request.getSession(true);
            session.setMaxInactiveInterval(180); // 3 minuti di inattività

            session.setAttribute("nome", utente.getNome());
            session.setAttribute("cognome", utente.getCognome());
            session.setAttribute("ruolo", utente.getRuolo().name());
            session.setAttribute("codiceFiscale", utente.getCodicefiscale());

            logger.info("Utente perfettamente autenticato: nome={}, cognome={}, ruolo={}", utente.getNome(), utente.getCognome(), utente.getRuolo());

            //  Reindirizzamento in base al ruolo
            gestisciReindirizzamento(utente, request, response);

        } catch (DAOExceptionRemoli ex) {
            forwardLoginError(
                    request,
                    response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Login service unavailable",
                    "Safe Flow could not verify your credentials because the authentication service is temporarily unavailable. Please try again later."
            );
            logger.error("Errore DAO durante il login: message={}", ex.getMessage());

        } catch (LoginNotFoundRemoli ex) {
            forwardLoginError(
                    request,
                    response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Login failed",
                    INVALID_CREDENTIALS_MESSAGE
            );
            logger.error("Tentativo di login fallito: email={}, Maskedpassw={}, message={}", ex.getEmail(), ex.getMaskedPassword(), ex.getMessage());
        } catch (InvalidLoginInputExceptionRemoli ex) {
            forwardLoginError(
                    request,
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Missing or invalid login details",
                    ex.getUserMessage()
            );
            logger.error("Errore di validazione input login: {}", ex.toString());
        }


    }
}

/*
Se la password deve stare nell'eccezione, allora la inserisco, ma sempre in modo sicuro, cioè MAI in chiaro,
perché una eccezione può finire nel log e il log è consultabile da tutti.
 */
