package it.web.routex.boundary.cli;

import it.web.routex.boundary.cli.view.HomePrincipaleCLI;
import it.web.routex.boundary.cli.view.LoginViewCLI;

public class main
{
    public static void main(String args[])
    {
        HomePrincipaleCLI home = new HomePrincipaleCLI();
        home.home();
    }
}
