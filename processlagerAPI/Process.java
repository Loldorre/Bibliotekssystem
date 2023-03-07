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
    public int kollaMedlemsStatus(int kontoID) throws Exception {
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
            tempAvstängning(listAvKonto[index].getKontoID(), new Date(year, month, day));
            medlemsstatus = 2;
            return medlemsstatus;
        }

        return medlemsstatus;
    }
//behöver kasta Exception om den måste svartlista en medlem för test.
    @Override
    public int tempAvstängning(int kontoId, Date datum) throws Exception {

        String message = "";
        long persNr = 0;
        int index = 0;
        int nummerAvAvstängdaDagar = 0;
        Konto [] listaAvKonto = DatabasAPI.hämtaKonton();

        Date date = new Date();
        int day = date.getDay();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date firstDate = sdf.parse(date.toString());
        Date secondDate = sdf.parse(datum.toString());

        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        int diff = (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);



        if (listaAvKonto[index].getAntalAvstangningar() >= 2) {
            DatabasAPI.läggTillSvartlista(persNr);
            DatabasAPI.avslutaKonto(listaAvKonto[index].getKontoID());
            if (!DatabasAPI.läggTillSvartlista(kontoId).equals("Personnummer tillagt i svartlista")) {
                message = "Person var redan 2 gånger avstängt. Avstängning failade, gör svartlistning manuellt.";
            } else {
                message = "Personen var redan 2 gånger avstängd och bli svartlistad";
            }
            throw new Exception();
        } else {
            DatabasAPI.registreraTempAvstänging(kontoId, nummerAvAvstängdaDagar);

            return ;
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

        return återlämning;
    }
}