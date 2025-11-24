package Controller.Applicativo;
import Model.DAO.PaypalDAO;
import Model.DAO.TicketDAOLayer;
import Model.Domain.*;
import Exception.DAOExceptionRemoli;
import utility.Decorator.DecoratorTicket.BaseTicketCode;
import utility.Decorator.DecoratorTicket.CittaDecorator;
import utility.Decorator.DecoratorTicket.Component;
import utility.Decorator.DecoratorTicket.TimestampDecorator;
import utility.Factory.FactoryPersistence;
import utility.Singleton.Credentials;
import Exception.CredentialsExceptionRemoli;
import Exception.PaymentValidationExceptionRemoli;


import java.util.ArrayList;
import java.util.List;

public class PagamentoPaypal extends RegistrazionePagamentoController
{
    String email;
    String codice;
    public List<String> run() throws DAOExceptionRemoli, PaymentValidationExceptionRemoli, CredentialsExceptionRemoli
    {
        final List<String> codiciBiglietti;
        Paypal p = new PaypalDAO().GetPaymentPaypal(email, codice);
        if (p != null)
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

    public PagamentoPaypal(String email, String codiceTransazione, Credentials cred
            ,double tot, int quantita, String citta)
    {
        super(tot, quantita, citta, cred);
        this.email = email;
        this.codice = codiceTransazione;

    }

    private void registra_pagamento_permanente(List<String> codiciBiglietti) throws CredentialsExceptionRemoli {
        if (credenziali == null) {
            System.err.println(" Nessun utente associato al pagamento.");
            throw new CredentialsExceptionRemoli("Nessun utente loggato associato al pagamento.", "Errore nel PagamentoPaypal.java");
        }
        //SalvaTicketDBDAO dao = new SalvaTicketDBDAO();
        System.out.println("Traveler " + credenziali.getNome() + " "+ credenziali.getCodiceFiscale() + " "+credenziali.getNome()+ " "+credenziali.getCognome()+ " " + credenziali.getDisabile() + " ha effettuato un pagamento di " + totale + "€");
        //dao.salvataggio(credenziali,codiciBiglietti,"Paypal", city);

        TicketDAOLayer daoLayerJDBC = FactoryPersistence.createTicketDAO();
        daoLayerJDBC.salvataggio(credenziali, codiciBiglietti, "Paypal", city);
        return;


    }

}
