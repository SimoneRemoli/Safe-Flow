package controller.applicativo;

import Bean.TicketBean;
import Controller.Applicativo.AreaRiservata;
import Model.Domain.Ruolo;
import Model.Domain.TypesOfPersistenceLayer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utility.Decorator.DecoratorTicket.BaseTicketCode;
import utility.Decorator.DecoratorTicket.CittaDecorator;
import utility.Decorator.DecoratorTicket.Component;
import utility.Decorator.DecoratorTicket.TimestampDecorator;
import utility.Factory.ConnectionFactory;
import utility.Singleton.PersistenceMode;
import Exception.PathNotFoundExceptionRemoli;

import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)

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
        PersistenceMode.getInstance().setTipo(TypesOfPersistenceLayer.JDBC);
        AreaRiservata reserved = new AreaRiservata();
        List<TicketBean> tickets = reserved.runTicket(codiceFiscale);
        assertTrue(tickets.size() > 0);
    }
    @ParameterizedTest
    @MethodSource("invalidTicketCFProvider")
    void checkTicketErrorCF(String codiceFiscale) throws Exception {

        ConnectionFactory.Cambio_Di_Ruolo(Ruolo.TRAVELER);
        PersistenceMode.getInstance().setTipo(TypesOfPersistenceLayer.JDBC);
        AreaRiservata reserved = new AreaRiservata();
        assertThrows(PathNotFoundExceptionRemoli.class, () -> {
            reserved.runTicket(codiceFiscale);
        });
    }
}
