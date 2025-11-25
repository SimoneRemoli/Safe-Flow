package Controller.Applicativo;

import Model.DAO.MastercardDAO;
import Exception.DAOExceptionRemoli;
import Exception.CredentialsExceptionRemoli;
import Exception.PaymentValidationExceptionRemoli;
import Model.DAO.TicketDAOLayer;
import Model.Domain.*;
import utility.Decorator.DecoratorTicket.BaseTicketCode;
import utility.Decorator.DecoratorTicket.CittaDecorator;
import utility.Decorator.DecoratorTicket.Component;
import utility.Decorator.DecoratorTicket.TimestampDecorator;
import utility.Factory.FactoryPersistence;
import utility.Singleton.Credentials;
import java.util.ArrayList;
import java.util.List;

public class PagamentoMastercard extends RegistrazionePagamentoController
{
    String numeroCarta;
    String scadenza;
    String cvv;

    public List<String> run() throws DAOExceptionRemoli, PaymentValidationExceptionRemoli, CredentialsExceptionRemoli {

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


    private void registra_pagamento_permanente(List<String> codiciBiglietti) throws CredentialsExceptionRemoli {
        if (credenziali == null) {
            throw new CredentialsExceptionRemoli("Nessun utente loggato associato al pagamento.", "Errore nel PagamentoMastercard.java");
        }
        //SalvaTicketDBDAO dao = new SalvaTicketDBDAO();
        System.out.println("Traveler " + credenziali.getNome() + " "+ credenziali.getCodiceFiscale() + " "+credenziali.getNome()+ " "+credenziali.getCognome()+ " " + credenziali.getDisabile() + " ha effettuato un pagamento di " + totale + "€");
       // dao.salvataggio(credenziali,codiciBiglietti,"Mastercard", city);


        TicketDAOLayer daoLayer = FactoryPersistence.createTicketDAO();
        daoLayer.salvataggio(credenziali, codiciBiglietti, "Mastercard", city);
        return;

    }
}


