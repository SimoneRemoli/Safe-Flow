package it.web.routex.bean;

public class TicketBean
{
    private String codice;
    private String citta;
    private String dataAcquisto;


    public void setCodice(String codice) {
        this.codice = codice;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public void setDataAcquisto(String dataAcquisto) {
        this.dataAcquisto = dataAcquisto;
    }

    public String getCodice() {
        return codice;
    }

    public String getCitta() {
        return citta;
    }

    public String getDataAcquisto() {
        return dataAcquisto;
    }
}
