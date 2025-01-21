package Control;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameHistoryControllerFactory {

    public static GameHistoryController createGameHistoryController(Stage stage) {
        try {
            // Load the FXML file and create the controller
            FXMLLoader loader = new FXMLLoader(GameHistoryControllerFactory.class.getResource("/View/GameHistory.fxml"));
            Parent root = loader.load();

            // Get the controller
            GameHistoryController controller = loader.getController();

            // Set up the stage with the loaded scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Game History");
            setCustomIcon(stage);
            stage.show();

            return controller;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create GameHistoryController: " + e.getMessage());
        }
    }

    private static void setCustomIcon(Stage stage) {
        try {
            // Load the custom icon for the stage
            javafx.scene.image.Image icon = new javafx.scene.image.Image(
                GameHistoryControllerFactory.class.getResourceAsStream("/img/backgammon.png")
            );
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("Error loading custom icon: " + e.getMessage());
        }
    }
}
