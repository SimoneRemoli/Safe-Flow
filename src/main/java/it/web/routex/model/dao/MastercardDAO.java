package it.web.routex.model.dao;

import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.model.domain.PaymentMethod;
import it.web.routex.utility.Factory.ConnectionFactory;
import it.web.routex.model.domain.Mastercard;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import it.web.routex.exception.PaymentValidationExceptionRemoli;

public class MastercardDAO {

    public Mastercard GetPaymentMastercard(String nC, String sc, String cvv)
            throws DAOExceptionRemoli, PaymentValidationExceptionRemoli{

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
                    throw new PaymentValidationExceptionRemoli("Nessun pagamento trovato per la carta indicata.", PaymentMethod.MASTERCARD, "MastercardDAO.java ha fallito");
                   //System.out.println("[DAO] Nessun pagamento trovato per la carta indicata.");
                   // return null;
                }
            }

        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Errore interno alla connessione: " + e.getMessage());
        }
    }
}
