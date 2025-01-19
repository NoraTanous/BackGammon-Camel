package Control;

import Model.GameHistory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.List;

public class GameHistoryController {
    @FXML
    private TableView<GameHistory> gameHistoryTable;

    @FXML
    private TableColumn<GameHistory, String> playerOneColumn;
    @FXML
    private TableColumn<GameHistory, String> playerTwoColumn;
    @FXML
    private TableColumn<GameHistory, Integer> scorePlayerOneColumn;
    @FXML
    private TableColumn<GameHistory, Integer> scorePlayerTwoColumn;
    @FXML
    private TableColumn<GameHistory, String> winnerColumn;
    @FXML
    private TableColumn<GameHistory, String> dateTimeColumn;
    @FXML
    private Button backButton;

    // The initialize method goes here
    public void initialize() {
        try {
            List<GameHistory> historyData = GameHistoryJsonUtil.loadHistory();

            // Handle null values for display
            historyData.forEach(history -> {
                if (history.getWinner() == null) {
                    history.setWinner("Incomplete Game");
                }
                if (history.getPlayerOne() == null) {
                    history.setPlayerOne("Unknown Player");
                }
                if (history.getPlayerTwo() == null) {
                    history.setPlayerTwo("Unknown Player");
                }
            });

            playerOneColumn.setCellValueFactory(new PropertyValueFactory<>("playerOne"));
            playerTwoColumn.setCellValueFactory(new PropertyValueFactory<>("playerTwo"));
            scorePlayerOneColumn.setCellValueFactory(new PropertyValueFactory<>("scorePlayerOne"));
            scorePlayerTwoColumn.setCellValueFactory(new PropertyValueFactory<>("scorePlayerTwo"));
            winnerColumn.setCellValueFactory(new PropertyValueFactory<>("winner"));
            dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

            gameHistoryTable.setItems(FXCollections.observableArrayList(historyData));
        } catch (Exception e) {
            e.printStackTrace();
        }

        backButton.setOnAction(event -> closeWindow());
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
