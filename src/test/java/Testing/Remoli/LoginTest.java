package Testing.Remoli;

import Bean.AutenticazioneBean;
import Bean.UtenteBeanGenerico;
import Controller.Applicativo.LoginController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utility.Singleton.Credentials;
import Exception.DAOExceptionRemoli;
import Exception.LoginNotFoundRemoli;

import java.util.ResourceBundle;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest
{
    private static final ResourceBundle RB = ResourceBundle.getBundle("configurations/testpaths");
    static Stream<String> invalidCredentialsProvider() {
        return Stream.of(RB.getString("invalidCredentials"));
    }
    static Stream<String> validCredentialsProvider() {
        return Stream.of
                (RB.getString("validCredentials1"),
                RB.getString("validCredentials2"));
    }
    @ParameterizedTest
    @MethodSource("invalidCredentialsProvider")
    void testLoginInvalidCredentials(String strings) {

        String[] parts = strings.split(":");
        AutenticazioneBean credenziali = new AutenticazioneBean();
        credenziali.setEmail(parts[0]);
        credenziali.setPassword(parts[1]);
        LoginController loginController = new LoginController(credenziali);
        assertThrows(LoginNotFoundRemoli.class, () -> {
            loginController.autenticaUtente();
        });
    }
    @ParameterizedTest
    @MethodSource("validCredentialsProvider")
    void testLoginValidCredentials(String strings) throws DAOExceptionRemoli, LoginNotFoundRemoli {

        String[] parts = strings.split(":");
        AutenticazioneBean credenziali = new AutenticazioneBean();
        credenziali.setEmail(parts[0]);
        credenziali.setPassword(parts[1]);
        LoginController loginController = new LoginController(credenziali);
        UtenteBeanGenerico ut = loginController.autenticaUtente();
        assertTrue(ut != null);
    }
}
