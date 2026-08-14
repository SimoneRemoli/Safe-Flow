package it.web.safeflow.controller.applicativo;
import it.web.safeflow.bean.CityBean;
import it.web.safeflow.dao.LayerPersistenza;
import it.web.safeflow.model.City;
import it.web.safeflow.exception.DAOExceptionRemoli;
import it.web.safeflow.exception.InvalidCityDataExceptionRemoli;
import it.web.safeflow.utility.factory.FactoryLayerPersistenza;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller applicativo responsabile della logica legata alle città.
 * Si occupa del recupero delle informazioni dal DAO e le espone al layer grafico.
 * @SimoneRemoli!
 */
public class CityController {

    /**
     * Recupera tutte le città dal database e le converte in CityBean per la View.
     */
    public List<CityBean> getAllCities() throws InvalidCityDataExceptionRemoli, DAOExceptionRemoli {

        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        List<City> cities = layer.listCitiesRAM(); // ACCESSO DB

        // === CONTROLLI DI VALIDAZIONE ===

        // NON CARICATO
        if (cities == null) {
            throw new InvalidCityDataExceptionRemoli(
                    "Nessun dato disponibile al momento.",
                    "DAO ha restituito null nella lista delle città.",
                    InvalidCityDataExceptionRemoli.Severity.CRITICAL
            );
        }
        // CARICATO MA NESSUN DATO []
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

}
