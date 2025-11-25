package controller.applicativo;

import Bean.InformazioniPercorsoBean;
import Controller.Applicativo.PathController;
import Model.DAO.TicketDAODB;
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

    static Stream<String> validPathsProvider() {
        return Stream.of(
                RB.getString("valid1"),
                RB.getString("valid2")
        );
    }

    static Stream<String> invalidPathsProvider() {
        return Stream.of(
                RB.getString("invalid1"),
                RB.getString("invalid2")
        );
    }

    @Test
    void TestPersistenza()
    {
        PersistenceMode.getInstance().setTipo(TypesOfPersistenceLayer.JDBC);
        //PersistenceMode.getInstance().setTipo(TypesOfPersistenceLayer.FileSystem);
        TicketDAOLayer dao = FactoryPersistence.createTicketDAO();
        assertTrue(dao instanceof TicketDAODB);
        //assertTrue(dao instanceof TicketDAOFile);
    }
    @ParameterizedTest
    @MethodSource("validPathsProvider")
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

