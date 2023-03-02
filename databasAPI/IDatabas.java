package databasAPI;

import java.math.BigInteger;
import java.util.Date;
public interface IDatabas {

    /* Hämta alla böcker med riktig titel som inte är utlånade (alltså i samling men inte i lån).*/
 Bok[] hämtaTillgänglighet (String titel);

    /*skapa en Lån och returnera en String
    *
    * behöver definiera vad databasen kan tänkas svara... för tester
    *
    */
    String skapaLån (Date startDatum, int kontoID, int ISBN);


    /*ta bort lån enligt medlems personnummer eller Bokens ISBN
     *
     * behöver definiera vad databasen kan tänkas svara... för tester
     *
     */
    String taBortLån(int kontoID, int ISBN);

/*lägga till en medlem på svartlista*/
    /*svartlista medlem genom medlems personnummer
     *
     * behöver definiera vad databasen kan tänkas svara... för tester
     *
     * */
    String läggTillSvartlista(BigInteger personNr);


    /*returnera String att Konto är skapad*/
    /*skapa Konto med medlemens namn, efternamn, personnummer och roll
    *
    * behöver definiera vad databasen kan tänkas svara... för tester
    *
    * */
    String skapaKonto(String fnamn, String  enamn, BigInteger personNr, String roll);


    /* returnera en String efter att Konto är avslutad.
    *
    * behöver definiera vad databasen kan tänkas svara... för tester
    *
    */
    String avslutaKonto(int kontoID);

    /*  returnera en Konto[] objekt med alla konton på servern.
    *
    * */
    Konto[] hämtaKonton();

    /* returnera en String efter att Konto är avslutad.
     *
     * behöver definiera vad databasen kan tänkas svara... för tester
     *
     */
    public String registreraTempAvstänging(int kontoID);

}

