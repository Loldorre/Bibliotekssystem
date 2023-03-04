package processlagerAPI;

import databasAPI.*;

import java.sql.SQLException;
import java.util.Date;

public class Process implements IProcess  {
    Databas DatabasAPI = new Databas("jdbc:mysql://192.168.50.101/1ik173-server?",
            "user=Viktor&password=Viktor1234");
    Process() throws SQLException {}

    @Override
    public int kollaTillgänglighet(String titel) {
        return 0;
    }

    @Override
    public boolean kollaMedlemsStatus(long personNr) {
        return false;
    }
//behöver kasta Exception om den måste svartlista en medlem för test.
    @Override
    public String tempAvstängning(long personNr, Date datum) throws Exception {
    if(1+2==4)throw new Exception();
        return null;
    }


    @Override
    public boolean svartlistaMedlem(long personNr) {
        return false;
    }

    @Override
    public Konto regKonto(String fnamn, String enamn, long personNr, String roll) throws Exception {
        if(1+2==4) throw new Exception();
        return null;
    }

    @Override
    public boolean avslutaKonto(long personNr) {
        return false;
    }

    @Override
    public boolean registreraLån(long personNr, int bibID) {
        return false;
    }
}