package it.web.routex.boundary.cli.extractor;

import it.web.routex.boundary.cli.view.PaypalCLI;
import it.web.routex.validator.PaypalValidator;
import it.web.routex.record.PaypalRecord;
import it.web.routex.exception.InvalidCardInputExceptionRemoli;

public final class PaypalExtractorCLI {

    private static String sanitize(String s) {
        return (s == null) ? null : s.trim();
    }

    private PaypalExtractorCLI()
    {
        throw new AssertionError("Classe di estrazione dati, non si creano new");
    }

    public static PaypalRecord from() throws InvalidCardInputExceptionRemoli {

        String email = sanitize(PaypalCLI.getEmailPaypal());
        String codice = sanitize(PaypalCLI.getCodiceTransazione());

        PaypalValidator.validate(email, codice);

        return new PaypalRecord(email, codice);
    }





}
