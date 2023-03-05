package processlagerAPI;

import databasAPI.Bok;
import databasAPI.Konto;


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
    public boolean kollaMedlemsStatus(long personNr);

    /*
     *Returnera ett datum hur länge personen är temporärt avstängd.
     */
    public String tempAvstängning(long personNr, Date datum) throws Exception ;

    /*Returnera true om personen är svartlistad*/
    public boolean svartlistaMedlem(long personNr);

    /*return Konto efter det registerades*/
    public Konto regKonto(String fnamn, String enamn, long personNr, String roll) throws Exception;

    /*return true if konto är avslutad*/
    public boolean avslutaKonto(long personNr);

    /*return true om lån är registrera*/
    public boolean registreraLån(long personNr, int bibID);

}