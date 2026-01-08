package it.web.routex.boundary.cli.view;

import it.web.routex.boundary.cli.controller.grafico.SelectModeControllerGraficoCLI;

import java.util.Scanner;


public class SelectModeCLI {

    private final Scanner scanner = new Scanner(System.in);
    public static String scelta = "";
    private String mode = "";

    public void choiceDemoFull() {

        System.out.println("\n================================");
        System.out.println("     ROUTEX - TYPE MODE       ");
        System.out.println("================================");
        System.out.println("- DEMO  -");
        System.out.println("- FULL  -");
        System.out.print("Scrivi una modalità tra DEMO e FULL: ");
        mode = scanner.nextLine();
        scelta = mode.toUpperCase();
        SelectModeControllerGraficoCLI.doPost();
    }
}
