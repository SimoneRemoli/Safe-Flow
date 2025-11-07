package Model.DAO;

import Factory.ConnectionFactory;
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
                        if (resultIndex == 1 && rs.next())
                            this.stazioneDiPartenza = rs.getInt("id");

                        if (resultIndex == 2 && rs.next())
                            this.stazioneDiArrivo = rs.getInt("id");
                    }
                }
                hasResults = cs.getMoreResults();
            }

        } catch (SQLException e) {
            throw new SQLException("Errore durante l'esecuzione della stored procedure RestituisciStazioni", e);
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