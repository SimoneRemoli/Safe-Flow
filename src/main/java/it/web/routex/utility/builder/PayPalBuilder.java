package it.web.routex.utility.builder;
import it.web.routex.boundary.cli.view.PaypalCLI;
import it.web.routex.utility.builder.data.PaypalData;

public class PayPalBuilder
{
    private String emailPaypal;
    private String codiceTransazione;

    public PayPalBuilder(String emailPaypal)
    {
        this.emailPaypal = emailPaypal;
    }
    public PayPalBuilder codiceTransazione(String codiceTransazione)
    {
        this.codiceTransazione = codiceTransazione;
        return this;
    }
    public void build() {

        PaypalData data = new PaypalData();
        data.setCodiceTransazione(codiceTransazione);
        data.setEmailPaypal(emailPaypal);
        PaypalCLI.from(data);

    }
}
