package it.web.routex.boundary.cli.view;
@SuppressWarnings("java:S106")
public class TuttoRisoltoCLIView
{
    public static void mostraRisoluzione(String messaggio) {
        System.out.println("\n================================");
        System.out.println("        ECCELLENTE!          ");
        System.out.println("================================");
        System.out.println("Ok. " + messaggio);
        System.out.println("================================\n");
    }
}
