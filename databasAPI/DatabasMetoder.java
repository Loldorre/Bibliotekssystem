package databasAPI;
import com.mysql.cj.protocol.Resultset;

import javax.swing.plaf.nimbus.State;
import javax.xml.crypto.Data;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.time.ZoneId;

public class DatabasMetoder implements IDatabas {
    int kontoIdDecider = 0;
    Connection connection;

    DatabasMetoder() {
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
        ArrayList<Bok> arrayOfBooks = new ArrayList<>();
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
    public String skapaLån(Date startDatum, int kontoID, int ISBN) {
        return null;
    }

    @Override
    public String taBortLån(int kontoID, int ISBN) {
        return null;
    }

    @Override
    public String läggTillSvartlista(long personNr) {
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
    public String registreraTempAvstänging(int kontoID) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate todaysDate = java.time.LocalDate.now();
        LocalDate endOfBan = todaysDate.plusDays(14);
        Date inputDate = Date.from(endOfBan.atStartOfDay(defaultZoneId).toInstant());
        String datePattern = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
        String sqlInput = formatter.format(inputDate);

        //Hämtar tidigare antalAvstängningar och sparar det i amountOfBan som sen blir en större
        int amountOfBan = 0;
        DatabasMetoder accessKonto = new DatabasMetoder();
        for (Konto kon : accessKonto.hämtaKonton()) {
            if (kon.getKontoID() == kontoID) {
                amountOfBan = kon.getAntalAvstangningar();
                amountOfBan++;
            }
        }


        try {
            Statement stmt = connection.createStatement();
            String getAccount = "update konto set antalAvstängningar=" + amountOfBan + ", avstängd=\"" + sqlInput + "\" where kontoID=" + kontoID;
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

    public static void main(String[] args) {
        DatabasMetoder x = new DatabasMetoder();
        x.registreraTempAvstänging(1111);
    }}


