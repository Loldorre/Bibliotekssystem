package processlagerAPI;

import databasAPI.*;

import java.sql.SQLException;
import java.util.Date;

public class Process implements IProcess  {
    Databas DatabasAPI = new Databas("jdbc:mysql://192.168.50.101/1ik173-server?",
            "user=Viktor&password=Viktor1234");
    Process() throws SQLException {}

    @Override
    public int kollaTillgänglighet(String titel) throws SQLException {
            int tillgänglighetsCase = 0;
            Bok [] listaAvBöcker = DatabasAPI.hämtaTillgänglighet(titel);

            if (listaAvBöcker.length == 0) {
                return tillgänglighetsCase;
            } else {
                return 1;
            }
        }

    @Override
    public boolean kollaMedlemsStatus(long personNr)  {
        boolean medlemsstatus = false;
        Konto [] listAvKonto = DatabasAPI.hämtaKonton();

        for (int i = 0; i < listAvKonto.length; i++) {
            if (listAvKonto[i].getPersonNr() == personNr) {
                medlemsstatus = true;
            }
        }
        return medlemsstatus;
    }
//behöver kasta Exception om den måste svartlista en medlem för test.
    @Override
    public String tempAvstängning(long personNr, Date datum) throws Exception {

        String message = "";
        int kontoId = -1;
        int index = 0;
        Konto [] listaAvKonto = DatabasAPI.hämtaKonton();

        for (int i = 0; i < listaAvKonto.length; i++) {
            if (listaAvKonto[i].getPersonNr() == personNr) {
                kontoId = listaAvKonto[i].getKontoID();
                index = i;
            }
        }

        if (listaAvKonto[index].getAntalAvstangningar() > 2) {
            DatabasAPI.läggTillSvartlista(personNr);
            if (!DatabasAPI.läggTillSvartlista(personNr).equals("Personnummer tillagt i svartlista")) {
                message = "Person var redan 2 gånger avstängt. Avstängning failade, gör svartlistning manuellt.";
            } else {
                message = "Personen var redan 2 gånger avstängd och bli svartlistad";
            }
            return message;
        } else {
            DatabasAPI.registreraTempAvstänging(kontoId);
            return DatabasAPI.registreraTempAvstänging(kontoId);
        }
    }


    @Override
    public boolean svartlistaMedlem(long personNr) {
        return false;
    }

    //Viktor is doing
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
        boolean lån = false;
        Konto [] listaAvKonto = DatabasAPI.hämtaKonton();

        return lån;
    }

    @Override
    public boolean återlämnaBok(long personNr, int bibID) {
        boolean återlämning = false;
        Konto [] listAvKonto = DatabasAPI.hämtaKonton();


        return återlämning;
    }
}