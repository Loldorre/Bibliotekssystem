package userAPI;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.String;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

import databasAPI.Bok;
import databasAPI.Konto;
import databasAPI.SaknasException;
import processlagerAPI.*;
import org.w3c.dom.ls.LSOutput;
import processlagerAPI.Process;

public class UI {
    public static void main(String[] args) throws FelException,SQLException, Exception,AvstängningKrävsException, SaknasException, BokFörsenadException, KontoAvstängtException, MaxBöckerException,SvartlistningKrävsException {

        Scanner scan = new Scanner(System.in);
        int val = 8;
        boolean running = true;
        Process processObj = new Process();
        Konto medlem;
        Bok bok;
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
                case 1: //låna bok (klar av viktor)

                    System.out.println(
                            "------------------------------------------\n" +
                                    "----------------Låna bok------------------\n" +
                                    "------------------------------------------\n" +
                                    "Skriv in kontoId: ");
                    int kontoId = scan.nextInt();

                    //Hämtar medlem ur databasen
                    try {
                        medlem = processObj.hämtaKonto(kontoId);
                        System.out.println("Konto hittat");
                    }
                    catch(MaxBöckerException a){
                        System.out.println(a.getMessage());
                        break;
                    }
                    catch (SaknasException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                    catch (SQLException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                    try{
                        //kollar medlemmens lån
                        medlem = processObj.kollaLån(medlem);
                    }
                    catch(BokFörsenadException a){
                        System.out.println(a.getMessage());
                            //kör öka försening på kontotot
                            medlem =processObj.uppdateraKonto(medlem,"förseningar",medlem.getAntalForseningar()+1);
                        }
                        //kollar förseningar.
                        try {
                            medlem = processObj.kollaAvstängning(medlem);
                        }
                        catch(KontoAvstängtException d){
                            System.out.println(d.getMessage());
                            break;
                        }
                        catch(AvstängningKrävsException b){
                            System.out.println(b.getMessage());
                            processObj.uppdateraKonto(medlem,"förseningar",0);
                            medlem= processObj.tempAvstängning(medlem,15);
                            break;
                        }
                        catch(SvartlistningKrävsException c){
                            try {
                                System.out.println(c.getMessage());
                                //Svartlista medlem
                                medlem = processObj.svartlistaMedlem(medlem);
                                //kör avsluta konto
                                medlem = processObj.avslutaKonto(medlem);
                                break;
                            }
                            catch(SaknasException x){
                                System.out.println(x.getMessage());
                                break;
                            }
                            catch(SQLException y){
                                System.out.println(y.getMessage());
                                break;
                            }
                        }
                    try{
                        System.out.println("Ange bokens ISBN: ");
                        scan.nextLine();
                        int ISBN = scan.nextInt();
                        bok = processObj.kollaTillgänglighet(ISBN);
                        processObj.registreraLån(medlem.getKontoID(), bok.getBibID());
                        System.out.println("Boken: " + bok.getTitel() + " med bid: " + medlem.getKontoID() + " lånad till " + kontoId + "\n");
                    }
                    catch (SaknasException s)
                    {
                        System.out.println(s.getMessage());
                        System.out.println("Inget lån");
                        break;
                    }
                    catch (SQLException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                    break;

                case 2: //återlämna bok (Linnea Klar)

                    break;

                case 3: //avsluta konto klar av Sanja
                    System.out.println("Vänligen ange kontoId: ");
                    kontoId = scan.nextInt();
                    try {
                        int Svarmedlemstatus = 99;

                        if (Svarmedlemstatus == 1) {
                            System.out.println("Angiven kontoId existerar inte!");
                        }
                        if (Svarmedlemstatus == 2) {
                            System.out.println("OPS! Databasstrul. Försök igen!");
                        }
                        if (Svarmedlemstatus == 0) {
                            System.out.println("Konto" + " " + kontoId + "avslutad!");
                        }
                    } catch (NumberFormatException n) {
                    }
                    break;

                case 4: //registrera konto (klar av Sanja)
                    System.out.println("Ange förnamn:");
                    scan.nextLine();
                    String fnamn = scan.nextLine();

                    System.out.println("Ange efternamn:");
                    String enamn = scan.nextLine();

                    System.out.println("Ange personnummer:");
                    long personNr = scan.nextLong();
                    scan.nextLine();
                    System.out.println("Ange typ:");
                    int roll = scan.nextInt();

                        int kontoinfo = processObj.regKonto(fnamn, enamn, personNr, roll);

                        if (kontoinfo == 1) {
                            System.out.println("Angiven konto finns redan!");
                        }

                        if (kontoinfo == 2) {
                            System.out.println("Medlem är svartlistad!Det går inte att skapa konto. ");

                        }
                        if (kontoinfo == 3) {
                            System.out.println("OPS! Databasstrul. Försök igen!");
                        }
                        if (kontoinfo > 999) {
                            System.out.println("Konto för " + fnamn + " " + enamn + " " + kontoinfo + "  är skapat!");
                        }
                    break;

                case 5: //svartlista medlem (klar av Sanja)
                {
                    System.out.println("Vänligen ange användarens personnummer:");
                    personNr = scan.nextLong();
                    System.out.println("Ange användarens kontoId:");
                    kontoId = scan.nextInt();
medlem = processObj.hämtaKonto(kontoId);
                        int svarpersonnumer = 99;
                        medlem = processObj.svartlistaMedlem(medlem);
                        if (svarpersonnumer == 1) {
                            System.out.println("Personen" + " " + personNr + " är redan svartlistad!");
                        }
                        if (svarpersonnumer == 2) {
                            System.out.println("OPS! Databasstrul. Försök igen!");
                        }
                        if (svarpersonnumer == 0) {
                            System.out.println("Personen" + " " + personNr + "  svartlistad!");

                            int svaravsluta = 99;
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
                    }
                break;

                case 6:  //tepmorär avstängning
                    System.out.println("Ange kontoId:");
                    kontoId = scan.nextInt();
                    System.out.println("Ange hur många dagar medlem ska stängas av: ");
                    int avstängningsDagar = scan.nextInt();

                    try{
                        int kollaavstangning = 99;
                        if (kollaavstangning == 3){
                            System.out.println("Konto finns inte!");
                        }
                        if (kollaavstangning== 2){
                            System.out.println("Avstängd "+avstängningsDagar+15+" dagar.");
                        }
                        if (kollaavstangning== 1){
                            System.out.println("Hade redan en avstängning och är nu Svartlistad och avstängd");
                        }
                        if (kollaavstangning== 0){
                            System.out.println("Medlem avstängd" + avstängningsDagar+" dagar");
                        }
                    }catch (NumberFormatException n) {
                    }
                    break;


                case 7: //kolla tillgänglighet
                    System.out.println("Ange bokens titel: ");
                    String bokTitle = scan.nextLine();
                    try {
                        int statustillganglighet = 99;
                        if (statustillganglighet == 0){
                            System.out.println("Inte tillgänglig för lån");
                        }
                        if (statustillganglighet > 0){
                            System.out.println("Bok: " + processObj.kollaTillgänglighet(bokTitle) + " är tillgänglig för lån");
                        }
                    }catch (NumberFormatException n) {
                    }
                    break;

                case 8: //kolla tillgänglighet
                    System.exit(0);
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