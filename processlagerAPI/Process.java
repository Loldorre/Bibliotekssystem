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
    public boolean kollaMedlemsStatus(int kontoID) {
        return false;
    }
//behöver kasta Exception om den måste svartlista en medlem för test.
    @Override
    public String tempAvstängning(int kontoId, Date datum) throws Exception {
    if(1+2==4)throw new Exception();
        return null;
    }


    @Override
    public boolean svartlistaMedlem(long personNr) {
        return false;
    }

    @Override
    public Konto regKonto(String fnamn, String enamn, long personNr, String roll) throws Exception {
        Konto[] konton = DatabasAPI.hämtaKonton();
        //kollar om personnummret redan är upptaget.
        for (Konto k : konton)
            if (k.getPersonNr() == personNr)
                throw new Exception("personnummer redan registrerat!");

        //personnummer inte redan i systemet så kollar om det är svartlistat.
        long[] svartlistade = DatabasAPI.hämtaSvartlistade();
        for (long l : svartlistade)
            if (l == personNr) {
                throw new Exception("personnummer svartlistat!");
            }
//allt ok så skapar konto och reggar i databasen.
        String dbresultat = DatabasAPI.skapaKonto(fnamn, enamn, personNr, roll);
        konton = DatabasAPI.hämtaKonton();
        for (Konto k : konton) {
            if (k.getPersonNr() == personNr)
                return k;
            }
        throw new Exception("Konto ej i databas efter registrering. mycket konstigt");
    }

    @Override
    public boolean avslutaKonto(int kontoId) {
        return false;
    }

    @Override
    public boolean registreraLån(int kontoId, int bibID) {
        return false;
    }
}