package Controller.Applicativo;

import Model.DAO.CityDAO;
import Model.Domain.City;
import Exception.DAOException;
import java.sql.SQLException;
import java.util.List;

public class CityController {

    public List<City> getAllCities() throws DAOException, SQLException {
        CityDAO dao = new CityDAO();
        return dao.ListCities();
    }
}
