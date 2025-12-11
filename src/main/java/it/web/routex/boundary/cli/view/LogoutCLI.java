package it.web.routex.boundary.cli.view;

import it.web.routex.boundary.cli.controller.grafico.LogoutControllerGraficoCLI;

public class LogoutCLI
{
    public static void logoutUser()
    {
        LogoutControllerGraficoCLI l = new LogoutControllerGraficoCLI();
        l.doGet();
    }
}
