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
        public void testKollaTillgänglighet(){
    when(dbapi.hämtaTillgänglighet("Bibeln")).thenReturn(new Bok[]{ new Bok(1,666666,"Bibeln","derp",100)});

    }

    @Nested
    @DisplayName("Test för kollaMedlemsstatus")
    class kollaMedlemsstatus {
        @Test
        @DisplayName("kollaMedlemsstatus: Elvis konto kollas för möjlighet att låna och får inga anmärkningar eller åtgärder")
        public void testkollaMedlemsstatus1() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{
                    new Konto("Elvis","Presley",311101012940L,0,1234,false, new Lån[]{new Lån(1,new Date(20250101),1234)},0,0)});
            assertEquals(0,p.kollaMedlemsstatus(1234));
        }
        @Test
        @DisplayName("kollaMedlemsstatus: Elvis konto kollas för möjlighet att låna och får avslag då hans konto inte finns")
        public void testkollaMedlemsstatus2() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{});
            assertEquals(1,p.kollaMedlemsstatus(1234));
        }
        @Test
        @DisplayName("kollaMedlemsstatus: Elvis konto kollas för möjlighet att låna och får godkänt trotts en försening")
        public void testkollaMedlemsstatus3() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{ new Konto("Elvis","Presley",311101012940L,0,1234,false, new Lån[]{new Lån(1,new Date(20220101),1234)},0,0)});
            when(dbapi.updateAntalFörseningar(1234)).thenReturn(0);
            assertEquals(0,p.kollaMedlemsstatus(1234));
        }
        @Test
        @DisplayName("kollaMedlemsstatus: Elvis konto kollas för möjlighet att låna och blir avstängd 15 dagar på grund av för många förseningar")
        public void testkollaMedlemsstatus4() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{ new Konto("Elvis","Presley",311101012940L,0,1234,false, new Lån[]{new Lån(1,new Date(20220101),1234)},0,2)});
            when(dbapi.registreraTempAvstänging(1234,15)).thenReturn(0);
            when(dbapi.updateAntalAvstängningar(1234)).thenReturn(0);
            assertEquals(2,p.kollaMedlemsstatus(1234));
        }
        @Test
        @DisplayName("kollaMedlemsstatus: Elvis konto kollas för möjlighet att låna och blir svarlistad på grund av för många avstängningar")
        public void testkollaMedlemsstatus5() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{ new Konto("Elvis","Presley",311101012940L,0,1234,null, new Lån[]{new Lån(1,new Date(20220101),1234)},2,2)});
            when(dbapi.läggTillSvartlistade(311101012940L)).thenReturn(0);
            when(dbapi.avslutaKonto(1234)).thenReturn(0);
            assertEquals(3,p.kollaMedlemsstatus(1234));
        }
        @Test
        @DisplayName("kollaMedlemsstatus: Elvis konto kollas för möjlighet att låna men är avstängd... (alla böcker återlämnade)")
        public void testkollaMedlemsstatus6() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{ new Konto("Elvis","Presley",311101012940L,0,1234,new Date(20990101), new Lån[]{},2,2)});
            when(dbapi.läggTillSvartlistade(311101012940L)).thenReturn(0);
            when(dbapi.avslutaKonto(1234)).thenReturn(0);
            assertEquals(2,p.kollaMedlemsstatus(1234));
        }

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