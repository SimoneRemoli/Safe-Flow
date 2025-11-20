package Controller.Applicativo;
import java.util.List;

import utility.Singleton.Credentials;

public abstract class RegistrazionePagamentoController
{
    double totale;
    String city;
    Credentials credenziali;
    int quantitativo;

    public RegistrazionePagamentoController(double tot, int quantita, String city, Credentials cred)
    {
        this.totale = tot;
        this.quantitativo = quantita;
        this.city = city;
        this.credenziali = cred;
    }
    public abstract List<String> run() throws Exception;
}
