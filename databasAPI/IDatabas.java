package databasAPI;

import java.sql.SQLException;
import java.util.Date;
public interface IDatabas {

    /* Hämta alla böcker med riktig titel som inte är utlånade (alltså i samling men inte i lån).*/
    Bok[] hämtaTillgänglighet (String titel);

    /*skapa en Lån och returnera en String
     *
     * behöver definiera vad databasen kan tänkas svara... för tester
     *
     */
    String skapaLån (int kontoID, int ISBN);


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
    String läggTillSvartlista(long personNr);


    /*returnera String att Konto är skapad*/
    /*skapa Konto med medlemens namn, efternamn, personnummer och roll
     *
     * behöver definiera vad databasen kan tänkas svara... för tester
     *
     * */
    String skapaKonto(String fnamn, String  enamn, long personNr, String roll);


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

    public String registreraTempAvstänging(int kontoID, int numOfDays);

    public String returnBook(int isbn);
    public String updateAntalFörseningar(int kontoID);
    public String updateAntalAvstängningar(int kontoID);

}
