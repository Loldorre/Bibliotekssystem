package databasAPI;

import java.math.BigInteger;
import java.util.Date;
public interface IDatabas {

/*
retunera tillgängligheten av en viss Bok, tillgänglig eller ej
*utifrån Bokens titel, Boken ISBN (identification number), Bokens titel och författare,  Bokens *författare
*/
//hämta alla böcker med riktig titel

    /*skapa en Lån och returnera en String*/
    String skapaLån (Date startDatum, int kontoID, int ISBN);


    /*ta bort lån enligt medlems personnummer eller Bokens ISBN*/
    String taBortLån(int kontoID, int ISBN);

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

    public String registreraTempAvstänging(int kontoID);

}

