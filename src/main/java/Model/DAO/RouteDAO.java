package Model.DAO;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Exception.DAOExceptionRemoli;
import Exception.PathNotFoundExceptionRemoli;
import utility.Factory.ConnectionFactory;
import Model.Domain.Route;

public class RouteDAO {
    public void save(Route route) throws DAOExceptionRemoli {
        try (Connection conn = ConnectionFactory.getConnection()){
            String sp = "{ CALL RouteX_Update.saveRoute(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
            CallableStatement cs = conn.prepareCall(sp);

            cs.setString(1, route.getPartenza());
            cs.setString(2, route.getArrivo());
            cs.setString(3, route.getCitta());
            cs.setString(4, route.getTipoViaggiatore());
            cs.setInt(5, route.getnCambi());
            cs.setString(6, route.getListaCambi());
            cs.setString(7, route.getStazInterscambio());
            cs.setDouble(8, route.getnStazAttraversate());
            cs.setDouble(9, route.getTempoDiArrivo());
            cs.setInt(10, route.getnStazioniCitta());
            cs.setDouble(11, route.getPercTerrenoUtilizzato());
            cs.setString(12, route.getUtente()); // qui salvo utente

            cs.execute();

        } catch (Exception e) {
            throw new DAOExceptionRemoli("Errore durante la registrazione del percorso: " + e.getMessage());
        }
    }

    public List<Route> getData(String cf) throws PathNotFoundExceptionRemoli, DAOExceptionRemoli {
        try (Connection conn = ConnectionFactory.getConnection()){

            String sp = "{ CALL RouteX_Update.getPath(?) }";
            CallableStatement cs = conn.prepareCall(sp);

            cs.setString(1, cf);

            boolean trovato = cs.execute();

            if (trovato) {
                ResultSet rs = cs.getResultSet();
                List<Route> lista = new ArrayList<>();
                while (rs.next()) {
                    Route r = new Route();
                    r.setPartenza(rs.getString("StartStation"));
                    r.setArrivo(rs.getString("EndStation"));
                    r.setCitta(rs.getString("City"));
                    r.setTipoViaggiatore(rs.getString("TipoViaggiatore"));
                    r.setnCambi(rs.getInt("NCambi"));
                    r.setnStazAttraversate(rs.getInt("NStazioniAttraversate"));
                    r.setTempoDiArrivo(rs.getDouble("TempoDiArrivo"));
                    r.setnStazioniCitta(rs.getInt("NStazioniCitta"));
                    r.setPercTerrenoUtilizzato(rs.getDouble("PercTerrenoUtilizzato"));
                    r.setUtente(cf);
                    r.setListaCambi(rs.getString("ListaCambi"));
                    r.setStazInterscambio(rs.getString("StazioneDiInterscambio"));


                    lista.add(r);
                    // Qui puoi salvare o stampare i dati
                    System.out.println("🟢 RouteInfo: " + r.getPartenza() + " | " + r.getArrivo() + " | " + r.getCitta() + " | " + r.getTipoViaggiatore() + " | " + r.getnCambi() + " | " + r.getListaCambi() + " | " + r.getStazInterscambio() + " | " + r.getnStazAttraversate() + " | " + r.getTempoDiArrivo() + " | " + r.getnStazioniCitta() + " | " + r.getPercTerrenoUtilizzato() + " | " + r.getUtente());
                }
                return lista;
            } else {
                System.out.println("Nessun percorso trovato per l’utente.");
                throw new PathNotFoundExceptionRemoli("Nessun percorso trovato per l’utente.", cf, 404, "Errore nella RouteDAO.java nel metodo getData");
            }

        } catch (Exception e) {
            throw new DAOExceptionRemoli("Errore durante la connessione al database" + e.getMessage());
        }
    }
}
