package it.web.routex.boundary.cli.extractor;

import javax.servlet.http.HttpServletRequest;

import it.web.routex.boundary.cli.view.MastercardCLI;
import it.web.routex.exception.InvalidCardInputExceptionRemoli;
import it.web.routex.model.record.MastercardRecord;

import java.time.LocalDate;


public final class MastercardExtractorCLI {

    private static String sanitize(String s) {
        return (s == null) ? null : s.trim();
    }


    private MastercardExtractorCLI()
    {
        throw new AssertionError("Classe di estrazione dati, non si creano new");
    }

    public static MastercardRecord from() throws InvalidCardInputExceptionRemoli {

        String rawNumero = MastercardCLI.getNumeroCarta();
        String rawScadenza = MastercardCLI.getScadenza();
        String rawCvv = MastercardCLI.getCvv();

        if (rawNumero == null || rawScadenza == null || rawCvv == null) {
            throw new InvalidCardInputExceptionRemoli(
                    "Dati carta non completi.",
                    "Uno o più parametri della carta sono null.",
                    InvalidCardInputExceptionRemoli.Severity.HIGH
            );
        }

        String numero = sanitize(rawNumero);
        String scadenza = sanitize(rawScadenza);
        String cvv = sanitize(rawCvv);

        if (!numero.matches("^\\d{16}$")) {
            throw new InvalidCardInputExceptionRemoli(
                    "Il numero della carta non è valido.",
                    "Formato non corretto, atteso 16 cifre, ricevuto: " + numero,
                    InvalidCardInputExceptionRemoli.Severity.MEDIUM
            );
        }
        if (!scadenza.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            throw new InvalidCardInputExceptionRemoli(
                    "La data di scadenza non è valida.",
                    "Formato errato, atteso YYYY-MM-DD (HTML5), ricevuto: " + scadenza,
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
