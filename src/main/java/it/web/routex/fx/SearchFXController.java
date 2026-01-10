package it.web.routex.fx;

import it.web.routex.bean.CityBean;
import it.web.routex.bean.InformazioniPercorsoBean;
import it.web.routex.controller.applicativo.CityController;
import it.web.routex.controller.applicativo.PathController;
import it.web.routex.validator.RouteValidator;
import it.web.routex.domain.UserStatusResolver;
import it.web.routex.record.RouteRecord;
import it.web.routex.utility.singleton.Credentials;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

public class SearchFXController {

    @FXML private ComboBox<CityBean> cityBox;
    @FXML private TextField startField;
    @FXML private TextField endField;

    @FXML
    public void initialize() {
        try {
            CityController cityController = new CityController();
            List<CityBean> cities = cityController.getAllCities();
            cityBox.setItems(FXCollections.observableArrayList(cities));
        } catch (Exception e) {
            showError("Errore", e.getMessage());
        }
    }

    @FXML
    public void onContinue() {

        CityBean city = cityBox.getValue();
        String start = startField.getText();
        String end = endField.getText();

        RouteRecord route = new RouteRecord(
                city.getName(),
                start,
                end
        );

        if (!RouteValidator.isValid(route)) {
            showError("Errore dati", "Dati del percorso non validi");
            return;
        }
        Credentials cred = Credentials.getInstanceSingleton();

        String status = UserStatusResolver.resolve(cred);


        try {
            PathController pathController = new PathController();
            InformazioniPercorsoBean dto = pathController.run(start, end, city.getName());

            pathController.saveRoute(
                    cred, dto, route, status
            );

            ResultContext.setResult(dto, status, route.start(), route.end(), route.city());
            SceneNavigator.switchTo("result.fxml");

        } catch (Exception e) {
            showError("Errore generico", e.getMessage());
        }
    }

    private void showError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
