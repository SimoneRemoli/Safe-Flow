package it.web.routex.boundary.cli.view;
import it.web.routex.boundary.cli.controller.grafico.PathControllerGraficoCLI;

import java.util.Scanner;

public class StartExploringCLI
{
    public static void mostraExploring() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("================================");
        System.out.println("        ROUTEX - Start Exploring CLI        ");
        System.out.println("================================");

        System.out.print("Inserisci città: ");
        String city = scanner.nextLine();

        System.out.print("Inserisci stazione di partenza ");
        String stazionePartenza = scanner.nextLine();

        System.out.print("Inserisci stazione di arrivo ");
        String stazioneArrivo = scanner.nextLine();

        PathControllerGraficoCLI a = new PathControllerGraficoCLI();
        a.post(city, stazionePartenza, stazioneArrivo);

    }
}
