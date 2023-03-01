package databasAPI;

import java.math.BigInteger;
import java.util.Date;
public interface IDatabas {

/*
retunera tillgängligheten av en viss Bok, tillgänglig eller ej
*utifrån Bokens titel, Boken ISBN (identification number), Bokens titel och författare,  Bokens *författare
*/
//hämta alla böcker med riktig titel
// Bok[] hämtaTillgängligheten (String titel);

//alla böcker med samma ISBN
// Bok[] hämtaTillgängligheten (int ISBN);

//alla böcker med samma titel och författare
// Bok[] hämtaTillgängligheten (String titel, String författare, int ISBN);

//alla böcker med samma författare
// Bok[] hämtaTillgängligheten (String författare);

    Bok[] hämtaTillgängligheten (String titel, String författare, int ISBN);
    Bok[] hämtaTillgängligheten (int bibID);

    /*skapa en Lån och returnera en String*/
    String skapaLån (Date startDatum, int kontoID, int ISBN);

    /*ta bort lån och returnera String */
//*void eller boolean?

    /*ta bort lån enligt medlems personnummer eller Bokens ISBN*/
    String taBortLån(int kontoID, int ISBN);

    /*uppdatera Generell info som nuvarande finns innan en åtgärd görs*/
/*uppdatera tabeller i collumn där where1 =  where2 till ny.

String uppdateraGenerell (String tabell, String column, String where1, String where2, String ny) ;

/*lägga till en medlem på svartlista*/
    /*svartlista medlem genom medlems kontoID */
    String läggTillSvartlista(BigInteger personNr);


    /*returnera String att Konto är skapad*/
    /*skapa Konto med medlemens namn, efternamn, personnummer och roll*/

    String skapaKonto(String fnamn, String  enamn, BigInteger personNr, String roll);


    //returnera en String efter att Konto är avslutad.
    String avslutaKonto(int kontoID);

    //returnera ett Konto[] objekt med alla konton på servern.
    Konto[] hämtaKonton();
}

