package utility.Factory;

import Controller.Applicativo.CityLifeController;
import Model.Domain.*;

public class CityLifeFactory {

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
