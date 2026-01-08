package it.web.routex.boundary.cli.view;

import it.web.routex.boundary.cli.controller.grafico.LoginControllerGraficoCLI;

import java.util.Scanner;

public class LoginViewCLI {

    public static String emailUtente;
    public static String passwordUtente;

    public static void mostraLogin() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("================================");
        System.out.println("        ROUTEX - LOGIN CLI        ");
        System.out.println("================================");

        System.out.print("Inserisci Email: ");
        emailUtente = scanner.nextLine();

        System.out.print("Inserisci Password: ");
        passwordUtente = scanner.nextLine();

        LoginControllerGraficoCLI login = new LoginControllerGraficoCLI();
        login.post();

    }
}
