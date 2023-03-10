package userAPI;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.String;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;
import processlagerAPI.Process;
import org.w3c.dom.ls.LSOutput;
public class Meny {
    public static void main(String[] args) throws Exception {

        Scanner scan = new Scanner(System.in);
        long  personNr;
        int val = 8;
        String valString;
        boolean fortsatt = false;
        boolean running = true;
        Process processObj = new Process();
        while(running){
            System.out.println(
                    "------------------------------------------\n"+
                            "--------Välkommen till biblioteket--------\n"+
                            "------------------------------------------");
            System.out.println("Välj i menyn vad du vill göra:\n"+
                    "1: Låna bok\n"+
                    "2: Återlämna bok\n"+
                    "3: Avsluta konto\n"+
                    "4: Registrera konto\n"+
                    "5: Svartlista medlem\n"+
                    "6: Temporär avstängning\n"+
                    "7: Kolla Tillgänglighet\n"+
                    "8: Avsluta programmet\n"+
                    "------------------------------------------\n");
            System.out.print("Välj vad du vill göra? (1 - 7): ");
            val = scan.nextInt();

            switch (val) {
                case 1: //låna bok

                    //kolla konto
                    //kolla tillgängloghet
                    //låna bok
                    System.out.println(
                            "------------------------------------------\n"+
                                    "----------------Låna bok------------------\n"+
                                    "------------------------------------------\n" + "Skriv in kontoId: ");
                    int kontoId = scan.nextInt();

                    //1- medlem svartlistad
                    //2- medlem avstängd i 15 dagar
                    //3- konto finns inte
                    try {
                        int kontoStatusSvar = processObj.kollaMedlemsstatus(kontoId);

                        if (kontoStatusSvar == 1) {
                            System.out.println("Medlem är svarlistad.Go fuck yourself. ");
                        }
                        //
                        if (kontoStatusSvar== 2) {
                            System.out.println("Medlem är tyvärr avstängd i 15 dagar. ");
                        }
                        if (kontoStatusSvar ==3){
                            System.out.println("Konto finns inte");
                        }
                        if (kontoStatusSvar == 0) {
                            System.out.println("Godkänt! Medlem kan låna bok! ");
                        }
                        while (kontoStatusSvar == 0 ) {
                            System.out.println("Ange bokens ISBN: ");
                            scan.nextLine();
                            int ISBN = scan.nextInt();

                            int tillgänglighetsvar = processObj.kollaTillgänglighet(ISBN);
                            if (tillgänglighetsvar == 0) {
                                System.out.println("Inget lån");
                            }
                            if (tillgänglighetsvar > 0) {
                                processObj.registreraLån(kontoId, tillgänglighetsvar);
                                System.out.println("Boken: " + ISBN+ " med bid: " + tillgänglighetsvar + " lånad till "+ kontoId);
                            }
                        }

                    } catch (NumberFormatException n) {
                    }
                    fortsatt = false;
                    break;

                case 2: //återlämna bok
                    System.out.println("Skriv in kontoId: ");
                    kontoId = scan.nextInt();
                    System.out.println("Skriv in bibId: ");
                    int bibID = scan.nextInt();
                    try{

                        int aterlamnastatus = processObj.kollaMedlemsstatus(kontoId);

                        if ( aterlamnastatus== 3){
                            System.out.println("Konto finns inte");
                        }
                        if (aterlamnastatus == 2){
                            System.out.println("Bok återlämnad och medlem avstängd i 15 dagar");
                        }
                        if (aterlamnastatus == 1){
                            int svar = processObj.återlämnaBok(bibID);
                            System.out.println("Boken återlämnad och medlem  svartlistad");
                        }
                        if (aterlamnastatus == 0){
                            int svar = processObj.återlämnaBok(bibID,kontoId);
                            System.out.println("Boken återlämnad!");
                        }


                    }catch (NumberFormatException n) {
                    }
                    fortsatt = false;
                    break;

                case 3: //avsluta konto klar av Sanja
                    System.out.println("Vänligen ange kontoId: ");
                    kontoId = scan.nextInt();
                    try {
                        int Svarmedlemstatus = processObj.avslutaKonto(kontoId);

                        if (Svarmedlemstatus == 1){
                            System.out.println("Angiven kontoId existerar inte!");
                        }
                        if (Svarmedlemstatus == 2){
                            System.out.println("OPS! Databasstrul. Försök igen!");
                        }
                        if (Svarmedlemstatus== 0){
                            System.out.println("Konto" + " " + kontoId + "avslutad!");
                        }
                    } catch (NumberFormatException n) {
                    }
                    fortsatt = false;
                    break;

                case 4: //registrera konto (klar av Sanja)
                    System.out.println("Vänligen ange förnamn:");
                    scan.nextLine();
                    String fnamn = scan.nextLine();

                    System.out.println("Vänligen ange efternamn:");
                    String enamn = scan.nextLine();

                    System.out.println("Vänligen ange personnummer:");
                    personNr = scan.nextLong();
                    scan.nextLine();
                    System.out.println("Vänligen ange roll:");
                    int roll = scan.nextInt();

                    try {

                        int kontoinfo = processObj.regKonto(fnamn, enamn, personNr, roll);

                        if (kontoinfo == 1){
                            System.out.println("Konto till angivna informationer finns redan!");
                        }

                        if (kontoinfo == 2){
                            System.out.println("Medlem är svartlistad! Det går inte att skapa konto. ");

                        }
                        if (kontoinfo == 3){
                            System.out.println("OPS! Databasstrul. Försök igen!");
                        }
                        if (kontoinfo > 999){
                            System.out.println("Konto  " + fnamn + " " +  enamn + " " +  kontoinfo + "  är skapat!");
                        }
                    } catch (NumberFormatException n) {
                    }
                    fortsatt = false;
                    break;

                case 5: //svartlista medlem (klar av Sanja)
                {
                    System.out.println("Vänligen ange användarens personnummer:");
                    personNr = scan.nextLong();
                    System.out.println("Ange användarens kontoId:");
                    kontoId = scan.nextInt();

                    try {
                        int svarpersonnumer = processObj.svartlistaMedlem(personNr);
                        if (svarpersonnumer == 1) {
                            System.out.println("Personen" + " " + personNr + " är redan svartlistad!");
                        }
                        if (svarpersonnumer == 2) {
                            System.out.println("OPS! Databasstrul. Försök igen!");
                        }
                        if (svarpersonnumer == 0) {
                            System.out.println("Personen" + " " +  personNr+ "  svartlistad!");

                            int svaravsluta = processObj.avslutaKonto(kontoId);
                            if (svaravsluta == 0) {
                                System.out.println("Medlems konto avslutad!");
                            }
                            if (svaravsluta == 1) {
                                System.out.println("Angivet kontoID existerar inte!");
                            }
                            if (svaravsluta == 2) {
                                System.out.println("OPS! Databasstrul. Försök igen!");
                            }
                        }

                    } catch (NumberFormatException n) {

                    }
                    fortsatt = false;
                }
                break;

                case 6:  //tepmorär avstängning
                    System.out.println("Ange kontoId:");
                    kontoId = scan.nextInt();
                    int avstängningsDagar = scan.nextInt();

                    try{
                        int kollaavstangning = processObj.tempAvstängning (kontoId, avstängningsDagar);
                        if (kollaavstangning == 3){
                            System.out.println("Konto finns inte!");
                        }
                        if (kollaavstangning== 2){
                            System.out.println("Avstängd innegående dagar + 15");
                        }
                        if (kollaavstangning== 1){
                            System.out.println("Svartlistad och avstängd");
                        }
                        if (kollaavstangning== 0){
                            System.out.println("Medlem avstängd" + avstängningsDagar+" dagar");
                        }
                    }catch (NumberFormatException n) {
                    }
                    fortsatt = false;
                    break;


                case 7: //kolla tillgänglighet
                    System.out.println("Ange bokens titel: ");
                    String bokTitle = scan.nextLine();

                    try {
                        int statustillganglighet = processObj.kollaTillgänglighet(bokTitle);
                        if (statustillganglighet == 0){
                            System.out.println("Inget lån!");
                        }
                        if (statustillganglighet > 0){
                            System.out.println("Bok: " + processObj.kollaTillgänglighet(bokTitle) + " är tillgänglig");
                        }
                    }catch (NumberFormatException n) {
                    }
                    fortsatt = false;
                    break;
            }
        }

    }
}


/*
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
*/