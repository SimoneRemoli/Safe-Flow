package Testing.Brondi;

import it.web.routex.bean.WorkerScheduleBean;
import it.web.routex.controller.applicativo.ViewWorkScheduleControllerApplicativo;
import it.web.routex.enumerator.Ruolo;
import it.web.routex.utility.factory.ConnectionFactory;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * ------------------------------------------------------------
 * Test Class : <ViewWorkScheduleTest>
 * Author     : Lorenzo Brondi
 * Description: Test per il recupero dei turni di lavoro.
 * ------------------------------------------------------------
 */
class ViewWorkScheduleTest {

    private static final ResourceBundle RB = ResourceBundle.getBundle("configurations/testpaths");

    @Test
    void TestGetSchedule() throws SQLException {

        ConnectionFactory.cambioDiRuolo(Ruolo.WORKER);

        String workerCf = RB.getString("workerCF");
        System.out.println("Testing Schedule per Worker CF: " + workerCf);

        ViewWorkScheduleControllerApplicativo controller = new ViewWorkScheduleControllerApplicativo();

        assertDoesNotThrow(() -> {
            WorkerScheduleBean schedule = controller.getSchedule(workerCf);
            assertNotNull(schedule, "Il bean dei turni non dovrebbe essere null");
            System.out.println("Turni recuperati correttamente.");
        });
    }
}