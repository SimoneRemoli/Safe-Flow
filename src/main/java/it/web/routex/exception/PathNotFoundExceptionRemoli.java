package it.web.routex.exception;

public class PathNotFoundExceptionRemoli extends Exception {

    String codice_fiscale_utente;
    int codice_di_errore;
    String details;

    public PathNotFoundExceptionRemoli(String message, String codice_fiscale_utente, int codice_di_errore, String details) {
        super(message);
        this.codice_fiscale_utente = codice_fiscale_utente;
        this.codice_di_errore = codice_di_errore;
        this.details = details;
    }

    public int getCodice_di_errore() {
        return codice_di_errore;
    }

    public String getCodice_fiscale_utente() {
        return codice_fiscale_utente;
    }
    public String getDetails() {
        return details;
    }
}
