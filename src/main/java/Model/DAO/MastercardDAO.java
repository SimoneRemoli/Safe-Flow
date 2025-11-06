package Model.DAO;

import Exception.DAOException;
import Factory.ConnectionFactory;
import Model.Domain.Mastercard;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MastercardDAO {

    public Mastercard GetPaymentMastercard(String nC, String sc, String cvv)
            throws DAOException, SQLException {

        final String query = "{ CALL RouteX_Update.getMastercardPayment(?,?,?) }";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(query)) {

            cs.setString(1, nC);
            cs.setString(2, sc);
            cs.setString(3, cvv);

            try (ResultSet rs = cs.executeQuery()) {

                //  Se la stored procedure ha trovato una riga
                if (rs.next()) {
                    // Costruisco e restituisco un oggetto Mastercard con i dati trovati
                    Mastercard found = new Mastercard();
                    found.setNumero_carta(rs.getString("numero_carta"));
                    found.setData_scadenza(rs.getString("data_scadenza"));
                    found.setCvv(rs.getString("cvv"));

                    System.out.println("[DAO] Pagamento trovato per carta: " + found.getNumero_carta());
                    return found;
                } else {
                    // Nessuna riga trovata → ritorna null
                    System.out.println("[DAO] Nessun pagamento trovato per la carta indicata.");
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Errore in GetPaymentMastercard: " + e.getMessage());
        }
    }
}
