package controller.applicativo;

import Bean.InformazioniPercorsoBean;
import Controller.Applicativo.PathController;
import Model.DAO.TicketDAODB;
import Model.DAO.TicketDAOLayer;
import Model.Domain.TypesOfPersistenceLayer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import utility.Factory.FactoryPersistence;
import utility.Singleton.PersistenceMode;
import Exception.DAOExceptionRemoli;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
class LoginControllerTest {

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
    @ValueSource(strings = {"Ottaviano:Quintiliani:Milan", "Spagna:Anagnina:Rome"})
    void TestPath(String strings) throws Exception {

        String[] parts = strings.split(":");
        String start = parts[0];
        String end = parts[1];
        String city = parts[2];

        PathController path = new PathController();
        InformazioniPercorsoBean dto = path.run(start, end, city);
        int stazioniTotali = dto.getCityLife().getNumero_stazioni_totali();
        assertTrue(stazioniTotali > 0);

    }
    @ParameterizedTest
    @ValueSource(strings = {"Ottaviano:Quintiliani:Naples", "AAAA:BBBB:Rome"})
    void TestExceptionPath(String strings) {

        String[] parts = strings.split(":");
        String start = parts[0];
        String end = parts[1];
        String city = parts[2];
        PathController path = new PathController();
        assertThrows(DAOExceptionRemoli.class, () ->
        {
            path.run(start, end, city);
        });
    }
}

