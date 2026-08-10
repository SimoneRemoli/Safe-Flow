package it.web.safeflow.controller.grafico;
import it.web.safeflow.bean.MessageBean;
import it.web.safeflow.controller.applicativo.ViewNotificationsControllerApplicativo;
import it.web.safeflow.domain.LoggedHttpServlet;
import it.web.safeflow.domain.SessionAuthUtil;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import it.web.safeflow.exception.BrondiException;

@WebServlet("/areaRiservata")
public class AreaRiservataControllerGrafico extends LoggedHttpServlet
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
            try {
                final HttpSession session = request.getSession(false);
                if (SessionAuthUtil.hasRole(session, "TRAVELER")) {
                    String cf = SessionAuthUtil.codiceFiscale(session).orElse(null);

                    if (cf != null) {
                        moveFlashMessages(request, session);
                        String ruolo = SessionAuthUtil.ruolo(session).orElse("");
                        ViewNotificationsControllerApplicativo notifications = new ViewNotificationsControllerApplicativo();
                        List<MessageBean> notifiche = notifications.messages(ruolo, cf);
                        request.setAttribute("notifiche", notifiche);
                        request.setAttribute("isTravelerView", true);
                        forward(request, response, "/WEB-INF/views/viewNotifications.jsp");
                        return;
                    }
                }
                // Se non sei loggato o cf è null, reindirizza a login
                redirectToLogin(request, response);
            } catch (BrondiException e) {
                logger.error("Errore applicativo durante il recupero delle notifiche dell'area riservata", e);
                forwardError(request, response, e.getMessage(), "/error.jsp");
            }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.sendRedirect(request.getContextPath() + "/areaRiservata");
        } catch (Exception e) {
            logger.error("Errore inatteso durante la gestione POST dell'area riservata traveler", e);
            request.setAttribute("errore", "Errore durante la gestione dell'area riservata.");
            try {
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            } catch (Exception ex) {
                logger.error("Errore durante il forward alla pagina di errore", ex);
            }
        }
    }

    private void moveFlashMessages(HttpServletRequest request, HttpSession session) {
        if (session == null) {
            return;
        }

        Object successMessage = session.getAttribute("successMessage");
        Object errorMessage = session.getAttribute("errorMessage");

        if (successMessage != null) {
            request.setAttribute("successMessage", successMessage);
            session.removeAttribute("successMessage");
        }

        if (errorMessage != null) {
            request.setAttribute("errorMessage", errorMessage);
            session.removeAttribute("errorMessage");
        }
    }

}
