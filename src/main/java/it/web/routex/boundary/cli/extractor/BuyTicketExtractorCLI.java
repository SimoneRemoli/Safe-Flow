package it.web.routex.boundary.cli.extractor;


import it.web.routex.extractor.BuyTicketValidator;
import it.web.routex.record.BuyTicketRecord;

import it.web.routex.exception.InvalidBuyTicketInputExceptionRemoli;


public final class BuyTicketExtractorCLI {

    private static String sanitize(String s) {
        return (s == null) ? null : s.trim();
    }

    private BuyTicketExtractorCLI()
    {
        throw new AssertionError("Classe di estrazione dati, non si creano new");
    }

    public static BuyTicketRecord from(String cityy, String quantityy)
            throws InvalidBuyTicketInputExceptionRemoli {

        String city = sanitize(cityy);
        String quantity = sanitize(quantityy);

        int quantita = BuyTicketValidator.validate(city, quantity);

        return new BuyTicketRecord(city, quantita);
    }

}

