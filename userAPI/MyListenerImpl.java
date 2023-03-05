package userAPI;
import java.lang.String;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

import org.w3c.dom.ls.LSOutput;
import processlagerAPI.Process;

interface MyListener {

    void onEvent(EventType type, String data);

    Scanner scan = new Scanner(System.in);

    boolean fortsatt = false;

}
 enum EventType {

    Lanabok,
    Aterlamnabok,
    Avslutakonto,
    Registrerakonto,
    Svartlistamedlem,

    Temporeravstangning,

    KollaTillganglighet,

    kollaMedlemsstatus

}


public class MyListenerImpl implements MyListener {
    public MyListenerImpl() throws SQLException {

    }


    @Override
    public void onEvent(EventType type, String data ) {
        switch (type) {
            case  Lanabok:
                handleLanabok();
                break;
            case Aterlamnabok:
                handleAterlamnabok();
                break;
            case Avslutakonto:
                handleAvslutakonto();
                break;
            case Registrerakonto:
                handleRegistrerakonto();
                break;
            case Svartlistamedlem:
                handleSvartlistamedlem();
                break;
            case Temporeravstangning:
                handleTeporeravstagning();
                break;
            case KollaTillganglighet:
                handleKollaTillganglighet();
                break;
            case kollaMedlemsstatus:
                handlekollaMedlemsstatus();

            default:
                System.out.println("Det gick inte!");
        }
    }


    Process object = new Process();

    private void handleLanabok() {
        System.out.println("Skriv in boktitel:");

            int kontoId = scan.nextInt();
            int bibID = scan.nextInt();

        try{
                object.registreraLån(kontoId, bibID);
            } catch (NumberFormatException n) {
        }

        // Handle event 1
    }

    public void handleTeporeravstagning() throws Exception {
        System.out.println("Ange medlems personnummer: ");
        int kontoId = scan.nextInt();

        object.tempAvstängning(kontoId, new Date());
        System.out.println("Sista avstängningsdatum för medlem " + kontoId + "är: ");


    }

    private void handleAvslutakonto() {
        System.out.println("Vänligen ange ditt personnumer: ");
        int kontoId = scan.nextInt();
        object.kollaMedlemsStatus(kontoId);

        System.out.println("Kontot avslutad!");

        // Handle event 3
    }

    private void handleRegistrerakonto() throws Exception {
        System.out.println("Ange förnamn:");
        String fnamn = scan.nextLine();
        System.out.println("Ange efternamn:");
        String lnamn = scan.nextLine();
        System.out.println("Ange personnummer:");
        long personNr = scan.nextLong();
        System.out.println("Ange roll:");
        String roll = scan.nextLine();

        object.regKonto(fnamn, lnamn, personNr, roll);
        System.out.println("Kontot registrerad!");

        // Handle event 4
    }

    private void handleSvartlistamedlem() {
        System.out.println("Ange användarens personnummer:");
        long personNr = scan.nextLong();

        object.svartlistaMedlem(personNr);
        System.out.println("Medlem svartlistad!");

        // Handle event 5

    }

    private void handleKollaTillganglighet() {
        System.out.println("Ange bokens titel: ");
        String titel = scan.nextLine();
        object.kollaTillgänglighet(titel);
        System.out.println("Bokens titel är" + titel);
    }

    private void handlekollaMedlemsstatus() {
        System.out.println("Ange kontoId: ");
        int kontoId = scan.nextInt();

        object.kollaMedlemsStatus(kontoId);

    }

    private void handleAterlamnabok() {
    }
}

//        System.out.println("Ange bokID: ");
//        isbn = scan.nextInt();
//object.aterlamnabok(isbn) ==  ;
//        System.out.println("Boken med ISBN" + isbn + "återlämnad!");
//
//        //potenciella fel
//
//
//        // Handle event 2



