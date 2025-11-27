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

    public void login(String email, String password) throws DAOExceptionRemoli, LoginNotFoundRemoli {

        Credentials credenzialiSingleton = Credentials.getInstanceSingleton();

        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.login_user(?, ?) }");

            cs.setString(1, email);
            cs.setString(2, password);

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                credenzialiSingleton.setCodiceFiscale(rs.getString("p_codiceFiscale"));
                credenzialiSingleton.setNome(rs.getString("p_nome"));
                credenzialiSingleton.setCognome(rs.getString("p_cognome"));
                credenzialiSingleton.setDataDiNascita(rs.getDate("p_dataDiNascita"));
                credenzialiSingleton.setDisabile(rs.getBoolean("p_disabile"));
                credenzialiSingleton.setRuolo(Ruolo.fromint(rs.getInt("ruolo")));
                credenzialiSingleton.setEmail(email);
                credenzialiSingleton.setPassword(password);
            } else {
                throw new LoginNotFoundRemoli("Credenziali non valide.", email, password);
            }

        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Errore durante il login: " + e.getMessage());
        }
    }
}

/*
L’eccezione LoginNotFoundRemoli scatterà quando la stored procedure login_user NON restituisce alcuna riga.
 */
