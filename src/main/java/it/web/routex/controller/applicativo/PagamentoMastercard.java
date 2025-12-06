package it.web.routex.controller.applicativo;

import it.web.routex.exception.PagamentoException;
import it.web.routex.model.dao.MastercardDAO;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.exception.CredentialsExceptionRemoli;
import it.web.routex.exception.PaymentValidationExceptionRemoli;
import it.web.routex.model.dao.TicketDAOLayer;
import it.web.routex.model.domain.Mastercard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import it.web.routex.utility.decorator.decoratorticket.BaseTicketCode;
import it.web.routex.utility.decorator.decoratorticket.CittaDecorator;
import it.web.routex.utility.decorator.decoratorticket.Component;
import it.web.routex.utility.decorator.decoratorticket.TimestampDecorator;
import it.web.routex.utility.factory.FactoryPersistence;
import it.web.routex.utility.singleton.Credentials;
import java.util.ArrayList;
import java.util.List;

public class PagamentoMastercard extends RegistrazionePagamentoController
{
    String numeroCarta;
    String scadenza;
    String cvv;

    public List<String> run() throws DAOExceptionRemoli, PaymentValidationExceptionRemoli, CredentialsExceptionRemoli {

        final List<String> codiciBiglietti;
        Mastercard mastercard = new MastercardDAO().getPaymentMastercard(numeroCarta, scadenza, cvv);
        if (mastercard != null)
        {
            Component gen = new TimestampDecorator(new CittaDecorator(new BaseTicketCode(), city));

            codiciBiglietti = new ArrayList<>();

            for (int i = 0; i < quantitativo; i++) {
                codiciBiglietti.add(gen.genera());
            }

            registraPagamentoPermanente(codiciBiglietti);
        }
        else {
            throw new PagamentoException("Pagamento fallito: dati non validi o circuito non disponibile.");
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


    private void registraPagamentoPermanente(List<String> codiciBiglietti) throws CredentialsExceptionRemoli {
        final Logger logger = LoggerFactory.getLogger(getClass());
        if (credenziali == null) {
            throw new CredentialsExceptionRemoli("Nessun utente loggato associato al pagamento.", "Errore nel PagamentoMastercard.java");
        }
        TicketDAOLayer daoLayer = FactoryPersistence.createTicketDAO();
        daoLayer.salvataggio(credenziali, codiciBiglietti, "Mastercard", city);
        logger.info("Traveler {} {} {} {} ha effettuato un pagamento di {} euro con la Mastercard", credenziali.getNome(), credenziali.getCodiceFiscale(), credenziali.getCognome(), credenziali.getDisabile(), totale);


    }
}


