package Testing.Remoli;

import Bean.CityBean;
import Bean.PrezzoTotaleBean;
import Controller.Applicativo.CityController;
import Model.Domain.Ruolo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utility.Factory.ConnectionFactory;
import Exception.DAOExceptionRemoli;

import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ------------------------------------------------------------
 *  Test Class : <CityTest>
 *  Author     : Simone Remoli
 *  Description: Test della classe CityController, in particolare
 *               del metodo ottieni_prezzo_totale e getAllCities.
 * ------------------------------------------------------------
 */

public class CityTest {

    private static final ResourceBundle RB = ResourceBundle.getBundle("configurations/testpaths");
    static Stream<String> prezzoTotaleProvider() {
        return Stream.of(
                RB.getString("prezzo1"),
                RB.getString("prezzo3")
        );
    }
    @ParameterizedTest
    @MethodSource("prezzoTotaleProvider")
    void TestPrezzoTotale(String s) throws Exception {

        ConnectionFactory.Cambio_Di_Ruolo(Ruolo.TRAVELER);

        String[] parts = s.split(":");
        String city = parts[0];
        String quantita = parts[1];
        double expected = Double.parseDouble(parts[2]);

        CityController cityController = new CityController();
        PrezzoTotaleBean prezzo = cityController.ottieni_prezzo_totale(city, Integer.parseInt(quantita));

        assertEquals(expected, prezzo.getPrezzo_totale());
    }
    @Test
    void OttieniCity() throws DAOExceptionRemoli, SQLException {

        ConnectionFactory.Cambio_Di_Ruolo(Ruolo.TRAVELER);
        CityController cityController = new CityController();
        List<CityBean> city = cityController.getAllCities();
        assertTrue(city.size() > 0);

    }
}
