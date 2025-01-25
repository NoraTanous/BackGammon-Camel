package Control;

public class DifficultyDecorator implements Explanation {
    private final Explanation explanation;

    public DifficultyDecorator(Explanation explanation) {
        this.explanation = explanation;
    }

    @Override
    public String getExplanationText() {
        return explanation.getExplanationText() + """
               \nAdditional Information:
               - Easy: Best for beginners to understand basic gameplay.
               - Medium: Adds a layer of strategy with Question Dice.
               - Hard: Challenges players with advanced moves and penalties.
               """;
    }
}