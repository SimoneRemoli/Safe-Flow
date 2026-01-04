package it.web.routex.dao;

import it.web.routex.bean.WorkerScheduleBean;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.exception.LoginNotFoundRemoli;
import it.web.routex.model.WorkerSchedule;
import it.web.routex.utility.factory.ConnectionFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkerScheduleDAO {

    public WorkerSchedule getWorkerSchedule(String codiceFiscale)
            throws DAOExceptionRemoli, LoginNotFoundRemoli {

        String sp = "{ CALL RouteX_Update.viewWorkSchedule(?) }";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(sp)) {

            cs.setString(1, codiceFiscale);

            try (ResultSet rs = cs.executeQuery())
            {

                if (!rs.next()) {
                    throw new DAOExceptionRemoli("Errore.");
                }

                int oraInizio = rs.getInt("oraInizio");
                int oraFine   = rs.getInt("oraFine");
                String luogo  = rs.getString("luogoDiLavoro");

                return new WorkerSchedule(
                        oraInizio,
                        oraFine,
                        luogo
                );
            }

        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Errore durante la chiamata a viewWorkSchedule: " + e.getMessage(), e);
        }
    }
}