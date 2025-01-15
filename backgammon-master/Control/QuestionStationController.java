package Control;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

import Model.Question;

public class QuestionStationController {
    @FXML
    private Button option1Button;
    @FXML
    private Button option2Button;
    @FXML
    private Button option3Button;
    @FXML
    private Button option4Button;
    @FXML
    private Label questionLabel;
    @FXML
    private Label timerLabel; // Label for displaying the timer

    public static boolean isCorrectAnswerd = false;
    private String correctAnswer;
    private Stage questionStage;
    private GameplayController gameplayController;
    private Timeline timer;
    private int timeRemaining = 30; // Default time in seconds

    public void setQuestionData(Question question, Stage stage, GameplayController gameplayController) {
        this.questionStage = stage;
        this.gameplayController = gameplayController;

        // Disable the close button
        stage.setOnCloseRequest(event -> event.consume());

        // Set the question text and answers
        questionLabel.setText(question.getQuestionText());
        List<String> answers = question.getAnswers();
        correctAnswer = question.getCorrectAnswer();

        option1Button.setText(answers.get(0));
        option2Button.setText(answers.get(1));
        option3Button.setText(answers.get(2));
        option4Button.setText(answers.get(3));

        // Attach button handlers
        option1Button.setOnAction(e -> handleAnswer(option1Button.getText()));
        option2Button.setOnAction(e -> handleAnswer(option2Button.getText()));
        option3Button.setOnAction(e -> handleAnswer(option3Button.getText()));
        option4Button.setOnAction(e -> handleAnswer(option4Button.getText()));

        // Start or reset the timer
        startOrResetTimer();
    }

    private void startOrResetTimer() {
        timeRemaining = 30; // Reset timer to 30 seconds
        updateTimerLabel();

        if (timer != null) {
            timer.stop(); // Stop any existing timer
        }

        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;
            updateTimerLabel();

            if (timeRemaining <= 0) {
                timer.stop();
                handleTimeout();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void updateTimerLabel() {
        timerLabel.setText("Time Left: " + timeRemaining + " seconds");
    }

    public void handleAnswer(String answer) {
        if (timer != null) {
            timer.stop(); // Stop the timer when an answer is submitted
        }

        if (answer.equals(correctAnswer)) {
            isCorrectAnswerd = true;
            gameplayController.allowPlayerToContinue(); // Allow the player to continue
           // gameplayController.placeCheckerOnQuestionStation(); // Place the checker only on a correct answer
        } else {
            gameplayController.consumeAllDiceForFailedQuestion(); // Handle incorrect answer
        }
        questionStage.close(); // Close the question window

    }


    public void handleTimeout() {
        if (timer != null) {
            timer.stop();
        }
        questionStage.close(); // Close the question window
        gameplayController.passTurnToNextPlayerDueToQuestionFailure(); // Pass turn on timeout
        
    }
}
