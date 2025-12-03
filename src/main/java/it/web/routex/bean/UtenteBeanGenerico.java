package it.web.routex.bean;
import it.web.routex.model.domain.Ruolo;

public class UtenteBeanGenerico
{
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private boolean isDisable;
    private Ruolo ruolo;

    public boolean isDisable() {
        return isDisable;
    }

    public void setDisable(boolean disable) {
        isDisable = disable;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getCodicefiscale() {
        return codiceFiscale;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getNome() {
        return nome;
    }
}
