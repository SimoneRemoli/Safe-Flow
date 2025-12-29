package it.web.routex.model.dao;
import it.web.routex.bean.MessageBean;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.utility.factory.ConnectionFactory;

import java.sql.CallableStatement;
import java.sql.Connection;

public class SendCommunicationDAO {

    public void sendMessage(MessageBean m) throws DAOExceptionRemoli {

        try (Connection conn = ConnectionFactory.getConnection()) {

            CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.spCommunication(?, ?) }");

            cs.setString(1, m.getMessage());
            cs.setTimestamp(2, m.getDate());

            cs.execute();

        } catch (Exception e) {
            throw new DAOExceptionRemoli("Errore durante l'invio del messaggio: " + e.getMessage());
        }
    }

    public void solvedMessage(MessageBean m) throws DAOExceptionRemoli {

        try (Connection conn = ConnectionFactory.getConnection()) {

            CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.spSolvedMessage(?, ?, ?) }");

            cs.setString(1, m.getMessage());
            cs.setTimestamp(2, m.getDate());
            cs.setBoolean(3, m.getRisolto());

            cs.execute();

        } catch (Exception e) {
            throw new DAOExceptionRemoli("Errore durante l'aggiornamento del messaggio: " + e.getMessage());
        }
    }
}