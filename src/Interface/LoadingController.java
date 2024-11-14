package Interface;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoadingController implements Initializable {

    @FXML
    private ProgressBar progressBar;

    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startProgressAnimation();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void startProgressAnimation() {
        Timeline timeline = new Timeline();

        // Augmente la progression de manière fluide sur 1.5 secondes
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(15), event -> {
            double progress = progressBar.getProgress();
            progress += 0.01;  // Augmentation progressive
            progressBar.setProgress(Math.min(progress, 1.0));
        }));

        // Répète 100 fois pour une animation de 1.5 secondes
        timeline.setCycleCount(100);

        // Charge login.fxml une fois l'animation terminée
        timeline.setOnFinished(event -> loadLoginScene());

        timeline.play();
    }

    private void loadLoginScene() {
        try {
//            // Ferme la fenêtre actuelle (loading)
//            stage.close();
//
//            // Charge le fichier FXML de login
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/Login.fxml"));
//            Scene loginScene = new Scene(loader.load());
//
//            // Ouvre la nouvelle scène (login.fxml)
//            Stage loginStage = new Stage();
//            loginStage.setScene(loginScene);
//            loginStage.show();


            // Charge le fichier FXML de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/Login.fxml"));
            Scene loginScene = new Scene(loader.load());
            
            stage.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
