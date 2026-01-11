package Testing.Remoli;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        LoginTest.class,
        PagamentoPaypalTest.class,
        PagamentoMastercardTest.class,
        PersistenzaTest.class,
        TicketTest.class,
        PercorsoTest.class,
        CityTest.class
})
public class  SuiteTestAll {
}