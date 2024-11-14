package launch;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import Interface.LoadingController;

import java.io.IOException;

public class Load extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Interface/Loading.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Enlève les bordures et le titre de la fenêtre
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

        // Récupère le contrôleur et lui passe le stage
        LoadingController controller = fxmlLoader.getController();
        controller.setStage(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
