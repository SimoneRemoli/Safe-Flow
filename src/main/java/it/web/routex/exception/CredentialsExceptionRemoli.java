package it.web.routex.exception;

public class CredentialsExceptionRemoli extends Exception {

    final String details;

    public CredentialsExceptionRemoli(String message,String details) {
        super(message);
        this.details = details;
    }

    public String getDetails() {
        return details;
    }
}
