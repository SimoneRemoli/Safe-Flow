package it.web.routex.controller.grafico;
import it.web.routex.bean.RouteBean;
import it.web.routex.bean.TicketBean;
import it.web.routex.controller.applicativo.AreaRiservata;
import it.web.routex.domain.LoggedHttpServlet;
import it.web.routex.domain.SessionAuthUtil;
import it.web.routex.utility.singleton.Credentials;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import it.web.routex.exception.PathNotFoundExceptionRemoli;
import it.web.routex.exception.DAOExceptionRemoli;

@WebServlet("/areaRiservata")
public class AreaRiservataControllerGrafico extends LoggedHttpServlet
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
            try {
                final HttpSession session = request.getSession(false);
                if (SessionAuthUtil.isLoggedIn(session)) {
                    Credentials cred = Credentials.getInstanceSingleton();
                    String cf = cred.getCodiceFiscale();
                    AreaRiservata reserved = new AreaRiservata();

                    if (cf != null) {
                        moveFlashMessages(request, session);
                        List<RouteBean> listaPercorsi = reserved.runPath(cf);
                        List<TicketBean> tickets = reserved.runTicket(cf);
                        request.setAttribute("listaPercorsi", listaPercorsi);
                        request.setAttribute("tickets", tickets);
                        forwardAreaRiservata(request, response);
                        return;
                    }
                }
                // Se non sei loggato o cf è null, reindirizza a login
                redirectToLogin(request, response);
            } catch (PathNotFoundExceptionRemoli remoli) {
                logger.error("Errore PathNotFoundExceptionRemoli. Messaggio={} Cf={} CodiceErrore={} Dettagli={}.", remoli.getMessage(), remoli.getCodiceFiscaleUtente(), remoli.getCodiceDiErrore(), remoli.getDetails());
                request.setAttribute("errore", remoli.getMessage());
                try {
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                }catch(Exception e) {
                    logger.error("Errore durante il forward alla pagina di errore", e);
                }
            } catch (DAOExceptionRemoli remoli) {
                logger.error("Errore DAOExceptionRemoli. Messaggio={} Causa{}", remoli.getMessage(), remoli.getCause());
                request.setAttribute("errore", remoli.getMessage());
                try {
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                }catch(Exception e) {
                    logger.error("Errore durante il forward alla pagina di errore", e);
                }
            }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            final HttpSession session = request.getSession(false);
            if (!SessionAuthUtil.isLoggedIn(session)) {
                redirectToLogin(request, response);
                return;
            }

            Credentials cred = Credentials.getInstanceSingleton();
            String cf = cred.getCodiceFiscale();
            if (cf == null) {
                redirectToLogin(request, response);
                return;
            }

            AreaRiservata reserved = new AreaRiservata();
            String action = request.getParameter("action");

            if ("deleteAll".equals(action)) {
                int deleted = reserved.deleteAllTickets(cf);
                session.setAttribute("successMessage", deleted > 0
                        ? "Eliminati " + deleted + " biglietti."
                        : "Non ci sono biglietti da eliminare.");
            } else if ("deleteAllRoutes".equals(action)) {
                int deleted = reserved.deleteAllRoutes(cf);
                session.setAttribute("successMessage", deleted > 0
                        ? "Eliminati " + deleted + " percorsi."
                        : "Non ci sono percorsi da eliminare.");
            } else if ("deleteSelected".equals(action)) {
                String[] selectedTickets = request.getParameterValues("selectedTickets");
                if (selectedTickets == null || selectedTickets.length == 0) {
                    session.setAttribute("errorMessage", "Seleziona almeno un biglietto da eliminare.");
                } else {
                    int deleted = reserved.deleteSelectedTickets(cf, Arrays.asList(selectedTickets));
                    session.setAttribute("successMessage", deleted > 0
                            ? "Eliminati " + deleted + " biglietti selezionati."
                            : "Nessun biglietto eliminato.");
                }
            } else if ("deleteSelectedRoutes".equals(action)) {
                String[] selectedRoutes = request.getParameterValues("selectedRoutes");
                if (selectedRoutes == null || selectedRoutes.length == 0) {
                    session.setAttribute("errorMessage", "Seleziona almeno un percorso da eliminare.");
                } else {
                    int deleted = reserved.deleteSelectedRoutes(cf, Arrays.asList(selectedRoutes));
                    session.setAttribute("successMessage", deleted > 0
                            ? "Eliminati " + deleted + " percorsi selezionati."
                            : "Nessun percorso eliminato.");
                }
            } else {
                session.setAttribute("errorMessage", "Azione non valida.");
            }

            response.sendRedirect(request.getContextPath() + "/areaRiservata");
        } catch (DAOExceptionRemoli remoli) {
            logger.error("Errore DAOExceptionRemoli durante l'eliminazione ticket. Messaggio={} Causa={}", remoli.getMessage(), remoli.getCause());
            request.setAttribute("errore", remoli.getMessage());
            try {
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            } catch (Exception e) {
                logger.error("Errore durante il forward alla pagina di errore", e);
            }
        } catch (Exception e) {
            logger.error("Errore inatteso durante la gestione POST dell'area riservata", e);
            request.setAttribute("errore", "Errore durante la gestione dell'operazione sui biglietti.");
            try {
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            } catch (Exception ex) {
                logger.error("Errore durante il forward alla pagina di errore", ex);
            }
        }
    }

    private void forwardAreaRiservata(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getRequestDispatcher("/areaRiservata.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Errore durante il forward alla pagina dell'area riservata", e);
        }
    }
    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        } catch (Exception e) {
            logger.error("Errore durante il redirect alla pagina di login", e);
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
