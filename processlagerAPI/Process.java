package processlagerAPI;

import databasAPI.*;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Process implements IProcess  {
    Databas DatabasAPI = null;

    public Process() {
        this.DatabasAPI = new Databas();
    }
    public Process(Databas db) {
        this.DatabasAPI = db;
    }
 //  public Process(Databas db) throws SQLException {
    //   DatabasAPI = db;
  // }

    @Override
    public int kollaTillgänglighet(String titel) {
            int tillgänglighetsCase = 0;
            Bok [] listaAvBöcker = DatabasAPI.hämtaTillgänglighet(titel);

            if (listaAvBöcker.length == 0) {
                return tillgänglighetsCase;
            } else {
                tillgänglighetsCase = listaAvBöcker[0].getBibID();
                return tillgänglighetsCase;
            }
        }

    @Override
    public int kollaMedlemsstatus(int kontoID) {
        int index = -1;
        Konto [] listAvKonto = DatabasAPI.hämtaKonton();

        for (int i = 0; i < listAvKonto.length; i++) {
            if (listAvKonto[i].getKontoID() == kontoID ) {
                index = i;
            }
        }

        if (index == -1) {
            return  3;
        }

        Date today = new Date();

        Lån [] lånadeBöcker = listAvKonto[index].hämtaLånFörKonto(listAvKonto[index].getKontoID());

        for (int i = 0; i < lånadeBöcker.length; i++) {

           Date lånDatum = lånadeBöcker[i].getLånDatum();

            LocalDate slutDatum = LocalDate.of(lånDatum.getYear(), lånDatum.getMonth(), lånDatum.getDate()).plusDays(14);
            Date returnTheBookDate = new Date(slutDatum.getYear(), slutDatum.getMonthValue(), slutDatum.getDayOfMonth());

            if (returnTheBookDate.compareTo(today) >= 0) {
                DatabasAPI.updateAntalFörseningar(listAvKonto[index].getKontoID());
                listAvKonto[index].setAntalForseningar(listAvKonto[index].getAntalForseningar() + 1);
            }

            if (listAvKonto[index].getAntalForseningar() > 1) {
                tempAvstängning(listAvKonto[index].getKontoID(), 15);
                LocalDate datum = LocalDate.now().plusDays(15);
                listAvKonto[index].setAvstangd(new Date(datum.getYear(), datum.getMonthValue(), datum.getDayOfMonth()));
                listAvKonto[index].setAntalForseningar(0);
                listAvKonto[index].setAntalAvstangningar(listAvKonto[index].getAntalAvstangningar()+1);
                return 2;
            }

            if (listAvKonto[index].getAntalAvstangningar() > 1) {
                svartlistaMedlem(listAvKonto[index].getPersonNr());
                avslutaKonto(listAvKonto[index].getKontoID());
                return 1;
            }
        }

        if (listAvKonto[index].getAntalAvstangningar() == 1) {
            return 2;
        }

        return 0;
    }

    public int kollaMedlemsstatus (int kontiId, int antalDagar) {
       return 0;
    }
//behöver kasta Exception om den måste svartlista en medlem för test.


    @Override
    public int tempAvstängning(int kontoId, int antalDagar) {

        Konto[] listaAvKonto = DatabasAPI.hämtaKonton();
        Konto konto = null;

        for (Konto value : listaAvKonto) {
            if (value.getKontoID() == kontoId) {
                konto = value;
                break;
            }
        }

        if (konto == null) {
            return 3;
        }

        int frånStatus = kollaMedlemsstatus(kontoId);

        if (frånStatus == 0) {
            DatabasAPI.registreraTempAvstänging(kontoId, antalDagar);
            return 0;
        } else {
            return frånStatus;
        }
    }



    @Override
    public int svartlistaMedlem(long personNr) {
        return 0;
    }

    //Viktor is doing
    @Override
    public Konto regKonto(String fnamn, String enamn, long personNr, String roll) {
        return null;
    }

    @Override
    public int avslutaKonto(int kontoID) {
        return 0;
    }

    @Override
    public int registreraLån(int kontoId, int bibID) {

       /* Konto [] listaAvKonto = DatabasAPI.hämtaKonton();
        Konto konto = null;

        for (Konto value : listaAvKonto) {
            if (value.getKontoID() == kontoId) {
                konto = value;
                break;
            }
        }
        if (konto == null) {
            return 3;
        }
        Lån [] lånadeBöcker = konto.getLanadeBocker(); */

        return DatabasAPI.skapaLån(kontoId, bibID);
    }

    @Override
    public int återlämnaBok(int kontoID, int bibID) {
        boolean återlämning = false;
        Konto [] listAvKonto = DatabasAPI.hämtaKonton();


        return 0;
    }
}