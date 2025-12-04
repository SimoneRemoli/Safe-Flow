package it.web.routex.model.domain;

public class Mastercard
{
    String numeroCarta;
    String dataScadenza;
    String cvv;

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public void setDataScadenza(String dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public void setNumeroCarta(String numeroCarta) {
        this.numeroCarta = numeroCarta;
    }
}
