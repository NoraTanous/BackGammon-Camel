package Control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AdminLoginController {

    @FXML
    private TextField adminNameField;

    @FXML
    private PasswordField adminCodeField;

    @FXML
    private Button backButton;

    private boolean isAuthorized = false;

    @FXML
    private void handleLogin() {
        // Get admin credentials from input fields
        String adminName = adminNameField.getText().trim();
        String adminCode = adminCodeField.getText().trim();

        // Check credentials
        if ("Camel".equalsIgnoreCase(adminName) && "20242025".equals(adminCode)) {
            isAuthorized = true;

            // Show success alert
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Access Granted");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Welcome, Admin!");
            successAlert.showAndWait();

            // Close the login window
            Stage stage = (Stage) adminNameField.getScene().getWindow();
         // Set custom icon for the menu stage
            setCustomIcon(stage);
            stage.close();
        } else {
            // Show error alert
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Access Denied");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Invalid admin name or code.");
            errorAlert.showAndWait();
        }
    }

    @FXML
    private void handleBackToMenu() {
        try {
            // Load the Menu.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Menu.fxml"));
            Parent root = loader.load();

            // Create a new stage for the menu window
            Stage menuStage = new Stage();
            menuStage.setTitle("Menu");
            menuStage.setScene(new Scene(root));
            menuStage.show();
         // Set custom icon for the menu stage
            setCustomIcon(menuStage);
            // Close the admin login window
            Stage currentStage = (Stage) backButton.getScene().getWindow();
         // Set custom icon for the menu stage
            setCustomIcon(currentStage);
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Unable to Load Menu");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
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

    // Getter for isAuthorized
    public boolean isAuthorized() {
        return isAuthorized;
    }
}
