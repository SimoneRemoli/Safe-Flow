package it.web.routex.controller.applicativo;
import it.web.routex.bean.PaymentResultBean;
import it.web.routex.dao.LayerPersistenza;
import it.web.routex.enumerator.PaymentMethod;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.exception.CredentialsExceptionRemoli;
import it.web.routex.exception.PaymentValidationExceptionRemoli;
import it.web.routex.model.Mastercard;
import it.web.routex.utility.factory.FactoryLayerPersistenza;
import it.web.routex.utility.decorator.decoratorticket.BaseTicketCode;
import it.web.routex.utility.decorator.decoratorticket.CittaDecorator;
import it.web.routex.utility.decorator.decoratorticket.Component;
import it.web.routex.utility.decorator.decoratorticket.TimestampDecorator;
import it.web.routex.utility.singleton.Credentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PagamentoMastercard extends RegistrazionePagamentoController
{
    String numeroCarta;
    String scadenza;
    String cvv;
    private final Logger logger = LoggerFactory.getLogger(getClass());


    public PaymentResultBean run() throws DAOExceptionRemoli, PaymentValidationExceptionRemoli, CredentialsExceptionRemoli {

        final List<String> codiciBiglietti;

        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        Mastercard mastercard = layer.getPaymentMastercard(numeroCarta, scadenza, cvv);
        if (mastercard == null) {
            throw new PaymentValidationExceptionRemoli(
                    "Carta non valida o non presente nel sistema.",
                    PaymentMethod.MASTERCARD,
                    "PagamentoMastercard.run"
            );
        }

        mastercard.validate();

        Component gen = new TimestampDecorator(new CittaDecorator(new BaseTicketCode(), city));

        codiciBiglietti = new ArrayList<>();

        for (int i = 0; i < quantitativo; i++) {
            codiciBiglietti.add(gen.genera());
        }

        registraPagamentoPermanente(codiciBiglietti, mastercard);

        return new PaymentResultBean(
                city,
                quantitativo,
                totale,
                mastercard.getMethod().getDisplayName(),
                codiciBiglietti
        );
    }
    public PagamentoMastercard(String numeroCarta, String scadenza, String cvv, Credentials cred,double tot, int quantita, String citta )
    {
        super(tot, quantita, citta, cred);
        this.numeroCarta = numeroCarta;
        this.scadenza = scadenza;
        this.cvv = cvv;
    }

    private void registraPagamentoPermanente(List<String> codiciBiglietti, Mastercard mastercard) throws CredentialsExceptionRemoli {
        if (credenziali == null) {
            throw new CredentialsExceptionRemoli("Nessun utente loggato associato al pagamento.", "Errore nel PagamentoMastercard.java");
        }
        LayerPersistenza layer = FactoryLayerPersistenza.createLayerPersistenza();
        layer.salvataggio(credenziali, codiciBiglietti, mastercard.getMethod().getDisplayName(), city);
        logger.info("Pagamento effettuato con Mastercard {}", mastercard.maskedNumber());

    }
}


