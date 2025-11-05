package Controller.Applicativo;
import java.sql.SQLException;
import java.util.List;
import Exception.DAOException;
import Model.Domain.Credentials;

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
    //public abstract int controlla_esistenza_card() throws DAOException, SQLException;
    //public abstract void registra_pagamento_permanente(double totale, List<String> codiciBiglietti, String city, Credentials cred) throws Exception;
}
