package Controller.Grafico;
import Controller.Applicativo.PagamentoMastercard;
import Controller.Applicativo.PagamentoPaypal;
import Controller.Applicativo.RegistrazionePagamentoController;
import Model.Domain.LoggedHttpServlet;
import Model.Extractor.MastercardExtractor;
import Model.Extractor.PagamentoExtractor;
import Model.Extractor.PaypalExtractor;
import Model.Record.MastercardRecord;
import Model.Record.PaymentRecord;
import Model.Domain.TypesOfPersistenceLayer;
import Model.Record.PaypalRecord;
import utility.Singleton.Credentials;
import Exception.PaymentValidationExceptionRemoli;
import Exception.CredentialsExceptionRemoli;
import Exception.DAOExceptionRemoli;
import utility.Singleton.PersistenceMode;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.util.*;
import Exception.InvalidPaymentInputExceptionRemoli;
import Exception.InvalidCardInputExceptionRemoli;

@WebServlet("/confermaPagamento")
public class ConfermaPagamentoControllerGrafico extends LoggedHttpServlet {

    List<String> codiciBiglietti;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    {

        try {
            final HttpSession session = request.getSession(false);
            if (session == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            PaymentRecord paymentRecord;
            final Credentials cred = Credentials.getInstanceSingleton();
            logger.info("[PROCESSAMENTO PAGAMENTO] Utente loggato: nome={}, cognome={}, ruolo={}", cred.getNome(), cred.getCognome(), cred.getRuolo());

            try{
                paymentRecord = PagamentoExtractor.from(request);
            }catch(InvalidPaymentInputExceptionRemoli e) {
                logger.error("Errore nell'input del pagamento: {}", e.toString());
                request.setAttribute("messaggioErrore", e.getUserMessage());
                request.getRequestDispatcher("/errorePagamento.jsp").forward(request, response);
                return;
            }

    //fin qui ok
            TypesOfPersistenceLayer persistenceLayer = paymentRecord.persistenceLayer();
            PersistenceMode.getInstance().setTipo(persistenceLayer); //singleton che mantiene il tipo di persistenza scelto

            //  Selezione metodo di pagamento ----------------------------------------------
            RegistrazionePagamentoController controllerPagamento = null;
            String messaggio;
            MastercardRecord master;
            PaypalRecord pay;

            switch (paymentRecord.method().toLowerCase())
            {
                case "mastercard" -> {
                    try {
                        master = MastercardExtractor.from(request);
                    }catch(InvalidCardInputExceptionRemoli ex)
                    {
                        logger.error("Errore nei dati Mastercard {}", ex.toString());
                        request.setAttribute("messaggioErrore", ex.getUserMessage());
                        request.getRequestDispatcher("/errorePagamento.jsp").forward(request, response);
                        return;
                    }
                    controllerPagamento = new PagamentoMastercard(master.numero_carta(), master.scadenza(), master.cvv(), cred, paymentRecord.total(), paymentRecord.quantity(), paymentRecord.city());
                    messaggio = "Pagamento con Mastercard completato (" + master.numero_carta() + ").";
                }
                case "paypal" -> {
                    try {
                        pay = PaypalExtractor.from(request);
                    } catch (InvalidCardInputExceptionRemoli e) {
                        logger.error("Errore nei dati Paypal {}", e.toString());
                        request.setAttribute("messaggioErrore", e.getUserMessage());
                        request.getRequestDispatcher("/errorePagamento.jsp").forward(request, response);
                        return;
                    }
                    controllerPagamento = new PagamentoPaypal(pay.email_paypal(), pay.codice_transazione(), cred, paymentRecord.total(), paymentRecord.quantity(), paymentRecord.city());
                    messaggio = "Pagamento con PayPal completato per " + pay.email_paypal() + ".";
                }
                default -> {
                    response.sendRedirect("errorePagamento.jsp");
                    return;
                }
            }
            try {
                codiciBiglietti = controllerPagamento.run(); // se la vede poi chi viene chiamato
            } catch (PaymentValidationExceptionRemoli remoli) {
                request.setAttribute("messaggioErrore", remoli.getMessage());
                request.getRequestDispatcher("/errorePagamento.jsp").forward(request, response);
                logger.error("Errore PaymentValidationExceptionRemoli : {}", remoli.toString());
                return;
            } catch (DAOExceptionRemoli remoli) {
                logger.error("Errore Errore DAOExceptionRemoli : {}", remoli.toString());
                request.setAttribute("messaggioErrore", remoli.getMessage());
                request.getRequestDispatcher("/errorePagamento.jsp").forward(request, response);
                return;
            } catch (CredentialsExceptionRemoli remoli) {
                logger.error("Errore CredentialsExceptionRemoli : {}", remoli.toString());
                request.setAttribute("messaggioErrore", remoli.getMessage());
                request.getRequestDispatcher("/errorePagamento.jsp").forward(request, response);
                return;
            } catch (Exception e) {
                request.setAttribute("messaggioErrore", e.getMessage());
                request.getRequestDispatcher("/errorePagamento.jsp").forward(request, response);
                logger.error("Errore CredentialsExceptionRemoli : {}", new RuntimeException(e));
                return;
            }

            // Passaggio dati alla view di successo
            request.setAttribute("city", paymentRecord.city());
            request.setAttribute("quantity", String.valueOf(paymentRecord.quantity()));
            request.setAttribute("totale", paymentRecord.total());
            request.setAttribute("metodo", paymentRecord.method());
            request.setAttribute("messaggio", messaggio);
            request.setAttribute("codiciBiglietti", codiciBiglietti);
            logger.info("Pagamento confermato con successo. City={}, Quantity={}, Total={}, Method={}, MessageCheck={}, TicketCode={}", paymentRecord.city(), String.valueOf(paymentRecord.quantity()), paymentRecord.total(), paymentRecord.method(), messaggio, codiciBiglietti );

            request.getRequestDispatcher("/successoPagamento.jsp").forward(request, response);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
