package it.web.routex.utility.factory;
import it.web.routex.model.domain.*;

public class CityLifeFactory {
    private CityLifeFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static CityModel createCity(String city) throws IllegalArgumentException {
        switch (city.toLowerCase()) {
            case "rome": return new Rome();
            case "naples": return new Naples();
            case "athens": return new Athens();
            case "budapest": return new Budapest();
            default: throw new IllegalArgumentException("[Errore nella factory] - Unknown city: " + city);
        }
    }
}
