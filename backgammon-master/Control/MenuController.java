package Control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    private CommandController commandController;

    @FXML
    private void initialize() {
        // START button functionality
      ////  startGameButton.setOnAction(event -> startGame());
        startGameButton.setOnMouseClicked(event -> openWindow("/View/Levels.fxml", "Levels"));


        // Set up event handlers for QA, History, and Explain
        QA.setOnMouseClicked(event -> openWindow("/View/QAView.fxml", "Question & Answer"));
        History.setOnMouseClicked(event -> openWindow("/View/HistoryView.fxml", "History"));
        Explain.setOnMouseClicked(event -> openWindow("/View/ExplainView.fxml", "Explanation"));
    }


 

    private void openWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
