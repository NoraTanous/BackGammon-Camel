package Control;

import Model.Dice;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LevelController {
    @FXML
    private Button startGameButton; // Linked to the FXML button
    @FXML
    private Button mediumButton;

    private CommandController commandController;
    public static String level;

    @FXML
    private void handleEasy(ActionEvent event) {
        System.out.println("Easy level selected!");
        level = "Easy";
        startGame("Easy");
    }

    @FXML
    private void handleMedium(ActionEvent event) {
        System.out.println("Medium level selected!");
        level = "Medium";
        startGame("Medium");
    }

    @FXML
    private void handleHard(ActionEvent event) {
        System.out.println("Hard level selected!");
        level = "Hard";
        startGame("Hard");
    }

    private void startGame(String level) {
        System.out.println("START button clicked! Level: " + level);

        if (startGameButton == null) {
            System.out.println("Error: startGameButton is null. Check fx:id in the FXML file.");
            return;
        }

        Stage stage = (Stage) startGameButton.getScene().getWindow();
       
        Dice.setLevel(level);
        // Initialize MatchController with the appropriate level
        MatchController matchController = new MatchController(stage,level);
        matchController.setLevel(level); // Pass the level to MatchController

        commandController = initializeCommandController(stage, matchController);
        System.out.println("Configuring " + level + " game...");
        setupGame(stage, matchController);
    }

    private CommandController initializeCommandController(Stage stage, MatchController matchController) {
        return new CommandController(
            stage,
            matchController,
            matchController.game,
            matchController.gameplay,
            matchController.infoPnl,
            matchController.cmdPnl,
            matchController.bottomPlayer,
            matchController.topPlayer,
            matchController.musicPlayer
        );
    }

    private void setupGame(Stage stage, MatchController matchController) {
        commandController.runStartCommand();

        Scene gameScene = new Scene(matchController);
        stage.setScene(gameScene);
        stage.setTitle("Backgammon - Game Screen");
        stage.show();
    }
}
