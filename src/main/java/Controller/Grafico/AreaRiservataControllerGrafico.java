package Controller.Grafico;
import Bean.RouteBean;
import Bean.TicketBean;
import Controller.Applicativo.AreaRiservata;
import Model.Domain.Credentials;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/areaRiservata")
public class AreaRiservataControllerGrafico extends HttpServlet
{
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        AreaRiservata reserved = new AreaRiservata();

        try {


            HttpSession session = request.getSession(false);
            if (session != null) {
                Credentials cred = Credentials.getInstance(session);
                String cf = cred.getCodiceFiscale();
                if (cf != null) {

                    List<RouteBean> listaPercorsi =  reserved.runPath(cf);
                    List<TicketBean> tickets = reserved.runTicket(cf);

                    // Salva in request per la JSP
                    request.setAttribute("listaPercorsi", listaPercorsi);

                    request.setAttribute("tickets", tickets);

                    request.getRequestDispatcher("areaRiservata.jsp").forward(request, response);
                    return;
                }

            }
            // Se non sei loggato o cf è null, reindirizza a login o pagina di errore
            response.sendRedirect("login.jsp");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
