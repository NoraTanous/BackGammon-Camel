package game;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class QuestionPromptHandler {

    private final QuestionManager questionManager;
    private static final int TIME_LIMIT_SECONDS = 10; // Time limit in seconds

    // Constructor to initialize the QuestionManager
    public QuestionPromptHandler(QuestionManager questionManager) {
        this.questionManager = questionManager;
    }

    // Method to show a question prompt with a timer
    public void showQuestionPrompt() {
        // Fetch a random question
        Map<String, Object> questionData = questionManager.getRandomQuestion();
        if (questionData == null || !questionData.containsKey("question") || 
            !questionData.containsKey("answers") || !questionData.containsKey("correct_ans")) {
            displayResult("Error", "Invalid question data. Please try again.");
            return;
        }

        String question = (String) questionData.get("question");

        // Safely extract answers list
        Object answersObject = questionData.get("answers");
        List<String> answers;
        if (answersObject instanceof List) {
            List<?> rawList = (List<?>) answersObject;
            answers = rawList.stream()
                .filter(item -> item instanceof String)
                .map(item -> (String) item)
                .collect(Collectors.toList());
        } else {
            displayResult("Error", "Invalid answers data. Please try again.");
            return;
        }

        String correctAnswerIndex = (String) questionData.get("correct_ans");
        int correctAnswerInt = Integer.parseInt(correctAnswerIndex) - 1; // Convert to 0-based index

        if (correctAnswerInt < 0 || correctAnswerInt >= answers.size()) {
            displayResult("Error", "Invalid correct answer index. Please try again.");
            return;
        }

        String correctAnswer = answers.get(correctAnswerInt);

        // Create a new Stage (modal)
        Stage questionStage = new Stage();
        questionStage.initModality(Modality.APPLICATION_MODAL);
        questionStage.setTitle("Question Station");

        // Create a Label for the question and timer
        Label questionLabel = new Label(question);
        Label timerLabel = new Label("Time left: " + TIME_LIMIT_SECONDS + " seconds");

        // VBox to hold the answer buttons
        VBox answerBox = new VBox(10);
        answerBox.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-spacing: 15;");
        answerBox.getChildren().addAll(timerLabel, questionLabel);

        // Add buttons for each answer
        for (String answer : answers) {
            Button answerButton = new Button(answer);
            answerButton.setPrefWidth(200);
            answerButton.setOnAction(event -> {
                if (answer.equals(correctAnswer)) {
                    displayResult("Correct!", "You answered correctly!");
                } else {
                    displayResult("Wrong!", "The correct answer was: " + correctAnswer);
                }
                questionStage.close(); // Close the question dialog
            });
            answerBox.getChildren().add(answerButton);
        }

        // Set the Scene and show the Stage
        Scene scene = new Scene(answerBox, 400, 300);
        questionStage.setScene(scene);

        // Timer logic
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            private int timeRemaining = TIME_LIMIT_SECONDS;

            @Override
            public void run() {
                Platform.runLater(() -> {
                    timeRemaining--;
                    if (timeRemaining > 0) {
                        timerLabel.setText("Time left: " + timeRemaining + " seconds");
                    } else {
                        // Time's up: Close the question stage and show an alert
                        timer.cancel();
                        questionStage.close();
                        displayResult("Time's Up!", "You ran out of time to answer.");
                    }
                });
            }
        };

        timer.scheduleAtFixedRate(task, 1000, 1000); // Schedule timer to run every second
        questionStage.setOnCloseRequest(event -> timer.cancel()); // Stop timer if the stage is closed

        questionStage.showAndWait();
    }

    // Helper method to display the result
    private void displayResult(String title, String message) {
        Alert resultAlert = new Alert(Alert.AlertType.INFORMATION);
        resultAlert.setTitle(title);
        resultAlert.setHeaderText(null);
        resultAlert.setContentText(message);
        resultAlert.showAndWait();
    }
}
