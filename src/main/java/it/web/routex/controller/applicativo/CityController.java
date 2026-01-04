package it.web.routex.controller.applicativo;

import it.web.routex.bean.CityBean;
import it.web.routex.bean.PrezzoTotaleBean;
import it.web.routex.dao.CityDAO;
import it.web.routex.model.City;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.exception.InvalidPriceCalculationExceptionRemoli;
import it.web.routex.exception.InvalidCityDataExceptionRemoli;

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
        List<City> cities = dao.listCities();

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
            if (c == null || !c.isValid()) {
                throw new InvalidCityDataExceptionRemoli(
                        "Sono stati trovati dati città non validi.",
                        "Oggetto City non valido secondo il dominio.",
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
    public PrezzoTotaleBean ottieniPrezzoTotale(String city, int quantity) throws DAOExceptionRemoli, InvalidPriceCalculationExceptionRemoli {

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

        List<City> cities = new CityDAO().listCities();
        for (City a : cities) {
            if (a.getName().equalsIgnoreCase(city)) {
                double totale = a.calcolaPrezzoTotale(quantity);
                return new PrezzoTotaleBean(totale);
            }
        }

        throw new InvalidPriceCalculationExceptionRemoli(
                "La città selezionata non è disponibile nel database.",
                "Nessuna corrispondenza in CityDAO.ListCities() per la city='" + city + "'",
                InvalidPriceCalculationExceptionRemoli.Severity.HIGH
        );
    }
}
