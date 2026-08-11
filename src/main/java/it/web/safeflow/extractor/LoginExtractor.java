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

        if (rawEmail == null && rawPassword == null)
            throw new InvalidLoginInputExceptionRemoli(
                    "Email and password are required to access the reserved area.",
                    "Parameters 'Email' and 'Password' are null in the login form.",
                    InvalidLoginInputExceptionRemoli.Severity.LOW
            );

        if (rawEmail == null)
            throw new InvalidLoginInputExceptionRemoli(
                    "Email is required to access the reserved area.",
                    "Parameter 'Email' is null in the login form.",
                    InvalidLoginInputExceptionRemoli.Severity.LOW
            );

        if (rawPassword == null)
            throw new InvalidLoginInputExceptionRemoli(
                    "Password is required to access the reserved area.",
                    "Parameter 'Password' is null in the login form.",
                    InvalidLoginInputExceptionRemoli.Severity.LOW
            );

        String email = sanitize(rawEmail);
        String password = sanitize(rawPassword);

        if (email.isBlank() && password.isBlank())
            throw new InvalidLoginInputExceptionRemoli(
                    "Email and password are required to access the reserved area.",
                    "Email and password are blank after sanitization.",
                    InvalidLoginInputExceptionRemoli.Severity.MEDIUM
            );

        if(password.isBlank() && !email.matches("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$") )
            throw new InvalidLoginInputExceptionRemoli(
                    "Enter a valid email address and provide your password.",
                    "Email format is invalid and password is blank.",
                    InvalidLoginInputExceptionRemoli.Severity.MEDIUM
            );


        if (email.isBlank())
            throw new InvalidLoginInputExceptionRemoli(
                    "Email is required to access the reserved area.",
                    "Email is blank after sanitization.",
                    InvalidLoginInputExceptionRemoli.Severity.MEDIUM
            );

        if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            throw new InvalidLoginInputExceptionRemoli(
                    "Enter a valid email address.",
                    "Email regex was not matched: " + email,
                    InvalidLoginInputExceptionRemoli.Severity.MEDIUM
            );
        }
        if (password.isBlank())
            throw new InvalidLoginInputExceptionRemoli(
                    "Password is required to access the reserved area.",
                    "Password is blank after sanitization.",
                    InvalidLoginInputExceptionRemoli.Severity.MEDIUM
            );
        return new LoginRecord(email, password);
    }
}
