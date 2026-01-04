package it.web.routex.dao;

import it.web.routex.bean.PathInfoBean;
import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.utility.factory.ConnectionFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PathInfoDAO {

    public List<PathInfoBean> getAllPathInfo() throws DAOExceptionRemoli {
        List<PathInfoBean> resultList = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection()) {
            CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.getAllPathInfo() }");

            boolean hasResult = cs.execute();

            if (hasResult) {
                try (ResultSet rs = cs.getResultSet()) {
                    while (rs.next()) {
                        PathInfoBean info = new PathInfoBean();
                        info.setStartStation(rs.getString("StartStation"));
                        info.setEndStation(rs.getString("EndStation"));
                        info.setCity(rs.getString("City"));
                        info.setTipoViaggiatore(rs.getString("TipoViaggiatore"));
                        info.setNCambi(rs.getInt("NCambi"));
                        info.setListaCambi(rs.getString("ListaCambi"));
                        info.setStazioneDiInterscambio(rs.getString("StazioneDiInterscambio"));
                        info.setNStazioniAttraversate(rs.getInt("NStazioniAttraversate"));
                        info.setTempoDiArrivo(rs.getDouble("TempoDiArrivo"));
                        info.setNStazioniCitta(rs.getInt("NStazioniCitta"));
                        info.setPercTerrenoUtilizzato(rs.getDouble("PercTerrenoUtilizzato"));
                        info.setUtente(rs.getString("Utente"));

                        resultList.add(info);
                    }
                }
            }

        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Errore nel recupero dei dati PathInfo: " + e.getMessage());
        }

        return resultList;
    }
}