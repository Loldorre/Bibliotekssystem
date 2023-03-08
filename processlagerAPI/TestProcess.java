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

    @Nested
    @DisplayName("Test för kollaMedlemsstatus")
    class kollaMedlemsstatus {
        @Test
        @DisplayName("kollaMedlemsstatus: Elvis konto kollas för möjlighet att låna och får inga anmärkningar eller åtgärder")
        public void testkollaMedlemsstatus1() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{
                    new Konto("Elvis","Presley",311101012940L,0,1234,null, new Lån[]{new Lån(1,new Date(20250101),1234)},0,0)});
            assertEquals(0,p.kollaMedlemsstatus(1234));
        }
        @Test
        @DisplayName("kollaMedlemsstatus: Elvis konto kollas för möjlighet att låna och får avslag då hans konto inte finns")
        public void testkollaMedlemsstatus2() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{});
            assertEquals(1,p.kollaMedlemsstatus(1234));
        }
        @Test
        @DisplayName("kollaMedlemsstatus: Elvis kollas och får godkänt trotts en försening")
        public void testkollaMedlemsstatus3() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{ new Konto("Elvis","Presley",311101012940L,0,1234,null, new Lån[]{new Lån(1,new Date(20220101),1234)},0,0)});
            when(dbapi.updateAntalFörseningar(1234)).thenReturn(0);
            assertEquals(0,p.kollaMedlemsstatus(1234));
        }
        @Test
        @DisplayName("kollaMedlemsstatus: Elvis kollas och blir avstängd 15 dagar på grund av för många förseningar")
        public void testkollaMedlemsstatus4() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{ new Konto("Elvis","Presley",3111012940L,0,1234,null, new Lån[]{new Lån(1,new Date(20220101),1234)},0,2)});
            when(dbapi.registreraTempAvstänging(1234,15)).thenReturn(0);
            when(dbapi.updateAntalAvstängningar(1234)).thenReturn(0);
            assertEquals(2,p.kollaMedlemsstatus(1234));
        }
        @Test
        @DisplayName("kollaMedlemsstatus: Elvis kollas och blir svarlistad på grund av för många avstängningar")
        public void testkollaMedlemsstatus5() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{ new Konto("Elvis","Presley",3111012940L,0,1234,null, new Lån[]{new Lån(1,new Date(20220101),1234)},2,2)});
            when(dbapi.läggTillSvartlistade(311101012940L)).thenReturn(0);
            when(dbapi.avslutaKonto(1234)).thenReturn(0);
            assertEquals(3,p.kollaMedlemsstatus(1234));
        }
        @Test
        @DisplayName("kollaMedlemsstatus: Elvis kollas men är avstängd... (alla böcker återlämnade)")
        public void testkollaMedlemsstatus6() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{ new Konto("Elvis","Presley",3111012940L,0,1234,new Date(20990101), new Lån[]{},2,2)});
            when(dbapi.läggTillSvartlistade(311101012940L)).thenReturn(0);
            when(dbapi.avslutaKonto(1234)).thenReturn(0);
            assertEquals(2,p.kollaMedlemsstatus(1234));
        }
        @Test
        @DisplayName("kollaMedlemsstatus(kontoID+dagar): Elvis ska bli avstängd i 20 dagar, men har redan en glömd bok och blir avstängd 35 dagar")
        public void testkollaMedlemsstatus7() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{ new Konto("Elvis","Presley",3111012940L,0,1234,new Date(20220116), new Lån[]{new Lån(1,new Date(20220101),1234)},1,1)});
            when(dbapi.registreraTempAvstänging(1234,35)).thenReturn(0);
            when(dbapi.updateAntalAvstängningar(1234)).thenReturn(0);
           assertEquals(0,p.kollaMedlemsstatus(1234,20));
        }
    }

    @Nested
    @DisplayName("Test för tempAvstängning")
    class tempAvstängning {
        @Test
        @DisplayName("tempAvstängning: Jesus Karlsson blir avstängd 15 dagar")
        public void testTempAvstängning1() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{ new Konto("Jesus","Karlsson",1112242990L,0,1111,new Date(20250101), new Lån[]{},1,2)});
            when(dbapi.registreraTempAvstänging(1111,15)).thenReturn(0);
            when(dbapi.updateAntalAvstängningar(1111)).thenReturn(0);

            assertEquals(0,p.tempAvstängning(1111,15));
        }
        @Test
        @DisplayName("tempAvstängning: Jesus Karlsson ska bli avstängd men kontot finns inte (return 1)")
        public void testTempAvstängning2() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{});
            assertEquals(1,p.tempAvstängning(1111,15));
        }
        @Test
        @DisplayName("tempAvstängning: Jesus Karlsson ska bli avstängd men databasen misslyckades (return 2)")
        public void testTempAvstängning3() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{ new Konto("Jesus","Karlsson",1112242990L,0,1111,new Date(20250101), new Lån[]{},1,2)});
            when(dbapi.registreraTempAvstänging(1111,15)).thenReturn(1);
            when(dbapi.updateAntalAvstängningar(1111)).thenReturn(1);

            assertEquals(2,p.tempAvstängning(1111,15));
        }
    }

    @Nested
    @DisplayName("Test för svartlistaMedlem")
    class svartlistaMedlem {
        @Test
        @DisplayName("svartlistaMedlem: Jesus Karlsson blir svartlistad")
        public void testSvartlistaMedlem1() {
            when(dbapi.hämtaSvarlistade()).thenReturn(new long[]{});
            when(dbapi.läggTillSvartlistade(1112242990L)).thenReturn(0);

            assertEquals(0,p.svartlistaMedlem(1112242990L));
        }
        @Test
        @DisplayName("svartlistaMedlem: Jesus Karlsson blir inte svartlistad för han är redan det (return 1)")
        public void testSvartlistaMedlem2() {
            when(dbapi.hämtaSvarlistade()).thenReturn(new long[]{1112242990L});
            when(dbapi.läggTillSvartlistade(1112242990L)).thenReturn(1);

            assertEquals(1,p.svartlistaMedlem(1112242990L));
        }
        @Test
        @DisplayName("svartlistaMedlem: Jesus Karlsson blir inte svartlistad för databasen strular (return 2)")
        public void testSvartlistaMedlem3() {
            when(dbapi.hämtaSvarlistade()).thenReturn(new long[]{});
            when(dbapi.läggTillSvartlistade(1112242990L)).thenReturn(2);

            assertEquals(2,p.svartlistaMedlem(1112242990L));
        }
    }

    @Nested
    @DisplayName("Test för regKonto")
    class regKonto {

        ///Identifierade problem med att kontoID inte kan kommas åt efter registrering.
        @Test
        @DisplayName("regKonto: Claes McGuyver registreras i systemet")
        public void testRegKonto1() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{ new Konto("Jesus","Karlsson",1112242990L,0,1111,new Date(20250101), new Lån[]{},1,2)});
            when(dbapi.hämtaSvarlistade()).thenReturn(new long[]{8701032990L});
            when(dbapi.skapaKonto("Claes","McGuyver",6501012990L,3)).thenReturn(1112);

                    assertEquals(1112,p.regKonto("Claes","McGuyver",6501012990L,3));
        }
        @Test
        @DisplayName("regKonto: Claes McGuyver registreras inte i systemet för han är svartlistad")
        public void testRegKonto2() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{
                    new Konto("Jesus","Karlsson",1112242990L,0,1111,new Date(20250101), new Lån[]{},1,2)
            });
            when(dbapi.hämtaSvarlistade()).thenReturn(new long[]{8701032990L,6501012990L});

            assertEquals(2,p.regKonto("Claes","McGuyver",6501012990L,3));
        }
        @Test
        @DisplayName("regKonto: Claes McGuyver registreras inte i systemet för han är redan medlem")
        public void testRegKonto3() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{
                    new Konto("Jesus","Karlsson",1112242990L,0,1111,new Date(20250101), new Lån[]{},1,2),
                    new Konto("Claes","McGuyver",6501012990L,3,1112,null, new Lån[]{},0,0)
            });

            assertEquals(1,p.regKonto("Claes","McGuyver",6501012990L,3));
        }
        @Test
        @DisplayName("regKonto: Claes McGuyver registreras inte i systemet för databasen strular")
        public void testRegKonto4() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{
                    new Konto("Jesus","Karlsson",1112242990L,0,1111,new Date(20250101), new Lån[]{},1,2)
            });
            when(dbapi.hämtaSvarlistade()).thenReturn(new long[]{8701032990L});
            when(dbapi.skapaKonto("Claes","McGuyver",6501012990L,3)).thenReturn(3);
            assertEquals(3,p.regKonto("Claes","McGuyver",6501012990L,3));
        }
    }

    @Nested
    @DisplayName("Test för avslutaKonto")
    class avslutaKonto {
        @Test
        @DisplayName("avslutaKonto: Claes McGuyver vill avsluta sitt konto och det godkänns")
        public void testAvslutaKonto1() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{
                    new Konto("Jesus","Karlsson",1112242990L,0,1111,new Date(20250101), new Lån[]{},1,2),
                    new Konto("Claes","McGuyver",6501012990L,3,1112,null, new Lån[]{},0,0)});

            when(dbapi.avslutaKonto(1112)).thenReturn(0);
            assertEquals(0,p.avslutaKonto(1112));
        }
        @Test
        @DisplayName("avslutaKonto: Claes McGuyver vill avsluta sitt konto men det finns inte (return 1)")
        public void testAvslutaKonto2() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{
                            new Konto("Jesus", "Karlsson", 1112242990L, 0, 1111, new Date(20250101), new Lån[]{}, 1, 2)
                    });
            assertEquals(1,p.avslutaKonto(1112));
        }
        @Test
        @DisplayName("avslutaKonto: Claes McGuyver vill avsluta sitt konto och databasen strular")
        public void testAvslutaKonto3() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{
                    new Konto("Jesus","Karlsson",1112242990L,0,1111,new Date(20250101), new Lån[]{},1,2),
                    new Konto("Claes","McGuyver",6501012990L,3,1112,null, new Lån[]{},0,0)});

            when(dbapi.avslutaKonto(1112)).thenReturn(1);
            assertEquals(2,p.avslutaKonto(1112));
        }
    }
    @Nested
    @DisplayName("Test för registreraLån")
    class registreraLån {
        @Test
        @DisplayName("registreraLån: konto 1112 lånar bibID 1")
        public void testRegistreraLån1() {
            when(dbapi.skapaLån(1112,1)).thenReturn(0);

            assertEquals(0,p.registreraLån(1112,1));
        }
        @Test
        @DisplayName("registreraLån: konto 1112 misslyckas låna bibID 1 (databasstrul)")
        public void testRegistreraLån2() {
            when(dbapi.skapaLån(1112,1)).thenReturn(1);

            assertEquals(1,p.registreraLån(1112,1));
        }
    }
    @Nested
    @DisplayName("Test för återlämnaBok")
    class återlämnaBok {
        @Test
        @DisplayName("återlämnaBok: Konto:1112 vill återlämna bibID 1 och lyckas ")
        public void testÅterlämnaBok1() {
            when(dbapi.taBortLån(1)).thenReturn(0);
            assertEquals(0,p.återlämnaBok(1112,1));
        }
        @Test
        @DisplayName("återlämnaBok: Konto:1112 vill återlämna bibID 1 och bibID stämmer inte")
        public void testÅterlämnaBok2() {
            when(dbapi.taBortLån(1)).thenReturn(1);
            assertEquals(1,p.återlämnaBok(1112,1));
        }
    }
}