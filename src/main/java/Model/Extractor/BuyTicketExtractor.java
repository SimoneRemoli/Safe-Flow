package Model.Extractor;

import Model.Record.BuyTicketRecord;

import javax.servlet.http.HttpServletRequest;
import Exception.DAOExceptionRemoli;

import static Model.Extractor.RouteInputExtractor.sanitize;

public class BuyTicketExtractor {

    public static BuyTicketRecord from(HttpServletRequest request) throws DAOExceptionRemoli {

        String rawCity = request.getParameter("city");
        String rawQuantity = request.getParameter("quantity");

        if (rawCity == null)
            throw new DAOExceptionRemoli("Parametro mancante: city.");

        if (rawQuantity == null)
            throw new DAOExceptionRemoli("Parametro mancante: quantity.");

        String city = sanitize(rawCity);
        String quantity = sanitize(rawQuantity);

        if (city == null || city.isBlank())
            throw new DAOExceptionRemoli("Il campo city è vuoto o non valido.");

        if (quantity == null || quantity.isBlank())
            throw new DAOExceptionRemoli("Il campo quantità è vuoto o non valido.");

        if (!city.matches("^[A-Za-zÀ-ÖØ-öø-ÿ\\s-]+$"))
            throw new DAOExceptionRemoli("Il nome della città contiene caratteri non consentiti.");

        int quantita;
        try {
            quantita = Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            throw new DAOExceptionRemoli("La quantità inserita non è un numero valido.");
        }

        if (quantita <= 0)
            throw new DAOExceptionRemoli("La quantità deve essere un numero positivo.");

        if (quantita > 10)
            throw new DAOExceptionRemoli("Quantità troppo elevata per un singolo acquisto.");

        return new BuyTicketRecord(
                city,
                quantita
        );
    }
}
