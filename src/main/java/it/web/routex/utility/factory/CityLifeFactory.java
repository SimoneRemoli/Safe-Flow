package it.web.routex.utility.factory;

import it.web.routex.controller.applicativo.CityLifeController;
import it.web.routex.model.domain.Athens;
import it.web.routex.model.domain.Budapest;
import it.web.routex.model.domain.Naples;
import it.web.routex.model.domain.Rome;

public class CityLifeFactory {
    private CityLifeFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static CityLifeController createCity(String city) throws IllegalArgumentException {
        switch (city.toLowerCase()) {
            case "rome": return new Rome();
            case "naples": return new Naples();
            case "athens": return new Athens();
            case "budapest": return new Budapest();
            default: throw new IllegalArgumentException("[Errore nella factory] - Unknown city: " + city);
        }
    }
}
