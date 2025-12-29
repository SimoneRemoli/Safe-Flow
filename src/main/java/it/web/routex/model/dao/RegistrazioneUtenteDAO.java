package it.web.routex.model.dao;


import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.model.domain.CredentialsDTO;
import it.web.routex.utility.factory.ConnectionFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class RegistrazioneUtenteDAO {

    public void save(CredentialsDTO cred) throws DAOExceptionRemoli {
        // Uso try-with-resources per chiudere automaticamente la connessione
        try (Connection conn = ConnectionFactory.getConnection()) {

            String sp = "{ CALL RouteX_Update.register_user(?, ?, ?, ?, ?, ?, ?, ?) }";
            try (CallableStatement cs = conn.prepareCall(sp)) {

                cs.setString(1, cred.getNome());
                cs.setString(2, cred.getCognome());
                cs.setString(3, cred.getCodiceFiscale());
                cs.setString(4, cred.getPassword());
                cs.setString(5, cred.getEmail());
                cs.setDate(6, cred.getDataDiNascita());
                cs.setBoolean(7, cred.getDisabile());
                cs.setInt(8, cred.getRuolo().getId());

                cs.execute();
            }

        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Errore durante la registrazione utente: " + e.getMessage(), e);
        }
    }
}