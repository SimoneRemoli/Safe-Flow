package it.web.routex.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RouteXFXApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(RouteXFXApp.class.getResource("home.fxml"));

        Scene scene = new Scene(loader.load(), 1200, 800);

        stage.setTitle("RouteX – Metro Finder");
        stage.setScene(scene);
        stage.show();
        SceneNavigator.setStage(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
