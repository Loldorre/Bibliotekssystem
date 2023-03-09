package processlagerAPI;
import databasAPI.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
    Process p = new Process(dbapi);

    public TestProcess() throws SQLException {
    }

    //Återställer mockobjektet inför varje test
    @BeforeEach
    void resetMock() {
        reset(dbapi);
    }

    @Nested
    @DisplayName("Test för kollaTillgänglighet")
    class KollaTillgänglighet {
        @Test
        @DisplayName("kollaTillgänglighet: Boken finns inte för lån")
        public void testKollaTillgänglighet1() {
            when(dbapi.hämtaTillgänglighet("Bibeln")).thenReturn(new Bok[]{
            });
            assertEquals(0,p.kollaTillgänglighet("Bibeln"));
        }
        @Test
        @DisplayName("kollaTillgänglighet: Boken finns för lån och första bokens bibId returneras")
        public void testKollaTillgänglighet2() {
            when(dbapi.hämtaTillgänglighet("Bibeln")).thenReturn(new Bok[]{
                    new Bok(6, 666666, "Bibeln", "derp", 100),
                    new Bok(7, 666666, "Bibeln", "derp", 100),
                    new Bok(8, 666666, "Bibeln", "derp", 100)
            });
            assertEquals(6,p.kollaTillgänglighet("Bibeln"));
        }
    }
}