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
import javafx.scene.control.Alert.AlertType;
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
public class SignInController implements Initializable {

    @FXML
    private TextField emailField;
    
    @FXML
    private TextField passwordField;
    
    @FXML
    private TextField passwordConfirmField;
    
    @FXML
    private Button signInBtn;
    
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
    private void onLoginBtnClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/Login.fxml"));
            Scene newScene = new Scene(loader.load());

            Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            currentStage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onSignInBtnClick(ActionEvent event) {
        Window window = signInBtn.getScene().getWindow();
        String passwordInput = passwordField.getText();
        String passwordConfirmInput = passwordConfirmField.getText();
        
        if (!isFormValid()) {
            signInBtn.setDisable(true);
        } else if(!passwordInput.equals(passwordConfirmInput)) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window, "Erreur",
                    "Les mots de passes entrés ne sont pas identiques.\n Il est nécéssaire de mettre un mot de passe de 8 charactères minimum\n dont 1 majuscule, 1 chiffre et 1 symbole");
            passwordField.setText("");
            passwordConfirmField.setText("");
            signInBtn.setDisable(true);
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/Login.fxml"));
                Scene newScene = new Scene(loader.load());

                Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                currentStage.setScene(newScene);
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
            signInBtn.setDisable(true);
        } else {
            emailField.setStyle("");
            signInBtn.setDisable(!isFormValid());
        }
    }

    @FXML 
    private void checkPasswordField() {
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        String passwordInput = passwordField.getText();
        if (!passwordInput.matches(passwordPattern)) {
            passwordField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            passwordErr.setText("Mot de passe faible");
            signInBtn.setDisable(true);
        } else {
            passwordField.setStyle("");
            passwordErr.setText("");
            signInBtn.setDisable(!isFormValid());
        }
    }
    
    private boolean isFormValid() {
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        String passwordPattern = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}";

        return emailField.getText().matches(emailPattern) && passwordField.getText().matches(passwordPattern);
    }
    
    private void showErrorDialog(String errorMessage) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }
}
