package it.web.routex.dao;

import it.web.routex.bean.MessageBean;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.utility.factory.ConnectionFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetCommunicationsDAO {
    public List<MessageBean> getMessages() throws DAOExceptionRemoli {
        List<MessageBean> resultList = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection()) {
            CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.getMessages() }");

            boolean hasResult = cs.execute();

            if (hasResult) {
                try (ResultSet rs = cs.getResultSet()) {
                    while (rs.next()) {
                        MessageBean info = new MessageBean();

                        info.setMessage(rs.getString("testo"));
                        info.setDate(rs.getTimestamp("data"));
                        info.setRisolto(rs.getBoolean("risolto"));

                        resultList.add(info);
                    }
                }
            }

        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Errore nel recupero dei messaggi: " + e.getMessage());
        }

        return resultList;
    }
}