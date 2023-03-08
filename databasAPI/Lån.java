package databasAPI;

import java.time.LocalDate;
import java.util.Date;

public class Lån {

    private int bid;
    private int kontoID;
    private Date lånDatum;

    LocalDate slutDatum = LocalDate.of(lånDatum.getYear(), lånDatum.getMonth(), lånDatum.getDate()).plusDays(14);

    private Date returnTheBookDate = new Date(slutDatum.getYear(), slutDatum.getMonthValue(), slutDatum.getDayOfMonth());

    public Lån(int bid, int kontoID, Date lånDatum, Date returnTheBookDate){
        this.bid = bid;
        this.kontoID = kontoID;
        this.lånDatum = lånDatum;
        this.returnTheBookDate = returnTheBookDate;

    }

    public Date getReturnTheBookDate() {
        return returnTheBookDate;
    }

    public void setReturnTheBookDate(Date returnTheBookDate) {
        this.returnTheBookDate = returnTheBookDate;
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

    public Date getLånDatum() {
        return lånDatum;
    }

    public void setLånDatum(Date lånDatum) {
        this.lånDatum = lånDatum;
    }
}
