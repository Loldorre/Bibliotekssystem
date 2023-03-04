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

public class TestProcess extends Process {

    //Sätter upp mockobjektet för databasapi inför alla test
    private static Logger logger = LogManager.getLogger(TestProcess.class.getName());

    //Skapar mockobjektet
    Databas dbapi = mock(Databas.class);

    TestProcess() throws SQLException {
    }

    //Återställer mockobjektet inför varje test
    @BeforeEach
    void resetMock(){
        reset(dbapi);
    }

    @Nested
    @DisplayName("Test för kollaTillgänglighet")
    class KollaTillgänglighet{
    @Test
    @DisplayName("kollaTillgänglighet: Bok finns för utlån")
    public void kollaTillgänglighetTest1() throws SQLException{
when(dbapi.hämtaTillgänglighet("Atomic Habits")).thenReturn(new Bok[]{
        new Bok(4,124494,"Atomic Habits","James Clear",2018),
        new Bok(5,124494,"Atomic Habits","James Clear",2018),
        new Bok(6,124494,"Atomic Habits","James Clear",2018),});
assertEquals(1, kollaTillgänglighet("Atomic Habits"));
    }
    @Test
    @DisplayName("kollaTillgänglighet: Bok finns inte för utlån")
    public void kollaTillgänglighetTest2() throws SQLException {
        when(dbapi.hämtaTillgänglighet("Atomic Habits")).thenReturn(new Bok[]{});
        assertEquals(0, kollaTillgänglighet("Atomic Habits"));
    }
    }
    @Nested
    @DisplayName("Test för kollaMedlemsstatus")
    class kollaMedlemsstatus{
        @Test
        @DisplayName("kollaMedelmsstatus: Ghandi får låna")
        public void kollaMedelmsstatusTest1() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{
                    new Konto("Mahatma","Ghandi",1001012980L,"teacher",1111,false,new int[]{4,5,6},0,0),
                    new Konto("Dorian","Jones",9712201234L,"undergraduate",6969,false,new int[]{1,2,3},0,0),
                    new Konto("Elvis","Presley",3111010129L,"candidate",1234,false,new int[]{7,8,9,10,11,12,13},0,0),
            });
            assertTrue(kollaMedlemsStatus(1111));
        }
        @Test
        @DisplayName("kollaMedelmsstatus: Dorian får inte låna pga av för många lånade böcker")
        public void kollaMedelmsstatusTest2() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{
                    new Konto("Mahatma","Ghandi",1001012980L,"teacher",1111,false,new int[]{4,5,6},0,0),
                    new Konto("Dorian","Jones",9712201234L,"undergraduate",6969,false,new int[]{1,2,3},0,0),
                    new Konto("Elvis","Presley",3111010129L,"candidate",1234,true,new int[]{7,8,9,10,11,12,13},0,0),
            });
            assertFalse(kollaMedlemsStatus(6969));
        }
        @Test
        @DisplayName("kollaMedelmsstatus: Elvis får inte låna pga av avstängning")
        public void kollaMedelmsstatusTest3() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{
                    new Konto("Mahatma","Ghandi",1001012980L,"teacher",1111,false,new int[]{4,5,6},0,0),
                    new Konto("Dorian","Jones",9712201234L,"undergraduate",6969,false,new int[]{1,2,3},0,0),
                    new Konto("Elvis","Presley",3111010129L,"candidate",1234,true,new int[]{7,8,9,10,11,12,13},0,0),
            });
            assertFalse(kollaMedlemsStatus(1234));
        }
    }
    @Nested
    @DisplayName("Test för tempAvstängning")
    class tempAvstängning{

        @Test
        @DisplayName("tempAvstängning: Elvis är avstängd tills 1/6 2025")
        public void tempAvstängningTest1() throws Exception {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{
                    new Konto("Elvis","Presley",3111010129L,"candidate",1234,true,new int[]{7,8,9,10,11,12,13},0,0),
            });
            when(dbapi.registreraTempAvstänging(1234,15)).thenReturn("1 row(s) affected Rows matched: 1  Changed: 1  Warnings: 0");

            assertEquals(new Date(2025,5,1),tempAvstängning(1234,new Date(2025,5,1)));
        }

        @Test
        @DisplayName("tempAvstängning: Dorian är avstängd för många gånger och blir permanent avstängd och svartlistad( throws Exception)")
        public void tempAvstängningTest2() throws Exception {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{
                    new Konto("Dorian","Jones",9712201234L,"undergraduate",6969,false,new int[]{1,2,3},2,0),
            });

            when(dbapi.läggTillSvartlista(9712201234L)).thenReturn("1 row(s) affected");
            when(dbapi.avslutaKonto(6969)).thenReturn("1 row(s) affected)");
            when(dbapi.registreraTempAvstänging(6969,15)).thenReturn("0 row(s) affected Rows matched: 0  Changed: 0  Warnings: 0");

            //Ser till att tempAvstängning() kastar en Exception...
            assertThrows(Exception.class, ()-> tempAvstängning(6969,new Date(2025,5,1)));
        }
    }
    @Nested
    @DisplayName("Test för svartlistaMedlem")
    class svartlistaMedlem{
        @Test
        @DisplayName("svartlistaMedlem: Dorian blir svartlistad och är inte längre tillgänglig)")
        public void svartlistaMedlemTest1() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{
                    new Konto("Dorian","Jones",9712201234L,"undergraduate",6969,false,new int[]{1,2,3},2,0),
            },new Konto[]{});
            when(dbapi.läggTillSvartlista(9712201234L)).thenReturn("1 row(s) affected");
            when(dbapi.avslutaKonto(6969)).thenReturn("1 row(s) affected)");

            //Ser till att Dorian inte finns med på kontolistan andra gången hämtaKonton körs och inte längre är tillgänglig...
            assertTrue(svartlistaMedlem(9712201234L));
        }
    }
    @Nested
    @DisplayName("Test för regKonto")
    class regKonto{
        @Test
        @DisplayName("regKonto: Registerar nytt konto för Viktor Sjögren)")
        public void regKontoTest1() throws Exception{
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{
                    new Konto("Dorian","Jones",9712201234L,"undergraduate",6969,false,new int[]{1,2,3},2,0),
            },
                    new Konto[]{
                            new Konto("Dorian", "Jones", 9712201234L, "undergraduate", 6969, false, new int[]{1, 2, 3}, 2, 0),
                            new Konto("Viktor", "Sjögren", 8701032990L, "candidate", 1234, false, new int[]{}, 0, 0),
                    });
            when(dbapi.hämtaSvartlistade()).thenReturn(new long[0]);
            when(dbapi.skapaKonto("Viktor","Sjögren",8701032990L,"candidate")).thenReturn("1 row(s) returned");
            //Kontot finns nu i databasen och personnummer i objektet som returneras stämmer med det i databasen.
            assertEquals(regKonto("Viktor","Sjögren",8701032990L,"candidate").getPersonNr(),8701032990L);
        }
        @Test
        @DisplayName("regKonto: registrerar nytt konto för Viktor men det funkar inte för han är svartlistad)")
        public void regKontoTest2(){
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{
                    new Konto("Dorian","Jones",9712201234L,"undergraduate",6969,false,new int[]{1,2,3},2,0),
            });
            when(dbapi.hämtaSvartlistade()).thenReturn(new long[]{8701032990L});

            //Kontot kan inte skapas och metoden kastar en Exception.
    assertThrows(Exception.class, ()->regKonto("Viktor","Sjögren",8701032990L,"candidate"));
        }
        @Test
        @DisplayName("regKonto: registrerar nytt konto för Dorian men det funkar inte för han är redan registrerad)")
        public void regKontoTest3(){
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{
                    new Konto("Dorian","Jones",9712201234L,"undergraduate",6969,false,new int[]{1,2,3},2,0),
            });

            //Kontot kan inte skapas och metoden kastar en Exception.
            assertThrows(Exception.class, ()->regKonto("Dorian","Jones",9712201234L,"undergraduate"));
        }
    }
    @Nested
    @DisplayName("Test för avslutaKonto")
    class avslutaKonto{
        @Test
        @DisplayName("avslutaKonto: Dorians konto avslutas och finns inte längre i databasen.)")
        public void avslutaKontoTest1() throws Exception{
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{
                            new Konto("Dorian","Jones",9712201234L,"undergraduate",6969,false,new int[]{1,2,3},2,0),
                    }
                    ,new Konto[]{});
            //Kontot finns nu inte databasen och metoden svarar med true efter hämtning.
            assertTrue(avslutaKonto(6969));
        }
    }
    @Nested
    @DisplayName("Test för registreraLån")
    class registreraLån{
        @Test
        @DisplayName("registreraLån: )")
        public void registreraLånTest1(){
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{
                            new Konto("Dorian","Jones",9712201234L,"undergraduate",6969,false,new int[]{1,2},2,0),
                    },new Konto[]{new Konto("Dorian","Jones",9712201234L,"undergraduate",6969,false,new int[]{1,2,3},2,0),});

            //Registrerar bok 3 till dorian och returnerar true efter att ha kollat databasen.
            assertTrue(registreraLån(6969,3));
        }
    }
}