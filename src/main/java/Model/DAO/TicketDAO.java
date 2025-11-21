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
public class TicketDAO {

    public List<TicketBean> getTicketByCF(String cf)
            throws DAOExceptionRemoli, PathNotFoundExceptionRemoli {

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
                            "Errore in TicketDAO.getTicketByCF"
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

}
