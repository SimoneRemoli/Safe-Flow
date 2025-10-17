package Model.DAO;
import Factory.ConnectionFactory;
import Model.Domain.City;
import Exception.DAOException;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CityDAO
{
    private final List<City> informazioni = new ArrayList<>();
    public List<City> ListCities() throws DAOException, SQLException
    {
        try {

            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.getAllCity() }");
            ResultSet rs =  cs.executeQuery();
            //while(rs.next()) informazioni.add(new City(rs.getString(1)));
            while (rs.next()) {
                String nome = rs.getString(1);
                double costo = rs.getDouble(2);
                System.out.println("Città trovata: " + nome);
                informazioni.add(new City(nome,costo));
            }


        }catch (SQLException e) {
        throw new DAOException("Errore. " + e.getMessage());
        }
        return informazioni;

    }

}
