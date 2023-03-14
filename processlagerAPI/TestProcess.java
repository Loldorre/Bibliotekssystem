package processlagerAPI;

import databasAPI.Databas;
import databasAPI.Konto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

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
    @DisplayName("Test för tempAvstängning")
    class tempAvstängning {
        @Test
        @DisplayName("Max blir avstängd 6 dagar och det går genom")
        void testTempAvstängning() throws Exception {

            Konto acc = new Konto("Max", "Cool", 9901010000L,0,
                    2000,null,null,0,0);

            when(dbapi.registreraTempAvstänging(acc, 6)).thenReturn
                    (new Konto("Max", "Cool", 9901010000L,0,
                            2000, LocalDate.of(2023,03,20),null,1,0));

            assertEquals(LocalDate.of(2023,03,20), p.tempAvstängning(acc,6).getAvstangd());
        }

        @Test
        @DisplayName("Försöker stänga av medlem men Databasen strular")
        void testTempAvstängning1() throws SQLException {

            Konto acc = new Konto("Max", "Cool", 9901010000L,0,2001,null,null,0,0);

            when(dbapi.registreraTempAvstänging(acc, 6)).thenThrow(new SQLException());

            assertThrows(SQLException.class,()-> p.tempAvstängning(acc, 6));
        }
    }

    @Nested
    @DisplayName("Test för svartlistaMedlem")
    class svartlistaMedlem {

    }

    @Nested
    @DisplayName("Test för regKonto")
    class regKonto {
        @Test
        @DisplayName("Försöker registrera nytt konto och det går bra")
        public void testRegKonto() throws Exception {

            when(dbapi.skapaKonto("Bertram", "Snetand", 2603180001L,2)).thenReturn(new Konto("Bertram", "Snetand", 2603180001L,2, 2000, null,null,0,0));
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{});
            when(dbapi.hämtaSvarlistade()).thenReturn(new long[]{});


           assertEquals(2603180001L, p.regKonto("Bertram", "Snetand",2603180001L, 2).getPersonNr());
        }

        @Test
        @DisplayName("Försöker registrera nytt konto men databasen vill inte")
        public void testRegKonto1() throws Exception {

            when(dbapi.skapaKonto("Bertram", "Snetand", 2603180001L,2)).thenThrow(new SQLException());
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{});
            when(dbapi.hämtaSvarlistade()).thenReturn(new long[]{});

            assertThrows(SQLException.class,()-> p.regKonto("Bertram","Snetand", 2603180001L,2));
        }

        @Test
        @DisplayName("Försöker registrera konto men personen är svartlistad")
        public void testRegKonto3() throws Exception {
            when(dbapi.hämtaSvarlistade()).thenReturn(new long[]{2603180001L});

            assertNull(p.regKonto("Kurt", "Wallander", 2603180001L, 0));
        }

    }

    @Nested
    @DisplayName("Test för avslutaKonto")
    class avslutaKonto {

    }
    @Nested
    @DisplayName("Test för registreraLån")
    class registreraLån {
        @Test
        @DisplayName("Försöker regisrera ett lån och det går bra")
        void registreraLånPos() throws SQLException {
            when(dbapi.skapaLån(1009, 4)).thenReturn(0);

            assertEquals(0, p.registreraLån(1009,4));
        }

        @Test
        @DisplayName("Försöker att skapa ett lån men databasen strular")
        void regisreraLånNeg() throws SQLException {
            when(dbapi.skapaLån(1009, 4)).thenThrow(new SQLException());

            assertThrows(SQLException.class,() -> p.registreraLån(1009,4));
        }

    }
    @Nested
    @DisplayName("Test för återlämnaBok")
    class återlämnaBok {

    }
}