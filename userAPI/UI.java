package userAPI;

import java.lang.String;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

import databasAPI.Bok;
import databasAPI.Konto;
import databasAPI.Lån;
import processlagerAPI.*;
import processlagerAPI.Process;

import static java.time.temporal.ChronoUnit.DAYS;

public class UI {
    public static void main(String[] args) throws Exception {

        Scanner scan = new Scanner(System.in);
        int ISBN;
        int kontoId;
        boolean running = true;
        boolean menyval = true;
        Process processObj = new Process();
        Konto medlem;
        Bok bok;
        while (running) {
            int val;
                System.out.println(
                                        "------------------------------------------\n" +
                                        "-------- Välkommen till BibSus -----------\n" +
                                        "------------------------------------------\n"+
                                        "-----------------------------version -1.2-\n"+
                                                "------------------------------------------\n");
                System.out.println("Välj i menyn vad du vill göra:\n" +
                        "       1: Låna bok\n" +
                        "       2: Återlämna bok\n" +
                        "       3: Avsluta konto\n" +
                        "       4: Registrera konto\n" +
                        "       5: Svartlista medlem\n" +
                        "       6: Temporär avstängning\n" +
                        "       7: Kolla Tillgänglighet\n" +
                        "       8: kotrollera konto\n" +
                        "       9: Återanslut till Databas-server\n" +
                        "       10: Avsluta BibSus\n"+
                        "------------------------------------------\n");
            try {
            System.out.print("Välj vad du vill göra? (1 - 7): ");
                    val = scan.nextInt();
                }
            catch (InputMismatchException e) {
                System.out.println("Välj en siffra i menyn.");
                scan.nextLine();
                continue;
            }

            switch (val) {
                case 1: //låna bok
                    medlem = null;
                    bok = null;
                    System.out.println(
                            "------------------------------------------\n" +
                                    "----------------Låna bok------------------\n" +
                                    "------------------------------------------\n" +
                                    "Skriv in kontoId: ");
                    //Skriv in kontoID.
                        try {
                            kontoId = scan.nextInt();
                        }
                        catch (InputMismatchException e) {
                            System.out.println("kontoId är ett nummer med 4 siffror");
                            scan.next();
                            continue;
                        }
                    try {
                            //Hämtar medlem ur databasen
                            medlem = processObj.hämtaKonto(kontoId);
                        }
                         catch (SQLException e) {
                            System.out.println(e.getMessage());
                            break;
                        }
                            if (medlem == null) {
                                System.out.println("Medlems konto med kontoID: " + kontoId + " inte hittat.");
                                menyval = false;
                                continue;
                            }
                            System.out.println("Konto hittat");



                            //kollar om medlem bör få låna
                            if (medlem.ärAvstängd()) {
                                System.out.println(
                                        "Medlem " + medlem.getfNamn() + " " + medlem.geteNamn() +
                                                ", med kotoID: " + medlem.getKontoID() + "är avstängd");
                                break;
                            }
                            //Kollar om medlem har max antal lån
                            if (medlem.harMaxLån()) {
                                System.out.println(
                                        "Medlem " + medlem.getfNamn() + " " + medlem.geteNamn() +
                                                ", med kotoID: " + medlem.getKontoID() + " , har lånat sin maximala mängd böcker.");
                                break;
                            }

                        try {
                            System.out.println("Ange ISBN för önskad bok: ");
                            ISBN = scan.nextInt();
                        }
                        catch (InputMismatchException e) {
                            System.out.println("ISBN är ett nummer med 6 siffror.");
                            scan.next();
                            break;
                        }
                        try {
                            bok = processObj.kollaTillgänglighet(ISBN);
                            if (bok == null) {
                                System.out.println("Bok inte tillgänglig för lån");
                                break;
                            }
                            int svar = processObj.registreraLån(medlem.getKontoID(), bok.getBibID());
                            System.out.println("Boken: " + bok.getTitel() + " med bid: " + bok.getBibID() + " lånad till " + medlem.getKontoID() + "\n");
                            if (svar == 5) {
                                System.out.println("Lån misslyckades");
                                break;
                            }
                            break;
                        }
                        catch (SQLException e) {
                            break;
                        }

                        case 2: //återlämna bok (Linnea Klar)
                            medlem = null;
                            bok = null;
                            System.out.println(
                                            "------------------------------------------\n" +
                                            "--------------Återlämna bok---------------\n" +
                                            "------------------------------------------\n" +
                                            "Skriv in kontoId: ");
                            //Skriv in kontoID.
                            try {
                                kontoId = scan.nextInt();
                            }
                            catch (InputMismatchException e) {
                                System.out.println("kontoId är ett nummer med 4 siffror");
                                scan.next();
                                continue;
                            }
                            try {
                                //Hämtar medlem ur databasen
                                medlem = processObj.hämtaKonto(kontoId);
                            }
                            catch (SQLException e) {
                                System.out.println(e.getMessage());
                                break;
                            }
                            if (medlem == null) {
                                System.out.println("Medlems konto med kontoID: " + kontoId + " inte hittat.");
                                menyval = false;
                                continue;
                            }
                            System.out.println("Konto hittat");

                            System.out.println("Ange bokID för boken som återlämnas: ");
                            try {
                                int bid = scan.nextInt();
                               int svar = processObj.återlämnaBok(medlem, bid);
                               if(svar == 2 ){
                                   System.out.println("Bok inte lånad av medlem");
                               }
                               else System.out.println("Bok återlämnad");
                            }
                            catch (InputMismatchException e) {
                                System.out.println("kontoId är ett nummer med 4 siffror");
                                scan.next();
                                continue;
                            }
                            catch (SQLException e) {
                                System.out.println(e.getMessage());
                                break;
                            }
break;
                        case 3: //avsluta konto
                            medlem = null;
                            bok = null;
                            System.out.println(
                                    "------------------------------------------\n" +
                                            "--------------Avsluta konto---------------\n" +
                                            "------------------------------------------\n" +
                                            "Skriv in kontoId: ");
                            //Skriv in kontoID.
                            try {
                                kontoId = scan.nextInt();
                            }
                            catch (InputMismatchException e) {
                                System.out.println("kontoId är ett nummer med 4 siffror");
                                scan.next();
                                continue;
                            }
                            try {
                                //Hämtar medlem ur databasen
                                medlem = processObj.hämtaKonto(kontoId);
                            }
                            catch (SQLException e) {
                                System.out.println(e.getMessage());
                                break;
                            }
                            if (medlem == null) {
                                System.out.println("Medlems konto med kontoID: " + kontoId + " inte hittat.");
                                menyval = false;
                                continue;
                            }
                            System.out.println("Konto hittat");

                            boolean avslutaKonto = true;
                            while (avslutaKonto) {
                                try {
                                    System.out.println(
                                            "Ska Medlem " + medlem.getfNamn() + " " + medlem.geteNamn() +
                                                    ", med kotoID: " + medlem.getKontoID() + "s konto verkligen avslutas?\n" +
                                                    "( J / N )");
                                    String svar = scan.next().toUpperCase();

                                    if (svar.contains("N")) {
                                        avslutaKonto = false;
                                        break;
                                    } else if (svar.contains("J")) {
                                        medlem = processObj.avslutaKonto(medlem);
                                        if (medlem == null) {
                                            System.out.println("medlems konto kunde inte avslutas");
                                            avslutaKonto = false;
                                            break;
                                        }
                                        avslutaKonto = false;
                                        System.out.println(
                                                "Medlem " + medlem.getfNamn() + " " + medlem.geteNamn() +
                                                        ", med kotoID: " + medlem.getKontoID() + "'s konto avslutat.");
                                        break;
                                    } else System.out.println("Välj: \"J\" för ja \"N\" för nej ");

                                } catch (SQLException e) {
                                    System.out.println(e.getMessage());
                                    break;
                                }
                            }
                                if (medlem.harLån() == true) {
                                    System.out.println(
                                            "Medlem " + medlem.getfNamn() + " " + medlem.geteNamn() +
                                                    ", med kotoID: " + medlem.getKontoID() + " har fortfarande lånade böcker.\n" +
                                                    "Konto kan inte avslutas");
                                    break;
                                }

break;
                        case 4: //registrera konto
                            System.out.println(
                                    "------------------------------------------\n" +
                                            "--------------Registrera konto---------------\n" +
                                            "------------------------------------------\n\n");
                            System.out.println("Ange förnamn:");
                            scan.nextLine();
                            String fnamn = scan.nextLine();

                            System.out.println("Ange efternamn:");
                            String enamn = scan.nextLine();

                            System.out.println("Ange personnummer:");
                            long personNr = scan.nextLong();
                            scan.nextLine();

                            System.out.println("Ange roll: Kandidatstudent = 0, Master student = 1, Doktorand = 2, Lärare eller Doktor = 3");
                            int roll = scan.nextInt();

                            int kontoinfo = processObj.regKonto(fnamn, enamn, personNr, roll);

                            if (kontoinfo == 1) {
                                System.out.println("Konto för personnummer: "+personNr+", finns redan!");
                            }

                            if (kontoinfo == 2) {
                                System.out.println("Medlem är svartlistad! Det går inte att skapa konto. ");

                            }
                            if (kontoinfo == 3) {
                                System.out.println("OPS! Databasstrul. Försök igen!");
                            }
                            if (kontoinfo > 999) {
                                System.out.println("Konto för " + fnamn + " " + enamn + " " + kontoinfo + "  är skapat!");
                            }
                            break;

                        case 5: //svartlista medlem
                            medlem = null;
                            bok = null;
                            System.out.println(
                                            "------------------------------------------\n" +
                                            "----------- Svartlista medlem ------------\n" +
                                            "------------------------------------------\n" +
                                            "Skriv in kontoId: ");
                            //Skriv in kontoID.
                            try {
                                kontoId = scan.nextInt();
                            }
                            catch (InputMismatchException e) {
                                System.out.println("kontoId är ett nummer med 4 siffror");
                                scan.next();
                                continue;
                            }
                            try {
                                //Hämtar medlem ur databasen
                                medlem = processObj.hämtaKonto(kontoId);
                            }
                            catch (SQLException e) {
                                System.out.println(e.getMessage());
                                break;
                            }
                            if (medlem == null) {
                                System.out.println("Medlems konto med kontoID: " + kontoId + " inte hittat.");
                                continue;
                            }
                            System.out.println("Konto hittat");

                            boolean svartlista = true;
                            while (svartlista) {
                                try {
                                    System.out.println(
                                            "Ska Medlem " + medlem.getfNamn() + " " + medlem.geteNamn() +
                                                    ", med kotoID: " + medlem.getKontoID() + "Verkligen Svartlistas?\n" +
                                                    "( J / N )");
                                   String svar = scan.next().toUpperCase();

                                    if (svar.contains("N")) {
                                        svartlista = false;
                                        break;
                                    }
                                    else if (svar.contains("J")) {
                                        medlem = processObj.svartlistaMedlem(medlem);
                                        if (medlem == null) {
                                            System.out.println("medlem kunde inte svartlistas");
                                            svartlista = false;
                                            break;
                                        }
                                        medlem = processObj.avslutaKonto(medlem);
                                        if (medlem == null) {
                                            System.out.println("medlems konto kunde inte svartlistas");
                                            svartlista = false;
                                            break;
                                        }
                                        svartlista = false;
                                        System.out.println(
                                                "Medlem " + medlem.getfNamn() + " " + medlem.geteNamn() +
                                                        ", med kotoID: " + medlem.getKontoID() + ", svartlistad och konto avslutat.");
                                        break;
                                    }
                                    else System.out.println("Välj: \"J\" för ja \"N\" för nej ");
                                } catch (SQLException e) {
                                    System.out.println("Databas strul- försök igen!");
                                }
                            }
                            break;
                        case 6:  //tepmorär avstängningmedlem = null;
                            System.out.println(
                                            "------------------------------------------\n" +
                                            "---------- Temporär avstängning ----------\n" +
                                            "------------------------------------------\n" +
                                            "Skriv in kontoId: ");
                            //Skriv in kontoID.
                            try {
                                kontoId = scan.nextInt();
                            }
                            catch (InputMismatchException e) {
                                System.out.println("kontoId är ett nummer med 4 siffror");
                                scan.next();
                                continue;
                            }
                            try {
                                //Hämtar medlem ur databasen
                                medlem = processObj.hämtaKonto(kontoId);
                            }
                            catch (SQLException e) {
                                System.out.println(e.getMessage());
                                break;
                            }
                            if (medlem == null) {
                                System.out.println("Medlems konto med kontoID: " + kontoId + " inte hittat.");
                                continue;
                            }
                            System.out.println("Konto hittat");

                            System.out.println("Ange hur många dagar medlem ska stängas av: ");
                            int avstängningsDagar = scan.nextInt();
                                medlem = processObj.tempAvstängning(medlem,(avstängningsDagar));

                                if (medlem.börSvartlistas()){
                                    svartlista = true;
                                    while (svartlista) {
                                        try {
                                            System.out.println(
                                                    "Ska Medlem " + medlem.getfNamn() + " " + medlem.geteNamn() +
                                                            ", med kotoID: " + medlem.getKontoID() + "Verkligen Svartlistas?\n" +
                                                            "( J / N )");
                                            String svar = scan.next().toUpperCase();

                                            if (svar.contains("N")) {
                                                svartlista = false;
                                                break;
                                            } else if (svar.contains("J")) {
                                                medlem = processObj.svartlistaMedlem(medlem);
                                                if (medlem == null) {
                                                    System.out.println("medlem kunde inte svartlistas");
                                                    svartlista = false;
                                                    break;
                                                }
                                                medlem = processObj.avslutaKonto(medlem);
                                                if (medlem == null) {
                                                    System.out.println("medlems konto kunde inte svartlistas");
                                                    svartlista = false;
                                                    break;
                                                }
                                                svartlista = false;
                                                System.out.println(
                                                        "Medlem " + medlem.getfNamn() + " " + medlem.geteNamn() +
                                                                ", med kotoID: " + medlem.getKontoID() + ", svartlistad och konto avslutat.");
                                            }
                                            else System.out.println("Välj: \"J\" för ja \"N\" för nej ");
                                            break;
                                        }
                                        catch (SQLException e) {
                                            System.out.println("Databas strul- försök igen!");
                                        }
                                    }

                                        }
                            break;

                        case 7: //kolla tillgänglighet
                            System.out.println(
                                            "------------------------------------------\n" +
                                            "---------- Kolla tillgänglighet ----------\n" +
                                            "------------------------------------------\n" );
                            System.out.println("Ange en sökterm (Författare, Titel, utgivningsår, bid): ");
                            scan.nextLine();
                            String sök =scan.nextLine().toLowerCase();
                            String[] böcker = processObj.hämtaSamling();

                            System.out.println("----- Följande böcker matchande söktermen fanns ----- ");
                            for(String s:böcker){
                                if(s.toLowerCase().contains(sök)){
                                System.out.println(s);
                            }
                            }

                            break;
                        case 8: //kontrollera konto
                            System.out.println(
                                            "------------------------------------------\n" +
                                            "----------- Kontrollera konto ------------\n" +
                                            "------------------------------------------\n" +
                                            "Skriv in kontoId: ");
                            //Skriv in kontoID.
                            try {
                                kontoId = scan.nextInt();
                            }
                            catch (InputMismatchException e) {
                                System.out.println("kontoId är ett nummer med 4 siffror");
                                scan.next();
                                continue;
                            }
                            try {
                                //Hämtar medlem ur databasen
                                medlem = processObj.hämtaKonto(kontoId);
                            }
                            catch (SQLException e) {
                                System.out.println(e.getMessage());
                                break;
                            }
                            if (medlem == null) {
                                System.out.println("Medlems konto med kontoID: " + kontoId + " inte hittat.");
                                continue;
                            }
                            System.out.println("Konto: "+medlem.getKontoID() +
                                    " har "+ medlem.getLånadeBöcker().length +
                                    " lån av "+medlem.getMaxLån());
                            for (Lån l:medlem.getLånadeBöcker())
                                System.out.println(l.toString());
                            System.out.println("Konto har: "+ medlem.getAntalForseningar()+" förseningar ");
                            if(medlem.börAvstängas()) System.out.println("Medlem bör avstängas 15 dagar.");
                            System.out.println("Konto har: "+ medlem.getAntalAvstangningar()+" avstängningar.");
                            if(medlem.börSvartlistas())System.out.println("Medlem bör svartlistas och konto avslutas permanent.");

                            break;
                        case 9: //Återanslut till BibliiotekServer
                            processObj = new Process();
                            break;
                        case 10: //avsluta
                            System.exit(0);
                    }
                }
                    }
                }
