/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Interface.Controller;

import Helper.AlertHelper;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class LoginController implements Initializable {
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField passwordField;
    
    @FXML
    private Button loginBtn;
    
    @FXML 
    private Label passwordErr;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void onCloseBtnClick() {
        System.exit(0);
    }
    
    @FXML
    private void onSignInBtnClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/SignIn.fxml"));
            Scene newScene = new Scene(loader.load());

            Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            currentStage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onLoginClick(ActionEvent event) {
        if (!isFormValid()) {
            Window window = loginBtn.getScene().getWindow();
            loginBtn.setDisable(true);
//            showErrorDialog("Formulaire invalide");
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Erreur",
                    "Formulaire invalide.");
        } else {
            try {
                // Ferme la fenêtre actuelle (loading)
                Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                currentStage.close();

                // Charge le fichier FXML de login
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/Dashboard.fxml"));
                Scene dashboardScene = new Scene(loader.load());

                // Ouvre la nouvelle scène (login.fxml)
                Stage dashboardStage = new Stage();
                dashboardStage.setTitle("GestApp");
                dashboardStage.setScene(dashboardScene);
                dashboardStage.setFullScreen(true);
                dashboardStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }   
    }
    
    @FXML 
    private void checkEmailField() {
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        String emailInput = emailField.getText();
        if (!emailInput.matches(emailPattern)) {
            emailField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            loginBtn.setDisable(true);
        } else {
            emailField.setStyle("");
            loginBtn.setDisable(!isFormValid());
        }
    }

    
    @FXML 
    private void checkPasswordField() {
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        String passwordInput = passwordField.getText();
        if (!passwordInput.matches(passwordPattern)) {
            passwordField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            passwordErr.setText("Mot de passe faible");
            loginBtn.setDisable(true);
        } else {
            passwordField.setStyle("");
            passwordErr.setText("");
            loginBtn.setDisable(!isFormValid());
        }
    }
    
    private boolean isFormValid() {
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        String passwordPattern = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}";

        return emailField.getText().matches(emailPattern) && passwordField.getText().matches(passwordPattern);
    }
    
    private void showErrorDialog(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }
}
