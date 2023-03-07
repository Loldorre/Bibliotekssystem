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
    Databas DatabasAPI = new Databas();
   public Process(Databas db) throws SQLException {
       DatabasAPI = db;
   }

    @Override
    public int kollaTillgänglighet(String titel) {
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
    public int kollaMedlemsstatus(int kontoID) {
        int medlemsstatus;
        int index = -1;
        Konto [] listAvKonto = DatabasAPI.hämtaKonton();

        for (int i = 0; i < listAvKonto.length; i++) {
            if (listAvKonto[i].getKontoID() == kontoID ) {
                index = i;
            }
        }

        Lån [] listAvBöcker = listAvKonto[index].getLanadeBocker();

        Date today = new Date();



        //visa om personen mate bestraffas
        //kolla alla bok igenom om de är försenad
        //om de är försenad uppdatera försening
        //om försening är över 2 bli personen avständ
        //om personen var redan avstängd tvü günger ska personen bli svartlistad


        //annars kolla om konto finns och dü bli det godkänd


        if (index == -1) {
            medlemsstatus = 0;
            return  medlemsstatus;
        }


        LocalDate date1 = LocalDate.now().plusDays(15);

        int year = date1.getYear();
        int month = date1.getMonthValue();
        int day = date1.getDayOfYear();

        if (listAvKonto[index].getAntalForseningar() >= 2) {
            tempAvstängning(listAvKonto[index].getKontoID(), 15);
            medlemsstatus = 2;
            return medlemsstatus;
        }

        return 0;
    }

    public int kollaMedlemsstatus (int kontiId, int antalDagar) {
       return 0;
    }
//behöver kasta Exception om den måste svartlista en medlem för test.
    @Override
    public int tempAvstängning(int kontoId, int antalDagar) {

        String message = "";
        long persNr = 0;
        int index = 0;
        int nummerAvAvstängdaDagar = 0;
        Konto[] listaAvKonto = DatabasAPI.hämtaKonton();

        Date date = new Date();
        int day = date.getDay();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);



        if (listaAvKonto[index].getAntalAvstangningar() >= 2) {
            DatabasAPI.läggTillSvartlista(persNr);
            DatabasAPI.avslutaKonto(listaAvKonto[index].getKontoID());
            if (!DatabasAPI.läggTillSvartlista(kontoId).equals("Personnummer tillagt i svartlista")) {
                message = "Person var redan 2 gånger avstängt. Avstängning failade, gör svartlistning manuellt.";
            } else {
                message = "Personen var redan 2 gånger avstängd och bli svartlistad";
            }

        } else
            DatabasAPI.registreraTempAvstänging(kontoId, nummerAvAvstängdaDagar);

            return 0;

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
        int lån = 1;
        Konto [] listaAvKonto = DatabasAPI.hämtaKonton();

        //kolla om det bli för münga lün

        return lån;
    }

    @Override
    public int återlämnaBok(int kontoID, int bibID) {
        boolean återlämning = false;
        Konto [] listAvKonto = DatabasAPI.hämtaKonton();

        //kolla medlemstatus?
        //hämta konto information
        //kolla om boken är för sent eller inte
        //uppdatera förseningar/avstängning/svartlistad

        /*for (int i = 0; i < listaLanadeBöcker.length; i++) {
            listaLanadeBöcker[i] //hämta lan att jämföra date som den skulle ha varit tillbaka
            if (listaLanadeBöcker[i].date.compareTo(currentDate < 0)) {
                listaAvKonto[i].getAntalForseningar();
                if (antalFörseningar > 2) {
                }
            }
        }*/

        return 0;
    }
}