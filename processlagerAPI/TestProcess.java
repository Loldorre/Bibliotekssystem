package processlagerAPI;

import databasAPI.Databas;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

public class TestProcess {
    private static Logger logger = LogManager.getLogger(TestProcess.class.getName());

    //Skapar mockobjektet
    Databas dbapi = mock(Databas.class);

    //Sätter upp mockobjektet för databasapi inför alla test
    Process p = new Process(dbapi);

    //Återställer mockobjektet inför varje test
    @BeforeEach

    void resetMock() {
        logger.debug("--Resetting mock--");
        reset(dbapi);
    }
    @Nested
    @DisplayName("Test för tempAvstängning")
    class tempAvstängning {
        @Test
        void test1(){
        boolean a=true;
        assertTrue(a);
        }
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
    @Nested
    @DisplayName("Test för återlämnaBok")
    class återlämnaBok {

    }
}