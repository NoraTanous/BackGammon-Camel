package Control;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * This class runs the entire application.
 * 
 * @teamname TeaCup
 * @author Bryan Sng
 * @author @LxEmily
 * @author Braddy Yeoh
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args); // Calls the start method.
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Load the Menu.fxml file
    	
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Menu.fxml"));
        Parent root = loader.load();
        // Set up the scene with the loaded Menu
        Scene scene = new Scene(root);
        stage.setScene(scene);
     // Set custom icon
        setCustomIcon(stage);
        stage.setTitle("Backgammon - Main Menu");
        stage.show();
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
}
