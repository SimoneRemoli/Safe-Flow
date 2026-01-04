package it.web.routex.dao;

import it.web.routex.model.Station;
import it.web.routex.utility.factory.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestituisciIdStazioniPartenzaArrivoDAO
{
    public List<Station> restituisciIdStazioni(String startStation, String endStation, String city) throws SQLException
    {
        String procedure = "{CALL RouteX_Update.RestituisciStazioni(?,?,?)}";
        List<Station> stations = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(procedure))
        {
            cs.setString(1, startStation);
            cs.setString(2, endStation);
            cs.setString(3, city);

            boolean hasResults = cs.execute();
            int resultIndex = 0;

            while (hasResults) {
                try (ResultSet rs = cs.getResultSet()) {
                    resultIndex++;
                    if (rs != null) {
                        if (resultIndex == 1 && rs.next()) {
                            stations.add(new Station(rs.getInt("id")));
                        }

                        if (resultIndex == 2 && rs.next()) {
                            stations.add(new Station(rs.getInt("id")));
                        }
                    }
                }
                hasResults = cs.getMoreResults();
            }

        } catch (SQLException e) {
            throw new SQLException("Errore durante l'esecuzione della stored procedure RestituisciStazioni", e);
        }
        return stations;
    }
}
