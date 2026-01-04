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

        String rawEmail = PaypalCLI.getEmailPaypal();
        String rawCodice = PaypalCLI.getCodiceTransazione();

        String email = sanitize(rawEmail);
        String codice = sanitize(rawCodice);

        boolean emailBlank = email.isBlank();
        boolean codiceBlank = codice.isBlank();

        boolean emailValid = email.matches("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$");
        boolean codiceValid = codice.matches("^TXN-[A-Za-z0-9]{6,20}$");

        boolean emailInvalid = !emailBlank && !emailValid;
        boolean codiceInvalid = !codiceBlank && !codiceValid;

        if (emailBlank && codiceBlank) {
            throw new InvalidCardInputExceptionRemoli(
                    "Email PayPal e codice transazione mancanti.",
                    "email blank + codice blank.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }
        if (emailBlank && codiceValid) {
            throw new InvalidCardInputExceptionRemoli(
                    "Inserisci la tua email PayPal.",
                    "Email PayPal blank dopo sanitizzazione.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }
        if (emailBlank && codiceInvalid) {
            throw new InvalidCardInputExceptionRemoli(
                    "Email mancante e codice transazione non valido.",
                    "email blank + codice formato errato.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }
        if (emailValid && codiceBlank) {
            throw new InvalidCardInputExceptionRemoli(
                    "Il codice transazione è obbligatorio.",
                    "Codice transazione blank.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }
        if (emailValid && codiceInvalid) {
            throw new InvalidCardInputExceptionRemoli(
                    "Il codice transazione non è valido.",
                    "Formato non conforme: " + codice,
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }
        if (emailInvalid && codiceBlank) {
            throw new InvalidCardInputExceptionRemoli(
                    "Email PayPal non valida e codice transazione mancante.",
                    "email formato errato + codice blank.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }
        if (emailInvalid && codiceValid) {
            throw new InvalidCardInputExceptionRemoli(
                    "L'email PayPal inserita non è valida.",
                    "Formato email non corretto: " + email,
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }
        if (emailInvalid && codiceInvalid) {
            throw new InvalidCardInputExceptionRemoli(
                    "Email PayPal e codice transazione non validi.",
                    "email errata + codice errato.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }
        return new PaypalRecord(email, codice);
    }
}
