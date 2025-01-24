package Control;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import constants.GameConstants;
import constants.MessageType;
import Control.OutOfTimeHandler;
import Control.OutOfTimeSelectedEvent;
import Control.TouchablesStorerHandler;
import Control.TouchablesStorerSelectedEvent;
import Model.Bar;
import Model.Checker;
import Model.Dice;
import Model.DoublingCubeHome;
import Model.TouchablesStorer;
import Model.Home;
import Model.Pip;
import Model.Question;
import Model.QuestionManager;

import Control.InputValidator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import View.CommandPanel;
import View.InfoPanel;
import View.RollDieButton;
//ddddddddddddd
/**
 * This class handles all the events that is triggered by the user.
 * Sub-controller of MainController.
 * 
 * @teamname TeaCup
 * @author Bryan Sng, 17205050
 * @author @LxEmily, 17200573
 * @author Braddy Yeoh, 17357376
 *
 */
////////dd///d//d/dd
public class EventController implements ColorParser, ColorPerspectiveParser, InputValidator{
	private Stage stage;
	private MatchController root;
	private GameComponentsController game;
	private GameplayController gameplay;
	private InfoPanel infoPnl;
	private RollDieButton rollDieBtn;
	private CommandPanel cmdPnl;
	private CommandController cmd;
	private QuestionManager questionManager;
	
	public EventController(Stage stage, MatchController root, GameComponentsController game, GameplayController gameplay,
			CommandPanel cmdPnl, CommandController cmd, InfoPanel infoPnl, RollDieButton rollDieBtn) {
		this.stage = stage;
		this.root = root;
		this.game = game;
		this.gameplay = gameplay;
		this.cmdPnl = cmdPnl;
		this.cmd = cmd;
		this.infoPnl = infoPnl;
		this.rollDieBtn = rollDieBtn;
		initGameListeners();
		initUIListeners();
		 try {
		        // Use the writable path directly
		        Path writablePath = QuestionManager.getWritableFilePath();
		        this.questionManager = new QuestionManager(writablePath);
		        System.out.println("QuestionManager initialized successfully.");
		    } catch (Exception e) {
		        e.printStackTrace();
		        System.err.println("Failed to initialize QuestionManager.");
		        this.questionManager = null;
		    }
		}
	

	/*private void handleQuestionStation(Pip pip, Checker checker) {
	    Platform.runLater(() -> {
	        try {
	            // Load the QuestionView
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/QuestionView.fxml"));
	            Parent root = loader.load();

	            Stage questionStage = new Stage();
	            questionStage.initModality(Modality.APPLICATION_MODAL);
	            questionStage.setTitle("Question Station");

	            QuestionStationController controller = loader.getController();
	            Question randomQuestion = questionManager.getRandomQuestion();
	            controller.setQuestionData(randomQuestion, questionStage, gameplay);

	            Scene scene = new Scene(root);
	            questionStage.setScene(scene);

	            // Set up a timer to enforce a question timeout
	            Timer questionTimer = new Timer();
	            questionTimer.schedule(new TimerTask() {
	                @Override
	                public void run() {
	                    Platform.runLater(() -> {
	                        if (questionStage.isShowing()) {
	                            controller.handleTimeout(); // Handle timeout
	                            questionStage.close();
	                        }
	                        questionTimer.cancel();
	                    });
	                }
	            }, 30000); // 30 seconds

	            // Handle window close event
	            questionStage.setOnCloseRequest(event -> {
	                questionTimer.cancel(); // Stop the timer
	                if (!QuestionStationController.isCorrectAnswerd) {
	                    System.out.println("Question failed or timed out. Checker not placed.");
	                }
	            });

	            // Display the question and wait for user interaction
	            questionStage.showAndWait();

	            // Place the checker only if the question was answered correctly
	            if (QuestionStationController.isCorrectAnswerd) {
	                pip.push(checker); // Add the checker to the pip
	                System.out.println("Correct answer! Checker placed on Question Station.");
	            } else {
	                System.out.println("Incorrect answer or timeout. Checker not placed.");
	            }

	            // Reset the answer state for future interactions
	            QuestionStationController.isCorrectAnswerd = false;

	        } catch (Exception e) {
	            System.err.println("Error handling question station: " + e.getMessage());
	            e.printStackTrace();
	        }
	    });
	}*/

	/*private boolean handleSurpriseStation(Pip pip, Checker checker) {
	    if (!pip.hasProcessedSurprise(checker)) {
	        pip.setProcessedSurprise(checker, true); // Mark as processed

	        Platform.runLater(() -> {
	            try {
	                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/StationView.fxml"));
	                Parent root = loader.load();
	                Stage stage = new Stage();
	                stage.initModality(Modality.APPLICATION_MODAL);
	                stage.setTitle("Surprise Station");

	                SurpriseStationController controller = loader.getController();
	                controller.setSurpriseData(stage, gameplay);
	                Scene scene = new Scene(root);
	                stage.setScene(scene);

	                // Set a timer to auto-close the popup
	                Timer timer = new Timer();
	                timer.schedule(new TimerTask() {
	                    @Override
	                    public void run() {
	                        Platform.runLater(() -> {
	                            if (stage.isShowing()) {
	                                controller.closeSurpriseStation();
	                                stage.close();
	                            }
	                            timer.cancel();
	                        });
	                    }
	                }, 10000); // Auto-close after 10 seconds

	                // Handle manual close
	                stage.setOnCloseRequest(event -> {
	                    timer.cancel();
	                    controller.closeSurpriseStation();
	                });

	                stage.showAndWait();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        });

	        return true; // Successfully processed
	    }
	    return false; // Already processed
	}*/
	private boolean handleSurpriseStation(Pip pip, Checker checker) {
	    infoPnl.print("DEBUG: handleSurpriseStation invoked.", MessageType.DEBUG);
	    Platform.runLater(() -> {
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/StationView.fxml"));
	            Parent root = loader.load();
	            Stage stage = new Stage();
	            stage.initModality(Modality.APPLICATION_MODAL);
	            stage.setTitle("Surprise Station");

	            Scene scene = new Scene(root);
	            stage.setScene(scene);
	            stage.showAndWait();
	        } catch (IOException e) {
	            e.printStackTrace();
	            infoPnl.print("Error loading FXML: " + e.getMessage(), MessageType.ERROR);
	        }
	    });
	    return true;
	}




	private void setExtraTurnAfterSurpriseStation() {
	    infoPnl.print("You earned an extra turn after completing your current moves!", MessageType.ANNOUNCEMENT);
	    gameplay.hasExtraRound = true; // Grant the extra turn flag
	}


	private void initGameListeners() {
		// Exit selection mode when any part of the game board is clicked.
		game.setOnMouseClicked((MouseEvent event) -> {
			resetSelections();
		});
		
		initTouchableListeners();
		initTouchablesStorersListeners();
		initOutOfTimeListener();
	}
	public void resetSelections() {
		// reshow the dices on the board,
		// if player selected cube in its box or player's home
		// but unselected it.
		if (gameplay.isStarted()) {
			if (!gameplay.isRolled() && !game.getBoard().isCubeInBoard()) {
				// if box or player's home clicked, 
				if (isCubeHomeSelectionMode || isHomeSelectionMode) {
					game.getBoard().redrawDices(gameplay.getCurrent().getColor());
				}
			}
		} else {
			if (!game.getBoard().isCubeInBoard()) game.getBoard().redrawDices();
		}
		
		game.unhighlightAll();
		isPipSelectionMode = false;
		isBarSelectionMode = false;
		isHomeSelectionMode = false;
		isCubeHomeSelectionMode = false;
		
		if (gameplay.isStarted()) {
			if (!gameplay.isRolled()) {
				if (gameplay.mustHighlightCube()) {
					game.highlightCube();
				}
			// highlight the possible moves if player hasn't move.
			} else if (!gameplay.isMoved()) {
				game.getBoard().highlightFromPipsAndFromBarChecker(gameplay.getValidMoves());
			}
		}
	}
	
	private void initTouchableListeners() {
		//root.addEventHandler(TouchableSelectedEvent.TOUCHABLE_SELECTED, touchableHandler);
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

	
	/*
	TouchableHandler touchableHandler = new TouchableHandler() {
		@Override
		public void onClicked(Touchable object) {
			if (object instanceof DoublingCube) {
				infoPnl.print("Doubling Cube selected.", MessageType.DEBUG);

				// if cube at its box.
				if (!game.getOtherHome().getCubeHome().isEmpty()) {
					
				} else if (!game) {
					
				}
				
				if (gameplay.isStarted()) {
					//  
					if (!gameplay.isDoubling()) {
						// highlight cube box.
						game.getOtherHome().getCubeHome().highlight();
					}
					
					game.getOtherHome().getHome(gameplay.getCurrent().getColor()).highlight();
				} else {
					// highlight player's other homes.
					game.getOtherHome().getHome(Settings.getBottomPerspectiveColor()).highlight();
					game.getOtherHome().getHome(Settings.getTopPerspectiveColor()).highlight();
				}
				isCubeSelectionMode = true;
			}
		}
	};
	*/
	
	private void initTouchablesStorersListeners() {
		root.addEventHandler(TouchablesStorerSelectedEvent.STORER_SELECTED, touchablesStorerHandler);
	}
	private boolean handleQuestionStation(Pip selectedPip, Checker checker) {
	    final boolean[] answeredCorrectly = {false};

	    Platform.runLater(() -> {
	        try {
	            Question question = questionManager.getRandomQuestion();
	            if (question == null) {
	                infoPnl.print("No questions available.", MessageType.ERROR);
	                return;
	            }

	            // Show the question popup and get the result
	            answeredCorrectly[0] = showQuestionPopup(question);

	            if (answeredCorrectly[0]) {
	                infoPnl.print("Correct answer! Proceeding with the move.", MessageType.SYSTEM);
	            } else {
	                infoPnl.print("Incorrect answer. Turn passed to the next player.", MessageType.ERROR);
	            }
	        } catch (Exception e) {
	            infoPnl.print("Error handling question station: " + e.getMessage(), MessageType.ERROR);
	            e.printStackTrace();
	        }
	    });

	    return answeredCorrectly[0];
	}
	private boolean showQuestionPopup(Question question) {
	    final boolean[] result = {false}; // Store the result of the question

	    try {
	        // Load the FXML layout
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/QuestionView.fxml")); // Update the path if needed
	        Parent root = loader.load();

	        // Get the controller from the FXML loader
	        QuestionStationController controller = loader.getController();

	        // Create a new stage for the popup
	        Stage questionStage = new Stage();
	        questionStage.initModality(Modality.APPLICATION_MODAL); // Block the main game window
	        questionStage.setTitle("Question Station");

	        // Pass the question data to the controller
	        controller.setQuestionData(question, questionStage, gameplay);

	        // Set the scene and display the popup
	        Scene scene = new Scene(root);
	        questionStage.setScene(scene);
	        questionStage.showAndWait(); // Wait for the popup to close

	        // Get the result from the controller
	        result[0] = QuestionStationController.isCorrectAnswerd;

	    } catch (IOException e) {
	        e.printStackTrace();
	        infoPnl.print("Error loading the QuestionView: " + e.getMessage(), MessageType.ERROR);
	    }

	    return result[0]; // Return whether the answer was correct
	}



	/**
	 * Event handler for all checker storers (pips, bars, homes).
	 * Separated from initTouchablesStorersListeners() for easier removal.
	 */
	private boolean isPipSelectionMode = false;
	private boolean isBarSelectionMode = false;
	private boolean isHomeSelectionMode = false;
	private boolean isCubeHomeSelectionMode = false;
	private TouchablesStorer storerSelected;
	TouchablesStorerHandler touchablesStorerHandler = new TouchablesStorerHandler() {
		@Override
		public void onClicked(TouchablesStorer object) {
			// Pip selected, basis for fromPip or toPip selection.
			if (object instanceof Pip) {
			    Pip selectedPip = (Pip) object;

			    // Handle pip selection or movement if not in selection mode
			    if (!isInSelectionMode()) {
			        storerSelected = object;
			        int fromPip = ((Pip) storerSelected).getPipNumber();

			        if (!gameplay.isStarted()) {
			            infoPnl.print("The game has not started yet.", MessageType.ERROR);
			            return;
			        }

			        if (gameplay.getValidMoves() == null) {
			            infoPnl.print("Valid moves not initialized.", MessageType.ERROR);
			            return;
			        }

			        if (gameplay.getValidMoves().isValidFro(fromPip)) {
			            gameplay.highlightPips(fromPip);
			            isPipSelectionMode = true;
			            infoPnl.print("Pip clicked is: " + gameplay.correct(fromPip) + ".", MessageType.DEBUG);
			        } else {
			            if (!gameplay.isRolled()) {
			                infoPnl.print("You can only move after rolling.", MessageType.ERROR);
			            } else {
			                infoPnl.print("You can only move from highlighted objects.", MessageType.ERROR);
			            }
			        }
			    } else if (isPipSelectionMode) {
			        int fromPip = ((Pip) storerSelected).getPipNumber();
			        int toPip = selectedPip.getPipNumber();

			        if (gameplay.getValidMoves() == null) {
			            infoPnl.print("Valid moves not initialized.", MessageType.ERROR);
			            return;
			        }

			        if (gameplay.getValidMoves().isValidFro(fromPip)) { // Check if the source pip is valid
			            if (gameplay.isValidTarget(fromPip, toPip)) { // Validate the target pip
			                if (selectedPip.isQuestionStation()) { // Check if the target pip is a question station
			                    Question question = questionManager.getRandomQuestion(); // Fetch a random question
			                    if (question == null) {
			                        infoPnl.print("No questions available.", MessageType.ERROR);
			                        gameplay.passTurnToNextPlayerDueToQuestionFailure();
			                        return;
			                    }

			                    // Show the question popup and handle the answer
			                    if (!showQuestionPopup(question)) {
			                        infoPnl.print("Incorrect answer. Turn passed to the next player.", MessageType.ERROR);
			                      //  gameplay.passTurnToNextPlayerDueToQuestionFailure();
			                        return; // Stop further processing
			                    }
			                }
				        	    infoPnl.print("Player landed on a Surprise Station.", MessageType.DEBUG); // Log
				        	    Checker checker = getCurrentChecker();
				        	    if (checker != null) {
				        	        boolean isProcessed = handleSurpriseStation(selectedPip, checker);
				        	        if (isProcessed) {
				        	            infoPnl.print("Surprise Station processed! Enjoy your surprise!", MessageType.ANNOUNCEMENT);
				        	            gameplay.hasExtraRound = true;
				        	        } else {
				        	            infoPnl.print("Surprise Station already processed.", MessageType.INFO);
				        	        }
				        	    } else {
				        	        infoPnl.print("No checker available for the Surprise Station.", MessageType.ERROR); // Log error
				        	    }
				        	}

			                

			                // Execute the move
			                cmd.runCommand("/move " + fromPip + " " + toPip);
			                gameplay.unhighlightPips();
			                isPipSelectionMode = false;
			            }
				       
				       
			        } else {
			            infoPnl.print("You cannot move from this pip. It is not a valid starting point.", MessageType.ERROR);
			        }
			    }
			
			        else if (isPipSelectionMode || isBarSelectionMode) {
			        int toPip = selectedPip.getPipNumber();

			        if (isPipSelectionMode) {
			            int fromPip = ((Pip) storerSelected).getPipNumber();
			            cmd.runCommand("/move " + fromPip + " " + toPip);
			        } else if (isBarSelectionMode) {
			            String fromBar = parseColor(((Bar) storerSelected).getColor());
			            cmd.runCommand("/move " + fromBar + " " + toPip);
			        }

			        gameplay.unhighlightPips();
			        isPipSelectionMode = false;
			        isBarSelectionMode = false;
			    }
			

			    // Either pip or bar selected, basis for toPip selection
			    else if (isPipSelectionMode || isBarSelectionMode) {
			        int toPip = selectedPip.getPipNumber();

			        // If it's a question station
			        if (selectedPip.isQuestionStation()) {
			            Checker currentChecker = getCurrentChecker(); // Get the checker being moved
			            if (currentChecker != null) {
			                boolean answeredCorrectly = handleQuestionStation(selectedPip, currentChecker);
			                if (!answeredCorrectly) {
			                    gameplay.passTurnToNextPlayerDueToQuestionFailure(); // Pass the turn
			                    return;
			                }
			            }
			        }

			        // Proceed with the move if not a question station or if the question was answered correctly
			        if (isPipSelectionMode) {
			            int fromPip = ((Pip) storerSelected).getPipNumber();
			            cmd.runCommand("/move " + fromPip + " " + toPip);
			        } else if (isBarSelectionMode) {
			            String fromBar = parseColor(((Bar) storerSelected).getColor());
			            cmd.runCommand("/move " + fromBar + " " + toPip);
			        }

			        gameplay.unhighlightPips();
			        isPipSelectionMode = false;
			        isBarSelectionMode = false;
			    }
			}
			else if (object instanceof Bar) {
				// prevent entering into both pip and bar selection mode.
				if (!isInSelectionMode()) {
					storerSelected = object;
					String fromBar = parseColor(((Bar) storerSelected).getColor());
					int fromBarPipNum = Settings.getPipBearOnBoundary(getPOV(parseColor(fromBar)));
					// same as ((gameplay.isStarted() && gameplay.isValidFro(fromPip)) || (!gameplay.isStarted()))
					if (!gameplay.isStarted() || gameplay.getValidMoves().isValidFro(fromBarPipNum)) {
						gameplay.highlightPips(fromBar);
						isBarSelectionMode = true;
						infoPnl.print("Bar clicked.", MessageType.DEBUG);
					} else {
						if (!gameplay.isRolled()) infoPnl.print("You can only move after rolling.", MessageType.ERROR);
						else infoPnl.print("You can only move from highlighted objects.", MessageType.ERROR);
					}
				}
			// home selected, basis for fromHome or toHome selection.
			} else if (object instanceof Home) {
				// some storer selected, basis for toHome selection.
				if (isPipSelectionMode || isBarSelectionMode || isCubeHomeSelectionMode) {
					String toHome = parseColor(((Home) object).getColor());
					if (isPipSelectionMode) {
						int fromPip = ((Pip) storerSelected).getPipNumber();
						cmd.runCommand("/move " + fromPip + " " + toHome);
					} else if (isBarSelectionMode) {
						String fromBar = parseColor(((Bar) storerSelected).getColor());
						cmd.runCommand("/move " + fromBar + " " + toHome);
					} else if (isCubeHomeSelectionMode) {
						if (gameplay.isStarted()) {
							cmd.runCommand("/accept");
						} else {
							DoublingCubeHome fromCubeHome = (DoublingCubeHome) storerSelected;
							cmd.runCommand("/movecube " + parseColor(fromCubeHome.getColor()) + " " + toHome);
						}
					}
					gameplay.unhighlightPips();
					game.unhighlightAllPlayersCubeHomes();
					isPipSelectionMode = false;
					isBarSelectionMode = false;
					isCubeHomeSelectionMode = false;
				// home not selected, basis for fromHome selection.
				// used to select the doubling cube.
				} else {
					if (!isInSelectionMode()) {
						if (!gameplay.isStarted() || (!gameplay.isRolled() && !root.isCrawfordGame() && gameplay.mustHighlightCube())) {
							// fromHome consideration only if its a doubling cube.
							storerSelected = object;
							Home fromHome = (Home) storerSelected;
							if (fromHome.getTopCube() != null) {
								gameplay.highlightBoardCubeZones();
								isHomeSelectionMode = true;
							}
						}
					}
				}
			// doubling cube home selected, basis for (from box to Board) or (from Board to box/home) selection.
			} else if (object instanceof DoublingCubeHome) {
				// player's home selected, basis for toBoard selection. 
				if (isHomeSelectionMode) {
					DoublingCubeHome toCubeHome = (DoublingCubeHome) object;
					if (toCubeHome.isOnBoard()) {
						Home fromHome = (Home) storerSelected;
						if (gameplay.isStarted()) {
							cmd.runCommand("/double");
						} else {
							cmd.runCommand("/movecube " + parseColor(fromHome.getColor()) + " " + parseColor(toCubeHome.getColor()));
						}
					}
					game.getBoard().unhighlightAllCubeHome();
					isHomeSelectionMode = false;
				// no cube home selected, basis for fromCubeHome selection.
				} else if (!isInSelectionMode()) {
					if (!gameplay.isStarted() || (!root.isCrawfordGame() && gameplay.mustHighlightCube())) {
						storerSelected = object;
						DoublingCubeHome fromCubeHome = (DoublingCubeHome) storerSelected;
						
						// check if the doubling cube home has the doubling cube.
						if (!fromCubeHome.isEmpty()) {
							// cube home selected is on board.
							if (fromCubeHome.isOnBoard()) {
								gameplay.highlightOtherHomeCubeZones();
							// cube home selected is in its box.
							} else {
								// highlight board.
								gameplay.highlightBoardCubeZones();
							}
							isCubeHomeSelectionMode = true;
						}
					}
				// cube home selected, basis for toCubeHome selection.
				// i.e. cube box to board's cube home, or vice versa.
				} else if (isCubeHomeSelectionMode) {
					DoublingCubeHome fromCubeHome = (DoublingCubeHome) storerSelected;
					DoublingCubeHome toCubeHome = (DoublingCubeHome) object;

					// check if it clicked on the same cube home,
					// if so, we ignore the selection.
					//
					// NOTE: if we don't check this, the code below will execute
					// and the cube will be unhighlighted when it should be.
					if (!fromCubeHome.equals(toCubeHome)) {
						// cube box to board.
						if (!fromCubeHome.isOnBoard() && toCubeHome.isOnBoard()) {
							if (gameplay.isStarted()) {
								cmd.runCommand("/double");
							} else {
								cmd.runCommand("/movecube box " + parseColor(toCubeHome.getColor()));
							}
						// board to cube box.
						} else if (fromCubeHome.isOnBoard() && !toCubeHome.isOnBoard()) {
							if (gameplay.isStarted()) {
								cmd.runCommand("/decline");
							} else {
								cmd.runCommand("/movecube " + parseColor(fromCubeHome.getColor()) + " box");
							}
						}
						game.unhighlightAllPlayersCubeHomes();
						game.getBoard().unhighlightAllCubeHome();
						isCubeHomeSelectionMode = false;
					}
				}
			} else {
				infoPnl.print("Other instances of checkersStorer were clicked.", MessageType.DEBUG);
			}
		}
	};
	public boolean isInSelectionMode() {
		return isPipSelectionMode || isBarSelectionMode || isCubeHomeSelectionMode || isHomeSelectionMode;
	}
	
	private void initOutOfTimeListener() {
		root.addEventHandler(OutOfTimeSelectedEvent.OUTOFTIME, outOfTimeHandler);
	}
	OutOfTimeHandler outOfTimeHandler = new OutOfTimeHandler() {
		@Override
		public void onOutOfTime() {
			infoPnl.print("You ran out of time.");
			root.handleMatchOver(true);
		}
	};

	/**
	 * Manages all the UI (infoPnl, cmdPnl, rollDieBtn) listeners.
	 */
	private void initUIListeners() {
		initCommandPanelListeners();
		initRollDieButtonListeners();

		if (!GameConstants.DEBUG_MODE)
			initStageListeners();
	}

	/**
	 * Manages command panel listeners.
	 * 		- if its a command (i.e. start with '/'), run it.
	 * 		- echoes player input to infoPanel.
	 * 		- does not echo empty strings/whitespace.
	 */
	private void initCommandPanelListeners() {
		cmdPnl.setOnAction((ActionEvent event) -> {
			String text = cmdPnl.getText();
			cmdPnl.addHistory(text);
			String[] args = text.split(" ");
			
			if (cmdPnl.isCommand(text)) {
				cmd.runCommand(cmdPnl.getText(), true);
			} else if (args.length == 2 && isPip(args[0]) && isPip(args[1])) {
				cmd.runCommand("/move " + text, true);
			} else if (gameplay.getGameplayMoves().isMapped() && gameplay.getGameplayMoves().isKey(text.toUpperCase().trim())) {
				cmd.runCommand(gameplay.getGameplayMoves().getMapping(text.toUpperCase().trim()));
			} else if (text.equals("double")) {
				cmd.runCommand("/double");
			} else if (text.equals("yes") && gameplay.isDoubling()) {
				cmd.runCommand("/accept");
			} else if (text.equals("no") && gameplay.isDoubling()) {
				cmd.runCommand("/decline");
			} else if (text.equals("start")) {
				cmd.runCommand("/start");
			} else if (text.equals("roll")) {
				cmd.runCommand("/roll");
			} else if (text.equals("next")) {
				cmd.runCommand("/next");
			} else if (text.equals("cheat")) {
				cmd.runCommand("/cheat");
			} else if (text.equals("save")) {
				cmd.runCommand("/save");
			} else if (text.equals("quit")) {
				cmd.runCommand("/quit");
			} else if (text.trim().isEmpty()) {
				// ignores if string empty or whitespace only.
			} else {
				infoPnl.print(text, MessageType.CHAT);
			}
			cmdPnl.setText("");
		});
	}
	
	private int dieState = 2;
	private void initRollDieButtonListeners() {
		
		if(LevelController.level.equals("Easy")) {
			rollDieBtn.setOnAction((ActionEvent event) -> {
				if (dieState == 1) {
					dieState = 2;
				} else {
					dieState = 1;
				}
				cmd.runCommand("/roll " + Integer.toString(dieState));
			});
		}
		if(LevelController.level.equals("Medium")) {
			
	    rollDieBtn.setOnAction((ActionEvent event) -> {
	    
	        // Execute the die rolling logic
	        if (dieState == 1) {
	            dieState = 2;
	        } else {
	            dieState = 1;
	        }
	        if (!gameplay.isRolled()) {
				infoPnl.print("Rolling...", MessageType.ANNOUNCEMENT);
				cmd.runCommand("/roll " + Integer.toString(dieState));
			      try {
			            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/QuestionView.fxml"));
			            Parent root = loader.load();

			            Stage stage = new Stage();
			            stage.initModality(Modality.APPLICATION_MODAL);
			            stage.setTitle("Question Station");

			            QuestionStationController controller = loader.getController();

			            // Roll the dice to determine difficulty
			            int difficultyRoll = Dice.roll2();
			            String difficulty;
			            switch (difficultyRoll) {
			                case 1:
			                    difficulty = "1";
			                    break;
			                case 2:
			                    difficulty = "2";
			                    break;
			                case 3:
			                    difficulty = "3";
			                    break;
			                default:
			                    throw new IllegalStateException("Unexpected value: " + difficultyRoll);
			            }

			            // Fetch a random question by the rolled difficulty
			            Question randomQuestion = questionManager.getRandomQuestion(difficulty);
			            controller.setQuestionData(randomQuestion, stage, gameplay);

			            Scene scene = new Scene(root);
			            stage.setScene(scene);

			            // Set timeout for the question
			            Timer timer = new Timer();
			            timer.schedule(new TimerTask() {
			                @Override
			                public void run() {
			                    Platform.runLater(() -> {
			                        if (stage.isShowing()) {
			                            controller.handleTimeout(); // Handle timeout
			                        }
			                        timer.cancel();
			                    });
			                }
			            }, 30000); // 30 seconds

			           // stage.setOnCloseRequest(event2 -> timer.cancel());
			            stage.showAndWait();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
			} else {
				infoPnl.print("Die has already been rolled.", MessageType.ERROR);
			}
	       
	  
	    });
		}
		if(LevelController.level.equals("Hard")) {
			
		    rollDieBtn.setOnAction((ActionEvent event) -> {
		    
		        // Execute the die rolling logic
		        if (dieState == 1) {
		            dieState = 2;
		        } else {
		            dieState = 1;
		        }
		        if (!gameplay.isRolled()) {
					infoPnl.print("Rolling...", MessageType.ANNOUNCEMENT);
					cmd.runCommand("/roll " + Integer.toString(dieState));
				      try {
				            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/QuestionView.fxml"));
				            Parent root = loader.load();

				            Stage stage = new Stage();
				            stage.initModality(Modality.APPLICATION_MODAL);
				            stage.setTitle("Question Station");

				            QuestionStationController controller = loader.getController();

				            // Roll the dice to determine difficulty
				            int difficultyRoll = Dice.roll2();
				            String difficulty;
				            switch (difficultyRoll) {
				                case 1:
				                    difficulty = "1";
				                    break;
				                case 2:
				                    difficulty = "2";
				                    break;
				                case 3:
				                    difficulty = "3";
				                    break;
				                default:
				                    throw new IllegalStateException("Unexpected value: " + difficultyRoll);
				            }

				            // Fetch a random question by the rolled difficulty
				            Question randomQuestion = questionManager.getRandomQuestion(difficulty);
				            controller.setQuestionData(randomQuestion, stage, gameplay);

				            Scene scene = new Scene(root);
				            stage.setScene(scene);

				            // Set timeout for the question
				            Timer timer = new Timer();
				            timer.schedule(new TimerTask() {
				                @Override
				                public void run() {
				                    Platform.runLater(() -> {
				                        if (stage.isShowing()) {
				                            controller.handleTimeout(); // Handle timeout
				                        }
				                        timer.cancel();
				                    });
				                }
				            }, 30000); // 30 seconds

				           // stage.setOnCloseRequest(event2 -> timer.cancel());
				            stage.showAndWait();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
				} else {
					infoPnl.print("Die has already been rolled.", MessageType.ERROR);
				}
		       
		  
		    });
			}
	}



	private void initStageListeners() {
		// checks if player really wants to exit game prevents accidental exits
		stage.setOnCloseRequest((WindowEvent event) -> {
			// Alert settings.
			Alert exitCheck = new Alert(Alert.AlertType.CONFIRMATION);
			exitCheck.setHeaderText("Do you really want to exit Backgammon?");
			exitCheck.initModality(Modality.APPLICATION_MODAL);
			exitCheck.initOwner(stage);

			infoPnl.print("Trying to quit game.");
			cmd.runSaveCommand();

			// Exit button.
			Button exitBtn = (Button) exitCheck.getDialogPane().lookupButton(ButtonType.OK);
			exitBtn.setText("Exit");

			// Exit application.
			Optional<ButtonType> closeResponse = exitCheck.showAndWait();
			if (!ButtonType.OK.equals(closeResponse.get())) {
				event.consume();
			}
		});
	}

	public void reset() {
		dieState = 2;
		storerSelected = null;
		resetSelections();
	}
}
