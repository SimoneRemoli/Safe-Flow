package Model.DAO;
import utility.Factory.ConnectionFactory;
import utility.Singleton.Credentials;
import Exception.DAOExceptionRemoli;
import Model.Domain.Ruolo;
import java.sql.*;
import java.sql.Connection;
import java.sql.CallableStatement;
import Exception.LoginNotFoundRemoli;

public class LoginProcedureDAO {

    public Credentials login(Credentials cred) throws DAOExceptionRemoli, LoginNotFoundRemoli {

        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.login_user(?, ?) }");

            cs.setString(1, cred.getEmail());
            cs.setString(2, cred.getPassword());

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                cred.setCodiceFiscale(rs.getString("p_codiceFiscale"));
                cred.setNome(rs.getString("p_nome"));
                cred.setCognome(rs.getString("p_cognome"));
                cred.setDataDiNascita(rs.getDate("p_dataDiNascita"));
                cred.setDisabile(rs.getBoolean("p_disabile"));
                cred.setRuolo(Ruolo.fromint(rs.getInt("ruolo")));
            } else {
                throw new LoginNotFoundRemoli("Credenziali non valide.", cred.getEmail(), cred.getPassword());
            }
            return cred;

        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Errore durante il login: " + e.getMessage());
        }
    }
}

/*
L’eccezione LoginNotFoundRemoli scatterà quando la stored procedure login_user NON restituisce alcuna riga.
 */
