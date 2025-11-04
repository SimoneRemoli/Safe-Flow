package Model.Domain;
import java.sql.Date;

public class Credentials //questa deve essere una classe singleton che mantiene l'utente loggato
{
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private String password;
    private String email;
    private Date dataDiNascita;
    private boolean disabile;
    private Ruolo ruolo;


    public Credentials(String n, String c, String cf, String pwd, String e, Date d, boolean di)
    {
        this.nome=n;
        this.cognome=c;
        this.codiceFiscale=cf;
        this.password = pwd;
        this.email = e;
        this.dataDiNascita = d;
        this.disabile = di;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDataDiNascita() {
        return dataDiNascita;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }
    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public void setDataDiNascita(Date dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

    public boolean getDisabile() {
        return disabile;
    }
    public void setDisabile(boolean disabile) {
        this.disabile = disabile;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo role)
    {
        this.ruolo = role;
    }
}
