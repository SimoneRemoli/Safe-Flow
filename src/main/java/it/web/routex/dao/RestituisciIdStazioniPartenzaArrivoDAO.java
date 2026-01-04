package it.web.routex.dao;

import it.web.routex.utility.factory.ConnectionFactory;
import java.sql.*;

public class RestituisciIdStazioniPartenzaArrivoDAO {

    private int stazioneDiPartenza;
    private int stazioneDiArrivo;

    public int getStazioneDiPartenza() {
        return stazioneDiPartenza;
    }

    public int getStazioneDiArrivo() {
        return stazioneDiArrivo;
    }

    public void restituisciIdStazioni(String startStation, String endStation, String city) throws SQLException {
        String procedure = "{CALL RouteX_Update.RestituisciStazioni(?,?,?)}";
        boolean partenzaTrovata = false;
        boolean arrivoTrovata = false;

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(procedure)) {

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
                            this.stazioneDiPartenza = rs.getInt("id");
                            partenzaTrovata = true;
                        }

                        if (resultIndex == 2 && rs.next()) {
                            this.stazioneDiArrivo = rs.getInt("id");
                            arrivoTrovata = true;
                        }
                    }
                }
                hasResults = cs.getMoreResults();
            }

        } catch (SQLException e) {
            throw new SQLException("Errore durante l'esecuzione della stored procedure RestituisciStazioni", e);
        }
        if (!partenzaTrovata) {
            this.stazioneDiPartenza = 9999; // Valore di default se non trovata
        }
        if (!arrivoTrovata) {
            this.stazioneDiArrivo = 9999; // Valore di default se non trovata
        }
    }
}



/* su RouteX DB - Remoto
+------+----------+----------+-------+
| id   | nome     | disabile | linea |
+------+----------+----------+-------+
|    0 | Rebibbia | no       | B     |
+------+----------+----------+-------+
 */