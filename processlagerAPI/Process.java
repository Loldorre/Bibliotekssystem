package processlagerAPI;

import databasAPI.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;

public class Process implements IProcess  {
    Databas DatabasAPI = new Databas("jdbc:mysql://192.168.50.101/1ik173-server?",
            "user=Viktor&password=Viktor1234");
   public Process(Databas db) throws SQLException {
       DatabasAPI = db;
   }

    @Override
    public int kollaTillgänglighet(String titel) throws SQLException {
            int tillgänglighetsCase = 0;
            Bok [] listaAvBöcker = DatabasAPI.hämtaTillgänglighet(titel);

            if (listaAvBöcker.length == 0) {
                return tillgänglighetsCase;
            } else {
                tillgänglighetsCase = 1;
                return tillgänglighetsCase;
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
        int nummerAvAvstängdaDagar = 0;
        Konto [] listaAvKonto = DatabasAPI.hämtaKonton();

        Date date = new Date();
        int day = date.getDay();

        while (!date.equals(datum)) {
            date = new Date(date.getYear(), date.getMonth(), day);
            nummerAvAvstängdaDagar++;
            day++;
        }

        for (int i = 0; i < listaAvKonto.length; i++) {
            if (listaAvKonto[i].getPersonNr() == personNr) {
                kontoId = listaAvKonto[i].getKontoID();
                index = i;
            }
        }


        int [] listaLanadeBöcker = listaAvKonto[index].getLanadeBocker();
        Date currentDate = new Date();


        if (listaAvKonto[index].getAntalAvstangningar() > 2) {
            DatabasAPI.läggTillSvartlista(personNr);
            if (!DatabasAPI.läggTillSvartlista(personNr).equals("Personnummer tillagt i svartlista")) {
                message = "Person var redan 2 gånger avstängt. Avstängning failade, gör svartlistning manuellt.";
            } else {
                message = "Personen var redan 2 gånger avstängd och bli svartlistad";
            }
            return message;
        } else {
            //DatabasAPI.registreraTempAvstänging(kontoId, nummerAvAvstängdaDagar);
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

        /*for (int i = 0; i < listaLanadeBöcker.length; i++) {
            listaLanadeBöcker[i] //hämta lan att jämföra date som den skulle ha varit tillbaka
            if (listaLanadeBöcker[i].date.compareTo(currentDate < 0)) {
                listaAvKonto[i].getAntalForseningar();
                if (antalFörseningar > 2) {
                }
            }
        }*/

        return återlämning;
    }
}