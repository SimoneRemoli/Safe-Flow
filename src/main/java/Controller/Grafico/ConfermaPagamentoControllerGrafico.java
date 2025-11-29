package Controller.Grafico;
import Controller.Applicativo.PagamentoMastercard;
import Controller.Applicativo.PagamentoPaypal;
import Controller.Applicativo.RegistrazionePagamentoController;
import Model.Extractor.PagamentoExtractor;
import Model.Record.PaymentRecord;
import Model.Domain.TypesOfPersistenceLayer;
import utility.Singleton.Credentials;
import Exception.PaymentValidationExceptionRemoli;
import Exception.CredentialsExceptionRemoli;
import Exception.DAOExceptionRemoli;
import utility.Singleton.PersistenceMode;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.util.*;

@WebServlet("/confermaPagamento")
public class ConfermaPagamentoControllerGrafico extends HttpServlet {

    List<String> codiciBiglietti;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        try {
            final HttpSession session = request.getSession(false);
            if (session == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            final Credentials cred = Credentials.getInstanceSingleton();
            System.out.printf("[PAGAMENTO] Utente loggato: %s %s (%s)%n",
                    cred.getNome(), cred.getCognome(), cred.getRuolo());
            PaymentRecord paymentRecord = PagamentoExtractor.from(request);
            TypesOfPersistenceLayer persistenceLayer = paymentRecord.persistenceLayer();
            PersistenceMode.getInstance().setTipo(persistenceLayer); //singleton che mantiene il tipo di persistenza scelto

            if (paymentRecord.city() == null || paymentRecord.quantity() == 0 || paymentRecord.total() == 0 || paymentRecord.method() == null) {
                response.sendRedirect("errorePagamento.jsp");
                return;
            }

            //  Selezione metodo di pagamento ----------------------------------------------
            RegistrazionePagamentoController controllerPagamento = null;
            String messaggio;

            switch (paymentRecord.method().toLowerCase()) {
                case "mastercard" -> {
                    final String numeroCarta = request.getParameter("numeroCarta");
                    final String scadenza = request.getParameter("scadenza");
                    final String cvv = request.getParameter("cvv");

                    if (numeroCarta == null || scadenza == null || cvv == null) {
                        response.sendRedirect("errorePagamento.jsp");
                        return;
                    }

                    controllerPagamento = new PagamentoMastercard(numeroCarta, scadenza, cvv, cred, paymentRecord.total(), paymentRecord.quantity(), paymentRecord.city());
                    messaggio = "Pagamento con Mastercard completato (" + numeroCarta + ").";
                }

                case "paypal" -> {
                    final String emailPaypal = request.getParameter("emailPaypal");
                    final String codiceTransazione = request.getParameter("codiceTransazione");

                    if (emailPaypal == null || codiceTransazione == null) {
                        response.sendRedirect("errorePagamento.jsp");
                        return;
                    }

                    controllerPagamento = new PagamentoPaypal(emailPaypal, codiceTransazione, cred, paymentRecord.total(), paymentRecord.quantity(), paymentRecord.city());
                    messaggio = "Pagamento con PayPal completato per " + emailPaypal + ".";
                }

                default -> {
                    response.sendRedirect("errorePagamento.jsp");
                    return;
                }
            }

            try {
                codiciBiglietti = controllerPagamento.run(); // se la vede poi chi viene chiamato
            } catch (PaymentValidationExceptionRemoli remoli) {
                System.out.println(" Errore PaymentValidationExceptionRemoli : " + remoli.getMessage() + " | " + remoli.getDetail() + "[" + remoli.getMethod() + "]");
                request.getRequestDispatcher("/errorePagamento.jsp").forward(request, response);
                return;
            } catch (DAOExceptionRemoli remoli) {
                System.out.println(" Errore DAOExceptionRemoli : " + remoli.getMessage());
                request.getRequestDispatcher("/errorePagamento.jsp").forward(request, response);
                return;
            } catch (CredentialsExceptionRemoli remoli) {
                System.out.println(" Errore CredentialsExceptionRemoli : " + remoli.getMessage() + " | " + remoli.getDetails());
                request.getRequestDispatcher("/errorePagamento.jsp").forward(request, response);
                return;
            } catch (Exception e) {
                request.getRequestDispatcher("/errorePagamento.jsp").forward(request, response);
                return;
            }

            // Passaggio dati alla view di successo
            request.setAttribute("city", paymentRecord.city());
            request.setAttribute("quantity", String.valueOf(paymentRecord.quantity()));
            request.setAttribute("totale", paymentRecord.total());
            request.setAttribute("metodo", paymentRecord.method());
            request.setAttribute("messaggio", messaggio);
            request.setAttribute("codiciBiglietti", codiciBiglietti);

            request.getRequestDispatcher("/successoPagamento.jsp").forward(request, response);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
