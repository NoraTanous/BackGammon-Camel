package Control;

public class WaitingForAnswerState implements QuestionState {
    private final QuestionStationController controller;

    public WaitingForAnswerState(QuestionStationController controller) {
        this.controller = controller;
    }

    @Override
    public void handleAnswer(String answer) {
        if (answer.equals(controller.getCorrectAnswer())) {
            controller.setState(new CorrectAnswerState(controller));
            controller.getGameplayController().allowPlayerToContinue();
        } else {
            controller.setState(new IncorrectAnswerState(controller));
            controller.getGameplayController().consumeAllDiceForFailedQuestion();
        }
        controller.closeQuestionWindow();
    }

    @Override
    public void handleTimeout() {
        controller.setState(new TimeoutState(controller));
        controller.getGameplayController().passTurnToNextPlayerDueToQuestionFailure();
        controller.closeQuestionWindow();
    }
}
