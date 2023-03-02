package databasAPI;

import java.math.BigInteger;

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
    private BigInteger personNr;

    //private String behorighetsniva;(redundant)

    //4 siffror
    private int kontoID;
    private boolean avstangd;
    //int[] med alla bid för lånade böcker
    private int[] lanadeBocker;
    private int antalAvstangningar;
    private int antalForseningar;

    public Konto (String fNamn, String eNamn, BigInteger personNr, String roll, int kontoID, boolean avstangd, int[] lanadeBocker, int antalAvstangningar, int antalForseningar) {
        this.fNamn = fNamn;
        this.eNamn = eNamn;
        this.roll = roll;
        this.personNr = personNr;

        //this.behorighetsniva = behorighetsniva;(redundant pga roll)

        this.kontoID = kontoID;
        this.lanadeBocker = lanadeBocker;
        this.avstangd = avstangd;
        this.antalAvstangningar = antalAvstangningar;
        this.antalForseningar = antalForseningar;
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

    public BigInteger getPersonNr() {
        return personNr;
    }

    public void setPersonNr(BigInteger personNr) {
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

    public boolean isAvstangd() {
        return avstangd;
    }

    public void setAvstangd(boolean avstangd) {
        this.avstangd = avstangd;
    }

    public int[] getLanadeBocker() {
        return lanadeBocker;
    }

    public void setLanadeBocker(int[] lanadeBocker) {
        this.lanadeBocker = lanadeBocker;
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
