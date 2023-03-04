package processlagerAPI;

import databasAPI.*;

import java.sql.SQLException;
import java.util.Date;

public class Process implements IProcess {
    Databas DatabasAPI = null;

    Process() {
        this.DatabasAPI = new Databas("jdbc:mysql://192.168.50.101/1ik173-server?",
                "user=Viktor&password=Viktor1234");
    }
    Process(Databas db) {
        this.DatabasAPI = db;
    }

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
        if (1 + 2 == 4) throw new Exception();
        return null;
    }


    @Override
    public boolean svartlistaMedlem(long personNr) {
        Konto[] konton = DatabasAPI.hämtaKonton();
        long[] svartlistade = DatabasAPI.hämtaSvartlistade();
        String dbresultat = "";
//----Redan svartlistad?
        for (int i=0;i< svartlistade.length;i++) {
            if (personNr == svartlistade[i])
                return false;
        }
//kollar om kontot finns
        for (int i = 0; i < konton.length; i++){
            if (konton[i].getPersonNr() == personNr){
               DatabasAPI.läggTillSvartlista(personNr);
               DatabasAPI.avslutaKonto(konton[i].getKontoID());
               return true;
            }
        }
        return false;
    }

    @Override
    public Konto regKonto(String fnamn, String enamn, long personNr, String roll) throws Exception {
        Konto[] konton = DatabasAPI.hämtaKonton();

        //kollar om personnummret redan är upptaget.
        for (int i = 0; i < konton.length; i++)
            if (konton[i].getPersonNr() == personNr)
                throw new Exception("personnummer redan registrerat!");

        //personnummer inte redan i systemet så kollar om det är svartlistat.
        long[] svartlistade = DatabasAPI.hämtaSvartlistade();
        for (int i = 0; i < svartlistade.length; i++)
            if (svartlistade[i] == personNr) {
                throw new Exception("personnummer svartlistat!");
            }
        //allt ok så skapar konto och reggar i databasen.
        DatabasAPI.skapaKonto(fnamn, enamn, personNr, roll);
        konton = DatabasAPI.hämtaKonton();
        for (int i = 0; i < konton.length; i++){
            if (konton[i].getPersonNr() == personNr){
                return konton[i];
    }
}
        throw new Exception("Konto ej i databas efter registrering. mycket konstigt");
        //Om kontot här trotts allt inte finns med i databasen kastar den en exception.
    }

    @Override
    public boolean avslutaKonto(int kontoId) {
        Konto[] konton = DatabasAPI.hämtaKonton();
        String dbresultat = "";
        //kollar om kontoId finns i databasen och avslutar kontot om så är fallet.
        for (Konto k: konton){
            if (k.getKontoID() == kontoId) {
               DatabasAPI.avslutaKonto(k.getKontoID());
               return true;
            }
        }
        return false;
    }

    @Override
    public boolean registreraLån(int kontoId, int bibID) {
        return false;
    }
}