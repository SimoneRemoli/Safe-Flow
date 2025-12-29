package it.web.routex.model.domain;

import java.sql.Date;

public class CredentialsDTO {

    private String nome;
    private String cognome;
    private String codiceFiscale;
    private String password;
    private String email;
    private Date dataDiNascita;
    private Boolean disabile;
    private Ruolo ruolo;
    private Integer oraInizio;
    private Integer oraFine;
    private String luogoDiLavoro;

    public CredentialsDTO() { }

    public CredentialsDTO(String nome, String cognome, String codiceFiscale,
                          String password, String email, Date dataDiNascita,
                          Boolean disabile, Ruolo ruolo, Integer oraInizio, Integer oraFine, String luogoDiLavoro) {
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.password = password;
        this.email = email;
        this.dataDiNascita = dataDiNascita;
        this.disabile = disabile;
        this.ruolo = ruolo;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.luogoDiLavoro = luogoDiLavoro;
    }

    // Getters & Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public String getCodiceFiscale() { return codiceFiscale; }
    public void setCodiceFiscale(String codiceFiscale) { this.codiceFiscale = codiceFiscale; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLuogoDiLavoro() { return luogoDiLavoro; }
    public void setLuogoDiLavoro(String luogoDiLavoro) { this.luogoDiLavoro = luogoDiLavoro; }

    public Date getDataDiNascita() { return dataDiNascita; }
    public void setDataDiNascita(Date dataDiNascita) { this.dataDiNascita = dataDiNascita; }

    public Boolean getDisabile() { return disabile; }
    public void setDisabile(Boolean disabile) { this.disabile = disabile; }

    public Ruolo getRuolo() { return ruolo; }
    public void setRuolo(Ruolo ruolo) { this.ruolo = ruolo; }
    //
    public Integer getOraInizio() { return oraInizio; }
    public void setOraInizio(Integer oraInizio) { this.oraInizio = oraInizio; }
    //
    public Integer getOraFine() { return oraFine; }
    public void setOraFine(Integer oraFine) { this.oraFine = oraFine; }
}