package Control;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

import Model.Question;

public class QuestionStationController {
    public static boolean isCorrectAnswerd;
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
    private Label timerLabel;

    private String correctAnswer;
    private Stage questionStage;
    private GameplayController gameplayController;
    private Timeline timer;
    private int timeRemaining = 60;

    private QuestionState currentState;

    public void setQuestionData(Question question, Stage stage, GameplayController gameplayController) {
        this.questionStage = stage;
        this.gameplayController = gameplayController;

        stage.setOnCloseRequest(event -> event.consume());

        questionLabel.setText(question.getQuestionText());
        List<String> answers = question.getAnswers();
        correctAnswer = question.getCorrectAnswer();

        option1Button.setText(answers.get(0));
        option2Button.setText(answers.get(1));
        option3Button.setText(answers.get(2));
        option4Button.setText(answers.get(3));

        option1Button.setOnAction(e -> currentState.handleAnswer(option1Button.getText()));
        option2Button.setOnAction(e -> currentState.handleAnswer(option2Button.getText()));
        option3Button.setOnAction(e -> currentState.handleAnswer(option3Button.getText()));
        option4Button.setOnAction(e -> currentState.handleAnswer(option4Button.getText()));

        startOrResetTimer();
        setCustomIcon(stage);

        currentState = new WaitingForAnswerState(this);
    }

    private void startOrResetTimer() {
        timeRemaining = 60;
        updateTimerLabel();

        if (timer != null) {
            timer.stop();
        }

        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;
            updateTimerLabel();

            if (timeRemaining <= 0) {
                timer.stop();
                currentState.handleTimeout();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void updateTimerLabel() {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        timerLabel.setText(String.format("Time Left: %02d:%02d", minutes, seconds));
    }

    public void closeQuestionWindow() {
        questionStage.close();
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public GameplayController getGameplayController() {
        return gameplayController;
    }

    public void setState(QuestionState state) {
        this.currentState = state;
    }

    private void setCustomIcon(Stage stage) {
        try {
            Image icon = new Image(getClass().getResourceAsStream("/img/backgammon.png"));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("Error loading custom icon: " + e.getMessage());
        }
    }

	public void handleTimeout() {
		// TODO Auto-generated method stub
		
	}
}
