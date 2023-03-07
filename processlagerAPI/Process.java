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
    public int tempAvstängning(int kontoId, int antalDagar) {
        return 0;
    }

    @Override
    public int svartlistaMedlem(long personNr) {
        return 0;
    }

    @Override
    public Konto regKonto(String fnamn, String enamn, long personNr, String roll) {
        return null;
    }

    @Override
    public int avslutaKonto(int kontoId) {
        return 0;
    }

    @Override
    public int registreraLån(int kontoId, int bibID) {
        return 0;
    }

    @Override
    public int återlämnaBok(int kontoId, int bibID) {
        return 0;
    }

    @Override
    public int kollaMedlemsstatus(int kontoID, int avstängningsDagar) {
        return 0;
    }
}