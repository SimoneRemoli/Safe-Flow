package it.web.routex.boundary.cli.view;

import it.web.routex.boundary.cli.controller.grafico.ConfermaPagamentoControllerGraficoCLI;

import java.util.Scanner;

public final class ConfermaPagamentoCLI
{
    private static String city;
    private static String quantity;
    private static double prezzoTotale;

    public ConfermaPagamentoCLI(String city, String quantity, double prezzoTotale)
    {
        this.city = city;
        this.quantity = quantity;
        this.prezzoTotale = prezzoTotale;
    }

    public static String getCity() {
        return city;
    }

    public static double getPrezzoTotale() {
        return prezzoTotale;
    }

    public static String getQuantity() {
        return quantity;
    }

    public static void stampa()
    {
        Scanner scanner = new Scanner(System.in);
        String metodoPagamento = "";
        String persistenza = "";
        String numeroCarta;
        String scadenza;
        String cvv;
        String emailPaypal;
        String codiceTransazione;
        boolean fine = true;
        boolean end = true;
        System.out.println("\n========================================");
        System.out.println("            CONFERMA PAGAMENTO           ");
        System.out.println("========================================");

        System.out.println("Città: " + getCity());
        System.out.println("Quantità dei biglietti: " + getQuantity());
        System.out.println("Prezzo: " + getPrezzoTotale());

        System.out.println("Vuoi confermare?(si/no) :");
        String scelta = scanner.nextLine();
        if ("si".equals(scelta)) {

                while(fine) {
                    System.out.println("* METODI DI PAGAMENTO DISPONIBILI *");
                    System.out.println("1. Mastercard \n");
                    System.out.println("2. Paypal \n");
                    System.out.println("Scegli il metodo di pagamento: ");
                    int value = scanner.nextInt();

                    if (value == 1)
                    {
                        metodoPagamento = "Mastercard";
                        fine = false;
                    }
                    if (value == 2)
                    {
                        metodoPagamento = "Paypal";
                        fine = false;
                    }
                    if (value < 1 || value > 2) fine = true;
                }

                if(metodoPagamento.equals("Mastercard"))
                {
                    // PULIZIA BUFFER UNA SOLA VOLTA
                    scanner.nextLine();

                    System.out.print("Numero carta: ");
                    numeroCarta = scanner.nextLine();

                    System.out.print("Scadenza (YYYY-MM-DD): ");
                    scadenza = scanner.nextLine();

                    System.out.print("CVV: ");
                    cvv = scanner.nextLine();

                    new MastercardCLI(numeroCarta, scadenza, cvv);
                }
                if(metodoPagamento.equals("Paypal"))
                {
                    scanner.nextLine();
                    System.out.println("Email paypal: ");
                    emailPaypal = scanner.nextLine();
                    System.out.println("Codice Transazione: ");
                    codiceTransazione = scanner.nextLine();
                    new PaypalCLI(emailPaypal, codiceTransazione);
                }

                while(end) {
                    System.out.println("* Modalità di persistenza*");
                    System.out.println("1. JDBC \n");
                    System.out.println("2. FILE_SYSTEM \n");
                    System.out.println("Scegli la modalità di persistenza: ");
                    int value = scanner.nextInt();

                    if (value == 1)
                    {
                        persistenza = "JDBC";
                        end = false;
                    }
                    if (value == 2)
                    {
                        persistenza = "FileSystem";
                        end = false;
                    }
                    if (value < 1 || value > 2) end = true;
                }

                ConfermaPagamentoControllerGraficoCLI pay = new ConfermaPagamentoControllerGraficoCLI();
                pay.doPost(getCity(), getQuantity(), String.valueOf(getPrezzoTotale()), metodoPagamento, persistenza);

        }
    }
}
