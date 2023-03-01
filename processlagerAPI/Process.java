package processlagerAPI;

import databasAPI.*;

import java.math.BigInteger;
import java.util.Date;

public class Process implements IProcess {
    Databas DatabasAPI = new Databas("jdbc:mysql://192.168.50.101/1ik173-server?",
            "user=Viktor&password=Viktor1234");
    Process(){}
    @Override
    public boolean kollaTillgänglighetBibID(int bibID) {
        return false;
    }

    @Override
    public Bok[] kollaTillgänglighetTitel(String titel) {
        return new Bok[0];
    }

    @Override
    public Bok[] kollaTillgänglighetISBN(int ISBN) {
        //Hej det är en kommentar
        //ksddbljshbfv
        //lkashbf

        System.out.println("Hello world");

        return new Bok[0];
    }

    @Override
    public Bok[] kollaTillgänglighetTitelForfattare(String titel, String forfattare) {
        return new Bok[0];
    }

    @Override
    public Bok[] kollaTillgänglighetForfattare(String forfattare) {
        return new Bok[0];
    }

    @Override
    public boolean kollaMedlemsstatus(BigInteger personNr) {
        return false;
    }

    @Override
    public String tempAvstängning(BigInteger personNr, Date datum) {
        return null;
    }

    @Override
    public String tempAvstängning(BigInteger personNr) {
        return null;
    }

    @Override
    public boolean svartlistaMedlem(BigInteger personNr) {
        return false;
    }

    @Override
    public Konto regKonto(String fnamn, String enamn, BigInteger personNr, String roll) {
        return null;
    }

    @Override
    public boolean avslutaKonto(BigInteger personNr) {
        return false;
    }

    @Override
    public boolean registreraLån(BigInteger personNr, int bibID) {
        return false;
    }
}