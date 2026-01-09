package it.web.routex.dao;

public class PaypalData
{
    private String emailPaypal;
    private String codiceTransazione;

    public String getEmailPaypal() {
        return emailPaypal;
    }

    public String getCodiceTransazione() {
        return codiceTransazione;
    }

    public void setCodiceTransazione(String codiceTransazione) {
        this.codiceTransazione = codiceTransazione;
    }

    public void setEmailPaypal(String emailPaypal) {
        this.emailPaypal = emailPaypal;
    }
}
