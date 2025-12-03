package it.web.routex.model.dao;

import it.web.routex.model.domain.PaymentMethod;
import it.web.routex.utility.Factory.ConnectionFactory;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.exception.PaymentValidationExceptionRemoli;
import it.web.routex.model.domain.Paypal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaypalDAO
{
    public Paypal GetPaymentPaypal(String email, String codice) throws DAOExceptionRemoli, PaymentValidationExceptionRemoli {

        final String query = "{ CALL RouteX_Update.getPaypalPayment(?,?) }";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(query)) {

            cs.setString(1, email);
            cs.setString(2, codice);

            try (ResultSet rs = cs.executeQuery()) {

                //  Se la stored procedure ha trovato una riga
                if (rs.next()) {
                    // Costruisco e restituisco un oggetto Paypal con i dati trovati
                    Paypal found = new Paypal();
                    found.setEmail(rs.getString("email_paypal"));
                    found.setCodice(rs.getString("codice_transazione"));
                    System.out.println("[DAO] Pagamento trovato per carta paypal: " + found.getCodice());
                    return found;
                } else {
                    // Nessuna riga trovata → ritorna null
                    throw new PaymentValidationExceptionRemoli("Nessun pagamento trovato per la carta indicata.", PaymentMethod.PAYPAL, "MastercardDAO.java ha fallito");
                }
            }

        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Errore in GetPaymentMastercard: " + e.getMessage());
        }
    }
}
