package it.web.routex.dao;

import it.web.routex.bean.WorkerScheduleBean;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.exception.LoginNotFoundRemoli;
import it.web.routex.utility.factory.ConnectionFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkerScheduleDAO {

    public WorkerScheduleBean getWorkerSchedule(String codiceFiscale)
            throws DAOExceptionRemoli, LoginNotFoundRemoli {

        String sp = "{ CALL RouteX_Update.viewWorkSchedule(?) }";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {

            cs.setString(1, codiceFiscale);

            try (ResultSet rs = cs.executeQuery()) {

                if (!rs.next()) {
                    throw new DAOExceptionRemoli("Errore.");
                }

                Integer oraInizio = rs.getObject("oraInizio", Integer.class);
                Integer oraFine   = rs.getObject("oraFine", Integer.class);
                String luogoDiLavoro = rs.getString("luogoDiLavoro");

                // --- Creo e ritorno il DTO ---
                WorkerScheduleBean cred = new WorkerScheduleBean(
                        oraInizio,
                        oraFine,
                        luogoDiLavoro
                );

                return cred;
            }

        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Errore durante il login: " + e.getMessage(), e);
        }
    }
}