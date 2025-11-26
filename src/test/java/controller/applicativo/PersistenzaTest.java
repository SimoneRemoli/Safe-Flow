package controller.applicativo;
import Model.DAO.TicketDAODB;
import Model.DAO.TicketDAOFile;
import Model.DAO.TicketDAOLayer;
import Model.Domain.TypesOfPersistenceLayer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utility.Factory.FactoryPersistence;
import utility.Singleton.PersistenceMode;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ------------------------------------------------------------
 *  Test Class : <PersistenzaTest>
 *  Author     : Simone Remoli
 *  Description: Test sul controllo della persistenza selezionata.
 * ------------------------------------------------------------
 */

@TestMethodOrder(MethodOrderer.MethodName.class)

public class PersistenzaTest {

    private static final ResourceBundle RB = ResourceBundle.getBundle("configurations/testpaths");
    static Stream<String> persistenceJDBC(){
        return Stream.of(RB.getString("persistenzaJDBC"));
    }
    static Stream<String> persistenceFile(){
        return Stream.of(RB.getString("persistenzafile"));
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
}
