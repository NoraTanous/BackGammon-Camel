package Control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SurpriseStationController {
    private Stage surpriseStage;
    private GameplayController gameplayController;

    @FXML
    private Text surpriseMessage; // Matches fx:id="surpriseMessage" in FXML
    @FXML
    private Button okButton;


    public void setSurpriseData(Stage stage, GameplayController gameplayController) {
        this.surpriseStage = stage;
        this.gameplayController = gameplayController;

        if (surpriseMessage != null) {
            surpriseMessage.setText("You won another round! Enjoy your extra turn.");
        } else {
            System.err.println("Error: surpriseMessage is null. Check FXML linkage.");
        }
        setCustomIcon(stage); // Add this line
    }

    public void closeSurpriseStation() {
        gameplayController.grantExtraTurn(); // Notify gameplay controller about the extra turn
        surpriseStage.close();
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
