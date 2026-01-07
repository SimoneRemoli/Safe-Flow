package it.web.routex.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneNavigator {

    private static Stage stage;

    public static void setStage(Stage s) {
        stage = s;
    }

    public static void switchTo(String fxml) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(SceneNavigator.class.getResource(fxml));
            stage.setScene(new Scene(loader.load(), 1200, 800));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
