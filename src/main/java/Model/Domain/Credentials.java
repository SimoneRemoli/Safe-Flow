package Model.Domain;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.sql.Date;

public class Credentials implements Serializable {

    private String nome;
    private String cognome;
    private String codiceFiscale;
    private String password;
    private String email;
    private Date dataDiNascita;
    private boolean disabile;
    private Ruolo ruolo;

    // Costruttore privato per forzare l'uso del getInstance
    public Credentials() {} //Infatti, dovrebbe essere privato [aggiustare]

    /**  Singleton per sessione */
    public static Credentials getInstance(HttpSession session) {
        Credentials cred = (Credentials) session.getAttribute("credentials"); //singleton per sessione (+ utenti dentro stessa sessione posso averli), non singleton globale, quindi non deve essere static cred
        if (cred == null) {
            cred = new Credentials();
            session.setAttribute("credentials", cred);
        }
        return cred;
    }

    /**  Metodo per rimuovere le credenziali dalla sessione (logout) */
    public static void clearInstance(HttpSession session) {
        session.removeAttribute("credentials");
    }

    // Getters & Setters standard ↓
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

    public Date getDataDiNascita() { return dataDiNascita; }
    public void setDataDiNascita(Date dataDiNascita) { this.dataDiNascita = dataDiNascita; }

    public boolean getDisabile() { return disabile; }
    public void setDisabile(boolean disabile) { this.disabile = disabile; }

    public Ruolo getRuolo() { return ruolo; }
    public void setRuolo(Ruolo ruolo) { this.ruolo = ruolo; }
}
