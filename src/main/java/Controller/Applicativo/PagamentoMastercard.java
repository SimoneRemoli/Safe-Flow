package Controller.Applicativo;

import Model.DAO.MastercardDAO;
import Exception.DAOExceptionRemoli;
import Model.DAO.SalvaPagamentoDAO;
import Model.Domain.*;
import utility.Decorator.DecoratorTicket.BaseTicketCode;
import utility.Decorator.DecoratorTicket.CittaDecorator;
import utility.Decorator.DecoratorTicket.Component;
import utility.Decorator.DecoratorTicket.TimestampDecorator;
import utility.Singleton.Credentials;

import java.util.ArrayList;
import java.util.List;

public class PagamentoMastercard extends RegistrazionePagamentoController
{
    String numeroCarta;
    String scadenza;
    String cvv;

    public List<String> run() throws Exception {

        final List<String> codiciBiglietti;
        Mastercard mastercard = new MastercardDAO().GetPaymentMastercard(numeroCarta, scadenza, cvv);
        if (mastercard != null)
        {
            Component gen = new TimestampDecorator(new CittaDecorator(new BaseTicketCode(), city));

            codiciBiglietti = new ArrayList<>();

            for (int i = 0; i < quantitativo; i++) {
                codiciBiglietti.add(gen.genera());
            }

            registra_pagamento_permanente(codiciBiglietti);
        }
        else {
            throw new RuntimeException();
        }

        return codiciBiglietti;

    }


    public PagamentoMastercard(String numeroCarta, String scadenza, String cvv, Credentials cred
    ,double tot, int quantita, String citta )
    {
        super(tot, quantita, citta, cred);
        this.numeroCarta = numeroCarta;
        this.scadenza = scadenza;
        this.cvv = cvv;
    }


    private void registra_pagamento_permanente(List<String> codiciBiglietti) throws Exception {
        if (credenziali == null) {
            System.err.println(" Nessun utente associato al pagamento.");
            throw new DAOExceptionRemoli("Nessun utente loggato associato al pagamento.");
        }
        SalvaPagamentoDAO dao = new SalvaPagamentoDAO();
        System.out.println("Traveler " + credenziali.getNome() + " "+ credenziali.getCodiceFiscale() + " "+credenziali.getNome()+ " "+credenziali.getCognome()+ " " + credenziali.getDisabile() + " ha effettuato un pagamento di " + totale + "€");
        dao.salvataggio(credenziali,codiciBiglietti,"Mastercard", city);



    }


}


