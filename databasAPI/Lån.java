package databasAPI;

import java.util.Date;

public class Lån {

    private int bid;
    private int kontoID;
    Date lånDatum;

    public Lån(int lån, int kontoID, Date lånDatum){
        this.bid = bid;
        this.kontoID = kontoID;
        this.lånDatum = lånDatum;
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
