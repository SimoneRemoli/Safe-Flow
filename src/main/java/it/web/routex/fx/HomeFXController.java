package it.web.routex.fx;

import javafx.fxml.FXML;
@SuppressWarnings("java:S106")
public class HomeFXController {

    @FXML
    public void onRegister() {
        // TODO: carica register.fxml
        System.out.println("Register button clicked");
    }

    @FXML
    public void onLogin() {
        // TODO: carica login.fxml
    }

    @FXML
    public void onStartExploring() {
        SceneNavigator.switchTo("search.fxml");
    }
}
