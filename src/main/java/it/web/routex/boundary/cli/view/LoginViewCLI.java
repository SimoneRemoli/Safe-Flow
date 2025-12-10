package it.web.routex.boundary.cli.view;

import it.web.routex.boundary.cli.controller.grafico.LoginControllerGraficoCLI;

import java.util.Scanner;

public class LoginViewCLI {

    public void mostraLogin() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("================================");
        System.out.println("        ROUTEX - LOGIN CLI        ");
        System.out.println("================================");

        System.out.print("Inserisci Email: ");
        String email = scanner.nextLine();

        System.out.print("Inserisci Password: ");
        String password = scanner.nextLine();

        LoginControllerGraficoCLI login = new LoginControllerGraficoCLI();
        login.post(email,password);

    }
}
