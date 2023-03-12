package databasAPI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import processlagerAPI.TestProcess;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.time.ZoneId;

public class Databas {
    private Connection connection;
    private static Logger logger = LogManager.getLogger(Databas.class.getName());
    public Databas() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://192.168.50.101:3306/1ik173-server", "Dorian", "Dorian1234");
            logger.debug("Databas ansluten ----->");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Hämtar en array av böcker som finns i Bok-tabellen och inte inte lån-tabellen

    //Hämtar en array av böcker som finns i Bok-tabellen och inte inte lån-tabellen
    public Bok[] hämtaTillgänglighet()throws SQLException {
        logger.debug("hämtaTillgänglighet ----->");
        //arrayOfBooks used with .toArray to create the return array
        ArrayList<Bok> arrayOfBooks = new ArrayList<>();
        //Getting an array of book with titel which is then returned

        Statement stmt = connection.createStatement();
        String getTitel = "SELECT distinct bok.titel, bok.författare, bok.utgivningsår, samling.bid,samling.isbn FROM samling,lån,bok where bok.isbn = samling.isbn and\n" +
                "samling.bid not in (select bid from lån) order by bid;";
        logger.debug("hämtar tillgängliga böcker ");
        ResultSet rS = stmt.executeQuery(getTitel);
        while (rS.next()) {
            logger.debug("lägger till: " + rS.getString("titel"), rS.getInt("bid"));
            arrayOfBooks.add(new Bok(rS.getInt("bid"), rS.getInt("isbn"), rS.getString("titel"), rS.getString("författare"), rS.getInt("utgivningsår")));
        }
        Bok[] returBookArray = new Bok[arrayOfBooks.size()];
        arrayOfBooks.toArray(returBookArray);
        logger.debug("<------ hämtaTillgänglighet");
        return returBookArray;
    }
    public int skapaLån(int kontoID, int bid) throws SQLException{
        logger.debug("SkapaLån ----->");

        //Getting todays date, converting it to Date object, converting to MySQL Date format
        ZoneId defaultZoneId = ZoneId.systemDefault();
        //LocalDates för nu och återlämning.
        LocalDate dateNow = java.time.LocalDate.now();
        LocalDate dateReturn = java.time.LocalDate.now().plusDays(30);
        //Date objekt skapade fån localdates
        Date dateToday = Date.from(dateNow.atStartOfDay(defaultZoneId).toInstant());
        Date dateThen = Date.from(dateReturn.atStartOfDay(defaultZoneId).toInstant());
        //omformatering av dates för input i databasen
        String datePattern = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
        String dateOfLoan = formatter.format(dateToday);
        String returnOfLoan = formatter.format(dateThen);

        //Create MySQL query and try to execute it in order to create a loan in database table "lån"

        Statement stmt = connection.createStatement();
        String skapaLån = "insert into lån values(" + bid + "," + kontoID + ",\"" + dateOfLoan+"\","+"\""+returnOfLoan+"\");";
        int rS = stmt.executeUpdate(skapaLån);
        if(rS==0){
            logger.debug("<------ skapaLån: misslyckat ");
            return 1;
        }
        logger.debug("<------ skapaLån: lyckat ");
        return 0;
    }

    public int läggTillSvartlistade(long personNr) throws SQLException{
        logger.debug("läggTillSartlistade ----->");
        Statement stmt = connection.createStatement();
        String addBlacklist = "insert into svartlista values (" + personNr + ")";
        long rS = stmt.executeUpdate(addBlacklist);
        if (rS == 0) {
            logger.debug("inga fält uppdaterade");
            return 1;
        } else {
            logger.debug("<----- läggTillSartlistade ");
            return 0;
        }
    }

    public int skapaKonto(String fnamn, String enamn, long personNr, int roll,int kontoID) throws Exception {
        logger.debug("Skapa Konto ----->");
        int failOrSuccess = 0;

            Statement stmt = connection.createStatement();
            Statement getStmt = connection.createStatement();
            String addAccount = "insert into konto values (\"" + fnamn + "\",\"" + enamn + "\"," + personNr + ",\"" + roll + "\"," + kontoID +", " + null + "," + 0 + "," + 0 + ")";
            String getAddedKontoId = "select kontoID from konto where personNr=" + personNr;
            int rS = stmt.executeUpdate(addAccount);
            if (rS == 0) {
                return 1;}
            ResultSet newRs = getStmt.executeQuery(getAddedKontoId);
            while(newRs.next()) {
                failOrSuccess = newRs.getInt("kontoid");
            }

        logger.debug("<----- Skapa Konto "+ failOrSuccess);
        return failOrSuccess;
    }

    public Konto[] hämtaKonton() throws SQLException {
        logger.debug("hämtaKonton ------->");
        ArrayList<Konto> arrayOfAccounts = new ArrayList<>();
        Statement stmt = connection.createStatement();
        Statement stmt2 = connection.createStatement();
        logger.debug("Databas ansluten");
        String getAccount = "select * from konto";
        ResultSet rS = stmt.executeQuery(getAccount);
        logger.debug("konton hämtade till rs");
        while (rS.next()) {

            //-------Hämtar lån från databsen baserat på kontoID---------
            String getLån = "SELECT lån.bid,lån.kontoid,lån.låndatum,lån.återlämningsdatum FROM lån,konto where konto.kontoid= lån.kontoid and konto.kontoid ="
                    + rS.getInt("kontoID") + ";";

            ResultSet lRS = stmt2.executeQuery(getLån);

            logger.debug("kontos lån hämtade till lRS");
            ArrayList<Lån> arrayOfLån = new ArrayList<>();
            Lån[] lånArray;

            while (lRS.next()) {
                Date låndate = new Date(
                        lRS.getDate("lånDatum").getYear(),
                        lRS.getDate("lånDatum").getMonth(),
                        lRS.getDate("lånDatum").getDay());
                Date slutdate = new Date(
                        lRS.getDate("återlämningsdatum").getYear(),
                        lRS.getDate("återlämningsdatum").getMonth(),
                        lRS.getDate("återlämningsdatum").getDay()
                );


                LocalDate lånDatum = låndate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate().minusDays(16);
                LocalDate återlämningsdatum = slutdate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate().plusDays(9);

                arrayOfLån.add(new Lån(lRS.getInt("bid"), lRS.getInt("kontoID"), lånDatum, återlämningsdatum));
            }

            logger.debug("lån lagda i Lånarray-list");
            lånArray = new Lån[arrayOfLån.size()];
            logger.debug("Lånarray-list till array");
            arrayOfLån.toArray(lånArray);
            logger.debug("skapar kontobjekt: " + rS.getInt("kontoID"));

            if (rS.getDate("avstängd") != null) {
                Date avstängd = new Date(
                        rS.getDate("avstängd").getYear(),
                        rS.getDate("avstängd").getMonth(),
                        rS.getDate("avstängd").getDay());
                LocalDate localavstängd= avstängd.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate().plusDays(12);
                arrayOfAccounts.add(new Konto(
                        rS.getString("fnamn"),
                        rS.getString("enamn"),
                        rS.getLong("personNr"),
                        rS.getInt("roll"),
                        rS.getInt("kontoID"),
                        localavstängd,
                        lånArray,
                        rS.getInt("antalAvstängningar"),
                        rS.getInt("antalFörseningar")));

                logger.debug("kontoobjekt lagt till arrayOfAccounts" + rS.getInt("kontoID"));
                logger.debug("antal böcker lånade: " + lånArray.length);

            } else {
                Date avstängd = new Date();
                LocalDate localavstängd = avstängd.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                arrayOfAccounts.add(new Konto(
                        rS.getString("fnamn"),
                        rS.getString("enamn"),
                        rS.getLong("personNr"),
                        rS.getInt("roll"),
                        rS.getInt("kontoID"),
                        localavstängd,
                        lånArray,
                        rS.getInt("antalAvstängningar"),
                        rS.getInt("antalFörseningar")));

                logger.debug("kontoobjekt lagt till arrayOfAccounts" + rS.getInt("kontoID"));
                logger.debug("antal böcker lånade: " + lånArray.length);
            }
        }
        Konto[] returKontoArray = new Konto[arrayOfAccounts.size()];
        arrayOfAccounts.toArray(returKontoArray);
        logger.debug("Kont[] returned");
        logger.debug("<----- hämtaKonto()" + "antal konton = " + returKontoArray.length);
        return returKontoArray;
    }
    public int avslutaKonto(int kontoID) throws SQLException {
        logger.debug("avsluta konto ------->");
        int failOrSuccess;

        logger.debug("skapar statement");
        Statement stmt = connection.createStatement();

        String deleteAccount = "delete from konto where kontoID =" + kontoID+";";
        logger.debug("kör sql kommando");
        int rS = stmt.executeUpdate(deleteAccount);
        if(rS == 0){
            logger.debug("konto gick inte att avsluta");
            failOrSuccess = 1;
            return failOrSuccess;}
        logger.debug("<------- avsluta konto " + 0);
        failOrSuccess = 0;
        return failOrSuccess;
    }

    public Konto registreraTempAvstänging(Konto medlem, int numOfDays) throws SQLException {
        logger.debug("registreraTempAvstänging ------->");

        //Hämtar dagens daturm, lägger på numOfDays och gör om till ett MySQL-vänligt Date objekt
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate todaysDate = java.time.LocalDate.now();
        LocalDate endOfBan = todaysDate.plusDays(numOfDays);
        Date inputDate = Date.from(endOfBan.atStartOfDay(defaultZoneId).toInstant());
        String datePattern = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
        String sqlInputDate = formatter.format(inputDate);

        //Ökar antalet avstängningar med 1
        Databas x = new Databas();
        x.updateAntalAvstängningar(medlem);

        //Uppdaterar kontots kolumn "avstängd" med date-objektet sqlInputDate

            Statement stmt = connection.createStatement();
            String getAccount = "update konto set avstängd=\"" + sqlInputDate + "\" where kontoID=" + medlem.getKontoID();
            int rS = stmt.executeUpdate(getAccount);


        logger.debug("<------- registreraTempAvstänging ");
        return medlem;
    }


    public int taBortLån(int bid) throws SQLException{
        logger.debug("taBortLån ------->");

        Statement stmt = connection.createStatement();
        String getAccount = "delete from lån where bid="+bid+";";
        int rS = stmt.executeUpdate(getAccount);
        logger.debug("<------- taBortLån " + "lånet fanns inte");
        if(rS<1){
            return 1;
        }
        logger.debug("<------- taBortLån ");
        return 0;
    }


    public Konto updateAntalAvstängningar(Konto medlem) throws SQLException {
        logger.debug("updateAntalAvstängningar -----> ");

        int amountOfBan = medlem.getAntalAvstangningar();
                amountOfBan++;


            Statement stmt = connection.createStatement();
            String getAccount = "update konto set antalAvstängningar="+amountOfBan + " where kontoID="+medlem.getKontoID();
            int rS = stmt.executeUpdate(getAccount);
            medlem.setAntalForseningar(amountOfBan);
        logger.debug("<------- updateAntalAvstängningar ");
        return medlem;
    }


    // Klar tack vare Z
    public long[] hämtaSvarlistade() {
        logger.debug("hämtaSvartlistade -----> ");
        //arrayOfBooks used with .toArray to create the return array
        ArrayList<Long> arrayOfBlacklist = new ArrayList<>();

        //Getting an array of book with titel which is then returned
        try {
            Statement stmt = connection.createStatement();
            String getPersonNr = "select personNr from svartlista";
            ResultSet rS = stmt.executeQuery(getPersonNr);
            while (rS.next()) {
                arrayOfBlacklist.add(rS.getLong("personNr"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        long[] returnBlacklistArray = new long[arrayOfBlacklist.size()];
        int i = 0;
        for (Long item : arrayOfBlacklist) {
            returnBlacklistArray[i] = item;
            i++;
        }
        logger.debug("<------- hämtaSvartlistade (listas längd = " + returnBlacklistArray.length);
        return returnBlacklistArray;
    }

    public Bok[] hämtaBöcker() throws SQLException{
        logger.debug("Databas: hämtaBöcker ------->");
        Statement stmt = connection.createStatement();
        ArrayList<Bok> arrayOfBooks = new ArrayList();
        String getBöcker = "SELECT distinct bok.titel, bok.författare,bok.utgivningsår, bok.isbn, samling.bid FROM bok,samling,lån where\n" +
                "bok.isbn = samling.isbn and samling.bid not in(select bid from lån) order by samling.bid;";
        logger.debug("hämtar alla böcker ");
        ResultSet rS = stmt.executeQuery(getBöcker);

        while (rS.next()) {
            arrayOfBooks.add(new Bok( rS.getInt("bid"), rS.getInt("isbn"), rS.getString("titel"), rS.getString("författare"), rS.getInt("utgivningsår")));
        }
        Bok[] returBookArray = new Bok[arrayOfBooks.size()];
        arrayOfBooks.toArray(returBookArray);
        logger.debug("<------ Databas: hämtaBöcker");
        return returBookArray;
    }


    //Förvandlar localDate till sql-vänlig Date...
    public static Date convertToDateUsingDate(LocalDate date) {
        return java.sql.Date.valueOf(date);
    }

    //Förvandlar localDate till sql-vänlig Date genom instant...
    public static Date convertToDateUsingInstant(LocalDate date) {
        return java.util.Date.from(date.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
    public boolean updateraFörseningar(Konto medlem,int antal) throws SQLException{
        logger.debug("Databas: updateraFörseningar -----> ");
        Statement stmt = connection.createStatement();
        String uppdateraFöreseningar = "update konto set antalFörseningar="+antal+" where kontoID="+ medlem.getKontoID()+";";
        logger.debug("<------- Databas: updateraFörseningar ");
        return true;
    }
    public boolean uppdateraAvstängningar(Konto medlem,int antal)throws SQLException{
        logger.debug("Databas: updateraAvstängningar -----> ");
        Statement stmt = connection.createStatement();
        String uppdateraAvstängningar = "update konto set antalAvstängningar="+antal+" where kontoID="+ medlem.getKontoID()+";";
        logger.debug("<------- Databas: updateraAvstängningar ");
        return true;
    }
}
