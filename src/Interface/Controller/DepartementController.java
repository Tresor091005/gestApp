package Interface.Controller;

import Entities.Departement;
import Helper.AlertHelper;
import Service.DepartementService;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class DepartementController implements Initializable {

    @FXML
    private TableView<Departement> liste;

    @FXML
    private TableColumn<Departement, String> nomColumn;

    @FXML
    private TextField nomField;
    
    private Integer selectedDepartementId;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
       
        loadDepartements();
        configureRowSelection();
    }

    private void loadDepartements() {
        // Obtenez tous les départements à partir du service
        DepartementService departementService = new DepartementService();
        List<Departement> departements = departementService.getAllDepartements();
        departementService.closeEntityManager();
        
        ObservableList<Departement> departementList = liste.getItems();
        departementList.clear();
        departementList.addAll(departements);
    }
    
    private void configureRowSelection() {
        // Ajouter un écouteur à la sélection de la TableView
        liste.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Caster la ligne sélectionnée en Departement
                Departement selectedDepartement = (Departement) newValue;
                nomField.setText(selectedDepartement.getNom());
                // Charger les données dans les champs
                selectedDepartementId = selectedDepartement.getId();
            }
        });
    }

    @FXML
    private void onAddButtonClick(ActionEvent event) {
        Window window = nomField.getScene().getWindow();
        try {
            // Get input values
            String nom = nomField.getText();
            
            // Validate required fields
            if (nom.isEmpty()) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "Veuillez remplir le champ nom");
                return;
            }
            
            Departement departement = new Departement();
            departement.setNom(nom);

            // Proceed to add the departement
            DepartementService departementService = new DepartementService();
            boolean success = departementService.addDepartement(departement);
            departementService.closeEntityManager();

            if (success) {
                AlertHelper.showAlert(Alert.AlertType.INFORMATION, window,
                        "Succès", "Département ajouté avec succès.");
                clearFields();
            } else {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "Echec de l'ajout du département.");
            }
        } catch (Exception e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "Erreur lors de l'ajout du département: " + e.getMessage());
        }
        
        selectedDepartementId = null;
        loadDepartements();
    }

    @FXML
    private void onDeleteButtonClick(ActionEvent event) {
        Window window = liste.getScene().getWindow();

        if (selectedDepartementId == null) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                    "Erreur", "Veuillez sélectionner un département dans le tableau à supprimer.");
            return;
        }

        DepartementService departementService = new DepartementService();
        Departement selectedDepartement = departementService.getDepartementById(selectedDepartementId);

        if (selectedDepartement == null) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                    "Erreur", "Impossible de récupérer les informations du département sélectionné.");
            departementService.closeEntityManager();
            return;
        }

        // Afficher une boîte de dialogue de confirmation
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation de suppression");
        confirmationDialog.setHeaderText("Voulez-vous vraiment supprimer ce département ?");
        confirmationDialog.setContentText("Nom : " + selectedDepartement.getNom());
        Optional<ButtonType> result = confirmationDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = departementService.deleteDepartementById(selectedDepartementId);
            departementService.closeEntityManager();

            if (success) {
                AlertHelper.showAlert(Alert.AlertType.INFORMATION, window,
                        "Succès", "Département supprimé avec succès.");
                clearFields();
            } else {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "Echec de la suppression du département.");
            }

            selectedDepartementId = null;
            loadDepartements();
        }
    }

    @FXML
    private void onUpdateButtonClick(ActionEvent event) {
        Window window = nomField.getScene().getWindow();
        try {
            if (selectedDepartementId == null) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "Veuillez sélectionner un département dans le tableau à mettre à jour.");
                return;
            }
            
            String nom = nomField.getText();

            if (nom.isEmpty()) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "Veuillez remplir le champ nom.");
                return;
            }

            Departement departement = new Departement();
            departement.setId(selectedDepartementId);
            departement.setNom(nom);
            
            DepartementService departementService = new DepartementService();
            boolean success = departementService.updateDepartement(departement);
            departementService.closeEntityManager();

            if (success) {
                AlertHelper.showAlert(Alert.AlertType.INFORMATION, window,
                        "Succès", "Département mis à jour avec succès.");
                clearFields();
            } else {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "Echec de la mise à jour du département.");
            }
        } catch (Exception e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                    "Erreur", "Erreur lors de la mise à jour du département: " + e.getMessage());
        }

        selectedDepartementId = null;
        loadDepartements();
    }

    private void clearFields() {
        nomField.setText("");
    }   
}
