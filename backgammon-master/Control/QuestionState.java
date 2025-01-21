package Control;

public interface QuestionState {
    void handleAnswer(String answer);
    void handleTimeout();
}
