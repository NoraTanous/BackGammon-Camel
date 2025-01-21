package Control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class MenuController {

    @FXML
    private Button startGameButton;

    @FXML
    private ImageView QA;

    @FXML
    private ImageView History;

    @FXML
    private ImageView Explain;

    @FXML
    private void initialize() {
        // Start Game Button functionality
        startGameButton.setOnMouseClicked(event -> new OpenWindowCommand("/View/Levels.fxml", "Levels").execute());

        // QA Button functionality
        QA.setOnMouseClicked(event -> {
            VerifyAdminCredentialsCommand verifyCommand = new VerifyAdminCredentialsCommand();
            verifyCommand.execute();
            if (verifyCommand.isAuthorized()) {
                new OpenWindowCommand("/View/QAView.fxml", "Question & Answer").execute();
            } else {
                new ShowAlertCommand("Access Denied", "You do not have permission to access this page.").execute();
            }
        });

        // History Button functionality
        History.setOnMouseClicked(event -> new OpenWindowCommand("/View/HistoryView.fxml", "History").execute());

        // Explain Button functionality
        Explain.setOnMouseClicked(event -> new OpenWindowCommand("/View/ExplainView.fxml", "Explanation").execute());
    }
}
