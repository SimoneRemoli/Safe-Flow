package it.web.routex.boundary.cli.view;

public class ComunicazioneInviataCLI
{
    public static void invioComunicazioneConfirm(String messaggio) {
        System.out.println("\n================================");
        System.out.println("        Comunicazione Inviata !          ");
        System.out.println("================================");
        System.out.println("Ok. " + messaggio);
        System.out.println("================================\n");
    }
}
