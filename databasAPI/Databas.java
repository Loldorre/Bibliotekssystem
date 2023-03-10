package databasAPI;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.time.ZoneId;

public class Databas implements IDatabas {
    private Connection connection;

   public Databas() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://192.168.50.101:3306/1ik173-server", "Dorian", "Dorian1234");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    //Hämtar en array av böcker som finns i Bok-tabellen och inte inte lån-tabellen
    public Bok[] hämtaTillgänglighet(String titel) {

        //arrayOfBooks used with .toArray to create the return array
        ArrayList<Bok> arrayOfBooks = new ArrayList<>();

        //Getting an array of book with titel which is then returned
        try {
            Statement stmt = connection.createStatement();
            String getTitel = "select * from bok where titel=\"" + titel + "\"";
            ResultSet rS = stmt.executeQuery(getTitel);
            while (rS.next()) {
                arrayOfBooks.add(new Bok(rS.getInt("isbn"), rS.getString("titel"), rS.getString("författare"), rS.getInt("utgivningsår"), rS.getInt("antal")));
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
        Bok[] returBookArray = new Bok[arrayOfBooks.size()];
        arrayOfBooks.toArray(returBookArray);
        return returBookArray;
    }

    @Override
    public int skapaLån(int kontoID, int bid /*bid från samling*/) {

       int returnValue = 0; //1 = fail, everything else is a KontoID which mean success

        //Getting todays date, converting it to Date object, converting to MySQL Date format
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate dateNow = java.time.LocalDate.now();
        Date dateToday = Date.from(dateNow.atStartOfDay(defaultZoneId).toInstant());
        String datePattern = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
        String dateOfLoan = formatter.format(dateToday);

        //Create MySQL query and try to execute it in order to create a loan in database table "lån"
        try {
            Statement stmt = connection.createStatement();
            String getTitel = "insert into lån values(" + bid + "," + kontoID + ",\"" + dateOfLoan+"\")";
            int rS = stmt.executeUpdate(getTitel);

        } catch (SQLException e) {
            returnValue = 1;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return returnValue;
    }


    @Override
    public int läggTillSvartlistade(long personNr) {

       int failOrSuccess = 0;

        try {
            Statement stmt = connection.createStatement();
            String addBlacklist = "insert into svartlista values (" + personNr + ")";
            long rS = stmt.executeUpdate(addBlacklist);
        } catch (SQLException e) {
            failOrSuccess = 1;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return failOrSuccess;
    }


    @Override //Hämtar inte kontoId. ska fixas
    public int skapaKonto(String fnamn, String enamn, long personNr, String roll) {
       int failOrSuccess = 0;

        try {
            Statement stmt = connection.createStatement();
            Statement getStmt = connection.createStatement();
            String addAccount = "insert into konto values (\"" + fnamn + "\",\"" + enamn + "\"," + personNr + ",\"" + roll + "\"," + 0 +", " + null + "," + 0 + "," + 0 + ")";
            String getAddedKontoId = "select kontoID from konto where personNr=" + personNr;
           int rS = stmt.executeUpdate(addAccount);
           ResultSet newRs = getStmt.executeQuery(getAddedKontoId);
           while(newRs.next()) {
               failOrSuccess = newRs.getInt("kontoid");
           }
        } catch (SQLException e) {
            failOrSuccess = 1;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


        return failOrSuccess;
    }
    public static void main(String[] args) {
        Databas x = new Databas();
        System.out.println(x.skapaKonto("Per", "Bolund", 7305240909L, "undergraduate"));
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

    @Override
    public int avslutaKonto(int kontoID) {
       int failOrSuccess = 0;

        try {
            Statement stmt = connection.createStatement();
            String deleteAccount = "delete from konto where kontoID =" + kontoID;
            int rS = stmt.executeUpdate(deleteAccount);

        } catch (SQLException e) {
            failOrSuccess = 1;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return failOrSuccess;
    }


    @Override
    public Konto[] hämtaKonton() {

        ArrayList<Konto> arrayOfAccounts = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            String getAccount = "select * from konto";
            ResultSet rS = stmt.executeQuery(getAccount);
            while (rS.next()) {
                arrayOfAccounts.add(new Konto(rS.getString("fnamn"), rS.getString("enamn"), rS.getLong("personNr"), rS.getInt("roll"), rS.getInt("kontoID"), rS.getDate("avstängd"), rS.getInt("antalAvstängningar"), rS.getInt("antalFörseningar")));
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
        Konto[] returKontoArray = new Konto[arrayOfAccounts.size()];
        arrayOfAccounts.toArray(returKontoArray);

        return returKontoArray;
    }


    @Override
    public int registreraTempAvstänging(int kontoID, int numOfDays) {
        int failOrSuccess = 0;

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
        x.updateAntalAvstängningar(kontoID);

        //Uppdaterar kontots kolumn "avstängd" med date-objektet sqlInputDate
        try {
            Statement stmt = connection.createStatement();
            String getAccount = "update konto set avstängd=\"" + sqlInputDate + "\" where kontoID=" + kontoID;
            int rS = stmt.executeUpdate(getAccount);

        } catch (SQLException e) {
            failOrSuccess = 1;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return failOrSuccess;
    }


    @Override
    public int taBortLån(int bid){

       int failOrSuccess = 0;
        try {
            Statement stmt = connection.createStatement();
            String getAccount = "delete from lån where bid="+bid;
            int rS = stmt.executeUpdate(getAccount);

        } catch (SQLException e) {
            failOrSuccess = 1;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return failOrSuccess;
    }


    @Override
    public int updateAntalAvstängningar(int kontoID) {

       int failOrSuccess = 0;

        int amountOfBan = 0;
        Databas accessKonto = new Databas();
        for (Konto kon : accessKonto.hämtaKonton()) {
            if (kon.getKontoID() == kontoID) {
                amountOfBan = kon.getAntalAvstangningar();
                amountOfBan++;
            }
        }

        try {
            Statement stmt = connection.createStatement();
            String getAccount = "update konto set antalAvstängningar="+amountOfBan + " where kontoID="+kontoID;
            int rS = stmt.executeUpdate(getAccount);

        } catch (SQLException e) {
            failOrSuccess=1;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return failOrSuccess;
    }

    @Override
    public int updateAntalFörseningar(int kontoID) {

       int failOrSuccess = 0;
        int amountOfLateReturns = 0;
        Databas accessKonto = new Databas();
        for (Konto kon : accessKonto.hämtaKonton()) {
            if (kon.getKontoID() == kontoID) {
                amountOfLateReturns = kon.getAntalForseningar();
                amountOfLateReturns++;
            }
        }

        try {
            Statement stmt = connection.createStatement();
            String getAccount = "update konto set antalFörseningar="+amountOfLateReturns+" where kontoID="+kontoID;
            int rS = stmt.executeUpdate(getAccount);

        } catch (SQLException e) {
            failOrSuccess = 1;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return failOrSuccess;
    }


    @Override
    // Klar tack vare Z
    public long[] hämtaSvarlistade() {

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


        return returnBlacklistArray;
    }

    public Lån[] hämtaLån(){

        ArrayList<Lån> loans = new ArrayList<>();

        //Getting an array of book with titel which is then returned
        try {
            Statement stmt = connection.createStatement();
            String getTitel = "select * from lån";
            ResultSet rS = stmt.executeQuery(getTitel);
            while (rS.next()) {
                loans.add(new Lån(rS.getInt("bid"), rS.getInt("kontoid"), rS.getDate("lånDatum")));
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
        Lån[] arrayOfLoans = new Lån[loans.size()];
        loans.toArray(arrayOfLoans);

       return arrayOfLoans;
    }



}




