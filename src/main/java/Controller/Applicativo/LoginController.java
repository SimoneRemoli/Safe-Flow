package Controller.Applicativo;

import Bean.AutenticazioneBean;
import Bean.TravelerBean;
import Bean.UtenteBeanGenerico;
import Bean.WorkerOAdminBean;
import Model.DAO.LoginProcedureDAO;
import Model.Domain.Credentials;
import Exception.DAOException;

import java.sql.SQLException;

public class LoginController
{
    AutenticazioneBean autentica;
    Credentials cred;
    public LoginController(AutenticazioneBean b)
    {
        this.autentica = b;
    }

    public UtenteBeanGenerico autenticaUtente() throws DAOException, SQLException {
        this.cred = new Credentials("", "", "", autentica.getPassword(), autentica.getEmail(), null, false, null);
        LoginProcedureDAO utente = new LoginProcedureDAO();
        this.cred= utente.login(cred);

        if(cred.getQuale_tabella()==1)//traveler
        {
            TravelerBean un = new TravelerBean();
            un.setNome(this.cred.getNome());
            un.setCognome(this.cred.getCognome());
            un.setCodiceFiscale(this.cred.getCodiceFiscale());
            un.setDisable(this.cred.getDisabile());
            return un;
        }
        if(cred.getQuale_tabella()==2)//worker o  admin
        {
            WorkerOAdminBean wa = new WorkerOAdminBean();
            wa.setNome(this.cred.getNome());
            wa.setCognome(this.cred.getCognome());
            wa.setRuolo(this.cred.getRuolo());
            return wa;

        }
        return null;
    }

}
