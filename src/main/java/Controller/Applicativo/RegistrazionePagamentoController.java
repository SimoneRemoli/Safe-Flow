package Controller.Applicativo;

import java.sql.SQLException;

import Bean.UtenteBeanGenerico;
import Exception.DAOException;


public abstract class RegistrazionePagamentoController
{
    protected UtenteBeanGenerico utente;
    public void setUtente(UtenteBeanGenerico utente) {
        this.utente = utente;
    }

    public UtenteBeanGenerico getUtente() {
        return utente;
    }
    public abstract int registra_pagamento() throws DAOException, SQLException;
    public abstract void salva_pagamento() throws DAOException, SQLException;

    /*public void gestisciPagamento(double totale) throws SQLException {
        if (utente != null) {
            utente.gestisciPagamento(this, totale);
        } else {
            System.err.println("⚠️ Nessun utente associato al pagamento.");
        }
    }*/

    public abstract void gestisciPagamento(double totale) throws Exception;

}
