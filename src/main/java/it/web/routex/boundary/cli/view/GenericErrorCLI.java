package it.web.routex.boundary.cli.view;

public class GenericErrorCLI
{
    public static void mostraErrore(String messaggio) {
        System.out.println("\n================================");
        System.out.println("        ERRORE!          ");
        System.out.println("================================");
        System.out.println("❌ " + messaggio);
        System.out.println("================================\n");
    }
}

