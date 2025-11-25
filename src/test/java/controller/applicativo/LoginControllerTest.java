package controller.applicativo;
import Bean.PrezzoTotaleBean;
import Bean.UtenteBeanGenerico;
import Controller.Applicativo.CityController;
import Exception.PaymentValidationExceptionRemoli;
import Exception.CredentialsExceptionRemoli;
import Bean.InformazioniPercorsoBean;
import Controller.Applicativo.PagamentoMastercard;
import Controller.Applicativo.PathController;
import Model.DAO.TicketDAODB;
import Model.DAO.TicketDAOFile;
import Model.DAO.TicketDAOLayer;
import Model.Domain.Ruolo;
import Model.Domain.TypesOfPersistenceLayer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utility.Factory.ConnectionFactory;
import utility.Factory.FactoryPersistence;
import utility.Singleton.Credentials;
import utility.Singleton.PersistenceMode;
import Exception.DAOExceptionRemoli;

import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
class LoginControllerTest {

    private static final ResourceBundle RB = ResourceBundle.getBundle("configurations/testpaths");

    static {
        System.out.println("La classe di Test viene caricata dalla JVM");
    }

    static Stream<String> persistenceJDBC(){
        return Stream.of(RB.getString("persistenzaJDBC"));
    }

    static Stream<String> persistenceFile(){
        return Stream.of(RB.getString("persistenzafile"));
    }

    static Stream<String> validPathsProvider() {
        return Stream.of(
                RB.getString("valid1"),
                RB.getString("valid2")
        );
        /*
        Stream.of("Ottaviano:Quintiliani:Rome", "Spagna:Anagnina:Rome")
         */
    }

    static Stream<String> invalidPathsProvider() {
        return Stream.of(
                RB.getString("invalid1"),
                RB.getString("invalid2")
        );
    }

    @ParameterizedTest
    @MethodSource("persistenceJDBC")
    void TestPersistenzaDB(String strings)
    {
        TypesOfPersistenceLayer type = TypesOfPersistenceLayer.valueOf(strings);
        PersistenceMode.getInstance().setTipo(type);
        TicketDAOLayer dao = FactoryPersistence.createTicketDAO();
        assertTrue(dao instanceof TicketDAODB);
    }
    @ParameterizedTest
    @MethodSource("persistenceFile")
    void TestPersistenzaFile(String strings)
    {
        TypesOfPersistenceLayer type = TypesOfPersistenceLayer.valueOf(strings);
        PersistenceMode.getInstance().setTipo(type);
        TicketDAOLayer dao = FactoryPersistence.createTicketDAO();
        assertTrue(dao instanceof TicketDAOFile);
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

    @Test
    void TestingPermessiSbagliati(){

        PagamentoMastercard pagamento = new PagamentoMastercard("1234567890123456", "12/25", "123", null, 10.0, 2, "Rome");

        assertThrows(DAOExceptionRemoli.class, () -> //l'utente login_user non ha i permessi sul db per una stored procedure
        {
           pagamento.run();
        });
    }

    @Test
    void TestingPermessiGiusti() throws DAOExceptionRemoli, PaymentValidationExceptionRemoli, CredentialsExceptionRemoli, SQLException {
        // Implementa il test per PagamentoMastercard qui

        ConnectionFactory.Cambio_Di_Ruolo(Ruolo.TRAVELER);

        Credentials cred = new Credentials();
        cred.setNome("Giulio");
        cred.setCognome("Andreotti");
        cred.setCodiceFiscale("BCCSLL98");
        cred.setDisabile(true);
        cred.setRuolo(Ruolo.TRAVELER);
        cred.setEmail("andreotti@gmail.com");
        cred.setPassword("mammina");

        PagamentoMastercard pagamento = new PagamentoMastercard("4539456721894321", "2027-03-01", "248", cred, 10.0, 2, "Rome");
        assertEquals(pagamento.run().size(), 2);
    }


    @Test
    void NoUserLoggedPayment() throws SQLException, DAOExceptionRemoli, PaymentValidationExceptionRemoli, CredentialsExceptionRemoli {
        ConnectionFactory.Cambio_Di_Ruolo(Ruolo.TRAVELER);
        PagamentoMastercard pagamento = new PagamentoMastercard("4539456721894321", "2027-03-01", "248", null, 10.0, 2, "Rome");
        assertThrows(CredentialsExceptionRemoli.class, () -> //l'utente login_user non ha i permessi sul db per una stored procedure
        {
            pagamento.run();
        });

        //Exception.CredentialsExceptionRemoli: Nessun utente loggato associato al pagamento.
    }

    @Test
    void TestPrezzoTotale() throws DAOExceptionRemoli,SQLException {

        ConnectionFactory.Cambio_Di_Ruolo(Ruolo.TRAVELER);
        CityController cityController = new CityController();
        PrezzoTotaleBean prezzo = cityController.ottieni_prezzo_totale("Rome", "2");
        assertEquals(3.0, prezzo.getPrezzo_totale());

    }
}

