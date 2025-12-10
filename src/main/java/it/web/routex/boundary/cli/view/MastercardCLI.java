package it.web.routex.boundary.cli.view;

public final class MastercardCLI
{
    private static String numeroCarta;
    private static String scadenza;
    private static String cvv;

    public MastercardCLI(String a, String b, String c)
    {
        this.numeroCarta = a;
        this.scadenza = b;
        this.cvv = c;
    }

    public static String getCvv() {
        return cvv;
    }

    public static String getNumeroCarta() {
        return numeroCarta;
    }

    public static String getScadenza() {
        return scadenza;
    }
}
