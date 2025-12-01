package Model.Extractor;
import javax.servlet.http.HttpServletRequest;
import Exception.InvalidCardInputExceptionRemoli;
import Model.Record.MastercardRecord;

import java.time.LocalDate;

import static Model.Extractor.RouteInputExtractor.sanitize;

public class MastercardExtractor {

    public static MastercardRecord from(HttpServletRequest request)
            throws InvalidCardInputExceptionRemoli {

        String rawNumero = request.getParameter("numeroCarta");
        String rawScadenza = request.getParameter("scadenza");
        String rawCvv = request.getParameter("cvv");

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
