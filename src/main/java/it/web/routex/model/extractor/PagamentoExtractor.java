package it.web.routex.model.extractor;

import it.web.routex.model.domain.TypesOfPersistenceLayer;
import it.web.routex.model.record.PaymentRecord;

import javax.servlet.http.HttpServletRequest;

import it.web.routex.exception.InvalidPaymentInputExceptionRemoli;

public class PagamentoExtractor {

    public static PaymentRecord from(HttpServletRequest request)
            throws InvalidPaymentInputExceptionRemoli {

        String city = request.getParameter("city");
        String quantityParam = request.getParameter("quantity");
        String totaleParam = request.getParameter("totale");
        String metodo = request.getParameter("metodoPagamento");
        String persistence = request.getParameter("persistence");

        if (city == null)
            throw new InvalidPaymentInputExceptionRemoli(
                    "Campo città mancante.",
                    "Parametro 'city' è null.",
                    InvalidPaymentInputExceptionRemoli.Severity.LOW
            );

        if (quantityParam == null)
            throw new InvalidPaymentInputExceptionRemoli(
                    "Campo quantità mancante.",
                    "Parametro 'quantity' è null.",
                    InvalidPaymentInputExceptionRemoli.Severity.LOW
            );

        if (totaleParam == null)
            throw new InvalidPaymentInputExceptionRemoli(
                    "Campo totale mancante.",
                    "Parametro 'totale' è null.",
                    InvalidPaymentInputExceptionRemoli.Severity.LOW
            );

        if (metodo == null)
            throw new InvalidPaymentInputExceptionRemoli(
                    "Metodo di pagamento mancante.",
                    "Parametro 'metodoPagamento' è null.",
                    InvalidPaymentInputExceptionRemoli.Severity.LOW
            );

        if (persistence == null)
            throw new InvalidPaymentInputExceptionRemoli(
                    "Parametro del livello di persistenza mancante.",
                    "Parametro 'persistence' è null.",
                    InvalidPaymentInputExceptionRemoli.Severity.LOW
            );

        int quantity;
        try {
            quantity = Integer.parseInt(quantityParam);
        } catch (NumberFormatException e) {
            throw new InvalidPaymentInputExceptionRemoli(
                    "La quantità inserita non è valida.",
                    "NumberFormatException per quantity='" + quantityParam + "'",
                    InvalidPaymentInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (quantity <= 0)
            throw new InvalidPaymentInputExceptionRemoli(
                    "La quantità deve essere maggiore di zero.",
                    "Quantity <= 0: " + quantity,
                    InvalidPaymentInputExceptionRemoli.Severity.MEDIUM
            );

        double totale;
        try {
            totale = Double.parseDouble(totaleParam);
        } catch (NumberFormatException e) {
            throw new InvalidPaymentInputExceptionRemoli(
                    "Il totale inserito non è valido.",
                    "NumberFormatException per totale='" + totaleParam + "'",
                    InvalidPaymentInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (totale < 0)
            throw new InvalidPaymentInputExceptionRemoli(
                    "Il totale non può essere negativo.",
                    "Totale negativo: " + totale,
                    InvalidPaymentInputExceptionRemoli.Severity.MEDIUM
            );

        if (metodo.isBlank())
            throw new InvalidPaymentInputExceptionRemoli(
                    "Metodo di pagamento vuoto.",
                    "'metodoPagamento' è blank.",
                    InvalidPaymentInputExceptionRemoli.Severity.MEDIUM
            );

        TypesOfPersistenceLayer persistenceLayer;
        if (persistence.equals("JDBC")) {
            persistenceLayer = TypesOfPersistenceLayer.JDBC;
        } else if (persistence.equals("FileSystem")) {
            persistenceLayer = TypesOfPersistenceLayer.FileSystem;
        } else {
            throw new InvalidPaymentInputExceptionRemoli(
                    "Tipo di persistenza non valido.",
                    "Parametro persistence='" + persistence + "' non riconosciuto.",
                    InvalidPaymentInputExceptionRemoli.Severity.HIGH
            );
        }
        return new PaymentRecord(
                city,
                quantity,
                totale,
                metodo,
                persistenceLayer
        );
    }
}
