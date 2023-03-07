package databasAPI;

import java.sql.SQLException;
import java.util.Date;
public interface IDatabas {

    int taBortLån(int bid);

    Bok[] hämtaTillgängligheten(String titel);

    Lån[] hämtaLån();

    Konto[] hämtaKonton();

    int updateAntalFörseningar(int kontoID);

    int registreraTempAvstänging(int kontoID, int numOfDays);

    int updateAntalAvstängningar(int kontoID);

    int läggTillSvartlista(long personNr);

    int avslutaKonto(int kontoID);

    int skapaLån(int kontoID, int ISBN);

    int skapaKonto(String fnamn, String enman, Long personNr, int roll);

    long[] hämtaSvartlistade();
}