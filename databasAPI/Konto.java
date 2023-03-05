package databasAPI;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Konto {
    private String fNamn;
    private String eNamn;
    /* -- undergraduate,postgraduate,candidate,teacher (måste vara definierat så man vet vad man ska sätta in vid registrering... borde egentligen vara en int tycker jag)
    Max lån:
    undergraduate = 3
    postgraduate = 5
    candidate = 7
    teacher = 10
    * */
    private String roll;
    private long personNr;


    private int kontoID;
    private Date avstangd;
    Lån[]  lånadeBöcker;
    private int antalAvstangningar;
    private int antalForseningar;

    public Konto (String fNamn, String eNamn, long personNr, String roll, int kontoID, Date avstangd, int antalAvstangningar, int antalForseningar) {
        this.fNamn = fNamn;
        this.eNamn = eNamn;
        this.roll = roll;
        this.personNr = personNr;
        this.kontoID = kontoID;
        this.avstangd = avstangd;
        if (roll=="undergraduate"){ lånadeBöcker=new Lån[3];}
        else if (roll=="postgraduate"){ lånadeBöcker=new Lån[5];}
        else if (roll=="candidate"){ lånadeBöcker=new Lån[7];}
        else if (roll=="teacher"){ lånadeBöcker=new Lån[10];}
        this.antalAvstangningar = antalAvstangningar;
        this.antalForseningar = antalForseningar;
    }

    public boolean kollaFörsening(int kontoID){
        boolean förseningsStatus = true;

        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate todaysDate = java.time.LocalDate.now();
        Date inputDate = Date.from(todaysDate.atStartOfDay(defaultZoneId).toInstant());
        String datePattern = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
        String sqlInputDate = formatter.format(inputDate);



        return förseningsStatus;
    }

    public String getfNamn() {
        return fNamn;
    }

    public void setfNamn(String fNamn) {
        this.fNamn = fNamn;
    }

    public String geteNamn() {
        return eNamn;
    }

    public void seteNamn(String eNamn) {
        this.eNamn = eNamn;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public long getPersonNr() {
        return personNr;
    }

    public void setPersonNr(long personNr) {
        this.personNr = personNr;
    }

/*
    public String getBehorighetsniva() {
        return behorighetsniva;
    }

    public void setBehorighetsniva(String behorighetsniva) {
        this.behorighetsniva = behorighetsniva;
    }
*/

    public int getKontoID() {
        return kontoID;
    }

    public void setKontoID(int kontoID) {
        this.kontoID = kontoID;
    }

    public Date isAvstangd() {
        return avstangd;
    }

    public void setAvstangd(Date avstangd) {
        this.avstangd = avstangd;
    }

    public int getAntalAvstangningar() {
        return antalAvstangningar;
    }

    public void setAntalAvstangningar(int antalAvstangningar) {
        this.antalAvstangningar = antalAvstangningar;
    }

    public int getAntalForseningar() {
        return antalForseningar;
    }

    public void setAntalForseningar(int antalForseningar) {
        this.antalForseningar = antalForseningar;
    }
}
