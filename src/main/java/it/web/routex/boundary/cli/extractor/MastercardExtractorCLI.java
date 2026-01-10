package it.web.routex.boundary.cli.extractor;

import it.web.routex.boundary.cli.view.MastercardCLI;
import it.web.routex.exception.InvalidCardInputExceptionRemoli;
import it.web.routex.record.MastercardRecord;

import java.time.LocalDate;



public final class MastercardExtractorCLI {

    private static String sanitize(String s) {
        return (s == null) ? null : s.trim();
    }


    private MastercardExtractorCLI()
    {
        throw new AssertionError("Classe di estrazione dati, non si creano new");
    }

    private static void validatePresenceAndFormat(
            String numero,
            String scadenza,
            String cvv
    ) throws InvalidCardInputExceptionRemoli {

        boolean numeroBlank = numero.isBlank();
        boolean scadenzaBlank = scadenza.isBlank();
        boolean cvvBlank = cvv.isBlank();

        boolean numeroValid = numero.matches("^\\d{16}$");
        boolean scadenzaValid = scadenza.matches("^\\d{4}-\\d{2}-\\d{2}$");
        boolean cvvValid = cvv.matches("^\\d{3}$");

        if (numeroBlank && scadenzaBlank && cvvBlank) {
            error("Numero carta, scadenza e CVV mancanti.");
        }
        if (!numeroBlank && !numeroValid) {
            error("Numero carta non valido.");
        }
        if (!scadenzaBlank && !scadenzaValid) {
            error("Scadenza non valida.");
        }
        if (!cvvBlank && !cvvValid) {
            error("CVV non valido.");
        }
    }

    private static void error(String msg) throws InvalidCardInputExceptionRemoli {
        throw new InvalidCardInputExceptionRemoli(
                msg,
                msg,
                InvalidCardInputExceptionRemoli.Severity.MEDIUM
        );
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

    public static MastercardRecord from() throws InvalidCardInputExceptionRemoli {

        String numero = sanitize(MastercardCLI.getNumeroCarta());
        String scadenza = sanitize(MastercardCLI.getScadenza());
        String cvv = sanitize(MastercardCLI.getCvv());

        validatePresenceAndFormat(numero, scadenza, cvv);
        validateExpiry(scadenza);
        validateCvv(cvv);

        return new MastercardRecord(numero, scadenza, cvv);
    }
}
