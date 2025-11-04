package Bean;
import Model.Domain.Ruolo;

public class UtenteBeanGenerico
{
    private String nome, cognome, codice_fiscale;
    private boolean isDisable;
    private Ruolo ruolo;

    public boolean isDisable() {
        return isDisable;
    }

    public void setDisable(boolean disable) {
        isDisable = disable;
    }

    public void setCodice_fiscale(String codice_fiscale) {
        this.codice_fiscale = codice_fiscale;
    }

    public String getCodicefiscale() {
        return codice_fiscale;
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
