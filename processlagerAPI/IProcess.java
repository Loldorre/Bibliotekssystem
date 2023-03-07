package processlagerAPI;

import databasAPI.Bok;
import databasAPI.Konto;

public interface IProcess {
    /*
     *kolla om boken är tillgängligt med olika parameter
     *returnera en multi array med alla möjliga böcker och deras information
     *return specifica boken
     */
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