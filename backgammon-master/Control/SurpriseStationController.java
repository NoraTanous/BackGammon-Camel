package Control;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SurpriseStationController {
    private Stage surpriseStage;
    private GameplayController gameplayController;

    @FXML
    private Text surpriseMessage; // Matches fx:id="surpriseMessage" in FXML

    public void setSurpriseData(Stage stage, GameplayController gameplayController) {
        this.surpriseStage = stage;
        this.gameplayController = gameplayController;

        if (surpriseMessage != null) {
            surpriseMessage.setText("You won another round! Enjoy your extra turn.");
        } else {
            System.err.println("Error: surpriseMessage is null. Check FXML linkage.");
        }
    }

    public void closeSurpriseStation() {
        gameplayController.grantExtraTurn();
        surpriseStage.close();
    }
}
