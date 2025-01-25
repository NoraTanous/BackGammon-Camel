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
        // Create the base explanation and decorate it
        Explanation explanation = new BasicExplanation();
        explanation = new DifficultyDecorator(explanation);
        explanation = new StrategyTipsDecorator(explanation);

        // Set the explanation text
        explanationTextArea.setText(explanation.getExplanationText());

        // Add back button functionality
        backButton.setOnAction(event -> closeWindow());
    }

    private void closeWindow() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        setCustomIcon(stage);
        stage.close();
    }

    private void setCustomIcon(Stage stage) {
        try {
            Image icon = new Image(getClass().getResourceAsStream("/img/backgammon.png"));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("Error loading custom icon: " + e.getMessage());
        }
    }
}