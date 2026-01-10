package it.web.routex.validator;

import it.web.routex.exception.InvalidCardInputExceptionRemoli;

public final class PaypalValidator {

    private PaypalValidator() {}

    public static void validate(String email, String codice)
            throws InvalidCardInputExceptionRemoli {

        if (email.isBlank() && codice.isBlank()) {
            error("Email PayPal e codice transazione mancanti.");
        }

        if (email.isBlank()) {
            error("Inserisci la tua email PayPal.");
        }

        if (codice.isBlank()) {
            error("Il codice transazione è obbligatorio.");
        }

        if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            error("L'email PayPal inserita non è valida.");
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
