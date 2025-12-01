package Controller.Applicativo;

import Bean.CityBean;
import Bean.PrezzoTotaleBean;
import Model.DAO.CityDAO;
import Model.Domain.City;
import Exception.DAOExceptionRemoli;
import Exception.InvalidPriceCalculationExceptionRemoli;
import Exception.InvalidCityDataExceptionRemoli;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller applicativo responsabile della logica legata alle città.
 * Si occupa sia del recupero delle informazioni dal DAO che del calcolo del prezzo totale.
 * @SimoneRemoli
 */
public class CityController {

    /**
     * Recupera tutte le città dal database e le converte in CityBean per la View.
     */
    public List<CityBean> getAllCities() throws InvalidCityDataExceptionRemoli, DAOExceptionRemoli {

        CityDAO dao = new CityDAO();
        List<City> cities = dao.ListCities();

        // === CONTROLLI DI VALIDAZIONE ===

        if (cities == null) {
            throw new InvalidCityDataExceptionRemoli(
                    "Nessun dato disponibile al momento.",
                    "DAO ha restituito null nella lista delle città.",
                    InvalidCityDataExceptionRemoli.Severity.CRITICAL
            );
        }

        if (cities.isEmpty()) {
            throw new InvalidCityDataExceptionRemoli(
                    "Nessuna città disponibile per l’acquisto al momento.",
                    "La lista delle città è vuota.",
                    InvalidCityDataExceptionRemoli.Severity.MEDIUM
            );
        }

        for (City c : cities) {
            if (c == null || c.getName() == null || c.getName().isBlank()) {
                throw new InvalidCityDataExceptionRemoli(
                        "Sono stati trovati dati città non validi.",
                        "Elemento città nullo o con nome vuoto.",
                        InvalidCityDataExceptionRemoli.Severity.HIGH
                );
            }
        }

        // === COSTRUZIONE BEAN ===
        List<CityBean> cityBeans = new ArrayList<>();
        for (City c : cities) {
            cityBeans.add(new CityBean(c));
        }

        return cityBeans;
    }



    /**
     * Calcola il prezzo totale per l’acquisto di uno o più biglietti
     * in base alla città selezionata e alla quantità indicata.
     *
     * @param city     nome della città selezionata
     * @param quantity quantità di biglietti richiesta
     * @return Bean contenente il prezzo totale
     * @throws DAOExceptionRemoli in caso di errore logico o di accesso ai dati
     */
    public PrezzoTotaleBean ottieni_prezzo_totale(String city, int quantity) throws DAOExceptionRemoli, InvalidPriceCalculationExceptionRemoli {

        if (city == null || city.isEmpty()) {
            throw new InvalidPriceCalculationExceptionRemoli(
                    "La città selezionata non è valida.",
                    "Parametro 'city' nullo o vuoto durante il calcolo del prezzo.",
                    InvalidPriceCalculationExceptionRemoli.Severity.LOW
            );
        }

        if (quantity <= 0) {
            throw new InvalidPriceCalculationExceptionRemoli(
                    "La quantità deve essere maggiore di zero.",
                    "Quantity <= 0 nel calcolo del prezzo. Valore: " + quantity,
                    InvalidPriceCalculationExceptionRemoli.Severity.MEDIUM
            );
        }

        List<City> cities = new CityDAO().ListCities();
        for (City a : cities) {
            if (a.getName().equalsIgnoreCase(city)) {
                double totale = calculate(a.getCosto_biglietto(), quantity);
                return new PrezzoTotaleBean(totale);
            }
        }

        throw new InvalidPriceCalculationExceptionRemoli(
                "La città selezionata non è disponibile nel database.",
                "Nessuna corrispondenza in CityDAO.ListCities() per la city='" + city + "'",
                InvalidPriceCalculationExceptionRemoli.Severity.HIGH
        );
    }

    /**
     * Calcola il prezzo totale in base al costo unitario e alla quantità.
     */
    private double calculate(double costo, int qty) {
        return qty * costo;
    }
}
