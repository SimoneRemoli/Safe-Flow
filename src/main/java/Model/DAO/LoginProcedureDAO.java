package Model.DAO;
import Factory.ConnectionFactory;
import Model.Domain.Credentials;
import Exception.DAOException;
import Model.Domain.Ruolo;
import java.sql.*;
import java.sql.Connection;
import java.sql.CallableStatement;


public class LoginProcedureDAO {

    public Credentials login(Credentials cred) throws DAOException, SQLException {

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
                throw new DAOException("Credenziali non valide.");
            }
            return cred;

        } catch (Exception e) {
            throw new DAOException("Errore durante il login: " + e.getMessage());
        }
    }
}


