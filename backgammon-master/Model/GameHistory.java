package Model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class GameHistory {
    private String playerOne;
    private String playerTwo;
    private int scorePlayerOne;
    private int scorePlayerTwo;
    private String winner;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime;

    // Default constructor (required for Jackson)
    public GameHistory() {
    }

    // Constructor with parameters
    public GameHistory(String playerOne, String playerTwo, int scorePlayerOne, int scorePlayerTwo, String winner) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.scorePlayerOne = scorePlayerOne;
        this.scorePlayerTwo = scorePlayerTwo;
        this.winner = winner;
        this.dateTime = LocalDateTime.now();
    }

    // Getters and setters
    public String getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(String playerOne) {
        this.playerOne = playerOne;
    }

    public String getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(String playerTwo) {
        this.playerTwo = playerTwo;
    }

    public int getScorePlayerOne() {
        return scorePlayerOne;
    }

    public void setScorePlayerOne(int scorePlayerOne) {
        this.scorePlayerOne = scorePlayerOne;
    }

    public int getScorePlayerTwo() {
        return scorePlayerTwo;
    }

    public void setScorePlayerTwo(int scorePlayerTwo) {
        this.scorePlayerTwo = scorePlayerTwo;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
