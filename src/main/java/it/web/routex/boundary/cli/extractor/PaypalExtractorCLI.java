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

        boolean emailBlank = email.isBlank();
        boolean codiceBlank = codice.isBlank();

        boolean emailValid = email.matches("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$");
        boolean codiceValid = codice.matches("^TXN-[A-Za-z0-9]{6,20}$");

        boolean emailInvalid = !emailBlank && !emailValid;
        boolean codiceInvalid = !codiceBlank && !codiceValid;

        if (emailBlank && codiceBlank) {
            error("Email PayPal e codice transazione mancanti.");
        }
        if (emailBlank && codiceValid) {
            error("Inserisci la tua email PayPal.");
        }
        if (emailBlank && codiceInvalid) {
            error("Email mancante e codice transazione non valido.");
        }
        if (emailValid && codiceBlank) {
            error("Il codice transazione è obbligatorio.");
        }
        if (emailValid && codiceInvalid) {
            error("Il codice transazione non è valido.");
        }
        if (emailInvalid && codiceBlank) {
            error("Email PayPal non valida e codice transazione mancante.");
        }
        if (emailInvalid && codiceValid) {
            error("L'email PayPal inserita non è valida.");
        }
        if (emailInvalid && codiceInvalid) {
            error("Email PayPal e codice transazione non validi.");
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
