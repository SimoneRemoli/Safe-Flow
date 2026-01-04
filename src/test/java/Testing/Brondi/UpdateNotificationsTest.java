package Testing.Brondi;

import it.web.routex.controller.applicativo.UpdateNotificationsControllerApplicativo;
import it.web.routex.enumerator.Ruolo;
import it.web.routex.utility.factory.ConnectionFactory;
import org.junit.jupiter.api.Test;

import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * ------------------------------------------------------------
 * Test Class : <UpdateNotificationsTest>
 * Author     : Lorenzo Brondi
 * Description: Test dell'aggiornamento stato notifiche (segnale come risolte).
 * ------------------------------------------------------------
 */
class UpdateNotificationsTest {

    private static final ResourceBundle RB = ResourceBundle.getBundle("configurations/testpaths");

    @Test
    void TestAggiornaNotifiche() {

        // 1. Setup Ruolo (Admin è colui che risolve le notifiche)
        try {
            ConnectionFactory.cambioDiRuolo(Ruolo.ADMIN);
        } catch (Exception e) {
            try { ConnectionFactory.cambioDiRuolo(Ruolo.TRAVELER); } catch (Exception ignored) {}
        }

        // 2. Preparazione Dati
        // Il controller si aspetta il formato: "timestamp|messaggio"
        // Generiamo un timestamp attuale per simulare un dato valido
        long timestamp = System.currentTimeMillis();
        String messageBody = RB.getString("msgDaRisolvere");

        // Costruiamo la stringa combinata
        String inputCombinato = timestamp + "|" + messageBody;

        // Creiamo l'array che il controller si aspetta
        String[] inputArr = new String[] { inputCombinato };

        // 3. Esecuzione
        UpdateNotificationsControllerApplicativo controller = new UpdateNotificationsControllerApplicativo();

        // 4. Verifica
        // Verifichiamo che l'operazione non lanci eccezioni (es. NumberFormatException o DAOException)
        assertDoesNotThrow(() -> controller.aggiornaStatoNotifiche(inputArr));
    }

    @Test
    void TestInputNull() {
        // Test per verificare che il controller gestisca array nulli senza crashare
        UpdateNotificationsControllerApplicativo controller = new UpdateNotificationsControllerApplicativo();

        assertDoesNotThrow(() -> controller.aggiornaStatoNotifiche(null));
    }
}