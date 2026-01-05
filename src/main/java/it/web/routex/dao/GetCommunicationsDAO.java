package it.web.routex.dao;

import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.model.Notification;
import it.web.routex.utility.factory.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GetCommunicationsDAO {

    public List<Notification> getMessages() throws DAOExceptionRemoli {

        List<Notification> result = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.getMessages() }")) {

            if (cs.execute()) {
                try (ResultSet rs = cs.getResultSet()) {
                    while (rs.next()) {
                        Notification notification = new Notification(
                                rs.getString("testo"),
                                rs.getTimestamp("data"),
                                rs.getBoolean("risolto")
                        );
                        result.add(notification);
                    }
                }
            }

            if (result.isEmpty()) {
                throw new DAOExceptionRemoli("Nessuna notifica presente nel sistema");
            }

        } catch (SQLException e) {
            throw new DAOExceptionRemoli(
                    "Errore nel recupero delle notifiche",
                    e
            );
        }

        return result;
    }

}
