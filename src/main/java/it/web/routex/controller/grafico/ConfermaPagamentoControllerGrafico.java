package it.web.routex.controller.grafico;
import it.web.routex.controller.applicativo.PagamentoMastercard;
import it.web.routex.controller.applicativo.PagamentoPaypal;
import it.web.routex.controller.applicativo.RegistrazionePagamentoController;
import it.web.routex.model.domain.LoggedHttpServlet;
import it.web.routex.model.extractor.MastercardExtractor;
import it.web.routex.model.extractor.PagamentoExtractor;
import it.web.routex.model.extractor.PaypalExtractor;
import it.web.routex.model.record.MastercardRecord;
import it.web.routex.model.record.PaymentRecord;
import it.web.routex.model.domain.TypesOfPersistenceLayer;
import it.web.routex.model.record.PaypalRecord;
import it.web.routex.utility.singleton.Credentials;
import it.web.routex.exception.PaymentValidationExceptionRemoli;
import it.web.routex.exception.CredentialsExceptionRemoli;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.utility.singleton.PersistenceMode;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.util.*;
import it.web.routex.exception.InvalidPaymentInputExceptionRemoli;
import it.web.routex.exception.InvalidCardInputExceptionRemoli;

@WebServlet("/confermaPagamento")
public class ConfermaPagamentoControllerGrafico extends LoggedHttpServlet {
    private static final String ATTR_MESSAGGIO_ERRORE = "messaggioErrore";
    private static final String PAGE_ERRORE_PAGAMENTO = "/errorePagamento.jsp";
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    {
        List<String> codiciBiglietti;

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
                request.setAttribute(ATTR_MESSAGGIO_ERRORE, e.getUserMessage());
                request.getRequestDispatcher(PAGE_ERRORE_PAGAMENTO).forward(request, response);
                return;
            }

            TypesOfPersistenceLayer persistenceLayer = paymentRecord.persistenceLayer();
            logger.info("Tipo di persistenza scelto {}", persistenceLayer);
            PersistenceMode.getSingletonInstance().setTipo(persistenceLayer);
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
                        request.setAttribute(ATTR_MESSAGGIO_ERRORE, ex.getUserMessage());
                        request.getRequestDispatcher(PAGE_ERRORE_PAGAMENTO).forward(request, response);
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
                        request.setAttribute(ATTR_MESSAGGIO_ERRORE, e.getUserMessage());
                        request.getRequestDispatcher(PAGE_ERRORE_PAGAMENTO).forward(request, response);
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
                codiciBiglietti = controllerPagamento.run();
            } catch (PaymentValidationExceptionRemoli remoli) {
                request.setAttribute(ATTR_MESSAGGIO_ERRORE, remoli.getMessage());
                request.getRequestDispatcher(PAGE_ERRORE_PAGAMENTO).forward(request, response);
                logger.error("Errore PaymentValidationExceptionRemoli : {}", remoli.toString());
                return;
            } catch (DAOExceptionRemoli remoli) {
                logger.error("Errore Errore DAOExceptionRemoli : {}", remoli.toString());
                request.setAttribute(ATTR_MESSAGGIO_ERRORE, remoli.getMessage());
                request.getRequestDispatcher(PAGE_ERRORE_PAGAMENTO).forward(request, response);
                return;
            } catch (CredentialsExceptionRemoli remoli) {
                logger.error("Errore CredentialsExceptionRemoli : {}", remoli.toString());
                request.setAttribute(ATTR_MESSAGGIO_ERRORE, remoli.getMessage());
                request.getRequestDispatcher(PAGE_ERRORE_PAGAMENTO).forward(request, response);
                return;
            } catch (Exception e) {
                request.setAttribute(ATTR_MESSAGGIO_ERRORE, e.getMessage());
                request.getRequestDispatcher(PAGE_ERRORE_PAGAMENTO).forward(request, response);
                logger.error("Errore conferma pagamento.", e);
                return;
            }

            // Passaggio dati alla view di successo
            request.setAttribute("city", paymentRecord.city());
            request.setAttribute("quantity", String.valueOf(paymentRecord.quantity()));
            request.setAttribute("totale", paymentRecord.total());
            request.setAttribute("metodo", paymentRecord.method());
            request.setAttribute("messaggio", messaggio);
            request.setAttribute("codiciBiglietti", codiciBiglietti);
            logger.info("Pagamento confermato con successo. City={}, Quantity={}, Total={}, Method={}, MessageCheck={}, TicketCode={}", paymentRecord.city(), paymentRecord.quantity(), paymentRecord.total(), paymentRecord.method(), messaggio, codiciBiglietti );

            request.getRequestDispatcher("/successoPagamento.jsp").forward(request, response);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
