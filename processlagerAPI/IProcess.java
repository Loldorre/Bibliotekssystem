package processlagerAPI;

import databasAPI.Bok;
import databasAPI.Konto;

import java.math.BigInteger;
import java.util.Date;

public interface IProcess {
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
    public boolean kollaMedlemsStatus(BigInteger personNr);

    /*
     *Returnera ett datum hur hur länge personen är temporärt avstängd.
     */
    public String tempAvstängning(BigInteger personNr, Date datum);

    /*Returnera true om personen är svartlistad*/
    public boolean svartlistaMedlem(BigInteger personNr);

    /*return Konto efter det registerades*/
    public Konto regKonto(String fnamn, String enamn, BigInteger personNr, String roll);

    /*return true if konto är avslutad*/
    public boolean avslutaKonto(BigInteger personNr);

    /*return true om lån är registrera*/
    public boolean registreraLån(BigInteger personNr, int bibID);

}


