package processlagerAPI;
import databasAPI.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestProcess {

    //Sätter upp mockobjektet för databasapi inför alla test
    private static Logger logger = LogManager.getLogger(TestProcess.class.getName());

    //Skapar mockobjektet
    Databas dbapi = mock(Databas.class);
Process p = new Process();
    //Återställer mockobjektet inför varje test
    @BeforeEach
    void resetMock() {
        reset(dbapi);
    }

    @Nested
    @DisplayName("Test för kollaTillgänglighet")
    class KollaTillgänglighet {

        @Test
        void test1() {
            assertEquals(1, 1);
        }
    }

    @Nested
    @DisplayName("Test för kollaMedlemsstatus")
    class kollaMedlemsstatus {

    }

    @Nested
    @DisplayName("Test för tempAvstängning")
    class tempAvstängning {

    }

    @Nested
    @DisplayName("Test för svartlistaMedlem")
    class svartlistaMedlem {

    }

    @Nested
    @DisplayName("Test för regKonto")
    class regKonto {
    }

    @Nested
    @DisplayName("Test för avslutaKonto")
    class avslutaKonto {
    }
    @Nested
    @DisplayName("Test för registreraLån")
    class registreraLån {
    }
}