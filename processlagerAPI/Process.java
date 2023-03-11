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
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import processlagerAPI.Process;
public class Process {
    private static Logger logger = LogManager.getLogger(Process.class.getName());

    Databas DatabasAPI = null;

    public Process() {
        this.DatabasAPI = new Databas();
    }
    public Process(Databas db) {
        this.DatabasAPI = db;
    }

public Konto uppdateraKonto(Konto medlem, String attribut,int antal)throws SQLException{
            if (attribut.toLowerCase() == "avstängningar") {
                DatabasAPI.uppdateraAvstängningar(medlem, antal);
                medlem.setAntalAvstangningar(antal);
            }
            if (attribut.toLowerCase() == "förseningar") {
                DatabasAPI.uppdateraAvstängningar(medlem, antal);
                medlem.setAntalForseningar(antal);
            }
    return medlem;
}
    public Bok kollaTillgänglighet(String titel) throws SQLException,SaknasException {
        logger.trace("kollaTillgänglighet (titel) --->");
        Bok[] listaAvBöcker = DatabasAPI.hämtaTillgänglighet();
        if (listaAvBöcker.length > 0) {
            logger.debug("Lista av böcker har längd: " + listaAvBöcker.length );
            for (Bok b : listaAvBöcker) {
                if (b.getTitel() == titel) {
                    logger.debug("<--- kollaTillgänglighet bibID " + b.getBibID() + "redo för lån = " +b.getBibID());
                    return b;
                }
    }
        }
        logger.debug("<--- kollaTillgänglighet bibID = "+ 0);
        throw new SaknasException( "Bok "+ titel +" ej tillgänglig" );
    }

    public Bok kollaTillgänglighet(int isbn) throws SQLException,SaknasException{
        logger.trace("kollaTillgänglighet (isbn)  --->");
        Bok[] listaAvBöcker = DatabasAPI.hämtaTillgänglighet();
        if (listaAvBöcker.length > 0) {
            logger.debug("Lista av böcker har längd: " + listaAvBöcker.length );
            for (Bok b : listaAvBöcker) {
                if (b.getISBN() == isbn) {
                    logger.debug("<--- kollaTillgänglighet bibID " + b.getBibID() + "redo för lån = " +b.getBibID());
                    return b;
                }
            }
        }
        logger.debug("<--- kollaTillgänglighet bibID = "+ 0);
        throw new SaknasException( "Bok "+ isbn +" ej tillgänglig" );
    }
public Konto hämtaKonto(int kontoID)throws Exception{
    logger.debug("P: hämtaKonto  --->");
    Konto medlem = null;
        Konto[] listAvKonton = DatabasAPI.hämtaKonton();
        //------------------------Letar efter kontot i listan från databasen---------------------------Hela -
        for (Konto k : listAvKonton) {
            if (k.getKontoID() == kontoID) {
                logger.debug("konto finns");
                medlem = k;
            }
        }
    if (medlem == null){
     throw new SaknasException("Medlems konto inte hittat");
    }
    else{
        logger.debug(" <--- P: hämtaKonto");
        return medlem;
}
    }
    public Konto kollaLån(Konto medlem) throws BokFörsenadException,MaxBöckerException {
        logger.debug("P: kollaLån  --->");
        Lån[] lånadeBöcker = medlem.getLånadeBöcker();
        for (Lån l : lånadeBöcker) {
            if (l.ärFörsenad()) {
                //----Letar efter försenade böcker----
                logger.debug("försenad bok hittad");
                logger.debug(" <--- P: kollaLån");
                throw new BokFörsenadException("Bok försenad");
            }
            //-------Kollar om medlem har uppnått maximalt antal böcker---------------
            int maxböcker = 0;

            if (medlem.getRoll() == 0) {
                logger.debug("medlem undergrad max 3");
                maxböcker = 3;
            }
            if (medlem.getRoll() == 1) {
                logger.debug("medlem är grad max 5");
                maxböcker = 5;
            }
            if (medlem.getRoll() == 2) {
                logger.debug("doctorate student max 7");
                maxböcker = 7;
            }
            if (medlem.getRoll() == 3) {
                logger.debug("medlem är postDoc or teacher max 10");
                maxböcker = 10;
            }
            logger.debug("kollar om medlem har uppnåt  max böcker");
            if (medlem.getLånadeBöcker().length >= maxböcker) {
                logger.debug("<--- medlem har uppnått max antal böcker (return 0)");
                throw new MaxBöckerException("Medlems max " + maxböcker + " uppnått.");
            }
        }
        //inga förseningar hittade
        logger.debug(" <--- P: kollaLån");
        return medlem;
    }
    public Konto kollaAvstängning(Konto medlem) throws KontoAvstängtException,AvstängningKrävsException,SvartlistningKrävsException {
        logger.debug("P: kollaAvstängning  --->");

        if (medlem.ärAvstängd() == false && medlem.getAntalAvstangningar() < 2) {
            //Inte avstängnd eller i behov av svartlistning.
            logger.debug(" <--- P: kollaAvstängning");
            return medlem;
        }
        if (medlem.ärAvstängd() == true && medlem.getAntalAvstangningar() < 2) {
            //medlem är avstängd
            logger.debug(" <--- P: kollaAvstängning");
            throw new KontoAvstängtException("Medlem är avstängd till" + medlem.getAvstangd());

        }
        if (medlem.getAntalAvstangningar() >= 2) {
            logger.debug("avstängningar =" + medlem.getAntalAvstangningar() + ", Konto bör svartlistas");
            throw new SvartlistningKrävsException(medlem.getKontoID() + "Svartlistas");
        }
        if(medlem.getAntalForseningar()>=3){
            logger.debug(" <--- P: kollaAvstängning Avstängning krävs");
            throw new AvstängningKrävsException("avstängd pga för många förseningar");
        }
        logger.debug(" <--- P: kollaAvstängning");
        return medlem;
    }

    public Konto tempAvstängning(Konto medlem, int antalDagar) throws Exception,SQLException {
        logger.debug(" tempAvstängning ---->");
            int svar = DatabasAPI.registreraTempAvstänging(medlem.getKontoID(), antalDagar);
            if(svar == 0){
                throw new Exception("inget uppdaterat");
            }
            else
            return medlem;
    }


    public Konto svartlistaMedlem(Konto medlem) throws SQLException, FelException {
        logger.debug(" svartlistaMedlem ---->");
        int databasSvar;
        long[] svartlistade;
        svartlistade = this.DatabasAPI.hämtaSvarlistade();
        //kollar om medlemmen redan är svartlistad.
       if (svartlistade != null){
        for (long l : svartlistade) {
            if (l == medlem.getPersonNr()) {
                logger.debug(" <------ svartListaMedlem. inga svartlistade>");
            }
                }
       }
        databasSvar = this.DatabasAPI.läggTillSvartlistade(medlem.getPersonNr());
                    logger.debug(" <------ svartListaMedlem. medlem svartlistad>");
                    return medlem;
            }

    public int regKonto(String fnamn, String enamn, long personNr, int roll) throws SQLException {
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


    public Konto avslutaKonto(Konto medlem) throws SaknasException, SQLException {
        logger.debug(" avslutaKonto ---->");
        Konto[] kontolista = this.DatabasAPI.hämtaKonton();
        int databasSvar;
        for (Konto k : kontolista) {
            if (k.getKontoID() == medlem.getKontoID()) {
                logger.debug("konto hittat - försöker avsluta" + medlem.getKontoID());
                databasSvar = this.DatabasAPI.avslutaKonto(medlem.getKontoID());

                if (databasSvar == 0) {
                    logger.debug("kontot borttaget ur databasen" + medlem.getKontoID());
                    return medlem;
                }
                if (databasSvar == 1) {
                    logger.debug("kontonummer finns inte");
                    throw new SaknasException("kontonummer finns inte");
                }

            }
        }
        return medlem;
    }

    public int registreraLån(int kontoId, int bibID) throws SQLException {
        logger.debug(" registreraLån ---->");
int svar = DatabasAPI.skapaLån(kontoId, bibID);
           if(svar > 0) return 5;
    return 0;
    }

    public int återlämnaBok(Konto medlem, int bibID) throws SQLException{
        logger.debug(" återlämnaBok ---->");
        boolean bokKoppladTillMedlem = false;

        Lån[] lånadeBöcker = DatabasAPI.hämtaLån();

        for (int i = 0; i < lånadeBöcker.length; i++) {
            if (lånadeBöcker[i].getKontoID() == medlem.getKontoID() && lånadeBöcker[i].getBid() == bibID) {
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