package it.web.routex.boundary.cli.view;

import it.web.routex.bean.RouteBean;
import it.web.routex.bean.TicketBean;
import it.web.routex.boundary.cli.controller.grafico.AreaRiservataControllerGraficoCLI;

import java.util.ArrayList;
import java.util.List;

public class AreaRiservataCLI
{
    private static List<RouteBean> listaPercorsi = new ArrayList<>();
    private static List<TicketBean> tickets = new ArrayList<>();

    public static void setListaPercorsi(List<RouteBean> listaPercorsi) {
        AreaRiservataCLI.listaPercorsi = listaPercorsi;
    }

    public static void setTickets(List<TicketBean> tickets) {
        AreaRiservataCLI.tickets = tickets;
    }

    public static void areaRiservata()
    {
        AreaRiservataControllerGraficoCLI area = new AreaRiservataControllerGraficoCLI();
        area.doGet();
    }
    public static void showArea() {

        System.out.println("\n========== AREA RISERVATA ==========\n");

        // --- PERCORSI TROVATI ---
        System.out.println("📍 I tuoi percorsi salvati:\n");

        if (listaPercorsi == null || listaPercorsi.isEmpty()) {

            System.out.println("   Nessun percorso disponibile.\n");

        } else {

            int index = 1;
            for (RouteBean r : listaPercorsi) {

                System.out.println("— Percorso #" + index++ + " —");
                System.out.println("   • Città: " + r.getCitta());
                System.out.println("   • Partenza: " + r.getPartenza());
                System.out.println("   • Arrivo: " + r.getArrivo());
                System.out.println("   • Numero cambi: " + r.getnCambi());
                System.out.println("   • Lista cambi: " + (r.getListaCambi() != null ? r.getListaCambi() : "Nessuno"));
                System.out.println("   • Stazioni attraversate: " + r.getnStazAttraversate());
                System.out.println("   • Stazioni nella città: " + r.getnStazioniCitta());
                System.out.println("   • Stazione di interscambio: " +
                        (r.getStazInterscambio() != null ? r.getStazInterscambio() : "Nessuna"));
                System.out.println("   • Tempo di arrivo stimato: " +
                        (r.getTempoDiArrivo() != null ? r.getTempoDiArrivo() + " min" : "N/D"));
                System.out.println("   • Percentuale terreno utilizzato: " +
                        (r.getPercTerrenoUtilizzato() != null ? r.getPercTerrenoUtilizzato() + "%" : "N/D"));
                System.out.println();
            }
        }

        System.out.println("\n I tuoi biglietti:");

        if (tickets == null || tickets.isEmpty()) {
            System.out.println("   Nessun biglietto acquistato.");
        } else {
            int index = 1;
            for (TicketBean t : tickets) {
                System.out.println("\n   Biglietto #" + index++);
                System.out.println("   • Codice biglietto: " + t.getCodice());
                System.out.println("   • City: " + t.getCitta());
                System.out.println("   • Data: " + t.getDataAcquisto());
            }
        }

        System.out.println("\n===========================\n");
    }

}
