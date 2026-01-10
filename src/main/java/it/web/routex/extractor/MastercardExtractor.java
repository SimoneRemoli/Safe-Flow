package it.web.routex.extractor;
import javax.servlet.http.HttpServletRequest;
import it.web.routex.exception.InvalidCardInputExceptionRemoli;
import it.web.routex.record.MastercardRecord;

import java.time.LocalDate;

import static it.web.routex.extractor.RouteInputExtractor.sanitize;

public final class MastercardExtractor {

    private MastercardExtractor() {
        throw new AssertionError("Classe di estrazione dati, non si creano new");
    }

    public static MastercardRecord from(HttpServletRequest request)
            throws InvalidCardInputExceptionRemoli {

        String numero = sanitizeParam(request, "numeroCarta");
        String scadenza = sanitizeParam(request, "scadenza");
        String cvv = sanitizeParam(request, "cvv");

        validatePresenceAndFormat(numero, scadenza, cvv);
        validateExpiry(scadenza);
        validateCvv(cvv);

        return new MastercardRecord(numero, scadenza, cvv);
    }


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

    private static void validatePresenceAndFormat(
            String numero,
            String scadenza,
            String cvv
    ) throws InvalidCardInputExceptionRemoli {

        if (numero.isBlank() && scadenza.isBlank() && cvv.isBlank()) {
            error("Numero carta, scadenza e CVV mancanti.");
        }

        if (!numero.isBlank() && !numero.matches("^\\d{16}$")) {
            error("Numero carta non valido.");
        }

        if (!scadenza.isBlank() && !scadenza.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            error("Scadenza non valida.");
        }

        if (!cvv.isBlank() && !cvv.matches("^\\d{3}$")) {
            error("CVV non valido.");
        }
    }

    private static void validateExpiry(String scadenza)
            throws InvalidCardInputExceptionRemoli {

        String[] parts = scadenza.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        if (month < 1 || month > 12) {
            error("Mese della carta non valido.");
        }

        LocalDate today = LocalDate.now();
        LocalDate expiry = LocalDate.of(year, month, 1)
                .plusMonths(1)
                .minusDays(1);

        if (expiry.isBefore(today)) {
            throw new InvalidCardInputExceptionRemoli(
                    "La carta è scaduta.",
                    "Data scadenza " + scadenza,
                    InvalidCardInputExceptionRemoli.Severity.HIGH
            );
        }
    }

    private static void validateCvv(String cvv)
            throws InvalidCardInputExceptionRemoli {

        if (!cvv.matches("^\\d{3}$")) {
            error("Il codice CVV non è valido.");
        }
    }

    private static void error(String msg)
            throws InvalidCardInputExceptionRemoli {

        throw new InvalidCardInputExceptionRemoli(
                msg,
                msg,
                InvalidCardInputExceptionRemoli.Severity.MEDIUM
        );
    }
}
