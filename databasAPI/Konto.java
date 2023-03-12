package databasAPI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private int roll;
    private long personNr;


    private int kontoID;
    private LocalDate avstangd;
    Lån[]  lånadeBöcker;
    private int antalAvstangningar;
    private int antalForseningar;
    private static Logger logger = LogManager.getLogger(Konto.class.getName());
    public Konto (String fNamn, String eNamn, long personNr, int roll, int kontoID, LocalDate avstangd, Lån[] lån, int antalAvstangningar, int antalForseningar) {

        this.fNamn = fNamn;
        this.eNamn = eNamn;
        this.roll = roll;
        this.personNr = personNr;
        this.kontoID = kontoID;
        this.avstangd = avstangd;
        this.lånadeBöcker = lån;
        this.antalAvstangningar = antalAvstangningar;
        this.antalForseningar = antalForseningar;
    }
    public boolean ärAvstängd() {
        logger.debug("ärAvstängd  --->");
        LocalDate avstängd = this.getAvstangd();

        if(avstängd!=null&&(avstängd.isAfter(LocalDate.now()))){
            //medlem är avstängd
            logger.debug("<--- ärAvstängd true till("+avstangd.toString() +")");
            return true;
        }
        if(avstängd.isBefore(LocalDate.now())){
            //Endast gamla avstängningar.
            logger.debug("<--- ärAvstängd (false men har gammal) "+avstängd.toString() +" före " + LocalDate.now().toString());
            return false;
        }
        //Inga avstängningar.
        logger.debug(" <--- ärAvstängd (false)");
        return false;

    }

    public boolean börAvstängas(){
        if(this.antalForseningar>2) return true;
        return false;
    }

    public boolean börSvartlistas(){
        if (this.antalAvstangningar>1){
            return true;
        }
        return false;
    }
    public boolean harLån(){
        if (lånadeBöcker.length > 0) return true;
        return false;
    }
    public int antalFörseningar(){
        logger.debug("antalFörseningar  --->");
        int försening=0;
        for (Lån l : lånadeBöcker) {
            if (l.ärFörsenad()) {
                försening++;
            }
        }
                logger.debug(" <--- antalFörseningar)");
                return försening;
    }
    public boolean harMaxLån(){
        logger.debug("harMaxlån  --->");

            if (lånadeBöcker.length >= this.getMaxLån()) {
                logger.debug("<--- harMaxlån true "+this.getMaxLån()+"st");
               return true;
            }
        logger.debug(" <--- harMaxlån false");
        return false;
    }
public int getMaxLån(){
    int maxböcker = 0;

    if (this.roll == 0) {
        logger.debug("medlem undergrad max 3");
        maxböcker = 3;
    }
    if (this.roll == 1) {
        logger.debug("medlem är grad max 5");
        maxböcker = 5;
    }
    if (this.roll == 2) {
        logger.debug("doctorate student max 7");
        maxböcker = 7;
    }
    if (this.roll == 3) {
        logger.debug("medlem är postDoc or teacher max 10");
        maxböcker = 10;
    }
    return maxböcker;
}
    public boolean harFörsening() {
        logger.debug("harFörsening  --->");
        for (Lån l : lånadeBöcker) {
            if (l.ärFörsenad()) {
                //----Letar efter försenade böcker----
                logger.debug("försenad bok hittad");
                logger.debug(" <--- P: kollaLån");
                logger.debug(" <--- harFörsening true: bid" + l.getBid());
                return true;
            }
        }
        logger.debug(" <--- harFörsening false");
        return false;
    }
public LocalDate getAvstangd(){
        return this.avstangd;
    }
    public Lån[] getLånadeBöcker() {
        return lånadeBöcker;
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

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public long getPersonNr() {
        return personNr;
    }

    public void setPersonNr(long personNr) {
        this.personNr = personNr;
    }
    public int getKontoID() {
        return kontoID;
    }

    public void setKontoID(int kontoID) {
        this.kontoID = kontoID;
    }

    public LocalDate isAvstangd() {
        return avstangd;
    }

    public void setAvstangd(LocalDate avstangd) {
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

    public static void main(String[] args) {

        Date avstängd = new Date(2023,11,12);


        LocalDate test = avstängd.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate().minusYears(1900);

        System.out.println(test);

        System.out.println("t1  "+LocalDate.now().toString());
        System.out.println("t2  "+LocalDate.parse("2025-12-11").toString());
        LocalDate t1 = LocalDate.now();
        LocalDate t2 = LocalDate.parse("2025-12-11");

        System.out.println("t1 is before t2= "+t1.isBefore(t2));
    }


}