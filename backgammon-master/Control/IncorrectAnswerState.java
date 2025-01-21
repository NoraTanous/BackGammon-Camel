package Control;

public class IncorrectAnswerState implements QuestionState {
    private final QuestionStationController controller;

    public IncorrectAnswerState(QuestionStationController controller) {
        this.controller = controller;
    }

    @Override
    public void handleAnswer(String answer) {
        // No further actions, as the player has already answered incorrectly.
    }

    @Override
    public void handleTimeout() {
        // No timeout behavior in this state.
    }
}
