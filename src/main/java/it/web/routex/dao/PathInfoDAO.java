package it.web.routex.dao;

import it.web.routex.exception.DAOExceptionRemoli;
import it.web.routex.model.Route;
import it.web.routex.utility.factory.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PathInfoDAO {

    public List<Route> getAllPathInfo() throws DAOExceptionRemoli {

        List<Route> resultList = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection()) {

            CallableStatement cs =
                    conn.prepareCall("{ CALL RouteX_Update.getAllPathInfo() }");

            if (cs.execute()) {
                try (ResultSet rs = cs.getResultSet()) {
                    while (rs.next()) {

                        Route info = new Route(
                                rs.getString("StartStation"),
                                rs.getString("EndStation"),
                                rs.getString("City"),
                                rs.getString("TipoViaggiatore"),
                                rs.getInt("NCambi"),
                                rs.getString("ListaCambi"),
                                rs.getString("StazioneDiInterscambio"),
                                rs.getInt("NStazioniAttraversate"),
                                rs.getDouble("TempoDiArrivo"),
                                rs.getInt("NStazioniCitta"),
                                rs.getDouble("PercTerrenoUtilizzato"),
                                rs.getString("Utente")
                        );

                        resultList.add(info);
                    }
                }
            }

        } catch (SQLException e) {
            throw new DAOExceptionRemoli(
                    "Errore nel recupero delle statistiche PathInfo",
                    e
            );
        }

        return resultList;
    }
}
