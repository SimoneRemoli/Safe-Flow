package it.web.routex.extractor;
import javax.servlet.http.HttpServletRequest;
import it.web.routex.exception.InvalidCardInputExceptionRemoli;
import it.web.routex.record.MastercardRecord;

import java.time.LocalDate;

import static it.web.routex.extractor.RouteInputExtractor.sanitize;

public final class MastercardExtractor {

    private MastercardExtractor()
    {
        throw new AssertionError("Classe di estrazione dati, non si creano new");
    }

    public static MastercardRecord from(HttpServletRequest request)
            throws InvalidCardInputExceptionRemoli {

        String rawNumero = request.getParameter("numeroCarta");
        String rawScadenza = request.getParameter("scadenza");
        String rawCvv = request.getParameter("cvv");

        if (rawNumero == null || rawScadenza == null || rawCvv == null) {
            throw new InvalidCardInputExceptionRemoli(
                    "I parametri non sono stati inviati dal form",
                    "I parametri non sono stati inviati dal form",
                    InvalidCardInputExceptionRemoli.Severity.HIGH
            );
        }

        String numero = sanitize(rawNumero);
        String scadenza = sanitize(rawScadenza);
        String cvv = sanitize(rawCvv);

        boolean numeroBlank = numero.isBlank();
        boolean scadenzaBlank = scadenza.isBlank();
        boolean cvvBlank = cvv.isBlank();

        boolean numeroValid = numero.matches("^\\d{16}$");
        boolean scadenzaValid = scadenza.matches("^\\d{4}-\\d{2}-\\d{2}$");
        boolean cvvValid = cvv.matches("^\\d{3}$");

        boolean numeroInvalid = !numeroBlank && !numeroValid;
        boolean scadenzaInvalid = !scadenzaBlank && !scadenzaValid;
        boolean cvvInvalid = !cvvBlank && !cvvValid;

        if (numeroBlank && scadenzaBlank && cvvBlank) {
            throw new InvalidCardInputExceptionRemoli(
                    "Numero carta, scadenza e CVV mancanti.",
                    "numeroCarta blank + scadenza blank + cvv blank.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }
        if (numeroBlank && scadenzaBlank && cvvValid) {
            throw new InvalidCardInputExceptionRemoli(
                    "Numero carta e scadenza mancanti.",
                    "numeroCarta blank + scadenza blank.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (numeroBlank && scadenzaValid && cvvBlank) {
            throw new InvalidCardInputExceptionRemoli(
                    "Numero carta e CVV mancanti.",
                    "numeroCarta blank + cvv blank.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (numeroValid && scadenzaBlank && cvvBlank) {
            throw new InvalidCardInputExceptionRemoli(
                    "Scadenza e CVV mancanti.",
                    "scadenza blank + cvv blank.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }
        if (numeroBlank && scadenzaBlank && cvvInvalid) {
            throw new InvalidCardInputExceptionRemoli(
                    "Numero carta mancante, scadenza mancante e CVV non valido.",
                    "numeroCarta blank + scadenza blank + cvv formato errato.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (numeroBlank && scadenzaInvalid && cvvBlank) {
            throw new InvalidCardInputExceptionRemoli(
                    "Numero carta mancante e CVV mancante; scadenza non valida.",
                    "numeroCarta blank + cvv blank + scadenza formato errato.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (numeroInvalid && scadenzaBlank && cvvBlank) {
            throw new InvalidCardInputExceptionRemoli(
                    "Scadenza e CVV mancanti; numero carta non valido.",
                    "scadenza blank + cvv blank + numeroCarta formato errato.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (numeroBlank && scadenzaValid && cvvValid) {
            throw new InvalidCardInputExceptionRemoli(
                    "Numero carta mancante.",
                    "numeroCarta blank.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (numeroValid && scadenzaBlank && cvvValid) {
            throw new InvalidCardInputExceptionRemoli(
                    "Scadenza mancante.",
                    "scadenza blank.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (numeroValid && scadenzaValid && cvvBlank) {
            throw new InvalidCardInputExceptionRemoli(
                    "CVV mancante.",
                    "cvv blank.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (numeroBlank && scadenzaValid && cvvInvalid) {
            throw new InvalidCardInputExceptionRemoli(
                    "Numero carta mancante e CVV non valido.",
                    "numeroCarta blank + cvv formato errato.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (numeroBlank && scadenzaInvalid && cvvValid) {
            throw new InvalidCardInputExceptionRemoli(
                    "Numero carta mancante e scadenza non valida.",
                    "numeroCarta blank + scadenza formato errato.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (numeroValid && scadenzaBlank && cvvInvalid) {
            throw new InvalidCardInputExceptionRemoli(
                    "Scadenza mancante e CVV non valido.",
                    "scadenza blank + cvv formato errato.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (numeroInvalid && scadenzaBlank && cvvValid) {
            throw new InvalidCardInputExceptionRemoli(
                    "Scadenza mancante e numero carta non valido.",
                    "scadenza blank + numeroCarta formato errato.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (numeroValid && scadenzaInvalid && cvvBlank) {
            throw new InvalidCardInputExceptionRemoli(
                    "CVV mancante e scadenza non valida.",
                    "cvv blank + scadenza formato errato.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (numeroInvalid && scadenzaValid && cvvBlank) {
            throw new InvalidCardInputExceptionRemoli(
                    "CVV mancante e numero carta non valido.",
                    "cvv blank + numeroCarta formato errato.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (numeroBlank && scadenzaInvalid && cvvInvalid) {
            throw new InvalidCardInputExceptionRemoli(
                    "Numero carta mancante, scadenza non valida e CVV non valido.",
                    "numeroCarta blank + scadenza errata + cvv errato.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (numeroInvalid && scadenzaBlank && cvvInvalid) {
            throw new InvalidCardInputExceptionRemoli(
                    "Scadenza mancante, numero carta non valido e CVV non valido.",
                    "scadenza blank + numeroCarta errato + cvv errato.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (numeroInvalid && scadenzaInvalid && cvvBlank) {
            throw new InvalidCardInputExceptionRemoli(
                    "CVV mancante, numero carta non valido e scadenza non valida.",
                    "cvv blank + numeroCarta errato + scadenza errata.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }
        if (numeroInvalid && scadenzaValid && cvvValid) {
            throw new InvalidCardInputExceptionRemoli(
                    "Numero carta non valido.",
                    "numeroCarta errato.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (numeroValid && scadenzaInvalid && cvvValid) {
            throw new InvalidCardInputExceptionRemoli(
                    "Scadenza non valida.",
                    "scadenza errata.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (numeroValid && scadenzaValid && cvvInvalid) {
            throw new InvalidCardInputExceptionRemoli(
                    "CVV non valido.",
                    "cvv errato.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (numeroValid && scadenzaInvalid && cvvInvalid) {
            throw new InvalidCardInputExceptionRemoli(
                    "Scadenza e CVV non validi.",
                    "scadenza errata + cvv errato.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (numeroInvalid && scadenzaValid && cvvInvalid) {
            throw new InvalidCardInputExceptionRemoli(
                    "Numero carta e CVV non validi.",
                    "numeroCarta errato + cvv errato.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (numeroInvalid && scadenzaInvalid && cvvValid) {
            throw new InvalidCardInputExceptionRemoli(
                    "Numero carta e scadenza non validi.",
                    "numeroCarta errato + scadenza errata.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }
        if (numeroInvalid && scadenzaInvalid && cvvInvalid) {
            throw new InvalidCardInputExceptionRemoli(
                    "Numero carta, scadenza e CVV non validi.",
                    "tutti i formati errati.",
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }

        String[] parts = scadenza.split("-");

        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

// Controllo range
        if (month < 1 || month > 12) {
            throw new InvalidCardInputExceptionRemoli(
                    "Mese della carta non valido.",
                    "Mese fuori range: " + month,
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }

        // Controllo scadenza
        LocalDate today = LocalDate.now();
        LocalDate expiry = LocalDate.of(year, month, 1).plusMonths(1).minusDays(1);

        if (expiry.isBefore(today)) {
            throw new InvalidCardInputExceptionRemoli(
                    "La carta è scaduta.",
                    "Data scadenza " + scadenza + " < oggi " + today,
                    InvalidCardInputExceptionRemoli.Severity.HIGH
            );
        }


        if (!cvv.matches("^\\d{3}$")) {
            throw new InvalidCardInputExceptionRemoli(
                    "Il codice CVV non è valido.",
                    "Formato CVV errato: " + cvv,
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }

        return new MastercardRecord(numero, scadenza, cvv);
    }
}
