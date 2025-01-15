package Control;

import Model.GameHistory;
import javafx.collections.FXCollections;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

    public void initialize() {
        try {
            // Load history from JSON file
            List<GameHistory> historyData = GameHistoryJsonUtil.loadHistory();
            System.out.println("Loaded history: " + historyData);

            // Ensure the table columns are properly mapped
            playerOneColumn.setCellValueFactory(new PropertyValueFactory<>("playerOne"));
            playerTwoColumn.setCellValueFactory(new PropertyValueFactory<>("playerTwo"));
            scorePlayerOneColumn.setCellValueFactory(new PropertyValueFactory<>("scorePlayerOne"));
            scorePlayerTwoColumn.setCellValueFactory(new PropertyValueFactory<>("scorePlayerTwo"));
            winnerColumn.setCellValueFactory(new PropertyValueFactory<>("winner"));
            dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

            // Populate the table with data
            gameHistoryTable.setItems(FXCollections.observableArrayList(historyData));
            System.out.println("TableView populated.");

        } catch (Exception e) {
            e.printStackTrace(); // Debugging: Check for any exceptions during initialization
        }

        // Back button functionality
        backButton.setOnAction(event -> closeWindow());
    }


    private void closeWindow() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}

