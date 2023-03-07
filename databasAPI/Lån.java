package databasAPI;

import java.util.Date;

public class Lån {
    int bid;
    Date datum;
    int kontoID;

    Lån(int bid,Date datum){
        this.bid = bid;
        this.datum = datum;
    }
}
