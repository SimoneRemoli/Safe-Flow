package Model.DAO;

/*import Bean.TravelerBean;
import Factory.ConnectionFactory;
import Model.Domain.Credentials;
import Exception.DAOException;
import Model.Domain.Ruolo;

import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.util.List;

public class SalvaPagamentoDAO
{
    public void salvataggio(TravelerBean traveler, List<String> codiciBiglietti, String metodopayment, String city) throws DAOException, SQLException {

        try {
            String bigliettiConcatenati = String.join(",", codiciBiglietti);
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.SavePayment(?,?,?,?,?,?,?) }");

            cs.setString(1, traveler.getCodiceFiscale());
            cs.setString(2, traveler.getNome());
            cs.setString(3, traveler.getCognome());
            cs.setString(4, String.valueOf(traveler.getDisabile()));
            cs.setString(5, metodopayment);
            cs.setString(6, bigliettiConcatenati);
            cs.setString(7, city);


            ResultSet rs = cs.executeQuery();


        } catch (Exception e) {
            throw new DAOException("Errore " + e.getMessage());
        }
    }
}

 */
