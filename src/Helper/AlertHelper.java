package Helper;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class AlertHelper {

    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        // Utilisation des Notifications pour afficher des messages simples
        if (alertType == Alert.AlertType.INFORMATION) {
            Notifications.create()
                    .title(title)
                    .text(message)
                    .hideAfter(Duration.seconds(10))
                    .showInformation();
        } else if (alertType == Alert.AlertType.ERROR) {
            Notifications.create()
                    .title(title)
                    .text(message)
                    .hideAfter(Duration.seconds(10))
                    .showError();
        } else {
            // Si vous souhaitez gérer d'autres types d'alertes, comme WARNING ou CONFIRMATION
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null); // Vous pouvez ajouter un en-tête si nécessaire
            alert.setContentText(message);

            // Affiche l'alerte modale
            alert.showAndWait();
        }
    }
}
