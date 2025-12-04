package Testing.Remoli;

import it.web.routex.bean.TicketBean;
import it.web.routex.controller.applicativo.AreaRiservata;
import it.web.routex.model.domain.Ruolo;
import it.web.routex.model.domain.TypesOfPersistenceLayer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import it.web.routex.utility.Decorator.DecoratorTicket.BaseTicketCode;
import it.web.routex.utility.Decorator.DecoratorTicket.CittaDecorator;
import it.web.routex.utility.Decorator.DecoratorTicket.Component;
import it.web.routex.utility.Decorator.DecoratorTicket.TimestampDecorator;
import it.web.routex.utility.Factory.ConnectionFactory;
import it.web.routex.utility.Singleton.PersistenceMode;
import it.web.routex.exception.PathNotFoundExceptionRemoli;

import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
/**
 * ------------------------------------------------------------
 *  Test Class : <TicketTest>
 *  Author     : Simone Remoli
 *  Description: Test della generazione del codice del ticket
 * ------------------------------------------------------------
 */
public class TicketTest {
    private static final ResourceBundle RB = ResourceBundle.getBundle("configurations/testpaths");
    static Stream<String> ticketCFProvider() {
        return Stream.of(RB.getString("ticketCF").split(":"));
    }
    static Stream<String> ticketGeneratingProvider() {
        return Stream.of(RB.getString("ticketgen").split(":"));
    }
    static Stream<String> timestampProvider() {
        return Stream.of(RB.getString("timestamp").split(":"));
    }
    static Stream<String> invalidTicketCFProvider() {
        return Stream.of(RB.getString("invalidTicketCF").split(":"));
    }
    @ParameterizedTest
    @MethodSource("ticketGeneratingProvider")
    void TestingGeneratingTicket(String city) {

        Component gen = new TimestampDecorator(new CittaDecorator(new BaseTicketCode(), city));
        String ticket = gen.genera();
        assertTrue(ticket.contains(city.toUpperCase()));
    }
    @ParameterizedTest
    @MethodSource("timestampProvider")
    void TestingTimeStamp(String city) {

        Component gen = new TimestampDecorator(new CittaDecorator(new BaseTicketCode(), city));
        String ticket = gen.genera();
        String[] st = ticket.split("-");
        String timestamp = st[0];
        assertEquals(13, timestamp.length());   // Deve essere lungo 13
    }
    @ParameterizedTest
    @MethodSource("ticketCFProvider")
    void checkticket(String codiceFiscale) throws Exception {

        ConnectionFactory.Cambio_Di_Ruolo(Ruolo.TRAVELER);
        PersistenceMode.getSingletonInstance().setTipo(TypesOfPersistenceLayer.JDBC);
        AreaRiservata reserved = new AreaRiservata();
        List<TicketBean> tickets = reserved.runTicket(codiceFiscale);
        assertTrue(tickets.size() > 0);
    }
    @ParameterizedTest
    @MethodSource("invalidTicketCFProvider")
    void checkTicketErrorCF(String codiceFiscale) throws Exception {

        ConnectionFactory.Cambio_Di_Ruolo(Ruolo.TRAVELER);
        PersistenceMode.getSingletonInstance().setTipo(TypesOfPersistenceLayer.JDBC);
        AreaRiservata reserved = new AreaRiservata();
        assertThrows(PathNotFoundExceptionRemoli.class, () -> {
            reserved.runTicket(codiceFiscale);
        });
    }
}
