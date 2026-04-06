package it.web.routex.dao;
import it.web.routex.model.Ticket;
import it.web.routex.utility.factory.ConnectionFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import it.web.routex.exception.PathNotFoundExceptionRemoli;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.utility.singleton.Credentials;
import it.web.routex.exception.CredentialsExceptionRemoli;

public class TicketDAODB {

    public List<Ticket> getTicketByCF(String cf)
            throws DAOExceptionRemoli, PathNotFoundExceptionRemoli {

        List<Ticket> lista = new ArrayList<>();
        String sP = "{ CALL RouteX_Update.getTicketByCF(?) }";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(sP)) {

            cs.setString(1, cf);

            try (ResultSet rs = cs.executeQuery()) {

                if (!rs.next()) {
                    throw new PathNotFoundExceptionRemoli(
                            "Nessun biglietto trovato",
                            cf,
                            404,
                            "TicketDAODB.getTicketByCF"
                    );
                }

                do {
                    Ticket t = new Ticket(
                            rs.getString("codice_biglietto"),
                            rs.getString("citta"),
                            rs.getTimestamp("data_pagamento").toLocalDateTime()
                    );
                    lista.add(t);
                } while (rs.next());
            }

            return lista;

        } catch (SQLException ex) {
            throw new DAOExceptionRemoli("Errore DB", ex);
        }
    }

    public int deleteTicketsByCodes(String cf, List<String> ticketCodes) throws DAOExceptionRemoli {
        int deleted = 0;
        String procedure = "{ CALL RouteX_Update.DeleteTicketByCode(?, ?, ?) }";

        try (Connection conn = ConnectionFactory.getConnection()) {
            for (String ticketCode : ticketCodes) {
                try (CallableStatement cs = conn.prepareCall(procedure)) {
                    cs.setString(1, cf);
                    cs.setString(2, ticketCode);
                    cs.registerOutParameter(3, java.sql.Types.INTEGER);
                    cs.execute();
                    deleted += cs.getInt(3);
                }
            }
            return deleted;
        } catch (SQLException ex) {
            throw new DAOExceptionRemoli("Errore DB durante l'eliminazione dei biglietti", ex);
        }
    }

    public int deleteAllTickets(String cf) throws DAOExceptionRemoli {
        String procedure = "{ CALL RouteX_Update.DeleteAllTickets(?, ?) }";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(procedure)) {

            cs.setString(1, cf);
            cs.registerOutParameter(2, java.sql.Types.INTEGER);
            cs.execute();
            return cs.getInt(2);
        } catch (SQLException ex) {
            throw new DAOExceptionRemoli("Errore DB durante l'eliminazione completa dei biglietti", ex);
        }
    }

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
            cs.executeQuery();

        } catch (SQLException e) {
            throw new CredentialsExceptionRemoli("Nessun salvataggio del percorso nel livello di persistenza " + e.getMessage(), "Errore in SalvaPagamentoDAO.java");
        }
    }

}
