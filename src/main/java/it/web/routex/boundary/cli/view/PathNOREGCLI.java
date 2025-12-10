package it.web.routex.boundary.cli.view;

import java.util.ArrayList;
import java.util.List;

public final class PathNOREGCLI
{
    static private List<String> percorsiConNomi = new ArrayList<>();
    static private int numeroCambi;
    static private List<String> linee = new ArrayList<>();
    static private List<String> sequenzeDiCambiamento = new ArrayList<>();
    static private int numeroStazioniUsate;
    static double minutaggio;
    static private List<String> sequenzeNodiCruciali = new ArrayList<>();
    static private int numeroStazioniTotali;
    static double percentualeStazioniUsate;
    static String status;
    static String start;
    static String end;
    static String city;

    public static double getMinutaggio() {
        return minutaggio;
    }

    public static double getPercentualeStazioniUsate() {
        return percentualeStazioniUsate;
    }

    public static int getNumeroCambi() {
        return numeroCambi;
    }

    public static int getNumeroStazioniTotali() {
        return numeroStazioniTotali;
    }

    public static int getNumeroStazioniUsate() {
        return numeroStazioniUsate;
    }

    public static List<String> getLinee() {
        return linee;
    }

    public static List<String> getPercorsiConNomi() {
        return percorsiConNomi;
    }

    public static List<String> getSequenzeDiCambiamento() {
        return sequenzeDiCambiamento;
    }

    public static List<String> getSequenzeNodiCruciali() {
        return sequenzeNodiCruciali;
    }

    public static String getCity() {
        return city;
    }

    public static String getEnd() {
        return end;
    }

    public static String getStart() {
        return start;
    }

    public static String getStatus() {
        return status;
    }


    public PathNOREGCLI(
            List<String> percorsiConNomi,
            int numeroCambi,
            List<String> linee,
            int numeroStazioniUsate,
            double minutaggio,
            int numeroStazioniTotali,
            double percentualeStazioniUsate,
            List<String> sequenzeDiCambiamento,
            List<String> sequenzeNodiCruciali
    ) {
        this.percorsiConNomi = percorsiConNomi;
        this.numeroCambi = numeroCambi;
        this.linee = linee;
        this.numeroStazioniUsate = numeroStazioniUsate;
        this.minutaggio = minutaggio;
        this.numeroStazioniTotali = numeroStazioniTotali;
        this.percentualeStazioniUsate = percentualeStazioniUsate;
        this.sequenzeDiCambiamento = sequenzeDiCambiamento;
        this.sequenzeNodiCruciali = sequenzeNodiCruciali;

    }
    public PathNOREGCLI(String status, String start, String end, String city)
    {
        this.status = status;
        this.start = start;
        this.end = end;
        this.city = city;
    }
    public static void stampa() {

        System.out.println("\n========================================");
        System.out.println("            DETTAGLI PERCORSO           ");
        System.out.println("========================================");

        System.out.println("Città: " + getCity());
        System.out.println("Stazione di partenza: " + getStart());
        System.out.println("Stazione di arrivo: " + getEnd());
        System.out.println("Stato: " + getStatus());

        System.out.println("\n--- Informazioni generali ---");
        System.out.println("Numero cambi: " + getNumeroCambi());
        System.out.println("Numero stazioni usate: " + getNumeroStazioniUsate());
        System.out.println("Numero stazioni totali: " + getNumeroStazioniTotali());
        System.out.println("Minutaggio: " + getMinutaggio() + " min");
        System.out.println("Percentuale stazioni usate: " + getPercentualeStazioniUsate() + " %");

        System.out.println("\n--- Linee coinvolte ---");
        if (getLinee() != null && getLinee().isEmpty()) {
            for (String linea : getLinee()) {
                System.out.println(" - " + linea);
            }
        } else {
            System.out.println(" Nessuna linea disponibile.");
        }

        System.out.println("\n--- Percorsi con nomi ---");
        if (getPercorsiConNomi() != null && !getPercorsiConNomi().isEmpty()) {
            for (String p : getPercorsiConNomi()) {
                System.out.println(" - " + p);
            }
        } else {
            System.out.println(" Nessun percorso disponibile.");
        }

        System.out.println("\n--- Sequenze di cambiamento ---");
        if (getSequenzeDiCambiamento() != null && !getSequenzeDiCambiamento().isEmpty()) {
            for (String c : getSequenzeDiCambiamento()) {
                System.out.println(" - " + c);
            }
        } else {
            System.out.println(" Nessuna sequenza disponibile.");
        }

        System.out.println("\n--- Nodi cruciali ---");
        if (getSequenzeNodiCruciali() != null && !getSequenzeNodiCruciali().isEmpty()) {
            for (String n : getSequenzeNodiCruciali()) {
                System.out.println(" - " + n);
            }
        } else {
            System.out.println(" Nessun nodo cruciale.");
        }

        System.out.println("========================================\n");
    }






}
