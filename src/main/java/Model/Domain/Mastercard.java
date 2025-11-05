package Model.Domain;

public class Mastercard
{
    String numero_carta, data_scadenza, cvv;


    public String getNumero_carta() {
        return numero_carta;
    }

    public String getCvv() {
        return cvv;
    }

    public String getData_scadenza() {
        return data_scadenza;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public void setData_scadenza(String data_scadenza) {
        this.data_scadenza = data_scadenza;
    }

    public void setNumero_carta(String numero_carta) {
        this.numero_carta = numero_carta;
    }
}
