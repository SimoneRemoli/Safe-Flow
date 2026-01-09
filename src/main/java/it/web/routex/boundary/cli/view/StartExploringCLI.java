package it.web.routex.boundary.cli.view;
import it.web.routex.boundary.cli.controller.grafico.PathControllerGraficoCLI;

import java.util.Scanner;

public class StartExploringCLI
{
    private static String city;
    private static String stazionePartenza;
    private static String stazioneArrivo;

    public static void mostraExploring() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("================================");
        System.out.println("        ROUTEX - Start Exploring CLI        ");
        System.out.println("================================");

        System.out.print("Inserisci città: ");
        city = scanner.nextLine();

        System.out.print("Inserisci stazione di partenza: ");
        stazionePartenza = scanner.nextLine();

        System.out.print("Inserisci stazione di arrivo: ");
        stazioneArrivo = scanner.nextLine();

        PathControllerGraficoCLI p = new PathControllerGraficoCLI();
        p.post();

    }

    public static String getCity() {
        return city;
    }

    public static String getStazioneArrivo() {
        return stazioneArrivo;
    }

    public static String getStazionePartenza() {
        return stazionePartenza;
    }
}
