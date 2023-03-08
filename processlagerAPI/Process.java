package processlagerAPI;

import databasAPI.*;
import java.sql.SQLException;
import java.util.Date;
import processlagerAPI.Process;
class Process implements IProcess {
    Databas DatabasAPI = null;

    public Process() {
        this.DatabasAPI = new Databas();
    }
    public Process(Databas db) {
        this.DatabasAPI = db;
    }

    @Override
    public
    int kollaTillgänglighet(String titel) {
        return 0;
    }

    @Override
    public int kollaMedlemsstatus(int kontoId) {
        return 0;
    }
    @Override
    public int kollaMedlemsstatus(int kontoId,int avstängningsDagar) {
        return 0;
    }
    @Override
    public int tempAvstängning(int kontoId, int antalDagar) {
        return 0;
    }

    @Override
    public int svartlistaMedlem(long personNr) {
        int databasSvar;
        long[] svartlistade;
        svartlistade = this.DatabasAPI.hämtaSvarlistade();
        //kollar om medlemmen redan är svartlistad.
        for (long l : svartlistade) {
            if (l == personNr) {return 1;}
                }
        databasSvar = this.DatabasAPI.läggTillSvartlistade(personNr);
                if (databasSvar == 0) {return 0;}
                else return 2;
            }

    @Override
    public int regKonto(String fnamn, String enamn, long personNr, int roll) {
        Konto[] kontolista = this.DatabasAPI.hämtaKonton();
        long[] svartlistade = this.DatabasAPI.hämtaSvarlistade();
        int databasSvar;
        for(Konto k :kontolista){
            if(k.getPersonNr()==personNr){return 1;}
        }
        for(long l:svartlistade) {
            if(l==personNr){return 2;}
        }
        databasSvar = this.DatabasAPI.skapaKonto(fnamn, enamn, personNr, roll);
        if(databasSvar>999){return databasSvar;}
        else{return 3;}
    }

    @Override
    public int avslutaKonto(int kontoId) {
        Konto[] kontolista = this.DatabasAPI.hämtaKonton();
        int databasSvar;
        for(Konto k :kontolista){
            if(k.getKontoID()==kontoId){
                databasSvar = this.DatabasAPI.avslutaKonto(kontoId);
                if(databasSvar == 0) { return 0; }
                if(databasSvar == 1) { return 2; }
             }
        }
        return 1;
    }

    @Override
    public int registreraLån(int kontoId, int bibID) {
        return 0;
    }

    @Override
    public int återlämnaBok(int kontoId, int bibID) {
        return 0;
    }

}