package processlagerAPI;

import databasAPI.Bok;
import databasAPI.Databas;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.*;

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
    class hämtaSamling {
        @Test
        @DisplayName("Samling hämtas ur databasen och ")
        void testHämtaSamling1() throws Exception{
        when(dbapi.hämtaBöcker()).thenReturn(new Bok[]{
                new Bok(1,123456,"Dorians stora bruna","Dorian Jones",2010),
                new Bok(2,123456,"Dorians stora bruna","Dorian Jones",2010),
                new Bok(3,123456,"Dorians stora bruna","Dorian Jones",2010)
        });

        assertArrayEquals ( new String[]{
                        "bokID: 1, Titel: Dorians stora bruna, Författare: Dorian Jones, Utgivningsår: 2010, ISBN: 123456,",
                        "bokID: 2, Titel: Dorians stora bruna, Författare: Dorian Jones, Utgivningsår: 2010, ISBN: 123456,",
                        "bokID: 3, Titel: Dorians stora bruna, Författare: Dorian Jones, Utgivningsår: 2010, ISBN: 123456,"
        }, p.hämtaSamling()
        );


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