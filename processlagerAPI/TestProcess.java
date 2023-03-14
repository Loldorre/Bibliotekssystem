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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestProcess {
    private static Logger logger = LogManager.getLogger(TestProcess.class.getName());

    //Skapar mockobjektet
    Databas dbapi = mock(Databas.class);

    //Sätter upp mockobjektet för databasapi inför alla test
    Process p = new Process(dbapi);

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



