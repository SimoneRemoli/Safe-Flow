package it.web.routex.boundary.cli.extractor;

import it.web.routex.boundary.cli.view.PaypalCLI;
import it.web.routex.model.record.PaypalRecord;
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

        if (rawEmail == null || rawCodice == null) {
            throw new InvalidCardInputExceptionRemoli(
                    "Dati PayPal incompleti.",
                    "Parametro emailPaypal o codiceTransazione null.",
                    InvalidCardInputExceptionRemoli.Severity.HIGH
            );
        }

        String email = sanitize(rawEmail);
        String codice = sanitize(rawCodice);

        if (email.isBlank()) {
            throw new InvalidCardInputExceptionRemoli(
                    "Inserisci la tua email PayPal.",
                    "Email PayPal blank dopo sanitizzazione.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }
        if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            throw new InvalidCardInputExceptionRemoli(
                    "L'email PayPal inserita non è valida.",
                    "Formato email non corretto: " + email,
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }
        if (codice.isBlank()) {
            throw new InvalidCardInputExceptionRemoli(
                    "Il codice transazione è obbligatorio.",
                    "Codice transazione blank.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }
        if (!codice.matches("^TXN-[A-Za-z0-9]{6,20}$")) {
            throw new InvalidCardInputExceptionRemoli(
                    "Il codice transazione non è valido.",
                    "Formato non conforme: " + codice,
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }
        return new PaypalRecord(email, codice);
    }
}
