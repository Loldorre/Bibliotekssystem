package processlagerAPI;
import databasAPI.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestProcess extends Process{

    //Sätter upp mockobjektet för databasapi inför alla test
    private static Logger logger = LogManager.getLogger(TestProcess.class.getName());

    //Skapar mockobjektet
    Databas dbapi = mock(Databas.class);

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
    public void kollaTillgänglighetTest1() {
when(dbapi.hämtaTillgänglighet("Atomic Habits")).thenReturn(new Bok[]{
        new Bok(4,124494,"Atomic Habits","James Clear",2018),
        new Bok(5,124494,"Atomic Habits","James Clear",2018),
        new Bok(6,124494,"Atomic Habits","James Clear",2018),});
assertEquals(1, kollaTillgänglighet("Atomic Habits"));
    }
    @Test
    @DisplayName("kollaTillgänglighet: Bok finns inte för utlån")
    public void kollaTillgänglighetTest2() {
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
                    new Konto("Mahatma","Ghandi",new BigInteger("1001012980"),"teacher",1111,false,new int[]{4,5,6},0,0),
                    new Konto("Dorian","Jones",new BigInteger("9712201234"),"undergraduate",6969,false,new int[]{1,2,3},0,0),
                    new Konto("Elvis","Presley",new BigInteger("311101012940"),"candidate",1234,false,new int[]{7,8,9,10,11,12,13},0,0),
            });
            assertTrue(kollaMedlemsStatus(new BigInteger("1001012980")));
        }
        @Test
        @DisplayName("kollaMedelmsstatus: Dorian får inte låna pga av för många lånade böcker")
        public void kollaMedelmsstatusTest2() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{
                    new Konto("Mahatma","Ghandi",new BigInteger("1001012980"),"teacher",1111,false,new int[]{4,5,6},0,0),
                    new Konto("Dorian","Jones",new BigInteger("9712201234"),"undergraduate",6969,false,new int[]{1,2,3},0,0),
                    new Konto("Elvis","Presley",new BigInteger("311101012940"),"candidate",1234,true,new int[]{7,8,9,10,11,12,13},0,0),
            });
            assertFalse(kollaMedlemsStatus(new BigInteger("9712201234")));
        }
        @Test
        @DisplayName("kollaMedelmsstatus: Elvis får inte låna pga av avstängning")
        public void kollaMedelmsstatusTest3() {
            when(dbapi.hämtaKonton()).thenReturn(new Konto[]{
                    new Konto("Mahatma","Ghandi",new BigInteger("1001012980"),"teacher",1111,false,new int[]{4,5,6},0,0),
                    new Konto("Dorian","Jones",new BigInteger("9712201234"),"undergraduate",6969,false,new int[]{1,2,3},0,0),
                    new Konto("Elvis","Presley",new BigInteger("311101012940"),"candidate",1234,true,new int[]{7,8,9,10,11,12,13},0,0),
            });
            assertFalse(kollaMedlemsStatus(new BigInteger("311101012940")));
        }
    }
    @Nested
    @DisplayName("Test för tempAvstängning")
    class tempAvstängning{

    }
    @Nested
    @DisplayName("Test för svartlistaMedlem")
    class svartlistaMedlem{

    }
    @Nested
    @DisplayName("Test för regKonto")
    class regKonto{

    }
    @Nested
    @DisplayName("Test för avslutaKonto")
    class avslutaKonto{

    }
    @Nested
    @DisplayName("Test för registreraLån")
    class registreraLån{

    }
}