package it.web.routex.extractor;

import it.web.routex.record.BuyTicketRecord;

import javax.servlet.http.HttpServletRequest;
import it.web.routex.exception.InvalidBuyTicketInputExceptionRemoli;

import static it.web.routex.extractor.RouteInputExtractor.sanitize;

public final class BuyTicketExtractor {

    private BuyTicketExtractor()
    {
        throw new AssertionError("Classe di estrazione dati, non si creano new");
    }

    public static BuyTicketRecord from(HttpServletRequest request)
            throws InvalidBuyTicketInputExceptionRemoli {

        String rawCity = request.getParameter("city");
        String rawQuantity = request.getParameter("quantity");

        if (rawCity == null)
            throw new InvalidBuyTicketInputExceptionRemoli(
                    "Campo mancante: città.",
                    "Parametro 'city' è null nel request.",
                    InvalidBuyTicketInputExceptionRemoli.Severity.LOW
            );

        if (rawQuantity == null)
            throw new InvalidBuyTicketInputExceptionRemoli(
                    "Campo mancante: quantità.",
                    "Parametro 'quantity' è null nel request.",
                    InvalidBuyTicketInputExceptionRemoli.Severity.LOW
            );

        String city = sanitize(rawCity);
        String quantity = sanitize(rawQuantity);

        if (city == null || city.isBlank())
            throw new InvalidBuyTicketInputExceptionRemoli(
                    "Il campo città non può essere vuoto.",
                    "City è null o blank dopo sanitizzazione.",
                    InvalidBuyTicketInputExceptionRemoli.Severity.MEDIUM
            );

        if (quantity == null || quantity.isBlank())
            throw new InvalidBuyTicketInputExceptionRemoli(
                    "Il campo quantità non può essere vuoto.",
                    "Quantity è null o blank dopo sanitizzazione.",
                    InvalidBuyTicketInputExceptionRemoli.Severity.MEDIUM
            );

        if (!city.matches("^[A-Za-zÀ-ÖØ-öø-ÿ\\s-]+$"))
            throw new InvalidBuyTicketInputExceptionRemoli(
                    "La città inserita non è valida.",
                    "City contiene caratteri non conformi alla regex.",
                    InvalidBuyTicketInputExceptionRemoli.Severity.MEDIUM
            );

        int quantita;
        try {
            quantita = Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            throw new InvalidBuyTicketInputExceptionRemoli(
                    "La quantità inserita non è un numero valido.",
                    "NumberFormatException parsando '" + quantity + "'",
                    InvalidBuyTicketInputExceptionRemoli.Severity.MEDIUM
            );
        }

        if (quantita <= 0)
            throw new InvalidBuyTicketInputExceptionRemoli(
                    "La quantità deve essere maggiore di zero.",
                    "Quantità <= 0: " + quantita,
                    InvalidBuyTicketInputExceptionRemoli.Severity.MEDIUM
            );

        if (quantita > 10)
            throw new InvalidBuyTicketInputExceptionRemoli(
                    "Puoi acquistare un massimo di 10 biglietti alla volta.",
                    "Quantità troppo grande: " + quantita,
                    InvalidBuyTicketInputExceptionRemoli.Severity.HIGH
            );

        return new BuyTicketRecord(city, quantita);
    }
}
