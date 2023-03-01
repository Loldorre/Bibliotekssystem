package databasAPI;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

public class Databas implements IDatabas {
    Connection conn = null;

    //konstruktor som ger databasens adress och inlogg till användare+lösenord för inloggning.
    public Databas(String adress, String användarnamnOchLösenord){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
        }
        //-- Connect to the server via adress and login via username and password. if any error prints results.
        // Connection made via variable "conn".
        try {
            conn = DriverManager.getConnection(adress + användarnamnOchLösenord);

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    @Override
    public Bok[] hämtaTillgängligheten(String titel, String författare, int ISBN) {
        return new Bok[0];
    }
    @Override
    public Bok[] hämtaTillgängligheten(int bibID) {
        return new Bok[0];//Dorian gjorde en ändring
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
        return new Konto[0];
    }
}
