package it.web.routex.dao;
import it.web.routex.utility.factory.ConnectionFactory;
import it.web.routex.model.City;
import it.web.routex.exception.DAOExceptionRemoli;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CityDAO
{
    public List<City> listCities() throws DAOExceptionRemoli {

        final List<City> informazioni = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{ CALL RouteX_Update.getAllCity() }");
             ResultSet rs = cs.executeQuery()) {

            // Se la stored procedure NON restituisce righe:  errore grave
            if (!rs.isBeforeFirst()) {  // controlla se ci sono righe
                throw new DAOExceptionRemoli(
                        "Il database non ha restituito nessuna città. "
                                + "Possibile errore nella stored procedure o nel caricamento dati."
                );
            }

            while (rs.next()) {
                String nome = rs.getString("nome_citta");
                double costo = rs.getDouble("prezzo_ticket");
                long numero = rs.getLong("numero_stazioni");

                informazioni.add(new City(nome, costo, numero));
            }

        } catch (SQLException e) {
            throw new DAOExceptionRemoli(
                    "Errore nella comunicazione con il database: " + e.getMessage()
            );
        }
        return informazioni;
    }
}
