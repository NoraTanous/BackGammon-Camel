package View;

import Control.Settings;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import constants.GameConstants;
import constants.MessageType;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * A TextFlow contained in a ScrollPane that displays the status of the game.
 * 
 * @teamname TeaCup
 * @author Bryan Sng, 17205050
 * @author @LxEmily, 17200573
 * @author Braddy Yeoh, 17357376
 * 
 */
public class InfoPanel extends ScrollPane {
	private TextFlow textContainer;
	private int textPadding;
	
	public InfoPanel() {
		super();
		textContainer = new TextFlow();
		styleScrollPane();
		styleTextContainer();
		initLayout();
		welcome();
	}
	
	private void styleScrollPane() {
		double height = GameConstants.getHalfBoardSize().getHeight();
		double width = GameConstants.getMiddlePartWidth() / 3.0;
		setMinHeight(height);
		setMaxHeight(height);
		setMinWidth(width);
		setMaxWidth(width);
		setFitToWidth(true);									// text fits into width.
		setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);		// no horizontal scroll bar.
		vvalueProperty().bind(textContainer.heightProperty());	// auto scroll down with texts.
		setFocusTraversable(false);
		drawScrollPane();
	}
	private void drawScrollPane() {
		if (Settings.DARK_THEME) setStyle("-fx-background-color: #60544c;");
	}
	
	private void styleTextContainer() {
	    double height = GameConstants.getHalfBoardSize().getHeight();
	    textPadding = 10; // Increased padding for better readability
	    textContainer.setPadding(new Insets(textPadding, textPadding * 3, textPadding, textPadding * 3));
	    textContainer.setLineSpacing(8); // Increased line spacing
	    textContainer.setMinHeight(height);
	    drawTextContainer();
	}

	private void drawTextContainer() {
		if (Settings.DARK_THEME) textContainer.setBackground(GameConstants.getPanelImage());
	}
	
	private void initLayout() {
		setContent(textContainer);
	}
	
	/**
	 * Outputs welcome message and prompts player to start game
	 */
	public void welcome() {
		print("Welcome to Backgammon!");
		
		print(" enter \"/help\" for a list of possible commands.");
	}

	// text padding at top and bottom.
	// 2 line spacing in a single line, top and bottom.
	//
	// First we get a rough possible lines,
	// multiply that with the line spacings,
	// divide by font size, and we get the number of lines
	// that should not be new lines.
	//
	// subtract that by the possible lines and we get our actual lines.
	public void clear() {
		TextFlow textContainer = getTextContainer();
		int possibleLines = (int) (getMinHeight() - textPadding * 2) / GameConstants.FONT_SIZE;
		int numberOfLines = (int) (possibleLines - (possibleLines * textContainer.getLineSpacing() * 2) / GameConstants.FONT_SIZE);
		printNewlines(numberOfLines);
	}
	
	/**
	 * Prints the given text to the information panel.
	 * @param msg - string to be printed
	 * @param mtype - message type, (i.e., error or system message) 
	 */
	public void print(String msg, MessageType mtype) {
		Text text = new Text();
		text.setFont(GameConstants.getFont());
		String prefix = "-";
		String type = "";
		
		if (Settings.DARK_THEME) {
		    switch (mtype) {
		        case ANNOUNCEMENT:
		            text.setFill(Color.LIGHTBLUE); // Softer blue for announcements
		            break;
		        case SYSTEM:
		            text.setFill(Color.LIGHTGREEN); // Softer green for system messages
		            break;
		        case ERROR:
		            text.setFill(Color.INDIANRED); // Softer red for errors
		            break;
		        case DEBUG:
		            text.setFill(Color.LIGHTGRAY); // Softer gray for debug messages
		            break;
		        case WARNING:
		            text.setFill(Color.GOLDENROD); // Milder yellow for warnings
		            break;
		        case CHAT:
		            text.setFill(Color.WHEAT); // Softer orange for chat
		            break;
		    }
		} else {
		    switch (mtype) {
		        case ANNOUNCEMENT:
		            text.setFill(Color.DODGERBLUE); // Softer blue
		            break;
		        case SYSTEM:
		            text.setFill(Color.LIMEGREEN); // Softer green
		            break;
		        case ERROR:
		            text.setFill(Color.SALMON); // Softer red
		            break;
		        case DEBUG:
		            text.setFill(Color.SILVER); // Neutral gray
		            break;
		        case WARNING:
		            text.setFill(Color.KHAKI); // Softer yellow
		            break;
		        case CHAT:
		            text.setFill(Color.PEACHPUFF); // Softer orange
		            break;
		    }
		}

		text.setText(prefix + " " + type + " " + msg + "\n");
		
		// same as
		// (debugMode || (!debugMode && mtype != MessageType.DEBUG))
		// (GameConstants.DEBUG_MODE || mtype != MessageType.DEBUG)
		if (GameConstants.DEBUG_MODE)
			appendText(text);
		else if (mtype != MessageType.DEBUG)
			appendText(text);
	}
	public void print(String msg) {
		print(msg, MessageType.SYSTEM);
	}

	/**
	 * Print empty row to information panel.
	 * @param times number of times to print the new line.
	 */
	public void printNewlines(int times) {
		for (int i = 0; i < times; i++) {
			appendText(new Text("\n"));
		}
	}
	
	/**
	 * Saves everything on the information panel to a text file.
	 */
	public boolean saveToFile() {
		StringBuilder sb = new StringBuilder();
		for (Node node : textContainer.getChildren()) {
			if (node instanceof Text) {
				sb.append(((Text) node).getText());
			}
		}
		return toFile(sb);
	}
	
	/**
	 * Appends texts in the string builder to the log file.
	 * @param sb the string builder containing the string.
	 * @return boolean value indicating if data was saved to file.
	 */
	private boolean toFile(StringBuilder sb) {
		boolean isSaved = false;
		try {
			BufferedWriter buffer = new BufferedWriter(new FileWriter(new File("log.txt"), true));
			buffer.append(sb);
			buffer.newLine();
			buffer.flush();
			buffer.close();
			isSaved = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isSaved;
	}
	
	public TextFlow getTextContainer() {
		return textContainer;
	}
	
	private void appendText(Text text) {
	    String originalText = text.getText();
	    if (originalText.length() > 100) { // Truncate long messages
	        text.setText(originalText.substring(0, 97) + "...");
	        text.setOnMouseClicked(event -> {
	            System.out.println("Full Message: " + originalText);
	        });
	    }
	    textContainer.getChildren().add(text);
	    removeExcessText();
	}

	
	// If exceed threshold, remove upper half of the text.
	private void removeExcessText() {
		if (isFull()) {
			StringBuilder removedTexts = new StringBuilder();
			
			// get removed text.
			int i = 0;
			for (Iterator<Node> iterTextNodes = textContainer.getChildren().iterator(); iterTextNodes.hasNext();) {
				Text aText = (Text) iterTextNodes.next();
				removedTexts.append(aText.getText());
				iterTextNodes.remove();
				i++;
				
				// remove half of the texts.
				if (i >= GameConstants.TEXT_CONTAINER_THRESHOLD/2) break;
			}
			toFile(removedTexts);
			print("Exceeded text container threshold, excess text removed.", MessageType.DEBUG);
		}
	}
	
	// Check if the number of text node in text container exceeds threshold.
	private boolean isFull() {
		return textContainer.getChildren().size() > GameConstants.TEXT_CONTAINER_THRESHOLD;
	}
	
	public void redraw() {
		drawScrollPane();
		drawTextContainer();
	}
	
	public void reset() {
		textContainer.getChildren().clear();
		welcome();
		redraw();
	}
}
