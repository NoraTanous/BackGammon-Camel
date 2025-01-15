package Control;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        stage.setTitle("Backgammon - Main Menu");
        stage.show();
    }
}
