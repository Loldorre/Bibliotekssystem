package databasAPI;

import java.sql.*;
import java.util.Date;

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
    int kontoIdDecider = 0;
    Connection connection;

    public Databas() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://192.168.50.101:3306/1ik173-server", "Dorian", "Dorian1234");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int taBortLån(int bibID) {
        return 0;
    }

    @Override
    public Lån[] hämtaLån() {
        return new Lån[0];
    }

    @Override
    public Konto[] hämtaKonton() {
        return new Konto[0];
    }

    @Override
    public int updateAntalFörseningar(int kontoID) {
        return 0;
    }

    @Override
    public int registreraTempAvstänging(int kontoID, int numOfDays) {
        return 0;
    }

    @Override
    public int updateAntalAvstängningar(int kontoID) {
        return 0;
    }

    @Override
    public long[] hämtaSvarlistade() {
        return new long[0];
    }
    @Override
    public int avslutaKonto(int kontoID) {
        return 0;
    }

    @Override
    public Bok[] hämtaTillgänglighet(String titel) {
        return new Bok[0];
    }

    @Override
    public int skapaLån(int kontoID, int bibID) {
        return 0;
    }

    @Override
    public int läggTillSvartlistade(long personNr) {
        return 0;
    }

    @Override
    public int skapaKonto(String fnamn, String enamn, long personNr, int roll) {
        return 0;
    }

}

