package it.web.routex.boundary.cli.view;

import java.util.Scanner;

public class HomePrincipaleCLI {
    private final Scanner scanner = new Scanner(System.in);

    public void home()
    {
        while (true) {
            System.out.println("\n================================");
            System.out.println("        ROUTEX - HOME CLI        ");
            System.out.println("================================");
            System.out.println("1) Start Exploring");
            System.out.println("2) Login");
            System.out.println("3) Stop");
            System.out.print("Seleziona un'opzione: ");

            String scelta = scanner.nextLine();

            switch(scelta)
            {
                case "1" -> StartExploringCLI.mostraExploring();

                case "2" -> {
                    LoginViewCLI.mostraLogin();
                    return;
                }
                case "3" -> {
                    return;
                }
            }
        }
    }
}
