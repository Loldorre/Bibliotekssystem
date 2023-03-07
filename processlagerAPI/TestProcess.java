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

public class TestProcess{

    //Sätter upp mockobjektet för databasapi inför alla test
    private static Logger logger = LogManager.getLogger(TestProcess.class.getName());

    //Skapar mockobjektet
    Databas dbapi = mock(Databas.class);
    Process PL = new Process(dbapi);

    public TestProcess() throws SQLException {
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

        }
        @Test
        @DisplayName("kollaTillgänglighet: Bok finns inte för utlån")
        public void kollaTillgänglighetTest2() throws SQLException {

        }
    }
    @Nested
    @DisplayName("Test för kollaMedlemsstatus")
    class kollaMedlemsstatus{
        @Test
        @DisplayName("kollaMedelmsstatus: Ghandi får låna")
        public void kollaMedelmsstatusTest1() {

        }
        @Test
        @DisplayName("kollaMedelmsstatus: Dorian får inte låna pga av för många lånade böcker")
        public void kollaMedelmsstatusTest2() {

           // assertFalse(PL.kollaMedlemsStatus(6969));
        }
        @Test
        @DisplayName("kollaMedelmsstatus: Elvis får inte låna pga av avstängning")
        public void kollaMedelmsstatusTest3() {

            //assertFalse(PL.kollaMedlemsStatus(1234));
        }
    }
    @Nested
    @DisplayName("Test för tempAvstängning")
    class tempAvstängning{

        @Test
        @DisplayName("tempAvstängning: Elvis är avstängd tills 1/6 2025")
        public void tempAvstängningTest1() throws Exception {

        }

        @Test
        @DisplayName("tempAvstängning: Dorian är avstängd för många gånger och blir permanent avstängd och svartlistad( throws Exception)")
        public void tempAvstängningTest2() throws Exception {

        }
    }
    @Nested
    @DisplayName("Test för svartlistaMedlem")
    class svartlistaMedlem{
        @Test
        @DisplayName("svartlistaMedlem: Dorian blir svartlistad och är inte längre tillgänglig)")
        public void svartlistaMedlemTest1() {

        }
    }
    @Nested
    @DisplayName("Test för regKonto")
    class regKonto{
        @Test
        @DisplayName("regKonto: Registerar nytt konto för Viktor Sjögren)")
        public void regKontoTest1() throws Exception{

        }
        @Test
        @DisplayName("regKonto: registrerar nytt konto för Viktor men det funkar inte för han är svartlistad)")
        public void regKontoTest2(){

        }
        @Test
        @DisplayName("regKonto: registrerar nytt konto för Dorian men det funkar inte för han är redan registrerad)")
        public void regKontoTest3(){

        }
    }
    @Nested
    @DisplayName("Test för avslutaKonto")
    class avslutaKonto{
        @Test
        @DisplayName("avslutaKonto: Dorians konto avslutas och finns inte längre i databasen.)")
        public void avslutaKontoTest1() throws Exception{

        }
    }
    @Nested
    @DisplayName("Test för registreraLån")
    class registreraLån{
        @Test
        @DisplayName("registreraLån: )")
        public void registreraLånTest1(){

        }
    }
}