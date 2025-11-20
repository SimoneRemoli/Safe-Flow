package Model.DAO;

import Factory.ConnectionFactory;
import utility.Singleton.Credentials;
import Exception.DAOException;
import java.sql.*;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.util.List;

public class SalvaPagamentoDAO
{
    public void salvataggio(Credentials cred, List<String> codiciBiglietti, String metodopayment, String city) throws DAOException, SQLException
    {

        try {
            Connection conn = ConnectionFactory.getConnection();
            String bigliettiConcatenati = String.join(",", codiciBiglietti);
            CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.SavePayment(?,?,?,?,?,?,?) }");

            cs.setString(1, cred.getCodiceFiscale());
            cs.setString(2, cred.getNome());
            cs.setString(3, cred.getCognome());
            cs.setString(4, String.valueOf(cred.getDisabile()));
            cs.setString(5, metodopayment);
            cs.setString(6, bigliettiConcatenati);
            cs.setString(7, city);
            ResultSet rs = cs.executeQuery();

        } catch (Exception e) {
            throw new DAOException("Errore " + e.getMessage());
        }
    }


}


