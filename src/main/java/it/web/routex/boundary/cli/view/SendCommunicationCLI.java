package it.web.routex.boundary.cli.view;

import it.web.routex.boundary.cli.controller.grafico.ConfirmCommunicationControllerGraficoCLI;

import java.util.Scanner;

public class SendCommunicationCLI
{
    private static final Scanner scanner = new Scanner(System.in);
    public static String message;
    public static void mostra()
    {
        System.out.println("\n================================");
        System.out.println("     Invia Comunicazione ai Workers     ");
        System.out.println("================================");
        System.out.print("Inserisci il messaggio da inviare: ");
        message = scanner.nextLine();

        ConfirmCommunicationControllerGraficoCLI.doPost();

    }
}
