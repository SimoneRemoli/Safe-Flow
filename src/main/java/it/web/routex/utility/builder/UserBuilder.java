package it.web.routex.utility.builder;



import it.web.routex.demo.User;
import it.web.routex.utility.builder.data.UserData;
import it.web.routex.enumerator.Ruolo;

import java.util.Date;

public class UserBuilder {

    private final String codiceFiscale;
    private String nome;
    private String cognome;
    private Date dataDiNascita;
    private boolean disabile;
    private Ruolo ruolo;
    private String email;
    private String password;

    public UserBuilder(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public UserBuilder nome(String nome) {
        this.nome = nome;
        return this;
    }

    public UserBuilder cognome(String cognome) {
        this.cognome = cognome;
        return this;
    }

    public UserBuilder dataDiNascita(Date dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
        return this;
    }

    public UserBuilder disabile(boolean disabile) {
        this.disabile = disabile;
        return this;
    }

    public UserBuilder ruolo(Ruolo ruolo) {
        this.ruolo = ruolo;
        return this;
    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }

    public User build() {
        UserData data = new UserData();
        data.setCodiceFiscale(codiceFiscale);
        data.setNome(nome);
        data.setCognome(cognome);
        data.setDataDiNascita((java.sql.Date) dataDiNascita);
        data.setDisabile(disabile);
        data.setRuolo(ruolo);
        data.setEmail(email);
        data.setPassword(password);

        return new User(data);
    }
}
