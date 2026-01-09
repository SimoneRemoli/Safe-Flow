package it.web.routex.boundary.cli.view;
@SuppressWarnings("java:S106")
public class ErroreLoginCLI
{
    public static void mostraErrore(String messaggio) {
        System.out.println("\n================================");
        System.out.println("        ERRORE DI LOGIN          ");
        System.out.println("================================");
        System.out.println("❌ " + messaggio);
        System.out.println("================================\n");
        LoginViewCLI a = new LoginViewCLI();
        a.mostraLogin();
    }
}
