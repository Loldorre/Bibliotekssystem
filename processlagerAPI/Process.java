package processlagerAPI;

import databasAPI.*;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import processlagerAPI.Process;
class Process implements IProcess {
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
        int tillgänglighetsCase = 0;
        Bok [] listaAvBöcker = DatabasAPI.hämtaTillgänglighet(titel);

        if (listaAvBöcker.length == 0) {
            return tillgänglighetsCase;
        } else {
            tillgänglighetsCase = listaAvBöcker[0].getBibID();
            return tillgänglighetsCase;
        }
    }
    @Override
    public int kollaMedlemsstatus(int kontoID) {
logger.trace("kollaMedlemsstatus  --->");
        Konto [] listAvKonto = DatabasAPI.hämtaKonton();
        Konto medlem= null;
        int svar;
        //------------------------Letar efter kontot i listan från databasen---------------------------
        for (Konto k: listAvKonto) {
            if (k.getKontoID() == kontoID) {
                logger.debug("konto finns");
                medlem = k;
            }
        }
            if(medlem == null) {
                logger.debug("<--- konto finns inte (return 3)");//-----Konto finns inte slut på koll!---------------------------------------
            return  3;
        }
        //---------------kollar om kontot redan är avstängt
        if (medlem.getAvstangd() != null) {
            Date avstangdDate = new Date(medlem.getAvstangd().getYear(),medlem.getAvstangd().getMonth(),medlem.getAvstangd().getDay());
            if (avstangdDate.before(new Date())) { //------Kontot redan avstängt----
                logger.debug("<--- konto är redan avstängt (return 2)");
                return 2;
            }
        }

//---------------------------------Kollar om det finns försenade böcker---------------------------------
        Lån [] lånadeBöcker = medlem.getLånadeBöcker();

        for (Lån l: lånadeBöcker) {
            Date slutDatum = new Date(l.getLånDatum().getYear(), l.getLånDatum().getMonth(), l.getLånDatum().getDay() + 14);
            if (slutDatum.before(new Date())) {
                logger.debug("försenad bok hittad");
                medlem.setAntalForseningar(medlem.getAntalForseningar() + 1);
                break;
            }
        }
        logger.debug("Kollar antal förseningar");
            if (medlem.getAntalForseningar() > 2) { //Om kontot nu har > 2 förseningar Stängs kontoto av.-----
                logger.debug("Medlem har >2 förseningar");
                svar = tempAvstängning(medlem.getKontoID(), 15);
                LocalDate datum = LocalDate.now().plusDays(15);;
                medlem.setAntalAvstangningar(medlem.getAntalAvstangningar()+1);

                logger.debug("kollar antal avstängningar");
                if (medlem.getAntalAvstangningar() > 1) { //------- Om kontot har mer än en avstängning nu så svartlist aoch avsluta kontot.----
                    logger.debug("Medlem har >1 avstängning");
                    svar = this.svartlistaMedlem(medlem.getPersonNr());
                    svar = this.avslutaKonto(medlem.getKontoID());
                    logger.debug("<--- medlem avstängd MedlemSvartlistad (return 3)");
                    return 1;
                }

                logger.debug("<--- medlem avstängd (return 2)");
            return 2;
            }
        logger.debug("<--- medlem godkänd (return 0)");
        return 0;
    }

    public int kollaMedlemsstatus (int kontiId, int antalDagar) {
        return 0;
    }
//behöver kasta Exception om den måste svartlista en medlem för test.


    @Override
    public int tempAvstängning(int kontoId, int antalDagar) {
        String message = "";
        long persNr = 0;
        int index = 0;
        int nummerAvAvstängdaDagar = 0;
        Konto[] listaAvKonto = DatabasAPI.hämtaKonton();

        Date date = new Date();
        int day = date.getDay();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

        if (listaAvKonto[index].getAntalAvstangningar() >= 2) {
            DatabasAPI.läggTillSvartlistade(persNr);
            DatabasAPI.avslutaKonto(listaAvKonto[index].getKontoID());
            if (DatabasAPI.läggTillSvartlistade(kontoId) != 1) {
                message = "Person var redan 2 gånger avstängt. Avstängning failade, gör svartlistning manuellt.";
            } else {
                message = "Personen var redan 2 gånger avstängd och bli svartlistad";
            }

        } else
            DatabasAPI.registreraTempAvstänging(kontoId, nummerAvAvstängdaDagar);

        return 0;
    }

    @Override
    public int svartlistaMedlem(long personNr) {
        int databasSvar;
        long[] svartlistade;
        svartlistade = this.DatabasAPI.hämtaSvarlistade();
        //kollar om medlemmen redan är svartlistad.
       if (svartlistade != null){
        for (long l : svartlistade) {
            if (l == personNr) {return 1;}
                }}
        databasSvar = this.DatabasAPI.läggTillSvartlistade(personNr);
                if (databasSvar == 0) {return 0;}
                else return 2;
            }

    @Override
    public int regKonto(String fnamn, String enamn, long personNr, int roll) {
        Konto[] kontolista = this.DatabasAPI.hämtaKonton();
        long[] svartlistade = this.DatabasAPI.hämtaSvarlistade();
        int databasSvar;
        for(Konto k :kontolista){
            if(k.getPersonNr()==personNr){return 1;}
        }
        for(long l:svartlistade) {
            if(l==personNr){return 2;}
        }
        databasSvar = this.DatabasAPI.skapaKonto(fnamn, enamn, personNr, roll);
        if(databasSvar>999){return databasSvar;}
        else{return 3;}
    }

    @Override
    public int avslutaKonto(int kontoId) {
        Konto[] kontolista = this.DatabasAPI.hämtaKonton();
        int databasSvar;
        for(Konto k :kontolista){
            if(k.getKontoID()==kontoId){
                databasSvar = this.DatabasAPI.avslutaKonto(kontoId);
                if(databasSvar == 0) { return 0; }
                if(databasSvar == 1) { return 2; }
             }
        }
        return 1;
    }

    @Override
    public int registreraLån(int kontoId, int bibID) {
        return 0;
    }

    @Override
    public int återlämnaBok(int kontoId, int bibID) {
int databasSvar = this.DatabasAPI.taBortLån(bibID);
if (databasSvar == 1)
    return 1;
if (databasSvar == 2)
    return 1;
if (databasSvar == 3)
    return 1;
if (databasSvar == 4)
            return 1;
if (databasSvar == 5)
            return 1;
else return 0;
    }
}