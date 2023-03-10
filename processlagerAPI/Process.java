package processlagerAPI;

import databasAPI.Bok;
import databasAPI.Databas;
import databasAPI.Konto;
import databasAPI.Lån;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.Date;
public class Process implements IProcess {
    private static Logger logger = LogManager.getLogger(TestProcess.class.getName());

    Databas DatabasAPI = null;

    public Process() {
        this.DatabasAPI = new Databas();
    }
    public Process(Databas db) {
        this.DatabasAPI = db;
    }

    @Override
    public int kollaTillgänglighet(String titel) {
        int returnValue=0;
        logger.trace("kollaTillgänglighet  --->");
        Bok[] listaAvBöcker = DatabasAPI.hämtaTillgänglighet();
        if (listaAvBöcker.length > 0) {
            logger.debug("Lista av böcker har längd: " + listaAvBöcker.length );
            for (Bok b : listaAvBöcker) {
                if (b.getTitel().equals(titel)){
                    logger.debug("<--- kollaTillgänglighet bibID " + b.getBibID() + "redo för lån = " +b.getBibID());
                    returnValue = b.getBibID();
                }
            }
        }
        logger.debug("<--- kollaTillgänglighet bibID = "+ 0);
        return returnValue;
    }

    public int kollaTillgänglighet(int isbn) {
        logger.trace("kollaTillgänglighet  --->");
        Bok[] listaAvBöcker = DatabasAPI.hämtaTillgänglighet();
        if (listaAvBöcker.length > 0) {
            logger.debug("Lista av böcker har längd: " + listaAvBöcker.length );
            for (Bok b : listaAvBöcker) {
                if (b.getISBN() == isbn) {
                    logger.debug("<--- kollaTillgänglighet bibID " + b.getBibID() + "redo för lån = " +b.getBibID());
                    return b.getBibID();
                }
            }
        }
        logger.debug("<--- kollaTillgänglighet bibID = "+ 0);
        return 0;
    }
    @Override
    public int kollaMedlemsstatus(int kontoID) {
        logger.trace("kollaMedlemsstatus  --->");
        Konto[] listAvKonto = DatabasAPI.hämtaKonton();
        Konto medlem = null;
        int svar;
        //------------------------Letar efter kontot i listan från databasen---------------------------
        for (Konto k : listAvKonto) {
            if (k.getKontoID() == kontoID) {
                logger.debug("konto finns");
                medlem = k;
            }
        }
        if (medlem == null) {
            logger.debug("<--- konto finns inte (return 3)");//-----Konto finns inte slut på koll!---------------------------------------
            return 3;
        }
        //---------------kollar om kontot redan är avstängt---------
        if (medlem.getAvstangd() != null) {
            Date avstangdDate = new Date(medlem.getAvstangd().getYear(), medlem.getAvstangd().getMonth(), medlem.getAvstangd().getDay());
            if (avstangdDate.before(new Date())) { //------Kontot redan avstängt----
                logger.debug("<--- konto är redan avstängt (return 2)");
                return 2;
            }
        }

//---------------------------------Kollar om det finns försenade böcker---------------------------------
        Lån[] lånadeBöcker = medlem.getLånadeBöcker();

        for (Lån l : lånadeBöcker) {
            Date slutDatum = new Date(l.getLånDatum().getYear(), l.getLånDatum().getMonth(), l.getLånDatum().getDay() + 14);
            if (slutDatum.before(new Date())) {
                logger.debug("försenad bok hittad");
                medlem.setAntalForseningar(medlem.getAntalForseningar() + 1);
                break;
            }
        }
        logger.debug("Kollar antal förseningar");
        if (medlem.getAntalForseningar() > 2 ) { //Om kontot nu har > 2 förseningar Stängs kontoto av.-----

            logger.debug("kollar antal avstängningar");
            if (medlem.getAntalAvstangningar() > 1) { //------- Om kontot har mer än en avstängning nu så svartlista och avsluta kontot.----
                logger.debug("Medlem har >1 avstängning");
                svar = this.svartlistaMedlem(medlem.getPersonNr());
                svar = this.avslutaKonto(medlem.getKontoID());
                logger.debug("<--- medlem avstängd MedlemSvartlistad (return 3)");
                return 1;
            }
                else {
                logger.debug("Medlem har >2 förseningar");
            svar = tempAvstängning(medlem.getKontoID(), 15);
            LocalDate datum = LocalDate.now().plusDays(15);
            medlem.setAntalAvstangningar(medlem.getAntalAvstangningar() + 1);
            logger.debug("<--- medlem avstängd (return 2)");
            return 2;
        }
            }

        logger.debug("<--- medlem godkänd (return 0)");
        return 0;
    }

    public int kollaMedlemsstatus (int kontoID, int antalDagar) {
        logger.trace("kollaMedlemsstatus  --->");
        Konto[] listAvKonto = DatabasAPI.hämtaKonton();
        Konto medlem = null;
        int svar;
        //------------------------Letar efter kontot i listan från databasen---------------------------
        for (Konto k : listAvKonto) {
            if (k.getKontoID() == kontoID) {
                logger.debug("konto finns");
                medlem = k;
            }
        }
        if (medlem == null) {
            logger.debug("<--- konto finns inte (return 3)");//-----Konto finns inte slut på koll!---------------------------------------
            return 3;
        }

        //---------------kollar om kontot redan är avstängt
        if (medlem.getAvstangd() != null) {
            Date avstangdDate = new Date(medlem.getAvstangd().getYear(), medlem.getAvstangd().getMonth(), medlem.getAvstangd().getDay());
            if (avstangdDate.after(new Date())) { //------Kontot redan avstängt----
                logger.debug("<--- konto är redan avstängt (return 2)");
                return 2;
            }
        }
        logger.debug("ökar medlemmens avstängningar");
        medlem.setAntalAvstangningar(medlem.getAntalAvstangningar() + 1);

//---------------------------------Kollar om det finns försenade böcker---------------------------------
        Lån[] lånadeBöcker = medlem.getLånadeBöcker();

        for (Lån l : lånadeBöcker) {
            Date slutDatum = new Date(l.getLånDatum().getYear(), l.getLånDatum().getMonth(), l.getLånDatum().getDay() + 14);
            if (slutDatum.before(new Date())) {
                logger.debug("försenad bok hittad");
                medlem.setAntalForseningar(medlem.getAntalForseningar() + 1);
                break;
            }
        }
        logger.debug("Kollar antal förseningar");
        if (medlem.getAntalForseningar() > 2) { //Om kontot nu har > 2 förseningar Stängs kontoto av.-----
            logger.debug("kollar antal avstängningar");

            if (medlem.getAntalAvstangningar() > 1) { //------- Om kontot har mer än en avstängning nu så svartlist aoch avsluta kontot.----
                logger.debug("Medlem har >1 avstängning");
                svar = this.svartlistaMedlem(medlem.getPersonNr());
                svar = this.avslutaKonto(medlem.getKontoID());
                logger.debug("<--- medlem avstängd MedlemSvartlistad (return 3)");
                return 1;
            }
            else {
                logger.debug("Medlem har >2 förseningar");
                svar = tempAvstängning(medlem.getKontoID(), 15);
                LocalDate datum = LocalDate.now().plusDays(15);
                medlem.setAntalAvstangningar(medlem.getAntalAvstangningar() + 1);
                logger.debug("<--- medlem avstängd (return 2)");
                return 2;
            }
        }
        return 2;
    }
//behöver kasta Exception om den måste svartlista en medlem för test.

    @Override
    public int tempAvstängning(int kontoId, int antalDagar) {
        logger.debug(" tempAvstängning ---->");
            int svar = DatabasAPI.registreraTempAvstänging(kontoId, antalDagar);
            if(svar >0){
                return 5;}
            return 0;
    }

    @Override
    public int svartlistaMedlem(long personNr) {
        logger.debug(" svartlistaMedlem ---->");
        int databasSvar;
        long[] svartlistade;
        svartlistade = this.DatabasAPI.hämtaSvarlistade();
        //kollar om medlemmen redan är svartlistad.
       if (svartlistade != null){
        for (long l : svartlistade) {
            if (l == personNr) {
                logger.debug(" <------ svartListaMedlem. inga svartlistade>");
                return 1;
            }
                }}
        databasSvar = this.DatabasAPI.läggTillSvartlistade(personNr);
                if (databasSvar == 0) {
                    return 0;
                }
                else return 2;
            }

    @Override
    public int regKonto(String fnamn, String enamn, long personNr, int roll) {
        logger.debug(" regKonto ---->");
        Konto[] kontolista = this.DatabasAPI.hämtaKonton();
        logger.debug("hämtar svartlistade");
        long[] svartlistade = this.DatabasAPI.hämtaSvarlistade();
        logger.debug("svartlistade = "+ svartlistade.length );
        int databasSvar;
        logger.debug("kollar om kontot finns");
        for(Konto k :kontolista){
            if(k.getPersonNr()==personNr){
                logger.debug(" <----- kontot finns");
                return 1;
            }
        }
        logger.debug("letar i svartlistan");
        for(long l:svartlistade) {
            if(l==personNr){
                logger.debug(" <----- personnummer" + personNr + "svartlistat");
                return 2;
            }
        }
        logger.debug("skapar konto");
        databasSvar = this.DatabasAPI.skapaKonto(fnamn, enamn, personNr, roll);
        if(databasSvar>999){
            logger.debug("<----- skapaKonto()"+ fnamn+", "+enamn+","+personNr+"," + roll);
            return databasSvar;}

        else{
            logger.debug("<----- skapaKonto() databas strul...");
            return 3;
        }
    }

    @Override
    public int avslutaKonto(int kontoId) {
        logger.debug(" avslutaKonto ---->");
        Konto[] kontolista = this.DatabasAPI.hämtaKonton();
        int databasSvar;
        for(Konto k :kontolista){
            if(k.getKontoID()==kontoId){
                logger.debug("konto hittat - försöker avsluta" + kontoId);
                databasSvar = this.DatabasAPI.avslutaKonto(kontoId);

                if(databasSvar == 0) {
                    logger.debug("kontot borttaget ur databasen" + kontoId);
                    return 0;
                }
                if(databasSvar == 1) {
                    logger.debug("kontonummer finns inte");
                    return 1;
                }
                if(databasSvar == 2) {
                    logger.debug("databas strul");
                    return 2;
                }
             }
        }
        return 1;
    }

    @Override
    public int registreraLån(int kontoId, int bibID) {
        logger.debug(" registreraLån ---->");
       /* Konto [] listaAvKonto = DatabasAPI.hämtaKonton();
        Konto konto = null;
        for (Konto value : listaAvKonto) {
            if (value.getKontoID() == kontoId) {
                konto = value;
                break;
            }
        }
        if (konto == null) {
            return 3;
        }
        Lån [] lånadeBöcker = konto.getLanadeBocker(); */
int svar = DatabasAPI.skapaLån(kontoId, bibID);
           if(svar > 0) return 5;
    return 0;
    }
    @Override
    public int återlämnaBok( int kontoID,int bibID) {
        logger.debug(" återlämnaBok ---->");
        int databasSvar = this.DatabasAPI.taBortLån(bibID);
        if (databasSvar == 1)
        {
            logger.debug("<---- återlämnaBok returns" +1);
            return 1;
        }
        if (databasSvar == 2)
        {
            logger.debug("<---- återlämnaBok returns" +1);
            return 1;
        }
        if (databasSvar == 3)
        {
            logger.debug("<---- återlämnaBok returns" +1);
            return 1;
        }
        if (databasSvar == 4)

        {
            logger.debug("<---- återlämnaBok returns" +1);
            return 1;
        }
        if (databasSvar == 5) {
            logger.debug("<---- återlämnaBok returns" +1);
            return 1;
        }
        else{
            logger.debug("<---- återlämnaBok returns" +0);
            return 0;}
    }

    //För svartlistade som vill lämna tillbaka.
    public int återlämnaBok(int bibID) {
        logger.debug(" återlämnaBok ---->");
int databasSvar = this.DatabasAPI.taBortLån(bibID);
if (databasSvar == 1)
{
    logger.debug("<---- återlämnaBok returns" +1);
    return 1;
}
if (databasSvar == 2)
{
    logger.debug("<---- återlämnaBok returns" +1);
    return 1;
}
if (databasSvar == 3)
{
    logger.debug("<---- återlämnaBok returns" +1);
    return 1;
}
if (databasSvar == 4)

{
    logger.debug("<---- återlämnaBok returns" +1);
    return 1;
}
if (databasSvar == 5) {
    logger.debug("<---- återlämnaBok returns" +1);
    return 1;
}
else{
    logger.debug("<---- återlämnaBok returns" +0);
    return 0;}
    }
}