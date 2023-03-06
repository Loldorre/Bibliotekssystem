package databasAPI;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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
    private Connection connection;


    private int kontoID;
    private Date avstangd;
    Lån[]  lånadeBöcker;
    private int antalAvstangningar;
    private int antalForseningar;

    public Konto (String fNamn, String eNamn, long personNr, String roll, int kontoID, Date avstangd, int antalAvstangningar, int antalForseningar) {

        //Kolla anslutning till databasen
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://192.168.50.101:3306/1ik173-server", "Dorian", "Dorian1234");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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

        lånadeBöcker = hämtaLånFörKonto(kontoID);
    }

    public Lån[] hämtaLånFörKonto(int kontoID){

        //arrayOfLoans used with .toArray to create the return array
        ArrayList<Lån> arrayOfLoans = new ArrayList<>();

        //Getting an array of Lån with kontoID which is then returned
        try {
            Statement stmt = connection.createStatement();
            String getTitel = "select * from lån where kontoid=" + kontoID;
            ResultSet rS = stmt.executeQuery(getTitel);
            while (rS.next()) {
                arrayOfLoans.add(new Lån(rS.getInt("bid"), rS.getInt("kontoid"), rS.getDate("låndatum")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        Lån[] loanArray = new Lån[arrayOfLoans.size()];
        arrayOfLoans.toArray(loanArray);

        return loanArray;
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
