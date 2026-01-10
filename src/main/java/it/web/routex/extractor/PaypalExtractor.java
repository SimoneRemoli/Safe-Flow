package it.web.routex.extractor;

import it.web.routex.record.PaypalRecord;
import it.web.routex.exception.InvalidCardInputExceptionRemoli;

import javax.servlet.http.HttpServletRequest;

import static it.web.routex.extractor.RouteInputExtractor.sanitize;

public final class PaypalExtractor {

    private PaypalExtractor() {
        throw new AssertionError("Classe di estrazione dati, non si creano new");
    }

    public static PaypalRecord from(HttpServletRequest request)
            throws InvalidCardInputExceptionRemoli {

        String email = sanitizeParam(request, "emailPaypal");
        String codice = sanitizeParam(request, "codiceTransazione");

        validateEmailAndCode(email, codice);

        return new PaypalRecord(email, codice);
    }

    // =====================
    // Helpers
    // =====================

    private static String sanitizeParam(HttpServletRequest request, String name)
            throws InvalidCardInputExceptionRemoli {

        String value = request.getParameter(name);

        if (value == null) {
            throw new InvalidCardInputExceptionRemoli(
                    "Parametro mancante: " + name,
                    "Parametro " + name + " non presente nella request",
                    InvalidCardInputExceptionRemoli.Severity.HIGH
            );
        }

        return sanitize(value);
    }

    private static void validateEmailAndCode(String email, String codice)
            throws InvalidCardInputExceptionRemoli {

        if (email.isBlank() && codice.isBlank()) {
            error("Email PayPal e codice transazione mancanti.");
            return;
        }

        if (email.isBlank()) {
            error("Inserisci la tua email PayPal.");
            return;
        }

        if (codice.isBlank()) {
            error("Il codice transazione è obbligatorio.");
            return;
        }

        if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            error("L'email PayPal inserita non è valida.");
            return;
        }

        if (!codice.matches("^TXN-[A-Za-z0-9]{6,20}$")) {
            error("Il codice transazione non è valido.");
        }
    }

    private static void error(String message)
            throws InvalidCardInputExceptionRemoli {

        throw new InvalidCardInputExceptionRemoli(
                message,
                message,
                InvalidCardInputExceptionRemoli.Severity.MEDIUM
        );
    }
}
