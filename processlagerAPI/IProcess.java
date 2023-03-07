package processlagerAPI;

import databasAPI.Bok;
import databasAPI.Konto;


import java.sql.SQLException;
import java.util.Date;

public interface IProcess{
    /*
     *kolla om boken är tillgängligt
     *returnera en multi array med alla möjliga böcker och deras information
     */

    //return en int om boken är tillgänglig eller ej.
    // 0 om finns inte
    // 1 om finns.
    // 2 om finns men alla utlånade.(stödjs inte i databaslagret i nuvarande lösning då hämtarmetoden där endast hämtar de böcker som inte är utlånade)
    public int kollaTillgänglighet(String titel);

    /*return true or false om Personen är medlem eller inte*/
    public int kollaMedlemsstatus(int kontoId);

    /*
     *Returnera ett datum hur hur länge personen är temporärt avstängd.
     *Eller String hur länge och varför?
     */
    public int tempAvstängning(int kontoId,int antalDagar);


    /*Returnera true om personen är svartlistad*/
    public int svartlistaMedlem(long personNr);

    /*return Konto efter det registerades*/
    public Konto regKonto(String fnamn, String enamn, long personNr, String roll);

    /*return true if konto är avslutad*/
    public int avslutaKonto(int kontoId);

    /*return true om lån är registrera*/
    public int registreraLån(int kontoId, int bibID);

    public int återlämnaBok(int kontoId, int bibID);

    public int kollaMedlemsstatus (int kontoID, int avstängningsDagar);

}


