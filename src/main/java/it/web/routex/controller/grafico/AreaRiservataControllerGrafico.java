package it.web.routex.controller.grafico;
import it.web.routex.bean.RouteBean;
import it.web.routex.bean.TicketBean;
import it.web.routex.controller.applicativo.AreaRiservata;
import it.web.routex.model.domain.LoggedHttpServlet;
import it.web.routex.utility.Singleton.Credentials;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import it.web.routex.exception.PathNotFoundExceptionRemoli;
import it.web.routex.exception.DAOExceptionRemoli;

@WebServlet("/areaRiservata")
public class AreaRiservataControllerGrafico extends LoggedHttpServlet
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        try {
            try {
                final HttpSession session = request.getSession(false);
                if (session != null) {
                    Credentials cred = Credentials.getInstanceSingleton();
                    String cf = cred.getCodiceFiscale();
                    AreaRiservata reserved = new AreaRiservata();

                    if (cf != null) {

                        List<RouteBean> listaPercorsi = reserved.runPath(cf);
                        List<TicketBean> tickets = reserved.runTicket(cf);
                        request.setAttribute("listaPercorsi", listaPercorsi);
                        request.setAttribute("tickets", tickets);
                        request.getRequestDispatcher("areaRiservata.jsp").forward(request, response);
                        return;
                    }
                }
                // Se non sei loggato o cf è null, reindirizza a login
                response.sendRedirect("login.jsp");
            } catch (PathNotFoundExceptionRemoli remoli) {
                logger.error("Errore PathNotFoundExceptionRemoli. Messaggio={} Cf={} CodiceErrore={} Dettagli={}.", remoli.getMessage(), remoli.getCodice_fiscale_utente(), remoli.getCodice_di_errore(), remoli.getDetails());
                request.setAttribute("errore", remoli.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            } catch (DAOExceptionRemoli remoli) {
                logger.error("Errore DAOExceptionRemoli. Messaggio={} Causa{}", remoli.getMessage(), remoli.getCause());
                request.setAttribute("errore", remoli.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
