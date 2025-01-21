package Control;

public class TimeoutState implements QuestionState {
    private final QuestionStationController controller;

    public TimeoutState(QuestionStationController controller) {
        this.controller = controller;
    }

    @Override
    public void handleAnswer(String answer) {
        // No further actions, as the timer has already expired.
    }

    @Override
    public void handleTimeout() {
        // No further timeout actions needed.
    }
}
