package Controller.Applicativo;

import java.sql.SQLException;
import java.util.List;

import Bean.UtenteBeanGenerico;
import Exception.DAOException;


public abstract class RegistrazionePagamentoController
{
    protected UtenteBeanGenerico utente;
    public void setUtente(UtenteBeanGenerico utente) {
        this.utente = utente;
    }


    public abstract int registra_pagamento() throws DAOException, SQLException;
    public abstract void gestisciPagamento(double totale, List<String> codiciBiglietti, String city) throws Exception;

}
