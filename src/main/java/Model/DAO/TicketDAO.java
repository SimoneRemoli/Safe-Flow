package Model.DAO;

import Factory.ConnectionFactory;
import Bean.TicketBean;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

    public List<TicketBean> getTicketByCF(String cf) throws SQLException {

        List<TicketBean> lista = new ArrayList<>();

        String SP = "{ CALL RouteX_Update.getTicketByCF(?) }";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(SP)) {

            cs.setString(1, cf);

            try (ResultSet rs = cs.executeQuery()) {

                while (rs.next()) {
                    TicketBean t = new TicketBean();
                    t.setCodice(rs.getString("codice_biglietto"));
                    t.setCitta(rs.getString("citta"));
                    t.setDataAcquisto(rs.getString("data_pagamento"));
                    lista.add(t);
                }
            }
        }

        return lista;
    }
}
