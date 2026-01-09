package it.web.routex.dao;

import it.web.routex.enumerator.Ruolo;
import java.sql.Date;

public class UserData {

    private String codiceFiscale;
    private String nome;
    private String cognome;
    private Date dataDiNascita;
    private boolean disabile;
    private Ruolo ruolo;
    private String email;
    private String password;

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDataDiNascita(Date dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

    public void setDisabile(boolean disabile) {
        this.disabile = disabile;
    }

    public Boolean getDisabile()
    {
        return disabile;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public String getNome() {
        return nome;
    }

    public Date getDataDiNascita() {
        return dataDiNascita;
    }

    public String getEmail() {
        return email;
    }

    public String getCognome() {
        return cognome;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public String getPassword() {
        return password;
    }
}
