package it.web.routex.dao;

import it.web.routex.enumerator.Ruolo;

import java.sql.Date;

/**
 * Modello utente minimale per DEMO version.
 */
public class User {

    private String codiceFiscale;
    private String nome;
    private String cognome;
    private Date dataDiNascita;
    private boolean disabile;
    private Ruolo ruolo;
    private String email;
    private String password;

    public User(UserData data) {
        this.codiceFiscale = data.getCodiceFiscale();
        this.nome = data.getNome();
        this.cognome = data.getCognome();
        this.dataDiNascita = data.getDataDiNascita();
        this.disabile = data.getDisabile();
        this.ruolo = data.getRuolo();
        this.email = data.getEmail();
        this.password = data.getPassword();
    }


    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public Date getDataDiNascita() {
        return dataDiNascita;
    }

    public boolean isDisabile() {
        return disabile;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
