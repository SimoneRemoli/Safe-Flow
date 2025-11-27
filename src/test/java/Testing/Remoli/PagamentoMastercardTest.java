package Testing.Remoli;

import Controller.Applicativo.PagamentoMastercard;
import Model.Domain.Ruolo;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utility.Factory.ConnectionFactory;
import utility.Singleton.Credentials;
import Exception.DAOExceptionRemoli;
import Exception.CredentialsExceptionRemoli;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.MethodName.class)

/**
 * ------------------------------------------------------------
 *  Test Class : <PagamentoMastercardTest>
 *  Author     : Simone Remoli
 *  Description: Test della classe PagamentoMastercard
 * ------------------------------------------------------------
 */

public class PagamentoMastercardTest
{
    private static final ResourceBundle RB = ResourceBundle.getBundle("configurations/testpaths");
    static Stream<String> Permission(){
        return Stream.of(RB.getString("permission"));
    }
    static Stream<String> permessiGiustiProvider(){
        return Stream.of(RB.getString("permessiOK"));
    }
    static Stream<String> noUserProvider() {
        return Stream.of(RB.getString("noUser"));
    }
    @ParameterizedTest
    @MethodSource("Permission")
    void TestingPermessiSbagliati(String strings){

        String[] parts = strings.split(":");
        double tot = Double.parseDouble(parts[3]);
        int quantitativo = Integer.parseInt(parts[4]);
        PagamentoMastercard pagamento = new PagamentoMastercard(parts[0], parts[1], parts[2], null, tot, quantitativo, parts[5]);

        assertThrows(DAOExceptionRemoli.class, () -> //l'utente login_user non ha i permessi sul db per una stored procedure
        {
            pagamento.run();
        });
    }
    @ParameterizedTest
    @MethodSource("permessiGiustiProvider")
    void TestingPermessiGiusti(String s) throws Exception {

        String[] p = s.split(":");

        ConnectionFactory.Cambio_Di_Ruolo(Ruolo.TRAVELER);

        Credentials cred = new Credentials();
        cred.setNome(p[0]);
        cred.setCognome(p[1]);
        cred.setCodiceFiscale(p[2]);
        cred.setDisabile(Boolean.parseBoolean(p[3]));
        cred.setEmail(p[4]);
        cred.setPassword(p[5]);
        cred.setRuolo(Ruolo.TRAVELER);

        PagamentoMastercard pagamento = new PagamentoMastercard(
                p[6], p[7], p[8], cred,
                Double.parseDouble(p[9]),
                Integer.parseInt(p[10]),
                p[11]
        );

        assertEquals(2, pagamento.run().size());
    }
    @ParameterizedTest
    @MethodSource("noUserProvider")
    void NoUserLoggedPayment(String s) throws Exception {

        ConnectionFactory.Cambio_Di_Ruolo(Ruolo.TRAVELER);

        String[] p = s.split(":");

        Credentials cred = p[3].equals("null") ? null : new Credentials();

        PagamentoMastercard pagamento = new PagamentoMastercard(
                p[0],       // numero carta
                p[1],       // scadenza
                p[2],       // cvv
                cred,       // CREDENZIALI NULLE
                Double.parseDouble(p[4]),
                Integer.parseInt(p[5]),
                p[6]
        );
        assertThrows(CredentialsExceptionRemoli.class, pagamento::run);
    }
}
