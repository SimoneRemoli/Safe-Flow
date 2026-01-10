package it.web.routex.validator;

import it.web.routex.exception.InvalidBuyTicketInputExceptionRemoli;

public final class BuyTicketValidator {

    private BuyTicketValidator() {}

    public static int validate(String city, String quantity) throws InvalidBuyTicketInputExceptionRemoli {

        if (city == null || city.isBlank())
            error("Il campo città non può essere vuoto.", InvalidBuyTicketInputExceptionRemoli.Severity.MEDIUM);

        if (quantity == null || quantity.isBlank())
            error("Il campo quantità non può essere vuoto.", InvalidBuyTicketInputExceptionRemoli.Severity.MEDIUM);

        if (!city.matches("^[A-Za-zÀ-ÖØ-öø-ÿ\\s-]+$"))
            error("La città inserita non è valida.", InvalidBuyTicketInputExceptionRemoli.Severity.MEDIUM);

        int quantita;
        try {
            quantita = Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            error("La quantità inserita non è un numero valido.", InvalidBuyTicketInputExceptionRemoli.Severity.MEDIUM);
            return -1;
        }

        if (quantita <= 0)
            error("La quantità deve essere maggiore di zero.", InvalidBuyTicketInputExceptionRemoli.Severity.MEDIUM);

        if (quantita > 10)
            error("Puoi acquistare un massimo di 10 biglietti alla volta.", InvalidBuyTicketInputExceptionRemoli.Severity.HIGH);

        return quantita;
    }

    private static void error(String msg, InvalidBuyTicketInputExceptionRemoli.Severity severity) throws InvalidBuyTicketInputExceptionRemoli {
        throw new InvalidBuyTicketInputExceptionRemoli(msg, msg, severity);
    }
}