package uk.ac.mdx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static FXMLGUIController GUIController;

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLGUI.fxml"));
        Parent root = loader.load();



            // This returns the instance of the controller class
            GUIController = loader.getController();

            Font font = Font.loadFont(
                    App.class.getResource("/DaxRegular.ttf").toExternalForm(), 10
            );


            Scene scene = new Scene(root);
            stage.setScene(scene);

        stage.initStyle(StageStyle.UNIFIED);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static FXMLGUIController getController() {
        return GUIController;
    }

}