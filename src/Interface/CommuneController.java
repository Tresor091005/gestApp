package Interface;

import Entities.Commune;
import Entities.Departement;
import Helper.AlertHelper;
import Service.CommuneService;
import Service.DepartementService;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class CommuneController implements Initializable {

    @FXML
    private TableView<Commune> liste;

    @FXML
    private TableColumn<Commune, String> nomColumn;
    
    @FXML
    private TableColumn<Commune, String> departementColumn;

    @FXML
    private TextField nomField;
    
    @FXML
    private ComboBox<Departement> departementField;
    
    private Integer selectedCommuneId;
    
    private List<Departement> departements;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        departementColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDepartement().getNom()));

        loadDepartements();
        loadCommunes();
        configureRowSelection();
    }
    
    private void loadDepartements() {
        DepartementService departementService = new DepartementService();
        departements = departementService.getAllDepartements();
        departementService.closeEntityManager();

        ObservableList<Departement> departementOptions = FXCollections.observableArrayList(departements);
        departementField.setItems(departementOptions);
    }

    private void loadCommunes() {
        CommuneService communeService = new CommuneService();
        List<Commune> communes = communeService.getAllCommunes();
        communeService.closeEntityManager();

        ObservableList<Commune> communeList = liste.getItems();
        communeList.clear();
        communeList.addAll(communes);
    }
    
    private void configureRowSelection() {
        liste.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Commune selectedCommune = newValue;

                // Charger les données de la commune sélectionnée
                selectedCommuneId = selectedCommune.getId();
                nomField.setText(selectedCommune.getNom());

                // Sélectionner le département correspondant dans le ComboBox
                Departement departement = selectedCommune.getDepartement();
                departementField.getSelectionModel().select(departement);
            }
        });
    }

    @FXML
    private void onAddButtonClick(ActionEvent event) {
        Window window = nomField.getScene().getWindow();
        try {
            // Récupération des valeurs saisies
            String nom = nomField.getText();
            Departement departement = (Departement) departementField.getSelectionModel().getSelectedItem();

            // Validation des champs obligatoires
            if (nom.isEmpty() || departement == null) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "Veuillez remplir tous les champs.");
                return;
            }

            Commune commune = new Commune();
            commune.setNom(nom);
            commune.setDepartement(departement);

            // Ajouter la commune
            CommuneService communeService = new CommuneService();
            boolean success = communeService.addCommune(commune);
            communeService.closeEntityManager();

            if (success) {
                AlertHelper.showAlert(Alert.AlertType.INFORMATION, window,
                        "Succès", "Commune ajoutée avec succès.");
                clearFields();
            } else {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "Échec de l'ajout de la commune.");
            }
        } catch (Exception e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                    "Erreur", "Erreur lors de l'ajout de la commune : " + e.getMessage());
        }
        
        loadCommunes();
    }


    @FXML
    private void onDeleteButtonClick(ActionEvent event) {
        Window window = liste.getScene().getWindow();

        if (selectedCommuneId == null) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                    "Erreur", "Veuillez sélectionner une commune dans le tableau à supprimer.");
            return;
        }

        CommuneService communeService = new CommuneService();
        Commune selectedCommune = communeService.getCommuneById(selectedCommuneId);

        if (selectedCommune == null) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                    "Erreur", "Impossible de récupérer les informations de la commune sélectionnée.");
            communeService.closeEntityManager();
            return;
        }

        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation de suppression");
        confirmationDialog.setHeaderText("Voulez-vous vraiment supprimer cette commune ?");
        confirmationDialog.setContentText("Nom : " + selectedCommune.getNom());
        Optional<ButtonType> result = confirmationDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = communeService.deleteCommuneById(selectedCommuneId);
            communeService.closeEntityManager();

            if (success) {
                AlertHelper.showAlert(Alert.AlertType.INFORMATION, window,
                        "Succès", "Commune supprimée avec succès.");
                clearFields();
            } else {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "Échec de la suppression de la commune.");
            }

            loadCommunes();
        }
    }


    @FXML
    private void onUpdateButtonClick(ActionEvent event) {
        Window window = nomField.getScene().getWindow();
        try {
            if (selectedCommuneId == null) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "Veuillez sélectionner une commune dans le tableau à mettre à jour.");
                return;
            }

            String nom = nomField.getText();
            Departement departement = (Departement) departementField.getSelectionModel().getSelectedItem();

            if (nom.isEmpty() || departement == null) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "Veuillez remplir tous les champs.");
                return;
            }

            Commune commune = new Commune();
            commune.setId(selectedCommuneId);
            commune.setNom(nom);
            commune.setDepartement(departement);

            CommuneService communeService = new CommuneService();
            boolean success = communeService.updateCommune(commune);
            communeService.closeEntityManager();

            if (success) {
                AlertHelper.showAlert(Alert.AlertType.INFORMATION, window,
                        "Succès", "Commune mise à jour avec succès.");
                clearFields();
            } else {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "Échec de la mise à jour de la commune.");
            }
        } catch (Exception e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                    "Erreur", "Erreur lors de la mise à jour de la commune : " + e.getMessage());
        }

        loadCommunes();
    }


   private void clearFields() {
    nomField.clear();
    departementField.getSelectionModel().clearSelection();
    selectedCommuneId = null;
}
 
}
