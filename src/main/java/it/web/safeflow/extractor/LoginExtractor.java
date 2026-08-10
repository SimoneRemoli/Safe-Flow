package it.web.safeflow.extractor;

import it.web.safeflow.record.LoginRecord;
import it.web.safeflow.exception.InvalidLoginInputExceptionRemoli;

import javax.servlet.http.HttpServletRequest;

import static it.web.safeflow.extractor.RequestSanitizer.sanitize;

public final class LoginExtractor {

    private LoginExtractor()
    {
        throw new AssertionError("Classe di estrazione dati, non si creano new");
    }

    public static LoginRecord from(HttpServletRequest request) throws InvalidLoginInputExceptionRemoli {

        String rawEmail = request.getParameter("Email");
        String rawPassword = request.getParameter("Password");

        if (rawEmail == null)
            throw new InvalidLoginInputExceptionRemoli(
                    "Il campo email non è stato inviato dal form.",
                    "Parametro 'Email' null dal form.",
                    InvalidLoginInputExceptionRemoli.Severity.LOW
            );

        if (rawPassword == null)
            throw new InvalidLoginInputExceptionRemoli(
                    "Il campo password non è stato inviato dal form.",
                    "Parametro 'Password' null dal form.",
                    InvalidLoginInputExceptionRemoli.Severity.LOW
            );

        String email = sanitize(rawEmail);
        String password = sanitize(rawPassword);

        if (email.isBlank() && password.isBlank())
            throw new InvalidLoginInputExceptionRemoli(
                    "Email e password mancanti.",
                    "Email = null, password = null.",
                    InvalidLoginInputExceptionRemoli.Severity.MEDIUM
            );

        if(password.isBlank() && !email.matches("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$") )
            throw new InvalidLoginInputExceptionRemoli(
                    "Campo 'password' vuoto e email non conforme allo standard.",
                    "Email = non conforme, password = null.",
                    InvalidLoginInputExceptionRemoli.Severity.MEDIUM
            );


        if (email.isBlank())
            throw new InvalidLoginInputExceptionRemoli(
                    "Inserisci l'email per accedere.",
                    "Email è blank dopo sanitizzazione.",
                    InvalidLoginInputExceptionRemoli.Severity.MEDIUM
            );

        if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            throw new InvalidLoginInputExceptionRemoli(
                    "Inserisci una email valida.",
                    "Regex email non rispettata: " + email,
                    InvalidLoginInputExceptionRemoli.Severity.MEDIUM
            );
        }
        if (password.isBlank())
            throw new InvalidLoginInputExceptionRemoli(
                    "Inserisci la password per accedere.",
                    "Password blank dopo sanitizzazione.",
                    InvalidLoginInputExceptionRemoli.Severity.MEDIUM
            );
        return new LoginRecord(email, password);
    }
}
