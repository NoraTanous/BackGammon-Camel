package Control;

public class CorrectAnswerState implements QuestionState {
    private final QuestionStationController controller;

    public CorrectAnswerState(QuestionStationController controller) {
        this.controller = controller;
    }

    @Override
    public void handleAnswer(String answer) {
        // No further actions, as the player has already answered correctly.
    }

    @Override
    public void handleTimeout() {
        // No timeout behavior in this state.
    }
}
