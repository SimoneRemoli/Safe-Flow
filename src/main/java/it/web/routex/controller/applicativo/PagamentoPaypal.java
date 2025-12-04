package it.web.routex.controller.applicativo;
import it.web.routex.model.dao.PaypalDAO;
import it.web.routex.model.dao.TicketDAOLayer;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.model.domain.Paypal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import it.web.routex.utility.Decorator.DecoratorTicket.BaseTicketCode;
import it.web.routex.utility.Decorator.DecoratorTicket.CittaDecorator;
import it.web.routex.utility.Decorator.DecoratorTicket.Component;
import it.web.routex.utility.Decorator.DecoratorTicket.TimestampDecorator;
import it.web.routex.utility.Factory.FactoryPersistence;
import it.web.routex.utility.Singleton.Credentials;
import it.web.routex.exception.CredentialsExceptionRemoli;
import it.web.routex.exception.PaymentValidationExceptionRemoli;


import java.util.ArrayList;
import java.util.List;

public class PagamentoPaypal extends RegistrazionePagamentoController
{
    String email;
    String codice;
    public List<String> run() throws DAOExceptionRemoli, PaymentValidationExceptionRemoli, CredentialsExceptionRemoli
    {
        final List<String> codiciBiglietti;
        Paypal p = new PaypalDAO().getPaymentPaypal(email, codice);
        if (p != null)
        {

            Component gen = new TimestampDecorator(new CittaDecorator(new BaseTicketCode(), city));

            codiciBiglietti = new ArrayList<>();

            for (int i = 0; i < quantitativo; i++) {
                codiciBiglietti.add(gen.genera());
            }

            registraPagamentoPermanente(codiciBiglietti);
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

    private void registraPagamentoPermanente(List<String> codiciBiglietti) throws CredentialsExceptionRemoli {
        final Logger logger = LoggerFactory.getLogger(getClass());
        if (credenziali == null) {
            throw new CredentialsExceptionRemoli("Nessun utente loggato associato al pagamento.", "Errore nel PagamentoPaypal.java");
        }
        TicketDAOLayer daoLayerJDBC = FactoryPersistence.createTicketDAO();
        daoLayerJDBC.salvataggio(credenziali, codiciBiglietti, "Paypal", city);
        logger.info("Traveler {} {} {} {} ha effettuato un pagamento di {} euro con Paypal", credenziali.getNome(), credenziali.getCodiceFiscale(), credenziali.getCognome(), credenziali.getDisabile(), totale);
    }
}
