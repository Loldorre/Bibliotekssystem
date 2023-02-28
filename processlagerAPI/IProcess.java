package processlagerAPI;

import databasAPI.Bok;
import databasAPI.Konto;

import java.math.BigInteger;
import java.util.Date;

public interface IProcess {
    /*
     *kolla om boken är tillgängligt med olika parameter
     *returnera en multi array med alla möjliga böcker och deras information
     *return specifica boken
     */
    public boolean kollaTillgänglighetBibID(int bibID);

    //return alla böcker med liknande namn
    public Bok[] kollaTillgänglighetTitel(String titel);

    //return lista med alla böcker med samma ISBN
    public Bok[] kollaTillgänglighetISBN(int ISBN);

    //return alla böcker med samma titel och författare
    public Bok[] kollaTillgänglighetTitelForfattare(String titel, String forfattare);

    //return alla böcker av författaren
    public Bok[] kollaTillgänglighetForfattare(String forfattare);

    /*return true or false om Personen är medlem eller inte*/
    public boolean kollaMedlemsstatus(BigInteger personNr);

    /*
     *Returnera ett datum hur hur länge personen är temporärt avstängd.
     *Eller String hur länge och varför?
     */
    public String tempAvstängning(BigInteger personNr, Date datum);

    String tempAvstängning(BigInteger personNr);

    /*Returnera true om personen är svartlistad*/
    public boolean svartlistaMedlem(BigInteger personNr);

    /*return Konto efter det registerades*/
    public Konto regKonto(String fnamn, String enamn, BigInteger personNr, String roll);

    /*return true if konto är avslutad*/
    public boolean avslutaKonto(BigInteger personNr);

    /*return true om lån är registrera*/
    public boolean registreraLån(BigInteger personNr, int bibID);

}


