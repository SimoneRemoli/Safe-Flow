package Model.DAO;
import utility.Factory.ConnectionFactory;
import Bean.TicketBean;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Exception.PathNotFoundExceptionRemoli;
import Exception.DAOExceptionRemoli;
import utility.Singleton.Credentials;
import Exception.CredentialsExceptionRemoli;

public class TicketDAODB extends TicketDAOLayer {

    @Override
    public List<TicketBean> getTicketByCF(String cf) throws DAOExceptionRemoli, PathNotFoundExceptionRemoli
    {
        List<TicketBean> lista = new ArrayList<>();
        String SP = "{ CALL RouteX_Update.getTicketByCF(?) }";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(SP)) {

            cs.setString(1, cf);

            try (ResultSet rs = cs.executeQuery()) {

                // Nessun ticket trovato : eccezione personalizzata
                if (!rs.next()) {
                    throw new PathNotFoundExceptionRemoli(
                            "Nessun biglietto trovato per il codice fiscale fornito.",
                            cf,
                            404,
                            "Errore in TicketDAODB.getTicketByCF"
                    );
                }

                // Se arrivi qui, la prima riga ESISTE → la leggi
                do {
                    TicketBean t = new TicketBean();
                    t.setCodice(rs.getString("codice_biglietto"));
                    t.setCitta(rs.getString("citta"));
                    t.setDataAcquisto(rs.getString("data_pagamento"));
                    lista.add(t);
                } while (rs.next());
            }

            return lista;

        } catch (SQLException ex) {
            // QUALSIASI errore SQL : DAOException
            throw new DAOExceptionRemoli("Errore con il database " + ex.getMessage(), ex);
        }
    }

    @Override
    public void salvataggio(Credentials cred, List<String> codiciBiglietti, String metodopayment, String city) throws CredentialsExceptionRemoli {
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

        } catch (SQLException e) {
            throw new CredentialsExceptionRemoli("Nessun salvataggio del percorso nel livello di persistenza " + e.getMessage(), "Errore in SalvaPagamentoDAO.java");
        }
    }

}
