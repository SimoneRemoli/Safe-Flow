package it.web.routex.boundary.cli.view;

import it.web.routex.bean.PathInfoBean;
import it.web.routex.bean.ReportsStatsBean;
import it.web.routex.boundary.cli.controller.grafico.ReportsControllerGraficoCLI;
import it.web.routex.boundary.cli.controller.grafico.ViewWorkScheduleControllerGraficoCLI;

public class ViewReportsAndStatisticsCLI {

    public static void mostra()
    {
        ReportsControllerGraficoCLI not = new ReportsControllerGraficoCLI();
        not.doGet();
    }

    public static void mostrareports(ReportsStatsBean stats)
    {
        if (stats == null)
        {
            System.out.println("Nessun report disponibile.");
            return;
        }

        System.out.println("\n==============================================");
        System.out.println("        REPORT E STATISTICHE DI UTILIZZO        ");
        System.out.println("==============================================");

        System.out.println("Totale viaggi effettuati : " + stats.getTotalTrips());
        System.out.println("Distanza totale percorsa : " + String.format("%.2f", stats.getTotalDistance()) + " km");
        System.out.println("Tempo totale di viaggio  : " + String.format("%.2f", stats.getTotalTime()) + " ore");

        System.out.println("----------------------------------------------");

        /* ===== Utenti coinvolti ===== */
        if (stats.getUtenti() == null || stats.getUtenti().isEmpty())
        {
            System.out.println("Utenti coinvolti: nessuno");
        }
        else
        {
            System.out.println("Utenti coinvolti (" + stats.getUtenti().size() + "):");
            for (String u : stats.getUtenti())
            {
                System.out.println(" - " + u);
            }
        }

        System.out.println("----------------------------------------------");

        /* ===== Percorsi ===== */
        if (stats.getPaths() == null || stats.getPaths().isEmpty())
        {
            System.out.println("Nessun percorso registrato.");
        }
        else
        {
            System.out.println("Dettaglio percorsi:");
            int index = 1;

            for (PathInfoBean path : stats.getPaths())
            {
                System.out.println(index + ") " + path.toString());
                index++;
            }
        }

        System.out.println("==============================================\n");
    }

}
