package processlagerAPI;

import databasAPI.*;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import processlagerAPI.Process;
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
        logger.trace("kollaTillgänglighet  --->");
        Bok[] listaAvBöcker = DatabasAPI.hämtaTillgänglighet(titel);
        if (listaAvBöcker.length > 0) {
            logger.debug("Lista av böcker har längd: " + listaAvBöcker.length );
            for (Bok b : listaAvBöcker) {
                if (b.getTitel() == titel) {
                    logger.debug("<--- kollaTillgänglighet bibID " + b.getBibID() + "redo för lån = " +b.getBibID());
                    return b.getBibID();
                }
    }
        }
        logger.debug("<--- kollaTillgänglighet bibID = "+ 0);
        return 0;
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
        logger.debug("kollaMedlemsstatus  --->");
        Konto[] listAvKonto = DatabasAPI.hämtaKonton();
        Konto medlem = null;
        boolean avstängd = false;
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
            if (avstangdDate.after(new Date())) {
                avstängd = true;//------Kontot redan avstängt----
                logger.debug("konto är redan avstängt");
            }
        }
//---------------------------------Kollar om det finns försenade böcker---------------------------------
        Lån[] lånadeBöcker = medlem.getLånadeBöcker();
        for (Lån l : lånadeBöcker) {
            LocalDate today = LocalDate.now();
            Date getTime =new Date(l.getLånDatum().getYear(),l.getLånDatum().getMonth(),l.getLånDatum().getDay());
            LocalDate lånslut = getTime.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            lånslut = lånslut.plusDays(14);

            if (today.isAfter(lånslut)){
                logger.debug("försenad bok hittad");
                medlem.setAntalForseningar(medlem.getAntalForseningar() + 1);
                DatabasAPI.updateAntalFörseningar(medlem.getKontoID());
                break;
            }
        }
        logger.debug("Kollar antal förseningar");
        if (medlem.getAntalForseningar() > 2 || medlem.getAntalAvstangningar()>0) { //Om kontot nu har > 2 förseningar Stängs kontoto av.-----

            logger.debug("kollar antal avstängningar");
            if (medlem.getAntalAvstangningar() > 1) { //------- Om kontot har mer än en avstängning nu så svartlista och avsluta kontot.----
                logger.debug("Medlem har >1 avstängning");
                svar = this.svartlistaMedlem(medlem.getPersonNr());
                svar = this.avslutaKonto(medlem.getKontoID());
                logger.debug("<--- medlem avstängd MedlemSvartlistad (return 3)");
                return 1;
            }
            if(medlem.getAntalForseningar() > 2)
                logger.debug("Medlem har >2 förseningar");
            svar = tempAvstängning(medlem.getKontoID(), 15);
            LocalDate datum = LocalDate.now().plusDays(15);
            medlem.setAntalAvstangningar(medlem.getAntalAvstangningar() + 1);
            logger.debug("<--- medlem avstängd (return 2)");
            return 2;
        }

//-------Kollar om medlem har uppnått maximalt antal böcker---------------
        int maxböcker=0;

        if(medlem.getRoll()==0){
            logger.debug("medlem undergrad max 3");
            maxböcker=3;
        }
        if(medlem.getRoll()==1){
            logger.debug("medlem är grad max 5");
            maxböcker=5;
        }
        if(medlem.getRoll()==2){
            logger.debug("doctorate student max 7");
            maxböcker=7;
        }
        if(medlem.getRoll()==3){
            logger.debug("medlem är postDoc or teacher max 10");
            maxböcker=10;
        }
        logger.debug("kollar om medlem har uppnåt  max böcker");
        if(medlem.getLånadeBöcker().length >=maxböcker){
            logger.debug("<--- medlem har uppnått max antal böcker (return 0)");
            return 4;
        }
        if(avstängd==true){
            logger.debug("<----- konto är redan avstängt (return 2)");
            return 2;
        }
        logger.debug("<--- medlem godkänd (return 0)");
        return 0;
    }

    @Override
    public int kollaMedlemsstatus(int kontoID,int dagar) {
        logger.trace("kollaMedlemsstatus för avstängning  --->");
        Konto[] listAvKonto = DatabasAPI.hämtaKonton();
        boolean avstängd = false;
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
             LocalDate today = LocalDate.now();
             LocalDate avstangd = avstangdDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
             //------Kontot redan avstängt----
             if (avstangd.isAfter(today)) {
                 dagar += (int)today.until(avstangd, ChronoUnit.DAYS)+1;
             this.tempAvstängning(kontoID,dagar);
             medlem.setAntalAvstangningar(medlem.getAntalAvstangningar());
                logger.debug(" konto är redan avstängt lägger på " +dagar+ " på avstängning" );
            }
        }

//---------------------------------Kollar om det finns försenade böcker---------------------------------
        Lån[] lånadeBöcker = medlem.getLånadeBöcker();
        for (Lån l : lånadeBöcker) {
            logger.debug("återlämningsdatum" +l.getLånDatum().getYear(), l.getLånDatum().getMonth(), l.getLånDatum().getDay() + 14 );
            Date slutDatum = new Date(l.getLånDatum().getYear(), l.getLånDatum().getMonth(), l.getLånDatum().getDay() + 14);
            if (slutDatum.after(new Date())) {
                logger.debug("försenad bok hittad");
                medlem.setAntalForseningar(medlem.getAntalForseningar() + 1);
                DatabasAPI.updateAntalFörseningar(medlem.getKontoID());
                break;
            }
        }
        logger.debug("Kollar antal förseningar");
        if (medlem.getAntalForseningar() > 2 || medlem.getAntalAvstangningar()>0) { //Om kontot nu har > 2 förseningar Stängs kontoto av.-----

            logger.debug("kollar antal avstängningar");
            if (medlem.getAntalAvstangningar() > 1) { //------- Om kontot har mer än en avstängning nu så svartlista och avsluta kontot.----
                logger.debug("Medlem har >1 avstängning");
                svar = this.svartlistaMedlem(medlem.getPersonNr());
                svar = this.avslutaKonto(medlem.getKontoID());
                logger.debug("<--- medlem avstängd MedlemSvartlistad (return 1)");
                return 1;
            }
            if(medlem.getAntalForseningar() > 2) {
                logger.debug("Medlem har >2 förseningar");
                svar = tempAvstängning(medlem.getKontoID(), 15+ dagar);
                LocalDate datum = LocalDate.now().plusDays(15);
                medlem.setAntalAvstangningar(medlem.getAntalAvstangningar() + 1);
                logger.debug("<--- medlem avstängd 15 + "+ dagar + "(return 2)");
                return 2;
            }
        }

        logger.debug("<--- medlem avstängd endast " + dagar);
        svar = tempAvstängning(medlem.getKontoID(), dagar);
        return 0;
    }
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
int svar = DatabasAPI.skapaLån(kontoId, bibID);
           if(svar > 0) return 5;
    return 0;
    }
    @Override
    public int återlämnaBok(int bibID) {
        logger.debug(" återlämnaBok ---->");
        int databasSvar = this.DatabasAPI.taBortLån(bibID);

        if (databasSvar == 1) {
            return 1;
        } else if (databasSvar == 2) {
            return 1;
        } else if (databasSvar == 3) {
            return 1;
        } else if (databasSvar == 4) {
            return 1;
        } else if (databasSvar == 5) {
            return 1;
        } else
            logger.debug("<----- atermämna bok ");
        return 0;
    }

    @Override
    public int återlämnaBok(int kontoId, int bibID) {
        logger.debug(" återlämnaBok ---->");
        boolean bokKoppladTillMedlem = false;

        Lån[] lånadeBöcker = DatabasAPI.hämtaLån();

        for (int i = 0; i < lånadeBöcker.length; i++) {
            if (lånadeBöcker[i].getKontoID() == kontoId && lånadeBöcker[i].getBid() == bibID) {
                logger.debug("Det finns böcker kopplade i lån till kontoId");
                bokKoppladTillMedlem = true;
            }
        }
        if (!bokKoppladTillMedlem) {
            logger.debug("det fanns inga lånade böcker kopplade till kontoid med samma bid");
            return 2;
        }

        int databasSvar = this.DatabasAPI.taBortLån(bibID);

        if (databasSvar == 1) {
            return 1;
        } else if (databasSvar == 2) {
            return 1;
        } else if (databasSvar == 3) {
            return 1;
        } else if (databasSvar == 4) {
            return 1;
        } else if (databasSvar == 5) {
            return 1;
        } else if (databasSvar == 0) {
            logger.debug("<----- atermämna bok ");
            return 0;
        } else {
            return 1;
        }
    }

}