package Controller.Applicativo;

import Bean.TravelerBean;
import Bean.WorkerOAdminBean;
import Model.DAO.MastercardDAO;
import Exception.DAOException;
import Model.DAO.SalvaPagamentoDAO;
import Model.Domain.Mastercard;
import Model.Domain.Ruolo;

import java.sql.SQLException;

public class PagamentoMastercard extends RegistrazionePagamentoController
{
    Mastercard m;

    private void setting(String a, String b, String c)
    {
        this.m.setCvv(c);
        this.m.setData_scadenza(b);
        this.m.setNumero_carta(a);
    }

    public PagamentoMastercard(String numeroCarta, String scadenza, String cvv)
    {
        this.m = new Mastercard();
        setting(numeroCarta,scadenza,cvv);
    }

    @Override
    public int registra_pagamento() throws DAOException, SQLException {
        MastercardDAO mastercard = new MastercardDAO(this.m);
        return mastercard.GetPaymentMastercard();

    }

    public void gestisciPagamento(double totale) throws Exception {
        if (utente == null) {
            System.err.println("⚠️ Nessun utente associato al pagamento.");
            throw new DAOException("Nessun utente loggato associato al pagamento.");
        }

        SalvaPagamentoDAO dao = new SalvaPagamentoDAO();

        // --- Caso Traveler ---
        if (utente instanceof TravelerBean traveler) { //è per forza un traveler
            System.out.println("Traveler " + traveler.getNome() + " "+ traveler.getCodiceFiscale() + " "+traveler.getDisabile()+ " "+ traveler.getCognome()+ " ha effettuato un pagamento di " + totale + "€");
            //dao.salvaPagamento("Traveler", traveler.getCodiceFiscale(), totale);
        }

        // --- Caso Worker/Admin ---
        else if (utente instanceof WorkerOAdminBean worker) {
            Ruolo ruolo = worker.getRuolo();

            if (ruolo == Ruolo.WORKER) {
                System.out.println("⚠️ Worker " + worker.getNome() + " non può eseguire pagamenti.");
                throw new SecurityException("L’amministratore non può eseguire pagamenti.");
            } else if (ruolo == Ruolo.ADMIN) {
                System.out.println("⚠️ ADMIN " + worker.getNome() + " non può eseguire pagamenti.");
                throw new SecurityException("L’amministratore non può eseguire pagamenti.");
            }
        }
    }


}
