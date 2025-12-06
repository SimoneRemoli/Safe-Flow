package it.web.routex.exception;

public class PathNotFoundExceptionRemoli extends Exception {

    final String codiceFiscaleUtente;
    final int codiceDiErrore;
    final String details;

    public PathNotFoundExceptionRemoli(String message, String codiceFiscaleUtente, int codiceDiErrore, String details) {
        super(message);
        this.codiceFiscaleUtente = codiceFiscaleUtente;
        this.codiceDiErrore = codiceDiErrore;
        this.details = details;
    }

    public int getCodiceDiErrore() {
        return codiceDiErrore;
    }

    public String getCodiceFiscaleUtente() {
        return codiceFiscaleUtente;
    }
    public String getDetails() {
        return details;
    }
}
