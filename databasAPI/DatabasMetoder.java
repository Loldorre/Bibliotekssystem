package databasAPI;
import com.mysql.cj.protocol.Resultset;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;

public class DatabasMetoder implements IDatabas{
    Connection connection;
    DatabasMetoder(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://192.168.50.101:3306/1ik173-server","Dorian","Dorian1234");

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
            String getTitel = "select * from bok where titel=\"" + titel +"\"";
            ResultSet rS = stmt.executeQuery(getTitel);
            while(rS.next()){
                arrayOfBooks.add(new Bok(rS.getInt("isbn"),rS.getString("titel"),rS.getString("författare"),rS.getInt("utgivningsår"),rS.getInt("antal")));
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
    public String läggTillSvartlista(BigInteger personNr) {
        return null;
    }

    @Override
    public String skapaKonto(String fnamn, String enamn, BigInteger personNr, String roll) {
        return null;
    }

    @Override
    public String avslutaKonto(int kontoID) {
        return null;
    }

    @Override
    public Konto[] hämtaKonton() {
        ArrayList<Konto> arrayOfAccounts = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            String getAccount = "select * from konto";
            ResultSet rS = stmt.executeQuery(getAccount);
            while(rS.next()){
                arrayOfAccounts.add(new Konto(rS.getString("fnamn"),rS.getString("enamn"), rS.getLong("personNr"), rS.getString("roll"), rS.getInt("kontoID"), rS.getDate("avstängd"), rS.getInt("antalAvstängningar"),  rS.getInt("antalFörseningar")));
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
        return null;
    }
}
