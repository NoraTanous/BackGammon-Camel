package Control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    private Button startGameButton;

    @FXML
    private ImageView QA;

    @FXML
    private ImageView History;

    @FXML
    private ImageView Explain;

    @FXML
    private void initialize() {
    	
        // Start Game Button functionality
        startGameButton.setOnMouseClicked(event -> openWindow("/View/Levels.fxml", "Levels"));

        // QA Button functionality
        QA.setOnMouseClicked(event -> {
            if (verifyAdminCredentials()) {
                openWindow("/View/QAView.fxml", "Question & Answer");
            } else {
                showAlert("Access Denied", "You do not have permission to access this page.");
            }
        });

        // History and Explain Button functionality
        History.setOnMouseClicked(event -> openWindow("/View/HistoryView.fxml", "History"));
        Explain.setOnMouseClicked(event -> openWindow("/View/ExplainView.fxml", "Explanation"));
    }

    private boolean verifyAdminCredentials() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AdminLogin.fxml"));
            Parent root = loader.load();

            AdminLoginController controller = loader.getController();
            System.out.println("Loaded AdminLoginController.");

            Stage loginStage = new Stage();
            loginStage.setTitle("Admin Login");
            loginStage.setScene(new Scene(root));
            loginStage.showAndWait(); // Wait for admin login window to close

            boolean authorized = controller.isAuthorized();
            System.out.println("Admin authorization result: " + authorized);
            return authorized;
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load Admin Login: " + e.getMessage());
            return false;
        }
    }

    private void openWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            if ("/View/QAView.fxml".equals(fxmlPath)) {
                QAController controller = loader.getController();
                controller.setAdminAuthorization(true); // Pass authorization to QAController
            }

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
         // Set custom icon
            setCustomIcon(stage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load " + title + ": " + e.getMessage());
        }
    }
    
    private void setCustomIcon(Stage stage) {
        try {
            // Load the custom icon
            Image icon = new Image(getClass().getResourceAsStream("/img/backgammon.png")); // Replace with your actual icon path
            stage.getIcons().add(icon); // Add the icon to the stage
        } catch (Exception e) {
            System.err.println("Error loading custom icon: " + e.getMessage());
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
