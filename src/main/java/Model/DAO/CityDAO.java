package Model.DAO;
import utility.Factory.ConnectionFactory;
import Model.Domain.City;
import Exception.DAOExceptionRemoli;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CityDAO
{
    private final List<City> informazioni = new ArrayList<>();
    public List<City> ListCities() throws DAOExceptionRemoli
    {
        try {

            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.getAllCity() }");
            ResultSet rs =  cs.executeQuery();
            while (rs.next()) {
                String nome = rs.getString("nome_citta");
                double costo = rs.getDouble("prezzo_ticket");
                long numero = rs.getLong("numero_stazioni");
                informazioni.add(new City(nome, costo, numero));
                System.out.println("Città trovata: " + nome + "numero stazioni: "+numero);
            }


        }catch (SQLException e) {
            throw new DAOExceptionRemoli("Errore nella comunicazione con il database: " + e.getMessage());
        }
        return informazioni;

    }

}
