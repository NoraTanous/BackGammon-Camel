package Control;

public class BasicExplanation implements Explanation {

    @Override
    public String getExplanationText() {
        return """
               How to Play Backgammon:

               1. Game Overview:
                  - Backgammon is a two-player strategy game involving a board, checkers, and dice.
                  - The goal is to move all your checkers off the board before your opponent does.

               2. Components:
                  - Game Board: 24 triangular points divided into four quadrants.
                  - Checkers: Each player has 15 checkers of a unique color.
                  - Dice: Includes regular dice, question dice, and enhanced dice.

               3. Game Flow:
                  - Players roll dice to determine their moves.
                  - Land on Question Stations to answer questions.
                  - Move all your checkers to your home base and off the board to win.

               4. Difficulty Levels:
                  - Easy: Regular dice only.
                  - Medium: Includes Question Dice.
                  - Hard: Includes Enhanced Dice with backward movement.

               5. Winning:
                  - The first player to remove all their checkers wins.
                  - The game records winners and history for future reference.
               """;
    }
}