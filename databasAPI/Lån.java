package databasAPI;

import java.util.Date;

public class Lån {
    int bid;
    Date datum;
    int kontoID;

    public Lån(int bid,Date datum,int kontoID){
        this.bid = bid;
        this.datum = datum;
        this.kontoID = kontoID;
    }
}
