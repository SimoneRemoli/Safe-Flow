package it.web.routex.dao;

import it.web.routex.enumerator.PaymentMethod;
import it.web.routex.utility.factory.ConnectionFactory;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.exception.PaymentValidationExceptionRemoli;
import it.web.routex.model.Paypal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaypalDAO
{
    public Paypal getPaymentPaypal(String email, String codice) throws DAOExceptionRemoli, PaymentValidationExceptionRemoli {

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
                    return found;
                } else {
                    throw new PaymentValidationExceptionRemoli("Nessun pagamento trovato per i dati Paypal inseriti.", PaymentMethod.PAYPAL, "MastercardDAO.java ha fallito");
                }
            }
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Errore in GetPaymentMastercard: " + e.getMessage());
        }
    }
}
