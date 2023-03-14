package processlagerAPI;

import databasAPI.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static java.time.temporal.ChronoUnit.DAYS;

public class Process {
    private static Logger logger = LogManager.getLogger(Process.class.getName());

    Databas DatabasAPI = null;

    public Process() {
        this.DatabasAPI = new Databas();
    }
    public Process(Databas db) {
        this.DatabasAPI = db;
    }

    //Sanja
   public String[] hämtaSamling() throws Exception{
       Bok[] tillgängliga = DatabasAPI.hämtaBöcker();
       String[] böcker = new String[tillgängliga.length];
       for(int i=0;i<tillgängliga.length;i++){
           böcker[i]="bokID: "+tillgängliga[i].getBibID()+
                    ", Titel: "+tillgängliga[i].getTitel()+
                    ", Författare: "+tillgängliga[i].getForfattare()+
                    ", Utgivningsår: "+tillgängliga[i].getUtgivningsar()+
                    ", ISBN: "+tillgängliga[i].getISBN()+",";
       }
       return böcker;
    }
    //Linnea
    public Bok kollaTillgänglighet(int isbn) throws SQLException{
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
        logger.debug("<--- kollaTillgänglighet bok inte tillgänglig");
        return null;
    }
    //Viktor
public Konto hämtaKonto(int kontoID) throws Exception{
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
     logger.debug(" <--- hämtaKonto: Medlems konto inte hittat");
     return medlem;
    }
    else{
        logger.debug(" <--- hämtaKonto: medlem hittad");
        return medlem;
}
    }

    //Dorian
    public Konto tempAvstängning(Konto medlem, int antalDagar) throws Exception {
        logger.debug(" tempAvstängning ---->");
        int extra = 0;

            if(medlem.ärAvstängd()){
                logger.debug(" Är redan asvtängd");
                LocalDate slut= medlem.getAvstangd();
                extra = (int)DAYS.between(LocalDate.now(),slut);
                logger.debug("lägger till: "+extra+" dagar.");
            }
            medlem = DatabasAPI.registreraTempAvstänging(medlem, antalDagar+extra);
        logger.debug(" uppdaterar antal förseningar i medlems objekt, nu: "+(medlem.getAntalAvstangningar()+1));
        medlem.setAntalAvstangningar((medlem.getAntalAvstangningar()+1));
        logger.debug(" <--- tempAvstängning ");
            return medlem;
    }

    //Linnea
    public Konto svartlistaMedlem(Konto medlem) throws SQLException {
        logger.debug(" svartlistaMedlem ---->");
        int databasSvar;
        long[] svartlistade;
        svartlistade = this.DatabasAPI.hämtaSvarlistade();

        //kollar om medlemmen redan är svartlistad.
       if (svartlistade != null){
        for (long l : svartlistade) {
            if (l == medlem.getPersonNr()) {
                logger.debug(" <----- svartListaMedlem. personnummer redan svartlistat");
                return null;
            }
                }
       }
        databasSvar = this.DatabasAPI.läggTillSvartlistade(medlem.getPersonNr());
       if(databasSvar==1){
           logger.debug(" <------ svartListaMedlem. Inte svartlistad");
           return null;
       }
       logger.debug(" <------ svartListaMedlem. Svartlistad>");
                    return medlem;
            }
//Dorian
    public int regKonto(String fnamn, String enamn, long personNr, int roll) throws Exception {
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
        logger.debug("Slumpar fram unikt id.");

        int random = (int)(Math.random() * 10000);
        ArrayList kontoidlista = new ArrayList<>();
        for(Konto k:kontolista){
            kontoidlista.add(k.getKontoID());
        }
        while (kontoidlista.contains(random)||random<1000){
            random =(int)(Math.random() * 10000);
        }
        logger.debug("unikt id hittat");
        int kontoID = random;
        logger.debug("skapar konto");
        databasSvar = this.DatabasAPI.skapaKonto(fnamn, enamn, personNr, roll,kontoID);
        if(databasSvar>999){
            logger.debug("<----- skapaKonto()"+ fnamn+", "+enamn+","+personNr+"," + roll);
            return databasSvar;}

        else{
            logger.debug("<----- skapaKonto() databas strul...");
            return 3;
        }
    }

//Sanja
    public Konto avslutaKonto(Konto medlem) throws SQLException {
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
                    return null;
                }
            }
        }
        return medlem;
    }

    //Dorian
    public int registreraLån(int kontoId, int bibID) throws SQLException {
        logger.debug(" registreraLån ---->");
int svar = DatabasAPI.skapaLån(kontoId, bibID);
           if(svar > 0) return 5;
        logger.debug("<----- registreraLån ");
    return 0;
    }

    //Sanja
    public int återlämnaBok(Konto medlem, int bibID) throws SQLException{
        logger.debug(" återlämnaBok ---->");
        boolean bokKoppladTillMedlem = false;

        Lån[] lånadeBöcker = medlem.getLånadeBöcker();

        for (Lån l:lånadeBöcker) {
            if (l.getKontoID() == medlem.getKontoID() && l.getBid() == bibID) {
                logger.debug("Bok lånad av användare");
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
        } else if (databasSvar == 0) {
            logger.debug("<----- Återlämna bok ");
            return 0;
        } else {
            return 1;
        }
    }

}