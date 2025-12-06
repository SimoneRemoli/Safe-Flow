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
import java.io.IOException;
import java.util.*;
import it.web.routex.exception.InvalidPaymentInputExceptionRemoli;
import it.web.routex.exception.InvalidCardInputExceptionRemoli;

@WebServlet("/confermaPagamento")
public class ConfermaPagamentoControllerGrafico extends LoggedHttpServlet {
    private static final String ATTR_MESSAGGIO_ERRORE = "messaggioErrore";
    private static final String PAGE_ERRORE_PAGAMENTO = "/errorePagamento.jsp";
    private static final String FORWARDING = "Errore nel forwarding";
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession(false);
        if (!verificaSessione(session, response)) return;

        Credentials cred = Credentials.getInstanceSingleton();
        logUtente(cred);

        PaymentRecord paymentRecord = estraiPagamento(request, response);
        if (paymentRecord == null) return;

        impostaPersistenza(paymentRecord);

        RegistrazionePagamentoController controllerPagamento = creaControllerPagamento(paymentRecord, request, response, cred);

        if (controllerPagamento == null) return;

        List<String> codiciBiglietti = eseguiPagamento(controllerPagamento, request, response);

        if (codiciBiglietti == null) return;

        mostraSuccesso(request, response, paymentRecord, codiciBiglietti);
    }

    private PaymentRecord estraiPagamento(HttpServletRequest request, HttpServletResponse response)
    {
        try{
            return PagamentoExtractor.from(request);
        }catch(InvalidPaymentInputExceptionRemoli e) {
            logger.error("Errore nell'input del pagamento: {}", e.toString());
            request.setAttribute(ATTR_MESSAGGIO_ERRORE, e.getUserMessage());
            try {
                request.getRequestDispatcher(PAGE_ERRORE_PAGAMENTO).forward(request, response);
            }catch(Exception a){
                logger.error("Errore durante il forward alla pagina di errore", a);
            }
            return null;
        }
    }
    private boolean verificaSessione(HttpSession session, HttpServletResponse response) {
        if (session == null) {
            try {
                response.sendRedirect("login.jsp");
            } catch (IOException e) {
                logger.error("Errore durante il redirect", e);
            }
            return false;
        }
        return true;
    }
    private void logUtente(Credentials cred) {
        logger.info("[PROCESSAMENTO PAGAMENTO] Utente loggato: nome={}, cognome={}, ruolo={}",
                cred.getNome(), cred.getCognome(), cred.getRuolo());
    }
    private void impostaPersistenza(PaymentRecord paymentRecord) {
        TypesOfPersistenceLayer persistenceLayer = paymentRecord.persistenceLayer();
        logger.info("Tipo di persistenza scelto {}", persistenceLayer);
        PersistenceMode.getSingletonInstance().setTipo(persistenceLayer);
    }
    private RegistrazionePagamentoController creaControllerPagamento(
            PaymentRecord paymentRecord,
            HttpServletRequest request,
            HttpServletResponse response,
            Credentials cred) {

        String metodo = paymentRecord.method().toLowerCase();

        return switch (metodo) {
            case "mastercard" -> creaPagamentoMastercard(paymentRecord, request, response, cred);
            case "paypal"     -> creaPagamentoPaypal(paymentRecord, request, response, cred);
            default           -> gestisciMetodoNonValido(response);
        };
    }
    private RegistrazionePagamentoController creaPagamentoMastercard(
            PaymentRecord paymentRecord,
            HttpServletRequest request,
            HttpServletResponse response,
            Credentials cred) {

        try {
            MastercardRecord master = MastercardExtractor.from(request);
            return new PagamentoMastercard(
                    master.numero_carta(),
                    master.scadenza(),
                    master.cvv(),
                    cred,
                    paymentRecord.total(),
                    paymentRecord.quantity(),
                    paymentRecord.city()
            );
        } catch (InvalidCardInputExceptionRemoli e) {
            gestisciErroreInput(request, response, e.getUserMessage(), "Errore nei dati Mastercard", e);
            return null;
        }
    }
    private RegistrazionePagamentoController creaPagamentoPaypal(
            PaymentRecord paymentRecord,
            HttpServletRequest request,
            HttpServletResponse response,
            Credentials cred) {

        try {
            PaypalRecord pay = PaypalExtractor.from(request);
            return new PagamentoPaypal(
                    pay.email_paypal(),
                    pay.codice_transazione(),
                    cred,
                    paymentRecord.total(),
                    paymentRecord.quantity(),
                    paymentRecord.city()
            );
        } catch (InvalidCardInputExceptionRemoli e) {
            gestisciErroreInput(request, response, e.getUserMessage(), "Errore nei dati Paypal", e);
            return null;
        }
    }
    private RegistrazionePagamentoController gestisciMetodoNonValido(HttpServletResponse response) {
        try {
            response.sendRedirect("errorePagamento.jsp");
        } catch (IOException e) {
            logger.error("Errore nel redirect metodo non valido", e);
        }
        return null;
    }
    private List<String> eseguiPagamento(
            RegistrazionePagamentoController controllerPagamento,
            HttpServletRequest request,
            HttpServletResponse response) {

        try {
            return controllerPagamento.run();

        } catch (PaymentValidationExceptionRemoli | DAOExceptionRemoli | CredentialsExceptionRemoli e) {

            request.setAttribute(ATTR_MESSAGGIO_ERRORE, e.getMessage());
            try {
                request.getRequestDispatcher(PAGE_ERRORE_PAGAMENTO).forward(request, response);
            } catch (Exception ex) {
                logger.error(FORWARDING, ex);
            }

            logger.error("Errore durante il pagamento", e);
            return Collections.emptyList();

        } catch (Exception e) {
            request.setAttribute(ATTR_MESSAGGIO_ERRORE, e.getMessage());
            try {
                request.getRequestDispatcher(PAGE_ERRORE_PAGAMENTO)
                        .forward(request, response);
            } catch (Exception ex) {
                logger.error(FORWARDING, ex);
            }

            logger.error("Errore generico conferma pagamento", e);
            return Collections.emptyList();
        }
    }
    private void mostraSuccesso(
            HttpServletRequest request,
            HttpServletResponse response,
            PaymentRecord paymentRecord,
            List<String> codiciBiglietti) {

        request.setAttribute("city", paymentRecord.city());
        request.setAttribute("quantity", String.valueOf(paymentRecord.quantity()));
        request.setAttribute("totale", paymentRecord.total());
        request.setAttribute("metodo", paymentRecord.method());
        request.setAttribute("messaggio", "Pagamento completato");
        request.setAttribute("codiciBiglietti", codiciBiglietti);

        try {
            request.getRequestDispatcher("/successoPagamento.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            logger.error(FORWARDING, e);
        }
    }
    private void gestisciErroreInput(
            HttpServletRequest request,
            HttpServletResponse response,
            String messaggio,
            String log,
            Exception e) {

        logger.error(log, e);
        request.setAttribute(ATTR_MESSAGGIO_ERRORE, messaggio);

        try {
            request.getRequestDispatcher(PAGE_ERRORE_PAGAMENTO)
                    .forward(request, response);
        } catch (Exception ex) {
            logger.error(FORWARDING, ex);
        }
    }
}
