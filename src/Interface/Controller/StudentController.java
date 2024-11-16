package Interface.Controller;

import Entities.Student;
import Helper.AlertHelper;
import Service.StudentService;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class StudentController implements Initializable {

    @FXML
    private TableColumn<Student, String> ageColumn;

    @FXML
    private DatePicker ageField;

    @FXML
    private TableColumn<Student, String> emailColumn;

    @FXML
    private TextField emailField;

    @FXML
    private TableView<Student> listeEtudiants;

    @FXML
    private TableColumn<Student, Integer> matriculeColumn;

    @FXML
    private TextField matriculeField;

    @FXML
    private TableColumn<Student, String> nomColumn;

    @FXML
    private TextField nomField;

    @FXML
    private TableColumn<Student, String> prenomColumn;

    @FXML
    private TextField prenomField;

    @FXML
    private TableColumn<Student, Integer> telephoneColumn;

    @FXML
    private TextField telephoneField;
    
    private Integer selectedStudentId;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialiser les colonnes de la TableView
        nomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        prenomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrenom()));
        matriculeColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getMatricule()).asObject());
        telephoneColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTelephone()).asObject());
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        ageColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getAge())));

        // Charger les étudiants dans la TableView
        loadStudents();
        configureRowSelection();
    }

    private void loadStudents() {
        // Obtenez tous les étudiants à partir du service
        StudentService studentService = new StudentService();
        List<Student> students = studentService.getAllStudents();
        studentService.closeEntityManager();
        
        ObservableList<Student> studentList = listeEtudiants.getItems();
        studentList.clear();
        studentList.addAll(students);
    }
    
    private void configureRowSelection() {
        // Ajouter un écouteur à la sélection de la TableView
        listeEtudiants.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Caster la ligne sélectionnée en Student
                Student selectedStudent = (Student) newValue;

                // Charger les données dans les champs
                selectedStudentId = selectedStudent.getId();
                matriculeField.setText(String.valueOf(selectedStudent.getMatricule()));
                nomField.setText(selectedStudent.getNom());
                prenomField.setText(selectedStudent.getPrenom());
                
                ageField.setValue(selectedStudent.getDateNaissance().toInstant()
                                    .atZone(java.time.ZoneId.systemDefault())
                                    .toLocalDate());
                emailField.setText(selectedStudent.getEmail());
                telephoneField.setText(String.valueOf(selectedStudent.getTelephone()));
            }
        });
    }

    @FXML
    private void onAddButtonClick(ActionEvent event) {
        Window window = nomField.getScene().getWindow();
        try {
            // Get input values
            int matricule = Integer.parseInt(matriculeField.getText());
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            
            // Validate required fields
            if (nom.isEmpty() || prenom.isEmpty() || matriculeField.getText().isEmpty()) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "Veuillez remplir les champs nom, prénom et matricule");
                return;
            }

            // Get the selected date and check if it's not in the future
            LocalDate dateNaissance = ageField.getValue();
            if (dateNaissance == null || dateNaissance.isAfter(LocalDate.now())) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "La date de naissance ne peut pas être supérieure à aujourd'hui.");
                return;
            }

            String email = emailField.getText();
            int telephone = Integer.parseInt(telephoneField.getText());
            if (!telephoneField.getText().matches("\\d{8}")) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "Le numéro de téléphone doit comporter 8 chiffres.");
                return;
            }
            
            Student student = new Student();
            student.setMatricule(matricule);
            student.setNom(nom);
            student.setPrenom(prenom);
            student.setDateNaissance(java.util.Date.from(dateNaissance.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            student.setTelephone(telephone);
            student.setEmail(email);

            // Proceed to add the student
            StudentService studentService = new StudentService();
            boolean success = studentService.addStudent(student);
            studentService.closeEntityManager();

            if (success) {
                AlertHelper.showAlert(Alert.AlertType.INFORMATION, window,
                        "Succès", "Étudiant ajouté avec succès.");
                clearFields();
            } else {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "Echec de l'ajout de l'étudiant. \n- Vérifiez le matricule.");
            }
        } catch (NumberFormatException e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "Le matricule, l'âge et le numéro de téléphone doivent être valides.");
        } catch (Exception e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "Erreur lors de l'ajout de l'étudiant: " + e.getMessage());
        }
        
        selectedStudentId = null;
        loadStudents();
    }

    @FXML
    private void onDeleteButtonClick(ActionEvent event) {
        Window window = listeEtudiants.getScene().getWindow();

        if (selectedStudentId == null) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                    "Erreur", "Veuillez sélectionner un étudiant dans le tableau à supprimer.");
            return;
        }

        StudentService studentService = new StudentService();
        Student selectedStudent = studentService.getStudentById(selectedStudentId);

        if (selectedStudent == null) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                    "Erreur", "Impossible de récupérer les informations de l'étudiant sélectionné.");
            studentService.closeEntityManager();
            return;
        }

        // Afficher une boîte de dialogue de confirmation
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation de suppression");
        confirmationDialog.setHeaderText("Voulez-vous vraiment supprimer cet étudiant ?");
        confirmationDialog.setContentText("Nom : " + selectedStudent.getNom() + 
                                           "\nPrénom : " + selectedStudent.getPrenom());
        Optional<ButtonType> result = confirmationDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = studentService.deleteStudentById(selectedStudentId);
            studentService.closeEntityManager();

            if (success) {
                AlertHelper.showAlert(Alert.AlertType.INFORMATION, window,
                        "Succès", "Étudiant supprimé avec succès.");
                clearFields();
            } else {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "Echec de la suppression de l'étudiant.");
            }

            selectedStudentId = null;
            loadStudents();
        }
    }

    @FXML
    private void onUpdateButtonClick(ActionEvent event) {
        Window window = nomField.getScene().getWindow();
        try {
            if (selectedStudentId == null) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "Veuillez sélectionner un étudiant dans le tableau à mettre à jour.");
                return;
            }

            int matricule = Integer.parseInt(matriculeField.getText());
            String nom = nomField.getText();
            String prenom = prenomField.getText();

            if (nom.isEmpty() || prenom.isEmpty() || matriculeField.getText().isEmpty()) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "Veuillez remplir les champs nom, prénom et matricule.");
                return;
            }

            LocalDate dateNaissance = ageField.getValue();
            if (dateNaissance == null || dateNaissance.isAfter(LocalDate.now())) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "La date de naissance ne peut pas être supérieure à aujourd'hui.");
                return;
            }

            String email = emailField.getText();
            int telephone = Integer.parseInt(telephoneField.getText());
            if (!telephoneField.getText().matches("\\d{8}")) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "Le numéro de téléphone doit comporter 8 chiffres.");
                return;
            }

            Student student = new Student();
            student.setId(selectedStudentId);
            student.setMatricule(matricule);
            student.setNom(nom);
            student.setPrenom(prenom);
            student.setDateNaissance(java.util.Date.from(dateNaissance.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            student.setTelephone(telephone);
            student.setEmail(email);

            StudentService studentService = new StudentService();
            boolean success = studentService.updateStudent(student);
            studentService.closeEntityManager();

            if (success) {
                AlertHelper.showAlert(Alert.AlertType.INFORMATION, window,
                        "Succès", "Étudiant mis à jour avec succès.");
                clearFields();
            } else {
                AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                        "Erreur", "Echec de la mise à jour de l'étudiant.");
            }
        } catch (NumberFormatException e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                    "Erreur", "Le matricule, l'âge et le numéro de téléphone doivent être valides.");
        } catch (Exception e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, window,
                    "Erreur", "Erreur lors de la mise à jour de l'étudiant: " + e.getMessage());
        }

        selectedStudentId = null;
        loadStudents();
    }

    private void clearFields() {
        nomField.setText("");
        prenomField.setText("");
        ageField.setValue(null);
        telephoneField.setText("");
        matriculeField.setText("");
        emailField.setText("");
    }
    
}
