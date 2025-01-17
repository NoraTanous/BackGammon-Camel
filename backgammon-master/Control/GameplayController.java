package Control;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import constants.DieInstance;
import constants.GameEndScore;
import constants.MessageType;
import Model.Checker;
import Model.DieResults;
import Model.DoublingCube;
import Model.GameHistory;
import Model.Home;
import Model.Move;
import Model.PlayerPanel;
import Model.RollMoves;
import Model.TouchablesStorer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import Model.Moves;
import Model.Pip;
import View.Dialogs;
import View.InfoPanel;
import View.ScoreboardPrompt;

/**
 * This class handles the gameplay of Backgammon.
 * Sub-controller of MainController.
 * 
 * @teamname TeaCup
 * @author Bryan Sng, 17205050
 * @author @LxEmily, 17200573
 * @author Braddy Yeoh, 17357376
 *
 */
public class GameplayController implements ColorParser, ColorPerspectiveParser, InputValidator, IndexOffset, IntegerLettersParser {
	private boolean isStarted, isRolled;
	boolean isMoved;
	private boolean isFirstRoll;
	private boolean isTopPlayer;
	private boolean isDoubling;
	private boolean isDoubled;
	private boolean isMaxDoubling;
	private boolean isInTransition;
	private Player bottomPlayer, topPlayer, pCurrent, pOpponent;
	private List<GameHistory> gameHistoryList = new ArrayList<>();
	private Stage stage;
	private MatchController root;
	private CommandController cmd;
	private GameComponentsController game;
	private InfoPanel infoPnl;
	private GameplayMovesController gameplayMoves;
	private TouchablesStorer storerSelected;
	boolean hasExtraRound = false;

	
	 public GameplayController(Stage stage, MatchController root, GameComponentsController game, InfoPanel infoPnl, Player bottomPlayer, Player topPlayer) {
		    this.bottomPlayer = bottomPlayer;
		    this.topPlayer = topPlayer;
		    this.stage = stage;
		    this.root = root;
		    this.game = game;
		    this.infoPnl = infoPnl;
		    gameplayMoves = new GameplayMovesController(game, this, infoPnl);
		    reset();

		    // Handle the X button click
		    stage.setOnCloseRequest(event -> {
		        event.consume(); // Prevent default close behavior
		        System.out.println("Game window is closing. Saving current game state...");
		        handleWindowClose(); // Call the window close logic
		    });
		}
	 public boolean isListenerActive() {
		    return stage != null && stage.isShowing();
		}
	 private void handleWindowClose() {
		    if (cmd != null) {
		        System.out.println("Invoking /quit command...");
		        cmd.runCommand("/quit"); // Call the /quit logic
		    } else {
		        System.out.println("CommandController is not initialized. Exiting directly...");
		        handleGameCompletion(false); // Save incomplete game state
		        Platform.exit();
		        System.exit(0);
		    }
		}



	 

	
	public void reset() {
		isStarted = false;
		isRolled = false;
		isMoved = false;
		isFirstRoll = true;
		isTopPlayer = false;
		isDoubling = false;
		isDoubled = false;
		isMaxDoubling = false;
		isInTransition = false;
		if (nextPause != null) nextPause.stop();
		gameplayMoves.reset();
		stopCurrentPlayerTimer();
	}
	private void handleQuitOnClose() {
	    System.out.println("Quitting game via window close...");
	    try {
	        handleGameCompletion(false); // Save incomplete game
	    } catch (Exception e) {
	        System.err.println("Error saving game state: " + e.getMessage());
	    }
	    Platform.exit(); // Close the JavaFX application
	    System.exit(0);  // Ensure application terminates
	}

	private void saveGameHistory() {
	    String playerOne = bottomPlayer != null ? bottomPlayer.getName() : "Unknown Player";
	    String playerTwo = topPlayer != null ? topPlayer.getName() : "Unknown Player";
	    Integer scorePlayerOne = bottomPlayer != null ? bottomPlayer.getScore() : null; // Allow null if game incomplete
	    Integer scorePlayerTwo = topPlayer != null ? topPlayer.getScore() : null; // Allow null if game incomplete
	    String winner = (scorePlayerOne != null && scorePlayerTwo != null)
	            ? (scorePlayerOne > scorePlayerTwo ? playerOne : scorePlayerTwo > scorePlayerOne ? playerTwo : "Draw")
	            : null; // Set to null for incomplete games

	    // Create a GameHistory object
	    GameHistory history = new GameHistory(playerOne, playerTwo, scorePlayerOne, scorePlayerTwo, winner);

	    // Load existing history
	    List<GameHistory> historyList = GameHistoryJsonUtil.loadHistory();

	    // Add the new entry
	    historyList.add(history);

	    // Save updated history to file
	    GameHistoryJsonUtil.saveHistory(historyList);

	    System.out.println("Game history saved: " + history);
	}




	  public void moveChecker(Checker checker, Pip targetPip) {
	        System.out.println("Moving checker to Pip: " + targetPip.getPipNumber());
	        targetPip.addChecker(checker);
	    }
	 public List<GameHistory> getGameHistoryList() {
	        return gameHistoryList;
	    }
	
	public void setCommandController(CommandController cmd) {
		this.cmd = cmd;
		gameplayMoves.setCommandController(cmd);
	}
	public boolean isValidTarget(int fromPip, int toPip) {
	    // Check if the source pip is valid
	    if (!getValidMoves().isValidFro(fromPip)) {
	        return false; // Invalid source pip
	    }

	    // Check if the move from source to target is valid
	    for (RollMoves rollMoves : getValidMoves()) {
	        for (Move aMove : rollMoves.getMoves()) {
	            if (aMove.getFro() == fromPip && aMove.getTo() == toPip) {
	                return true; // Valid target pip
	            }
	        }
	    }

	    return false; // No valid move found
	}

	/**
	 * Auto roll die to see which player moves first.
	 * Called at /start.
	 */
	public void start() {
		isStarted = true;
		startGame(topPlayer, bottomPlayer);
		
		// facial expressions.
		game.getEmojiOfPlayer(pCurrent.getColor()).setThinkingFace();
		game.getEmojiOfPlayer(pOpponent.getColor()).setThinkingFace();
		
	}
	
	/**
	 * Rolls die, calculates possible moves and highlight top checkers.
	 * Called at /roll.
	 */
	public void startGame(Player player1, Player player2) {
	    // Initialize the players at the start of the game.
	    pCurrent = player1;
	    pOpponent = player2;

	    infoPnl.print("Game started. First player to move will be: " + pCurrent.getName() + ".");
	    isFirstRoll = true;

	    // If the first player is the top player, prepare the board accordingly.
	    if (pCurrent.equals(topPlayer)) {
	        game.getBoard().swapPipLabels();
	        isTopPlayer = true;
	    }

	    // Highlight the current player's checker in their player panel.
	    handleNecessitiesOfEachTurn();
	}

	public void roll() {
	    // Roll dice when it's time for the player's turn.
	    DieResults rollResult;
	    if (isFirstRoll) {
	        // First roll for the first turn.
	        rollResult = game.getBoard().rollDices(DieInstance.SINGLE);
	        infoPnl.print("Roll dice result: " + rollResult + ".");
	        isFirstRoll = false;
	    } else {
	        // Regular roll for subsequent turns.
	        rollResult = game.getBoard().rollDices(pCurrent.getPOV());
	        infoPnl.print("Roll dice result: " + rollResult + ".");
	    }

	    isRolled = true;

	    // Calculate possible moves.
	    setValidMoves(game.getBoard().calculateMoves(rollResult, pCurrent));
	    gameplayMoves.handleEndOfMovesCalculation(getValidMoves());
	}

	/**
	 * Returns first player to roll based on roll die result.
	 * @param rollResult roll die result.
	 * @return first player to roll.
	 */
	private Player getFirstPlayerToRoll(DieResults rollResult) {
		int bottomPlayerRoll = rollResult.getLast().getDiceResult();
		int topPlayerRoll = rollResult.getFirst().getDiceResult();

		if (bottomPlayerRoll > topPlayerRoll) {
			return bottomPlayer;
		} else if (topPlayerRoll > bottomPlayerRoll) {
			return topPlayer;
		}
		return null;
	}
	
	
	/**
	 * Returns the second player to roll based on first player.
	 * i.e. its one or the other.
	 * @param firstPlayer first player to roll.
	 * @return second player to roll.
	 */
	private Player getSecondPlayerToRoll(Player firstPlayer) {
		if (firstPlayer.equals(topPlayer)) {
			return bottomPlayer;
		} else {
			return topPlayer;
		}
	}
	
	/**
	 * Called at /move.
	 */
	public void move() {
	    gameplayMoves.setStalemateCount(0);

	    if (isGameOver()) {
	        handleGameOver();
	        return; // Stop further processing
	    }

	    updateMovesAfterMoving();

	    boolean moveMadeCausedPlayerAbleBearOff = !getValidMoves().isEmpty() && game.getBoard().isAllCheckersInHomeBoard(pCurrent);

	    if (moveMadeCausedPlayerAbleBearOff || getValidMoves().hasDiceResultsLeft()) {
	        recalculateMoves();
	    } else if (getValidMoves().isEmpty()) {
	        isMoved = true;
	        infoPnl.print("Move over.");

	        // Handle extra turn logic
	        if (hasExtraRound) {
	            infoPnl.print(pCurrent.getName() + " has an extra turn! Roll the dice again.", MessageType.ANNOUNCEMENT);
	            hasExtraRound = false; // Reset the extra turn flag
	            isRolled = false; // Allow rolling again
	            isMoved = false; // Allow moving again
	            recalculateMoves(); // Refresh moves for the extra turn
	        } else {
	            next(); // Swap turns if no extra turn is granted
	        }
	    } else {
	        gameplayMoves.handleCharacterMapping();
	        gameplayMoves.printMoves();
	    }
	}


	
	private void updateMovesAfterMoving() {
		game.getBoard().updateIsHit(getValidMoves(), pCurrent);
	}
	
	public void recalculateMoves() {
		if (isRolled()) {
			infoPnl.print("Recalculating moves.", MessageType.DEBUG);
			setValidMoves(game.getBoard().recalculateMoves(getValidMoves(), pCurrent));
			gameplayMoves.handleEndOfMovesCalculation(getValidMoves());
		}
	}
	
	/**
	 * Starts the timer for the respective player's turn.
	 * If the safe timer runs out (15 secs),
	 * it will start decrementing the player's individual timer per sec.
	 */
	private void startCurrentPlayerTimer() {
		if (pCurrent != null) game.getPlayerPanel(pCurrent.getColor()).getTimer().start();
	}
	
	/**
	 * Stops the timer for the respective player's turn.
	 * If timer is stopped within the safe timer's limit, then nothing is decremented,
	 * else, update the player's individual timer
	 */
	public void stopCurrentPlayerTimer() {
		if (pCurrent != null) game.getPlayerPanel(pCurrent.getColor()).getTimer().stop();
	}
	
	/**
	 * Swap players and pip number labels, used to change turns.
	 * Called at /next.
	 * @return the next player to roll.
	 */
	private Timeline nextPause;
	
	public Player next() {
	    if (hasExtraRound) {
	        infoPnl.print(pCurrent.getName() + " continues for the extra round.");
	        hasExtraRound = false; // Reset the extra turn flag
	        isRolled = false; // Allow dice roll again
	        isMoved = false; // Allow actions again
	        return pCurrent; // Do not switch players
	    }

	    // Swap players if no extra turn is granted
	    isRolled = false;
	    isMoved = false;
	    stopCurrentPlayerTimer();

	    infoPnl.print("Swapping turns...", MessageType.ANNOUNCEMENT);

	    if (Settings.ENABLE_NEXT_PAUSE) {
	        nextPause = new Timeline(new KeyFrame(Duration.seconds(2), ev -> {
	            isInTransition = false;
	            nextFunction();
	        }));
	        nextPause.setCycleCount(1);
	        nextPause.play();
	        isInTransition = true;
	    } else {
	        nextFunction();
	    }

	    return pCurrent;
	}




	public void nextFunction() {
		if (isDoubling()) stopCurrentPlayerTimer();
		
		infoPnl.print("It is now " + pOpponent.getName() + "'s (" + parseColor(pOpponent.getColor()) + ") move.");
		swapPlayers();
		game.getBoard().swapPipLabels();
		
		handleNecessitiesOfEachTurn();
		
		// if doubling cube can be highlighted,
		// then player can choose to roll or play double.
		if (mustHighlightCube()) {
			game.highlightCube();
			infoPnl.print("You may now roll the dice or play the double.");
		} else {
			if (Settings.ENABLE_AUTO_ROLL) {
				infoPnl.print("Cannot play double, auto rolling...");
				roll();
			} else infoPnl.print("You can only roll the dice.");
		}
	}
	private void swapPlayers() {
		Player temp = pCurrent;
		pCurrent = pOpponent;
		pOpponent = temp;
		if (pCurrent.equals(topPlayer)) {
			isTopPlayer = true;
		} else {
			isTopPlayer = false;
		}
	}
	
	private void handleNecessitiesOfEachTurn() {
		startCurrentPlayerTimer();
		// highlight the current player's checker in his player panel,
		// and unhighlight opponent's.
		game.getPlayerPanel(pCurrent.getColor()).highlightChecker();
		game.getPlayerPanel(pOpponent.getColor()).unhighlightChecker();
	}
	
	public boolean mustHighlightCube() {
		boolean mustHighlightCube = false;
		if (!root.isCrawfordGame() && !isInTransition() && !isMaxDoubling() || isDoubling()) {
			// if cube in player's home,
			// then highlight only when it is that player's turn.
			if (game.isCubeInHome() && !pCurrent.hasCube()) {
				mustHighlightCube = false;
			} else {
				mustHighlightCube = true;
			}
			
			// dont highlight cube if player's score
			// is already capped with current stakes.
			//
			// highlight only if doubling stakes hasn't been proposed.
			if (isCurrentPlayerScoreCapped() && !isDoubling()) {
				mustHighlightCube = false;
				infoPnl.print("Cube not highlighted, player's score is capped.", MessageType.DEBUG);
			}
		}
		return mustHighlightCube;
	}
	
	// checks if current player's score added with current cube multiplier
	// causes player to reach total matches,
	// i.e. player wins the match if player wins this game.
	// i.e. currentPlayer.score + 1*cubeMultiplier >= totalGames.
	public boolean isCurrentPlayerScoreCapped() {
		return pCurrent.getScore() + game.getCube().getEndGameMultiplier() >= Settings.TOTAL_GAMES_IN_A_MATCH;
	}
	
	/**
	 * Highlight pips and checkers based on mode.
	 * Used by EventController.
	 * @param fromPip
	 */
	public void highlightPips(int fromPip) {
		// gameplay mode.
		if (isRolled()) {
			game.getBoard().highlightToPipsAndToHome(getValidMoves(), fromPip);
		// free for all mode, i.e. before /start.
		} else {
			game.getBoard().highlightAllPipsExcept(fromPip);
		}
	}
	public void highlightPips(String fromBar) {
		// gameplay mode.
		if (isRolled()) {
			game.getBoard().highlightToPipsAndToHome(getValidMoves(), fromBar);
		// free for all mode, i.e. before /start.
		} else {
			game.getBoard().highlightAllPipsExcept(-1);
		}
	}
	
	/**
	 * Unhighlight pips based on mode.
	 * Used by EventController.
	 */
	public void unhighlightPips() {
		// gameplay mode.
		if (isStarted()) {
			if (isMoved()) game.unhighlightAll();
			else game.getBoard().highlightFromPipsAndFromBarChecker(getValidMoves());
		// free for all mode, i.e. before /start.
		} else {
			game.unhighlightAll();
		}
	}
	
	public void highlightOtherHomeCubeZones() {
		if (isStarted()) {
			game.highlightCubeZones(pCurrent.getColor());
		} else {
			game.highlightAllPlayersCubeHomes();
		}
	}
	
	public void highlightBoardCubeZones() {
		if (isStarted()) {
			game.getBoard().highlightCubeHome(pCurrent.getColor());
		} else {
			game.getBoard().highlightAllCubeHome();
		}
	}
	
	public void doubling() {
		if (isDoubling()) {
			isDoubling = false;
			isDoubled = true;
		} else {
			isDoubling = true;
			isDoubled = false;
		}
	}
	
	/**
	 * If either player's score is not equal to the max score per match,
	 * announces game over on infoPnl and dialog prompt,
	 * then ask if player wants another game.
	 * 
	 * Else announce the winner and ask if the players want to play another match.
	 */
	public void handleGameOver(boolean isIntermediate) {
	    // Output to infoPnl.
	    infoPnl.print("Game over.", MessageType.ANNOUNCEMENT);

	    stopCurrentPlayerTimer();
	    if (isIntermediate) swapPlayers();
	    handleGameOverScore(isIntermediate);

	 // Save the game history
	    handleGameCompletion(true); // Mark game as complete

	    if (root.isMatchOver())
	        root.handleMatchOver();
	    else {
	        int remainingScore = Settings.TOTAL_GAMES_IN_A_MATCH - pCurrent.getScore();
	        String playerResult = pCurrent.getScore() + " down, " + remainingScore + " to go.";

	        // Create dialog prompt.
	        Dialogs<ButtonType> dialog = new Dialogs<ButtonType>(
	            "Winner Winner Chicken Dinner! " + pCurrent.getShortName() + " wins! " + playerResult,
	            stage, "Next game"
	        );

	        // Add score board to dialog prompt
	        ScoreboardPrompt contents = new ScoreboardPrompt(topPlayer, bottomPlayer);
	        dialog.getDialogPane().setContent(contents);

	        // Auto save game log.
	        infoPnl.saveToFile();
	        

	        // Output to dialog prompt.
	        Optional<ButtonType> result = dialog.showAndWait();

	        // Restart game if player wishes,
	        // else exit gameplay mode and enter free-for-all mode.
	        reset();
	        if (result.get().equals(dialog.getButton())) {
	            cmd.runCommand("/start");
	        } else {
	            infoPnl.print("Enter /start if you wish to resume the game.", MessageType.ANNOUNCEMENT);
	            infoPnl.print("Enter /quit if you wish to quit.", MessageType.ANNOUNCEMENT);
	        }
	    }
	}
	

	public void handleGameOver() {
		handleGameOver(false);
	}
	
	private void handleGameOverScore(boolean isIntermediate) {
		Player winner = pCurrent;
		if (isIntermediate) {
			// round end, allocate points as required.
			PlayerPanel winnerPnl = game.getPlayerPanel(winner.getColor());
			winnerPnl.setPlayerScore(winner, getIntermediateScore());
		} else {
			Home filledHome = game.getMainHome().getFilledHome();
			if (filledHome.getColor().equals(winner.getColor())) {
				PlayerPanel winnerPnl = game.getPlayerPanel(winner.getColor());
				winnerPnl.setPlayerScore(winner, getGameOverScore());
			} else {
				infoPnl.print("[ERROR] FilledHome is not the expected winner's (i.e. pCurrent).", MessageType.DEBUG);
			}
		}
		// facial expressions.
		game.getEmojiOfPlayer(pCurrent.getColor()).setWinFace();
		game.getEmojiOfPlayer(pOpponent.getColor()).setLoseFace();
		infoPnl.print("Congratulations, " + winner.getName() + " won.");
		infoPnl.print(topPlayer.getName() + ": " + getScoreFormat(topPlayer.getScore()) + " vs " + bottomPlayer.getName() + ": " + getScoreFormat(bottomPlayer.getScore()));
	}
	private String getScoreFormat(int score) {
		return score + "/" + Settings.TOTAL_GAMES_IN_A_MATCH;
	}
	
	/**
	 * Check if any homes are filled.
	 * Game over when one of the player has all 15 checkers at their home.
	 * @return boolean value indicating if game is over.
	 */
	private boolean isGameOver() {
		return game.getMainHome().getFilledHome() != null;
	}
	
	// score if a player wins, i.e. 15 checkers in their home.
	public int getGameOverScore() {
		int score;
		// since current player is the one that made the winning move,
		// the opponent the loser.
		Player winner = pCurrent;
		Player loser = pOpponent;
		DoublingCube cube = game.getCube();
		score = winner.getScore() + game.getBoard().getGameScore(loser.getColor())*cube.getEndGameMultiplier();
		return score;
	}
	
	// score if a player rejects/declines a doubling of stakes.
	public int getIntermediateScore() {
		int score;
		// current player must be the proposer, hence the winner.
		// so turns must be swapped to get back to the proposer.
		Player winner = pCurrent;
		DoublingCube cube = game.getCube();
		score = winner.getScore() + GameEndScore.SINGLE.ordinal()*cube.getIntermediateGameMultiplier();
		return score;
	}
	
	public String correct(int pipNum) {
		return gameplayMoves.correct(pipNum);
	}
	
	public boolean isStarted() {
		return isStarted;
	}
	public boolean isRolled() {
		return isRolled;
	}
	public boolean isMoved() {
		return isMoved;
	}
	public boolean isDoubling() {
		return isDoubling;
	}
	public boolean isDoubled() {
		return isDoubled;
	}
	public boolean isMaxDoubling() {
		return isMaxDoubling;
	}
	public boolean isTopPlayer() {
		return isTopPlayer;
	}
	public boolean isInTransition() {
		return isInTransition;
	}
	public GameplayMovesController getGameplayMoves() {
		return gameplayMoves;
	}
	public Moves getValidMoves() {
		return gameplayMoves.getValidMoves();
	}
	public Player getCurrent() {
		return pCurrent;
	}
	public Player getOpponent() {
		return pOpponent;
	}
	public void setValidMoves(Moves moves) {
		gameplayMoves.setValidMoves(moves);
	}
	public void setIsMaxDoubling(boolean isMaxDoubling) {
		this.isMaxDoubling = isMaxDoubling;
	}
	public void allowPlayerToContinue() {
        infoPnl.print(pCurrent.getName() + " answered correctly and can continue their turn.");
        // The player can proceed with their turn.
    }

    public void passTurnToNextPlayerDueToQuestionFailure() {
        infoPnl.print(pCurrent.getName() + " failed to answer correctly. Passing the turn.");
        next(); // Pass turn to the next player.
    }
    public void grantExtraTurn() {
        infoPnl.print(pCurrent.getName() + " earned an extra turn! Complete this round and play again.", MessageType.ANNOUNCEMENT);
        hasExtraRound = true; // Set the flag for an extra turn
        isRolled = false; // Allow dice roll
        isMoved = false; // Allow moves
    }



    public Checker getCurrentChecker() {
        // Check if the currently selected storer is a Pip and return its top Checker.
        if (storerSelected instanceof Pip) {
            Pip selectedPip = (Pip) storerSelected;
            return selectedPip.getTopChecker(); // Assuming getTopChecker() exists in Pip or CheckersStorer.
        }
        // Return null if no valid Checker is currently selected.
        return null;
    }
    public void consumeAllDiceForFailedQuestion() {
        infoPnl.print(pCurrent.getName() + " failed to answer the question. Both dice are consumed.");
        if (gameplayMoves != null && gameplayMoves.getValidMoves() != null) {
            gameplayMoves.getValidMoves().clear(); // Clear remaining valid moves
        }
        isMoved = true; // Mark turn as completed
        next(); // Pass turn to the next player
    }
    public void placeCheckerOnQuestionStation() {
        if (storerSelected instanceof Pip) {
            Pip questionPip = (Pip) storerSelected;
            if (QuestionStationController.isCorrectAnswerd) {
                Checker currentChecker = getCurrentChecker();
                if (currentChecker != null) {
                    questionPip.push(currentChecker);
                    questionPip.drawCheckers(); // Refresh visuals
                    infoPnl.print(pCurrent.getName() + "'s checker placed at Question Station: " + questionPip.getPipNumber());
                } else {
                    infoPnl.print("No checker available for placement.");
                }
            } else {
                infoPnl.print("Checker placement skipped: Question not answered correctly.");
            }
        }
    }
    public void handleGameCompletion(boolean isGameComplete) {
        String playerOne = bottomPlayer != null && bottomPlayer.getName() != null ? bottomPlayer.getName() : "Unknown Player";
        String playerTwo = topPlayer != null && topPlayer.getName() != null ? topPlayer.getName() : "Unknown Player";

        // Use default values (e.g., 0 or null) for incomplete games
        Integer scorePlayerOne = isGameComplete && bottomPlayer != null ? bottomPlayer.getScore() : 0;
        Integer scorePlayerTwo = isGameComplete && topPlayer != null ? topPlayer.getScore() : 0;

        // Determine the winner only if both scores are available
        String winner;
        if (scorePlayerOne != null && scorePlayerTwo != null) {
            if (scorePlayerOne > scorePlayerTwo) {
                winner = playerOne;
            } else if (scorePlayerTwo > scorePlayerOne) {
                winner = playerTwo;
            } else {
                winner = "Draw";
            }
        } else {
            winner = "Incomplete Game"; // Default for incomplete games
        }

        // Log debug info
        System.out.println("Player One: " + playerOne + ", Score: " + scorePlayerOne);
        System.out.println("Player Two: " + playerTwo + ", Score: " + scorePlayerTwo);
        System.out.println("Winner: " + winner);

        // Save game history
        try {
            GameHistory gameHistory = new GameHistory(playerOne, playerTwo, scorePlayerOne, scorePlayerTwo, winner);
            GameHistoryJsonUtil.saveGameHistory(gameHistory);
            System.out.println("Game history saved successfully.");
        } catch (Exception e) {
            System.err.println("Error saving game history: " + e.getMessage());
            e.printStackTrace();
        }
    }



    
    
}
