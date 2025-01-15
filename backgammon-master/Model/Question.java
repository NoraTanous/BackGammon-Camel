package Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private final StringProperty question;
    private List<String> answers; // Made mutable to allow updating
    private final StringProperty correctAnswer;
    private final StringProperty difficulty;

    public Question(
            @JsonProperty("question") String question,
            @JsonProperty("answers") List<String> answers,
            @JsonProperty("correct_ans") String correctAnswer,
            @JsonProperty("difficulty") String difficulty
    ) {
        this.question = new SimpleStringProperty(question);
        this.answers = new ArrayList<>(answers); // Initialize with mutable list
        this.correctAnswer = new SimpleStringProperty(correctAnswer);
        this.difficulty = new SimpleStringProperty(difficulty);
    }

    public String getQuestionText() {
        return question.get();
    }

    public void setQuestionText(String question) {
        this.question.set(question);
    }

    public StringProperty questionTextProperty() {
        return question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> newAnswers) {
        if (newAnswers != null) {
            this.answers = new ArrayList<>(newAnswers); // Replace with new list
        }
    }

    public String getCorrectAnswer() {
        return correctAnswer.get();
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer.set(correctAnswer);
    }

    public StringProperty correctAnswerProperty() {
        return correctAnswer;
    }

    public String getDifficulty() {
        return difficulty.get();
    }

    public void setDifficulty(String difficulty) {
        this.difficulty.set(difficulty);
    }

    public StringProperty difficultyProperty() {
        return difficulty;
    }
}
