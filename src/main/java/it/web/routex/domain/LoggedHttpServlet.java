package it.web.routex.domain;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LoggedHttpServlet extends HttpServlet {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected boolean hasRole(HttpServletRequest request, String role) {
        return SessionAuthUtil.hasRole(request.getSession(false), role);
    }

    protected void redirectToLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        } catch (Exception e) {
            logger.error("Errore durante il redirect alla pagina di login", e);
        }
    }

    protected void forward(HttpServletRequest request, HttpServletResponse response, String view) {
        try {
            request.getRequestDispatcher(view).forward(request, response);
        } catch (Exception e) {
            logger.error("Errore nel forwarding verso {}", view, e);
        }
    }

    protected void forwardError(HttpServletRequest request,
                                HttpServletResponse response,
                                String message,
                                String view) {
        request.setAttribute("errore", message);
        forward(request, response, view);
    }
}
