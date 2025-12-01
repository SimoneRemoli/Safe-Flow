package Model.Extractor;

import Model.Record.LoginRecord;
import Exception.InvalidLoginInputExceptionRemoli;

import javax.servlet.http.HttpServletRequest;

import static Model.Extractor.RouteInputExtractor.sanitize;

public class LoginExtractor {

    public static LoginRecord from(HttpServletRequest request)
            throws InvalidLoginInputExceptionRemoli {

        String rawEmail = request.getParameter("Email");
        String rawPassword = request.getParameter("Password");

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

        String email = sanitize(rawEmail);
        String password = sanitize(rawPassword);

        if (email.isBlank())
            throw new InvalidLoginInputExceptionRemoli(
                    "Come pensi di autenticarti senza email? Io boh",
                    "Email è blank dopo sanitizzazione.",
                    InvalidLoginInputExceptionRemoli.Severity.MEDIUM
            );

        if (!email.matches("^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            throw new InvalidLoginInputExceptionRemoli(
                    "Ok che l'utente è scemo, ma non così tanto. Inserisci una email valida.",
                    "Regex email non rispettata: " + email,
                    InvalidLoginInputExceptionRemoli.Severity.MEDIUM
            );
        }
        if (password.isBlank())
            throw new InvalidLoginInputExceptionRemoli(
                    "Secondo te, la password può essere vuota? Sei uno scemo!",
                    "Password blank dopo sanitizzazione.",
                    InvalidLoginInputExceptionRemoli.Severity.MEDIUM
            );
        return new LoginRecord(email, password);
    }
}
