/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Interface;


import Helper.AlertHelper;
import Helper.StageHelper;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class DashboardController implements Initializable {
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    
    @FXML
    public void onDisconnectBtnClick(ActionEvent event) {
        try {
            // Ferme tous les stages ouverts
            for (Stage stage : StageHelper.getStages()) {
                stage.close();
            }

            // Charge et affiche la fenêtre de connexion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/Login.fxml"));
            Scene loginScene = new Scene(loader.load());

            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.setScene(loginScene);
            loginStage.centerOnScreen();
            loginStage.show();

        } catch (IOException e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, null, "Erreur", "Impossible de charger l'écran de connexion.");
            e.printStackTrace();
        }
    }
    
    @FXML
    public void onStudentBtnClick(ActionEvent event) {
        try {
            // Charge et affiche la fenêtre de connexion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/Student.fxml"));
            Scene studentScene = new Scene(loader.load());

            Stage studentStage = new Stage();
            studentStage.setTitle("GestApp - Etudiants");
            studentStage.setScene(studentScene);
            studentStage.centerOnScreen();
            studentStage.show();
        } catch (IOException e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, null, "Erreur", "Impossible de charger cet écran.");
            e.printStackTrace();
        }
    }
    
    @FXML
    public void onDepartementBtnClick(ActionEvent event) {
        try {
            // Charge et affiche la fenêtre de connexion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/Departement.fxml"));
            Scene studentScene = new Scene(loader.load());

            Stage studentStage = new Stage();
            studentStage.setTitle("GestApp - Etudiants");
            studentStage.setScene(studentScene);
            studentStage.centerOnScreen();
            studentStage.show();
        } catch (IOException e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, null, "Erreur", "Impossible de charger cet écran.");
            e.printStackTrace();
        }
    }

}
