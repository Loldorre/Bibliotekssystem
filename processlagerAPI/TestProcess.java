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
        void avslutakonto_rätt () throws SQLException {
            Databas dbapi = mock(Databas.class);
            Process cut = new Process(dbapi);

            Konto medlem = mock(Konto.class);
            int kontoID = 1007;
            when(dbapi.hämtaKonton()).thenReturn(
                    new Konto[]{ new Konto("Olle",
                            "Olsson",
                            9102137779L,
                            1,
                            kontoID,LocalDate.of(2023, 1, 1), new Lån[] {} , 0, 0)}
            );

            when(medlem.getKontoID()).thenReturn(kontoID);
            when(dbapi.avslutaKonto(kontoID)).thenReturn(0);

            assertEquals(medlem, cut.avslutaKonto(medlem));

        }
//----------------------------------------------------------------------

        @DisplayName("Konto som du försöker avsluta är redan borttaget!")
        @Test
        void avslutakonto_kontoredanborttaget() throws SQLException {
            Databas dbapi = mock(Databas.class);
            Process cut = new Process(dbapi);

            Konto medlem = mock(Konto.class);
            int kontoID = 1007;
            when(dbapi.hämtaKonton()).thenReturn(
                    new Konto[]{ new Konto("Olle","Olsson",0201, 1, kontoID,LocalDate.of(2023, 1, 1),new Lån[] {} ,0 ,0 )
                    }
            );

            when(medlem.getKontoID()).thenReturn(kontoID);
            when(dbapi.avslutaKonto(kontoID)).thenReturn(1);

            assertEquals(null, cut.avslutaKonto(medlem));

        }
//-------------------------------------------------------------------
        @DisplayName("Boken 1 lånad av användare 222")
        @Test

        void Återlämnabok_return0() throws SQLException {
            Databas dbapi = mock(Databas.class);
            Process cut = new Process(dbapi);

            // Mock av Konto
            Konto medlem = mock(Konto.class);
            int bookId = 1;
            int userId = 222;
            when(medlem.getLånadeBöcker()).thenReturn(
                    new Lån[]{new Lån(bookId,
                            userId,
                            LocalDate.of(2023, 1, 1),
                            LocalDate.of(2023, 1, 1))}
            );

            when(medlem.getKontoID()).thenReturn(userId);
            when(dbapi.taBortLån(bookId)).thenReturn(0);

            assertEquals(0, cut.återlämnaBok(medlem, 1));
        }

//----------------------------------------------------------
        @Test

        void Återlämnabok_return1() throws SQLException {
            Databas dbapi = mock(Databas.class);
            Process cut = new Process(dbapi);

            // Mock av Konto
            Konto medlem = mock(Konto.class);
            int bookId = 1;
            int userId = 222;
            when(medlem.getLånadeBöcker()).thenReturn(
                    new Lån[]{new Lån(bookId,
                            userId,
                            LocalDate.of(2023, 1, 1),
                            LocalDate.of(2023, 1, 1))}
            );

            when(medlem.getKontoID()).thenReturn(userId);
            when(dbapi.taBortLån(bookId)).thenReturn(1);

            assertEquals(1, cut.återlämnaBok(medlem, 1));
        }

//-------------------------------------------------------------------
        @DisplayName("Det fanns inga lånade böcker kopplade till kontoid med samma bid! ")
        @Test

        void Återlämnabok_return2() throws SQLException {

            Databas dbapi = mock(Databas.class);
            Process cut = new Process(dbapi);

            // Mock av Konto
            Konto medlem = mock(Konto.class);
            int bookId = 1;
            int userId = 222;
            when(medlem.getLånadeBöcker()).thenReturn(
                    new Lån[]{new Lån(bookId,
                            333,
                            LocalDate.of(2023, 1, 1),
                            LocalDate.of(2023, 1, 1))}
            );

            when(medlem.getKontoID()).thenReturn(userId);
            when(dbapi.taBortLån(bookId)).thenReturn(2);

            assertEquals(2, cut.återlämnaBok(medlem, 1));
        }
    }

        @Nested
        @DisplayName("Test för tempAvstängning")
        class kollaTillgänglighet {

            @Test
            void kollaTillgänglighet1() throws SQLException {
                when(dbapi.hämtaTillgänglighet())
                        .thenReturn(new Bok[] {
                                new Bok(1, 14526, "Wake up, Sir", "Jonathan Ames", 2004),
                                new Bok(2,14526, "Wake up, Sir", "Jonathan Ames", 2004),
                                new Bok(3,94636, "Eragon", "Christopher Paolini", 2003)});

                Bok bok = new Bok(1, 14526, "Wake up, Sir", "Jonathan Ames", 2004);

                assertEquals(1, p.kollaTillgänglighet(14526).getBibID());
            }

            @Test
            void kollaTillgänglighet2() throws SQLException {
                when(dbapi.hämtaTillgänglighet())
                        .thenReturn(new Bok[] {});

                assertNull(p.kollaTillgänglighet(14526));
            }

        }
        @Nested
        @DisplayName("Test för svartlistaMedlem")
        class svartlistaMedlem {

            @Test
            void svartlistaMedlemTest1() throws SQLException {
                when(dbapi.hämtaSvarlistade())
                        .thenReturn(new long[] {});

                when(dbapi.läggTillSvartlistade(196703046523L))
                        .thenReturn(0);

                Konto medlem = new Konto("Bert", "Bertinham", 196703046523L, 1, 6253, null, new Lån[] {}, 0, 0 );

                assertEquals(medlem, p.svartlistaMedlem(medlem));
            }

            @Test
            void svartlistaMedlemTest2() throws SQLException {
                when(dbapi.hämtaSvarlistade())
                        .thenReturn(new long[] {199803046523L, 198203032338L});

                Konto medlem = new Konto("Bert", "Bertinham", 198203032338L, 1, 6253, null, new Lån[] {}, 2, 0 );

                assertNull(p.svartlistaMedlem(medlem));
            }

            @Test
            void svartlistaMedlemTest3() throws SQLException {
                when(dbapi.hämtaSvarlistade())
                        .thenReturn(new long[] {199803046523L, 198263332338L});

                Konto medlem = new Konto("Bert", "Bertinham", 198203032338L, 1, 6253, null, new Lån[] {}, 2, 0 );
                Konto medlem2 = new Konto("Kent", "Kentingham", 197207037263L, 1, 6253, null, new Lån[] {}, 2, 0 );

                assertNotEquals(medlem, p.svartlistaMedlem(medlem2));
            }
        }

        @Test
        @DisplayName("Försöker registrera konto men personen är svartlistad")
        public void testRegKonto3() throws Exception {

            when(dbapi.hämtaKonton()).thenReturn(
                    new Konto[]{ new Konto("Olle","Olsson",0201, 1,2000,LocalDate.of(2023, 1, 1),new Lån[] {} ,0 ,0 )
                    });
            when(dbapi.hämtaSvarlistade()).thenReturn(new long[]{2603180001L});

            assertNull(p.regKonto("Kurt", "Wallander", 2603180001L, 0));
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
}