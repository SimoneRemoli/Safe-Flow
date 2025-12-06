package it.web.routex.controller.applicativo;
import java.util.List;

import it.web.routex.exception.CredentialsExceptionRemoli;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.exception.PaymentValidationExceptionRemoli;
import it.web.routex.utility.singleton.Credentials;

public abstract class RegistrazionePagamentoController
{
    double totale;
    String city;
    Credentials credenziali;
    int quantitativo;

    protected RegistrazionePagamentoController(double tot, int quantita, String city, Credentials cred)
    {
        this.totale = tot;
        this.quantitativo = quantita;
        this.city = city;
        this.credenziali = cred;
    }
    public abstract List<String> run() throws DAOExceptionRemoli, PaymentValidationExceptionRemoli, CredentialsExceptionRemoli;
}
