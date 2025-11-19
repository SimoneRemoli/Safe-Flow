package utility.Factory;

import Controller.Applicativo.CityLifeController;
import Model.Domain.*;

public class CityLifeFactory {

    public static CityLifeController createCity(String city) throws Exception {
        switch (city.toLowerCase()) {
            case "rome": return new Rome();
            case "milan": return new Milan();
            case "naples": return new Naples();
            case "athens": return new Athens();
            case "budapest": return new Budapest();
            case "stockholm": return new Stockholm();
            default: throw new IllegalArgumentException("Unknown city: " + city);
        }
    }
}
