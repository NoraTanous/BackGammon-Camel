package Control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SurpriseStationController {
    private Stage surpriseStage;
    private GameplayController gameplayController;
    private boolean surpriseHandled = false; // Tracks whether the surprise was handled

    @FXML
    private Text surpriseMessage; // Matches fx:id="surpriseMessage" in FXML
    @FXML
    private Button okButton;

    /**
     * Sets up the Surprise Station data for the popup.
     *
     * @param stage             The stage for the Surprise Station popup
     * @param gameplayController The gameplay controller to handle extra turn logic
     */
    public void setSurpriseData(Stage stage, GameplayController gameplayController) {
        this.surpriseStage = stage;
        this.gameplayController = gameplayController;

        if (surpriseMessage != null) {
            surpriseMessage.setText("You won another round! Enjoy your extra turn.");
        } else {
            System.err.println("Error: surpriseMessage is null. Check FXML linkage.");
        }
        setCustomIcon(stage);
    }

    /**
     * Closes the Surprise Station popup and grants an extra turn.
     */
    public void closeSurpriseStation() {
    	surpriseHandled = true;
        gameplayController.grantExtraTurn(); // Notify gameplay controller about the extra turn
        surpriseStage.close();
    }

    /**
     * Checks whether this is a Surprise Station.
     *
     * @return true if it is a Surprise Station, false otherwise
     */
    public boolean isSurpriseHandled() {
        return surpriseHandled;
    }

    /**
     * Sets a custom icon for the Surprise Station popup.
     *
     * @param stage The stage to set the custom icon for
     */
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
