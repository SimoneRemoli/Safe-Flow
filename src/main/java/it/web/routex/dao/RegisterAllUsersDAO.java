package it.web.routex.dao;


import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.domain.CredentialsDTO;
import it.web.routex.utility.factory.ConnectionFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class RegisterAllUsersDAO {
    public void save(CredentialsDTO cred) throws DAOExceptionRemoli {
        // Uso try-with-resources per chiudere automaticamente la connessione
        //U CAN DO IT:)
        //no i can't,.. sure? si
        try (Connection conn = ConnectionFactory.getConnection()) {

            String sp = "{ CALL RouteX_Update.register(?, ?) }";
            try (CallableStatement cs = conn.prepareCall(sp)) {

                cs.setString(1, cred.getCodiceFiscale());
                cs.setString(2, cred.getEmail());

                cs.execute();
            }

        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Errore durante la registrazione del lavoratore: " + e.getMessage(), e);
        }
    }
}