package Testing.Brondi;

import it.web.routex.bean.RouteBean;
import it.web.routex.bean.TicketBean;
import it.web.routex.controller.applicativo.AreaRiservata;
import it.web.routex.enumerator.Ruolo;
import it.web.routex.utility.factory.ConnectionFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * ------------------------------------------------------------
 * Test Class : <AreaRiservataTest>
 * Author     : Lorenzo Brondi
 * Description: Test del controller AreaRiservata.
 * Verifica il recupero di Ticket e Percorsi tramite CF specifico.
 * ------------------------------------------------------------
 */
class AreaRiservataTest {

    private static final ResourceBundle RB = ResourceBundle.getBundle("configurations/testpaths");

    static Stream<String> cfProvider() {
        String raw = RB.getString("areaRiservataCF");
        return Stream.of(raw.split(":"));
    }

    @ParameterizedTest
    @MethodSource("cfProvider")
    void TestRunTicket(String cf) throws Exception {

        ConnectionFactory.cambioDiRuolo(Ruolo.TRAVELER);

        System.out.println("Testing Ticket per CF: " + cf);

        AreaRiservata controller = new AreaRiservata();
        List<TicketBean> tickets = controller.runTicket(cf);

        assertNotNull(tickets, "La lista dei ticket non deve essere null");
        System.out.println("-> Ticket trovati: " + tickets.size());
    }

    @ParameterizedTest
    @MethodSource("cfProvider")
    void TestRunPath(String cf) throws Exception {

        ConnectionFactory.cambioDiRuolo(Ruolo.TRAVELER);

        System.out.println("Testing Percorsi per CF: " + cf);

        AreaRiservata controller = new AreaRiservata();
        List<RouteBean> percorsi = controller.runPath(cf);

        assertNotNull(percorsi, "La lista dei percorsi non deve essere null");
        System.out.println("-> Percorsi trovati: " + percorsi.size());
    }
}