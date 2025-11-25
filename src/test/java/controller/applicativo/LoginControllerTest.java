package controller.applicativo;

import Bean.InformazioniPercorsoBean;
import Controller.Applicativo.PathController;
import Model.DAO.TicketDAODB;
import Model.DAO.TicketDAOFile;
import Model.DAO.TicketDAOLayer;
import Model.Domain.TypesOfPersistenceLayer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utility.Factory.FactoryPersistence;
import utility.Singleton.PersistenceMode;
import Exception.DAOExceptionRemoli;
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
}

