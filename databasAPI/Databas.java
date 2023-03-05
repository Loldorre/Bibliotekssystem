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
    private int kontoIdDecider = 0;
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
    public String skapaLån(int kontoID, int bid /*bid från samling*/) {

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
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return "Lån registrerat";
    }



    @Override
    public String taBortLån(int kontoID, int bid) {

        //Create MySQL query and try to execute it in order to remove loan från databas table "lån"
        try {
            Statement stmt = connection.createStatement();
            String getTitel = "delete from lån where kontoID="+kontoID+" and bid="+bid;
            int rS = stmt.executeUpdate(getTitel);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }


    @Override
    public String läggTillSvartlistade(long personNr) {
        try {
            Statement stmt = connection.createStatement();
            String addBlacklist = "insert into svartlista values (" + personNr + ")";
            long rS = stmt.executeUpdate(addBlacklist);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return personNr + " tillagt i svartlista";
    }


    @Override
    public String skapaKonto(String fnamn, String enamn, long personNr, String roll) {
        try {
            Statement stmt = connection.createStatement();
            String addAccount = "insert into konto values (\"" + fnamn + "\",\"" + enamn + "\"," + personNr + ",\"" + roll + "\"," + kontoIdDecider + "," + null + "," + 0 + "," + 0 + ")";
            int rS = stmt.executeUpdate(addAccount);
            kontoIdDecider++;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return "Konto för " + fnamn + " " + enamn + " har skapats";
    }


    @Override
    public String avslutaKonto(int kontoID) {
        try {
            Statement stmt = connection.createStatement();
            String deleteAccount = "delete from konto where kontoID =" + kontoID;
            int rS = stmt.executeUpdate(deleteAccount);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return "Kontot har tagits bort";
    }


    @Override
    public Konto[] hämtaKonton() {
        ArrayList<Konto> arrayOfAccounts = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            String getAccount = "select * from konto";
            ResultSet rS = stmt.executeQuery(getAccount);
            while (rS.next()) {
                arrayOfAccounts.add(new Konto(rS.getString("fnamn"), rS.getString("enamn"), rS.getLong("personNr"), rS.getString("roll"), rS.getInt("kontoID"), rS.getDate("avstängd"), rS.getInt("antalAvstängningar"), rS.getInt("antalFörseningar")));
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
    public String registreraTempAvstänging(int kontoID, int numOfDays) {

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
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return "Temporär avstängning registrerad";
    }


    @Override
    public String returnBook(int bid){

        try {
            Statement stmt = connection.createStatement();
            String getAccount = "delete from lån where bid="+bid;
            int rS = stmt.executeUpdate(getAccount);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


        return "Bok är återlämnad";
    }


    @Override
    public String updateAntalAvstängningar(int kontoID) {
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
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return "antal avstägningar uppdaterat";
    }

    @Override
    public String updateAntalFörseningar(int kontoID) {
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
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return "antal förseningar uppdaterat";
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



}




