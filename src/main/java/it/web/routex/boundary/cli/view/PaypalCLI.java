package it.web.routex.boundary.cli.view;

public final class PaypalCLI
{
    private static String emailPaypal;
    private static String codiceTransazione;

    public PaypalCLI(String a, String b)
    {
        this.emailPaypal = a;
        this.codiceTransazione = b;
    }

    public static String getCodiceTransazione() {
        return codiceTransazione;
    }

    public static String getEmailPaypal() {
        return emailPaypal;
    }
}
