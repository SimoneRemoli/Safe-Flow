package it.web.routex.boundary.cli.extractor;

import it.web.routex.exception.InvalidLoginInputExceptionRemoli;
import it.web.routex.model.record.LoginRecord;

public class LoginExtractorCLI {

    private static String sanitize(String s) {
        return (s == null) ? null : s.trim();
    }

    public static LoginRecord from(String email, String password) throws InvalidLoginInputExceptionRemoli {

        String rawEmail = email;
        String rawPassword = password;

        // PARAMETRI NULL
        if (rawEmail == null)
            throw new InvalidLoginInputExceptionRemoli(
                    "Il campo email è obbligatorio.",
                    "Parametro 'Email' è null.",
                    InvalidLoginInputExceptionRemoli.Severity.LOW
            );

        if (rawPassword == null)
            throw new InvalidLoginInputExceptionRemoli(
                    "Il campo password è obbligatorio.",
                    "Parametro 'Password' è null.",
                    InvalidLoginInputExceptionRemoli.Severity.LOW
            );

        String emailOk = sanitize(rawEmail);
        String passwordOk = sanitize(rawPassword);

        if (emailOk.isBlank())
            throw new InvalidLoginInputExceptionRemoli(
                    "Come pensi di autenticarti senza email? Io boh",
                    "Email è blank dopo sanitizzazione.",
                    InvalidLoginInputExceptionRemoli.Severity.MEDIUM
            );

        if (!emailOk.matches("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            throw new InvalidLoginInputExceptionRemoli(
                    "Ok che l'utente è scemo, ma non così tanto. Inserisci una email valida.",
                    "Regex email non rispettata: " + email,
                    InvalidLoginInputExceptionRemoli.Severity.MEDIUM
            );
        }
        if (passwordOk.isBlank())
            throw new InvalidLoginInputExceptionRemoli(
                    "Secondo te, la password può essere vuota? Sei uno scemo!",
                    "Password blank dopo sanitizzazione.",
                    InvalidLoginInputExceptionRemoli.Severity.MEDIUM
            );
        return new LoginRecord(emailOk, passwordOk);
    }
}
