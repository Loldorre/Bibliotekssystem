package databasAPI;

import java.math.BigInteger;
import java.util.Date;

public interface IDatabas {

    //Konstruktor som ger databasens adress och inlogg till användare+lösenord för inloggning.
    //Void DatabasAPI(String adress, String användarnamnOchLösenord);

/*
retunera tillgängligheten av en viss Bok, tillgänglig eller ej
*utifrån Bokens titel, Boken ISBN (identification number), Bokens titel och författare, Bokens *författare
*/
//hämta alla böcker med riktig titel
//public Bok[] hämtaTillgängligheten (String titel);

//alla böcker med samma ISBN
//public Bok[] hämtaTillgängligheten (int ISBN);

//alla böcker med samma titel och författare
//public Bok[] hämtaTillgängligheten (String titel, String författare, int ISBN);

//alla böcker med samma författare
//public Bok[] hämtaTillgängligheten (String författare);

    Bok[] hämtaTillgängligheten (String titel, String författare, int ISBN);
    Bok[] hämtaTillgängligheten (int bibID);

    /*skapa en Lån och returnera en String som berättar vad som hänt*/
    String skapaLån (Date startDatum, BigInteger personNr, int ISBN);

    /*ta bort lån och returnera String */
//*void eller boolean?

    /*ta bort lån enligt medlems personnummer eller Bokens ISBN*/
    String taBortLån(BigInteger personNr, int ISBN);

    /*uppdatera Generell info som nuvarande finns innan en åtgärd görs*/
/*uppdatera tabeller i collumn där where1 = where2 till ny.

String uppdateraGenerell (String tabell, String column, String where1, String where2, String ny) ;

/*lägga till en medlem på svartlista*/
    /*Svartlista medlem genom medlems personnummer */
    String läggTillSvartlista(BigInteger personNr);


    /*returnera String att Konto är skapad*/
    /*skapa Konto med medlemens namn, efternamn, personnummer och roll*/

    String skapaKonto(String fnamn, String  enamn, BigInteger personNr, String roll);


    //returnera en String efter att Konto är avslutad.
    String avslutaKonto(BigInteger personNr);

    //returnera ett Konto[] objekt
    Konto[] hämtaKonton();
}
