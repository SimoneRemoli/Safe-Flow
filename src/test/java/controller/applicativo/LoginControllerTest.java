package controller.applicativo;
import Bean.*;
import Controller.Applicativo.AreaRiservata;
import Controller.Applicativo.CityController;
import Exception.PaymentValidationExceptionRemoli;
import Exception.CredentialsExceptionRemoli;
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
import utility.Decorator.DecoratorTicket.BaseTicketCode;
import utility.Decorator.DecoratorTicket.CittaDecorator;
import utility.Decorator.DecoratorTicket.Component;
import utility.Decorator.DecoratorTicket.TimestampDecorator;
import utility.Factory.ConnectionFactory;
import utility.Factory.FactoryPersistence;
import utility.Singleton.Credentials;
import utility.Singleton.PersistenceMode;
import Exception.DAOExceptionRemoli;
import Exception.PathNotFoundExceptionRemoli;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
class LoginControllerTest {

    private static final ResourceBundle RB = ResourceBundle.getBundle("configurations/testpaths");

    static {
        System.out.println("La classe di Test viene caricata dalla JVM");
    }
    static Stream<String> noUserProvider() {
        return Stream.of(RB.getString("noUser"));
    }

    static Stream<String> ticketCFProvider() {
        return Stream.of(RB.getString("ticketCF").split(":"));
    }


    static Stream<String> Permission(){
        return Stream.of(RB.getString("permission"));
    }

    static Stream<String> permessiGiustiProvider() {
        return Stream.of(RB.getString("permessiOK"));
    }

    static Stream<String> timestampProvider() {
        return Stream.of(RB.getString("timestamp").split(":"));
    }

    static Stream<String> invalidTicketCFProvider() {
        return Stream.of(RB.getString("invalidTicketCF").split(":"));
    }



    static Stream<String> prezzoTotaleProvider() {
        return Stream.of(
                RB.getString("prezzo1"),
                RB.getString("prezzo2"),
                RB.getString("prezzo3")
        );
    }

    static Stream<String> persistenceJDBC(){
        return Stream.of(RB.getString("persistenzaJDBC"));
    }

    static Stream<String> persistenceFile(){
        return Stream.of(RB.getString("persistenzafile"));
    }

    static Stream<String> pathCFProvider() {
        return Stream.of(RB.getString("pathsCF").split(":"));
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

    static Stream<String> ticketGeneratingProvider() {
        return Stream.of(RB.getString("ticketgen").split(":"));
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

    @ParameterizedTest
    @MethodSource("prezzoTotaleProvider")
    void TestPrezzoTotale(String s) throws Exception {

        ConnectionFactory.Cambio_Di_Ruolo(Ruolo.TRAVELER);

        String[] parts = s.split(":");
        String city = parts[0];
        String quantita = parts[1];
        double expected = Double.parseDouble(parts[2]);

        CityController cityController = new CityController();
        PrezzoTotaleBean prezzo = cityController.ottieni_prezzo_totale(city, quantita);

        assertEquals(expected, prezzo.getPrezzo_totale());
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
    @MethodSource("pathCFProvider")
    void CheckListaPercorsi(String codiceFiscale) throws Exception {

        ConnectionFactory.Cambio_Di_Ruolo(Ruolo.TRAVELER);
        AreaRiservata reserved = new AreaRiservata();
        List<RouteBean> listaPercorsi = reserved.runPath(codiceFiscale);
        assertTrue(listaPercorsi.size() > 0);
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

    @Test
    void OttieniCity() throws DAOExceptionRemoli, SQLException {

        ConnectionFactory.Cambio_Di_Ruolo(Ruolo.TRAVELER);
        CityController cityController = new CityController();
        List<CityBean> city = cityController.getAllCities();
        assertTrue(city.size() > 0);

    }
}

