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

    public User(String codiceFiscale,
                String nome,
                String cognome,
                Date dataDiNascita,
                boolean disabile,
                Ruolo ruolo,
                String email,
                String password) {

        this.codiceFiscale = codiceFiscale;
        this.nome = nome;
        this.cognome = cognome;
        this.dataDiNascita = dataDiNascita;
        this.disabile = disabile;
        this.ruolo = ruolo;
        this.email = email;
        this.password = password;
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
