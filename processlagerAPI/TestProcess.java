package processlagerAPI;

import databasAPI.Bok;
import databasAPI.Databas;
import databasAPI.Konto;
import databasAPI.Lån;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
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
    @DisplayName("Test för hämtaSamling")
    class hämtaSamling {
        @Test
        @DisplayName("Samling hämtas ur databasen och info samlas i string array")
        void testHämtaSamling1() throws Exception {
            when(dbapi.hämtaBöcker()).thenReturn(new Bok[]{
                    new Bok(1, 123456, "Dorians stora bruna", "Dorian Jones", 2010),
                    new Bok(2, 222222, "Den andra stora bruna", "Dorian Jones", 2010),
                    new Bok(3, 232323, "Nu är det brunt igen", "Dorian Jones", 2010)
            });
            assertArrayEquals(new String[]{
                    "bokID: 1, Titel: Dorians stora bruna, Författare: Dorian Jones, Utgivningsår: 2010, ISBN: 123456,",
                    "bokID: 2, Titel: Den andra stora bruna, Författare: Dorian Jones, Utgivningsår: 2010, ISBN: 222222,",
                    "bokID: 3, Titel: Nu är det brunt igen, Författare: Dorian Jones, Utgivningsår: 2010, ISBN: 232323,"
            }, p.hämtaSamling());
        }

        @Test
        @DisplayName("Samling hämtas ur databasen och den strular ()throws sql exception")
        void testHämtaSamling2() throws SQLException {
            when(dbapi.hämtaBöcker()).thenThrow(new SQLException());
            assertThrows(SQLException.class, () -> p.hämtaSamling());
        }
    }

    @Nested
    @DisplayName("Test för hämtaKonto")
    class hämtaKonto {

        @Test
        @DisplayName("Konton hämtas och rätt konto plockas ut (koll på Pnummer)")
        void testHämtaKonto1() throws Exception {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{
                    new Konto("Andy","Weir", 222222222222L, 0,1234,null,new Lån[]{},0,0),
                    new Konto("Nisse","Fel", 000000000000L, 0,1111,null,new Lån[]{},0,0),
            });
            assertEquals(222222222222L,p.hämtaKonto(1234).getPersonNr());
        }
        @Test
        @DisplayName("Konton hämtas för 0000 som inte finns, returnerat Konto är null")
        void testHämtaKonto2() throws Exception {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{
                    new Konto("Andy","Weir", 222222222222L, 0,1234,null,new Lån[]{},0,0),
                    new Konto("Nisse","Fel", 000000000000L, 0,1111,null,new Lån[]{},0,0),
            });
            assertNull(p.hämtaKonto(0000));
        }
    }
    @Test
    @DisplayName("Konton hämtas för 0000 men databasen strular, Throws SQLException")
    void testHämtaKonto3() throws Exception {
        when(dbapi.hämtaKonton()).thenThrow(new SQLException());
        assertThrows(SQLException.class,()-> p.hämtaKonto(0000));
    }
}