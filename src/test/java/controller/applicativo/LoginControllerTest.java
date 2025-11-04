package controller.applicativo;

import Bean.AutenticazioneBean;
import Controller.Applicativo.LoginController;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class LoginControllerTest {

    @Test
    void testCostruttoreLoginController() {
        // Arrange: creo un bean con email e password fittizi
        AutenticazioneBean bean = new AutenticazioneBean();

        // Act: creo il controller usando il bean
        LoginController controller = new LoginController(bean);

        // Assert: verifico che l'oggetto non sia nullo
        assertNotNull(controller, "Il controller non dovrebbe essere nullo");
    }
}
