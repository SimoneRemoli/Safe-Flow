package Testing.Brondi;

import it.web.routex.bean.MessageBean;
import it.web.routex.controller.applicativo.ConfirmCommunicationControllerApplicativo;
import it.web.routex.enumerator.Ruolo;
import it.web.routex.utility.factory.ConnectionFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * ------------------------------------------------------------
 * Test Class : <SendCommunicationTest>
 * Author     : Lorenzo Brondi
 * Description: Test della classe ConfirmCommunicationControllerApplicativo.
 * Verifica l'invio corretto di messaggi nel DB.
 * ------------------------------------------------------------
 */
class SendCommunicationTest {

    private static final ResourceBundle RB = ResourceBundle.getBundle("configurations/testpaths");

    // Recupera le stringhe "message1" e "message2" definite in testpaths.properties
    static Stream<String> messageProvider() {
        return Stream.of(
                RB.getString("message1"),
                RB.getString("message2")
        );
    }

    @ParameterizedTest
    @MethodSource("messageProvider")
    void TestCommunication(String s) throws Exception {

        ConnectionFactory.cambioDiRuolo(Ruolo.ADMIN);

        String[] parts = s.split(":");
        String messageBody = parts[1];

        MessageBean messageBean = new MessageBean();
        messageBean.setMessage(messageBody);
        messageBean.setDate(new Timestamp(System.currentTimeMillis()));
        messageBean.setRisolto(false);

        ConfirmCommunicationControllerApplicativo controller =
                new ConfirmCommunicationControllerApplicativo();

        assertDoesNotThrow(() ->
                controller.communication(messageBean)
        );
    }

}