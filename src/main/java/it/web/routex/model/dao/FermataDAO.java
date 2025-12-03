package it.web.routex.model.dao;

import it.web.routex.bean.FermataRecordBean;
import it.web.routex.utility.Factory.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FermataDAO {

    public List<FermataRecordBean> getFermateByIds(List<Integer> ids, String city) throws SQLException {
        List<FermataRecordBean> fermate = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.GetFermataById(?, ?) }")) {

            for (int id : ids) {
                cs.setString(1, city);
                cs.setInt(2, id);

                try (ResultSet rs = cs.executeQuery()) {
                    while (rs.next())
                    {
                        fermate.add(new FermataRecordBean(rs.getString("nome"), rs.getString("linea")));
                    }
                }
            }
        }
        return fermate;
    }
}
