package it.web.routex.controller.applicativo;
import it.web.routex.bean.PaymentResultBean;
import it.web.routex.dao.LayerPersistenza;
import it.web.routex.dao.LayerPersistenzaDemo;
import it.web.routex.dao.TicketDAOLayer;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.model.Paypal;
import it.web.routex.utility.factory.FactoryLayerPersistenza;
import it.web.routex.utility.singleton.ApplicationModeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import it.web.routex.utility.decorator.decoratorticket.BaseTicketCode;
import it.web.routex.utility.decorator.decoratorticket.CittaDecorator;
import it.web.routex.utility.decorator.decoratorticket.Component;
import it.web.routex.utility.decorator.decoratorticket.TimestampDecorator;
import it.web.routex.utility.factory.FactoryPersistence;
import it.web.routex.utility.singleton.Credentials;
import it.web.routex.exception.CredentialsExceptionRemoli;
import it.web.routex.exception.PaymentValidationExceptionRemoli;
import java.util.ArrayList;
import java.util.List;

public class PagamentoPaypal extends RegistrazionePagamentoController
{
    String email;
    String codice;
    public PaymentResultBean run() throws DAOExceptionRemoli, PaymentValidationExceptionRemoli, CredentialsExceptionRemoli
    {
        final List<String> codiciBiglietti;

        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        Paypal p = layer.getPaymentPaypal(email,codice);
        p.validate();

        Component gen = new TimestampDecorator(new CittaDecorator(new BaseTicketCode(), city));

        codiciBiglietti = new ArrayList<>();

        for (int i = 0; i < quantitativo; i++) {
            codiciBiglietti.add(gen.genera());
        }

        registraPagamentoPermanente(codiciBiglietti,p);

        return new PaymentResultBean(
                city,
                quantitativo,
                totale,
                p.getMethod().getDisplayName(),
                codiciBiglietti
        );
    }

    public PagamentoPaypal(String email, String codiceTransazione, Credentials cred,double tot, int quantita, String citta)
    {
        super(tot, quantita, citta, cred);
        this.email = email;
        this.codice = codiceTransazione;

    }

    private void registraPagamentoPermanente(List<String> codiciBiglietti, Paypal paypal) throws CredentialsExceptionRemoli {
        final Logger logger = LoggerFactory.getLogger(getClass());
        if (credenziali == null) {
            throw new CredentialsExceptionRemoli("Nessun utente loggato associato al pagamento.", "Errore nel PagamentoPaypal.java");
        }

        if(ApplicationModeManager.getSingletonInstance().getMode().toString().equals("DEMO")){
            logger.info("Modalità DEMO: il pagamento non viene salvato in persistenza.");

            LayerPersistenzaDemo layer = new LayerPersistenzaDemo();
            layer.salvataggio(credenziali, codiciBiglietti, paypal.getMethod().getDisplayName(), city);

        }
        else
        {
            TicketDAOLayer daoLayerJDBC = FactoryPersistence.createTicketDAO();
            daoLayerJDBC.salvataggio(credenziali, codiciBiglietti, paypal.getMethod().getDisplayName(), city);
            logger.info("Traveler {} {} {} {} ha effettuato un pagamento di {} euro con Paypal {}", credenziali.getNome(), credenziali.getCodiceFiscale(), credenziali.getCognome(), credenziali.getDisabile(), totale, paypal.maskedAccount());
        }
    }
}
