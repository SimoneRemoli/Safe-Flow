package Controller.Applicativo;

import Bean.CityBean;
import Bean.PrezzoTotaleBean;
import Model.DAO.CityDAO;
import Model.Domain.City;
import Exception.DAOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CityController {

    //List<City> cities;
    public List<CityBean> getAllCities() throws DAOException, SQLException {
        CityDAO dao = new CityDAO();
        List<City> cities =  dao.ListCities();

        List<CityBean> cityBeans = new ArrayList<>();
        for (City c : cities) {
            cityBeans.add(new CityBean(c));
        }
        return cityBeans;
    }

    public PrezzoTotaleBean ottieni_prezzo_totale(String city, String quantity) throws DAOException {
        int quantita = Integer.parseInt(quantity);
        PrezzoTotaleBean price = null;
        CityDAO dao = new CityDAO();
        List<City> cities =  dao.ListCities();
        for (City a : cities)
        {
            if(a.getName().equals(city))
            {
                price = new PrezzoTotaleBean(calculate(a.getCosto_biglietto(), quantita));
            }
        }
        return price;

    }
    private double calculate(double costo, int qty)
    {
        return qty*costo;
    }
}
