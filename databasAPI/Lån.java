package databasAPI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;

public class Lån {

    private int bid;
    private int kontoID;
    private LocalDate lånDatum;
    private LocalDate återlämningsDatum;

    private static Logger logger = LogManager.getLogger(Lån.class.getName());

    public Lån(int bid, int kontoID, LocalDate lånDatum, LocalDate återlämningsDatumDatum) {
        this.bid = bid;
        this.kontoID = kontoID;
        this.lånDatum = lånDatum;
        this.återlämningsDatum = återlämningsDatumDatum;
    }

    public boolean ärFörsenad() {
        logger.debug("Lån: ärFörsenad  --->");
        //----Letar efter försenade böcker----
        if (LocalDate.now().isAfter(this.återlämningsDatum)) {
            logger.debug("försenad bok hittad");
            logger.debug(" <--- P: kollaLån = True");
            return true;
        }
        //inga förseningar hittade
        logger.debug(" <--- P: ärFörsenad = False");
        return false;
    }
    @Override
    public String toString() {
        String lån = "BokID: " + this.getBid() + ", Låndatum: " + this.lånDatum + ", Återlämningsdatum: " + this.återlämningsDatum;
        return lån;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getKontoID() {
        return kontoID;
    }

    public void setKontoID(int kontoID) {
        this.kontoID = kontoID;
    }

    public LocalDate getLånDatum() {
        return lånDatum;
    }

    public void setLånDatum(LocalDate lånDatum) {
        this.lånDatum = lånDatum;
    }

}