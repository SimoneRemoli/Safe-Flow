package controller.applicativo;

import Bean.InformazioniPercorsoBean;
import Bean.RouteBean;
import Controller.Applicativo.AreaRiservata;
import Controller.Applicativo.PathController;
import Model.Domain.Ruolo;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import Exception.DAOExceptionRemoli;
import utility.Factory.ConnectionFactory;

import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class PercorsoTest
{
    private static final ResourceBundle RB = ResourceBundle.getBundle("configurations/testpaths");

    static Stream<String> validPathsProvider() {
        return Stream.of(
                RB.getString("valid1"),
                RB.getString("valid2")
        );
        /*
        Stream.of("Ottaviano:Quintiliani:Rome", "Spagna:Anagnina:Rome")
         */
    }
    static Stream<String> pathCFProvider() {
        return Stream.of(RB.getString("pathsCF").split(":"));
    }
    static Stream<String> invalidPathsProvider() {
        return Stream.of(
                RB.getString("invalid1"),
                RB.getString("invalid2")
        );
    }
    @ParameterizedTest
    @MethodSource("validPathsProvider")
    /*
    Chiamo il metodo validPathsProvider();
    Prendo ogni valore dello stream
    e invoco TestPath(String) con uno alla volta.
     */
    void TestPath(String strings) throws Exception {

        String[] parts = strings.split(":");
        PathController path = new PathController();
        InformazioniPercorsoBean dto = path.run(parts[0], parts[1], parts[2]);
        int stazioniTotali = dto.getCityLife().getNumero_stazioni_totali();
        assertTrue(stazioniTotali > 0);

    }
    @ParameterizedTest
    @MethodSource("invalidPathsProvider")
    void TestExceptionPath(String strings) {

        String[] parts = strings.split(":");
        PathController path = new PathController();
        assertThrows(DAOExceptionRemoli.class, () ->
        {
            path.run(parts[0], parts[1], parts[2]);
        });
    }
    @ParameterizedTest
    @MethodSource("pathCFProvider")
    void CheckListaPercorsi(String codiceFiscale) throws Exception {

        ConnectionFactory.Cambio_Di_Ruolo(Ruolo.TRAVELER);
        AreaRiservata reserved = new AreaRiservata();
        List<RouteBean> listaPercorsi = reserved.runPath(codiceFiscale);
        assertTrue(listaPercorsi.size() > 0);
    }
}
