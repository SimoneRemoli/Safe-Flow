package Controller.Applicativo;

import Bean.AutenticazioneBean;
import Model.DAO.LoginProcedureDAO;
import Model.Domain.Credentials;
import Exception.DAOException;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController
{
    AutenticazioneBean autentica;
    Credentials cred;
    private static LoginController instance = null;

    public synchronized static LoginController getSingletonInstance(AutenticazioneBean b) {
        if (LoginController.instance == null)
            LoginController.instance = new LoginController(b);
        return instance;
    }

    public LoginController(AutenticazioneBean b)
    {
        this.autentica = b;
    }
    public Credentials autenticaUtente() throws DAOException, SQLException {
        this.cred = new Credentials("", "", "", autentica.getPassword(), autentica.getEmail(), null, false, null);
        LoginProcedureDAO utente = new LoginProcedureDAO();
        this.cred= utente.login(cred);
        return this.cred;
    }
    public Boolean qualeTabellA()
    {
        if(this.cred.getQuale_tabella()==1)
            return true;
        if(this.cred.getQuale_tabella()==2)
            return false;
        return true;
    }

}
