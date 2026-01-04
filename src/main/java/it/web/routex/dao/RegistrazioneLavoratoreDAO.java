package it.web.routex.dao;

import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.domain.CredentialsDTO;
import it.web.routex.utility.factory.ConnectionFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class RegistrazioneLavoratoreDAO {

    public void save(CredentialsDTO cred) throws DAOExceptionRemoli {
        // Uso try-with-resources per chiudere automaticamente la connessione
        try (Connection conn = ConnectionFactory.getConnection()) {

            String sp = "{ CALL RouteX_Update.register_worker(?, ?, ?, ?, ?, ?, ?, ?, ?) }";
            try (CallableStatement cs = conn.prepareCall(sp)) {

                cs.setString(1, cred.getNome());
                cs.setString(2, cred.getCognome());
                cs.setString(3, cred.getEmail());
                cs.setString(4, cred.getPassword());
                cs.setInt(5, cred.getRuolo().getId());
                cs.setString(6, cred.getCodiceFiscale());
                cs.setInt(7, cred.getOraInizio());
                cs.setInt(8, cred.getOraFine());
                cs.setString(9, cred.getLuogoDiLavoro());

                cs.execute();
            }

        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Errore durante la registrazione del lavoratore: " + e.getMessage(), e);
        }
    }
}