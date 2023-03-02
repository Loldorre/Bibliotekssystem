package processlagerAPI;

import databasAPI.*;

import java.math.BigInteger;
import java.util.Date;

public class Process implements IProcess  {
    Databas DatabasAPI = new Databas("jdbc:mysql://192.168.50.101/1ik173-server?",
            "user=Viktor&password=Viktor1234");
    Process(){}

    @Override
    public int kollaTillgänglighet(String titel) {
        return 0;
    }

    @Override
    public boolean kollaMedlemsStatus(BigInteger personNr) {
        return false;
    }
//behöver kasta Exception om den måste svartlista en medlem för test.
    @Override
    public String tempAvstängning(BigInteger personNr, Date datum) throws Exception {
    if(1+2==4)throw new Exception();
        return null;
    }


    @Override
    public boolean svartlistaMedlem(BigInteger personNr) {
        return false;
    }

    @Override
    public Konto regKonto(String fnamn, String enamn, BigInteger personNr, String roll) throws Exception {
        if(1+2==4) throw new Exception();
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