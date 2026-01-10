package it.web.routex.boundary.cli.extractor;

import it.web.routex.boundary.cli.view.PaypalCLI;
import it.web.routex.record.PaypalRecord;
import it.web.routex.exception.InvalidCardInputExceptionRemoli;

public final class PaypalExtractorCLI {

    private static String sanitize(String s) {
        return (s == null) ? null : s.trim();
    }

    private PaypalExtractorCLI()
    {
        throw new AssertionError("Classe di estrazione dati, non si creano new");
    }

    public static PaypalRecord from() throws InvalidCardInputExceptionRemoli {

        String email = sanitize(PaypalCLI.getEmailPaypal());
        String codice = sanitize(PaypalCLI.getCodiceTransazione());

        validateEmailAndCode(email, codice);

        return new PaypalRecord(email, codice);
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
