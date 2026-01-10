package it.web.routex.boundary.cli.extractor;

import it.web.routex.boundary.cli.view.MastercardCLI;
import it.web.routex.exception.InvalidCardInputExceptionRemoli;
import it.web.routex.extractor.MastercardValidator;
import it.web.routex.record.MastercardRecord;

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

        String numero = sanitize(MastercardCLI.getNumeroCarta());
        String scadenza = sanitize(MastercardCLI.getScadenza());
        String cvv = sanitize(MastercardCLI.getCvv());

        MastercardValidator.validate(numero, scadenza, cvv);

        return new MastercardRecord(numero, scadenza, cvv);
    }

}
