package Control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ExplainController {
    @FXML
    private TextArea explanationTextArea;

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        // Set the explanation text
        explanationTextArea.setText(getExplanationText());

        // Add back button functionality
        backButton.setOnAction(event -> closeWindow());
    }

    private String getExplanationText() {
        return """
               How to Play Backgammon:

               1. Game Overview:
                  - Backgammon is a two-player strategy game involving a board, checkers, and dice.
                  - The goal is to move all your checkers off the board before your opponent does.

               2. Components:
                  - Game Board: 24 triangular points divided into four quadrants.
                  - Checkers: Each player has 15 checkers of a unique color.
                  - Dice: Includes regular dice, question dice, and enhanced dice.

               3. Game Flow:
                  - Players roll dice to determine their moves.
                  - Land on Question Stations to answer questions.
                  - Move all your checkers to your home base and off the board to win.

               4.Difficulty Levels:
                  - Easy: Regular dice only.
                  - Medium: Includes Question Dice.
                  - Hard: Includes Enhanced Dice with backward movement.

               5. Winning:
                  - The first player to remove all their checkers wins.
                  - The game records winners and history for future reference.
               """;
    }

    private void closeWindow() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        setCustomIcon(stage);
        stage.close();
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
