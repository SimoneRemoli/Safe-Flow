package Model.DAO;

import Factory.ConnectionFactory;
import Exception.DAOException;
import Model.Domain.Paypal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaypalDAO
{
    public Paypal GetPaymentPaypal(String email, String codice) throws DAOException, SQLException {

        final String query = "{ CALL RouteX_Update.getPaypalPayment(?,?) }";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(query)) {

            cs.setString(1, email);
            cs.setString(2, codice);

            try (ResultSet rs = cs.executeQuery()) {

                //  Se la stored procedure ha trovato una riga
                if (rs.next()) {
                    // Costruisci e restituisci un oggetto Mastercard con i dati trovati
                    Paypal found = new Paypal();
                    found.setEmail(rs.getString("email_paypal"));
                    found.setCodice(rs.getString("codice_transazione"));
                    System.out.println("[DAO] Pagamento trovato per carta paypal: " + found.getCodice());
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
