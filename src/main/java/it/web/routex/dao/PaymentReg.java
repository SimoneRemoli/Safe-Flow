package it.web.routex.dao;

public class PaymentReg {

    private final String codiceFiscale;
    private final String nome;
    private final String cognome;
    private final boolean disabile;
    private final String metodoPagamento;
    private final String biglietti;
    private final String city;

    public PaymentReg(
            String codiceFiscale,
            String nome,
            String cognome,
            boolean disabile,
            String metodoPagamento,
            String biglietti,
            String city
    ) {
        this.codiceFiscale = codiceFiscale;
        this.nome = nome;
        this.cognome = cognome;
        this.disabile = disabile;
        this.metodoPagamento = metodoPagamento;
        this.biglietti = biglietti;
        this.city = city;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public String getBiglietti() {
        return biglietti;
    }

    public String getCity() {
        return city;
    }
}
