package Control;

public class StrategyTipsDecorator implements Explanation {
    private final Explanation explanation;

    public StrategyTipsDecorator(Explanation explanation) {
        this.explanation = explanation;
    }

    @Override
    public String getExplanationText() {
        return explanation.getExplanationText() + """
               \nStrategy Tips:
               - Focus on building blocks to prevent your opponent's movement.
               - Use Question Dice strategically to gain an advantage.
               - Anticipate your opponent's moves and plan accordingly.
               """;
    }
}