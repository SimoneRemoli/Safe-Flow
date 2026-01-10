package it.web.routex.extractor;

import it.web.routex.exception.InvalidCardInputExceptionRemoli;

import java.time.LocalDate;

public final class MastercardValidator {

    private MastercardValidator() {}

    public static void validate(
            String numero,
            String scadenza,
            String cvv
    ) throws InvalidCardInputExceptionRemoli {

        validatePresenceAndFormat(numero, scadenza, cvv);
        validateExpiry(scadenza);
        validateCvv(cvv);
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
