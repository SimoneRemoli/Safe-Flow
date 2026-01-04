package Testing.Brondi;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        AreaRiservataTest.class,
        SendCommunicationTest.class,
        UpdateNotificationsTest.class,
        ViewWorkScheduleTest.class
})
public class SuiteTestAll {
}