package Model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import Control.ColorParser;
import Control.LevelController;
import Control.MatchController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * This class represents the dice object in Backgammon game.
 * 
 * @teamname TeaCup
 * @author Bryan Sng, 17205050
 * @author @LxEmily, 17200573
 * @author Braddy Yeoh, 17357376
 *
 */
public class Dice extends ImageView implements ColorParser {
	private final int MAX_DICE_SIZE = 6;
	private final int MAX__HARD_DICE_SIZE = 9;

	private final static int MAX_DIFF_DICE_SIZE = 3;

	private Image[] dices;
	private Image[] hardDices;


	private int diceRollResult;
	private ColorAdjust colorAdjust;
	private Color color;
	private String level;
	/**
	 * Constructors
	 * 		- Initialize the dices array with the possible dice images (i.e. 1-6).
	 */
	public Dice(Dice otherDice) {
		this(otherDice.getColor());
		this.diceRollResult = otherDice.getDiceResult();
	}
	public Dice(int diceRollResult) {
		this(Color.RED);
		this.diceRollResult = diceRollResult;
	}
	public Dice(Color color) {
		super();
		 
		this.color = color;
		if(LevelController.level.equals("Medium") || (LevelController.level.equals("Easy"))) {
					dices = new Image[MAX_DICE_SIZE];
					
		}
		if(LevelController.level.equals("Hard")) {
			dices = new Image[MAX__HARD_DICE_SIZE];
		}
		initImages();
		
		colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(-0.5);
	}
	
	public Dice() {
		// TODO Auto-generated constructor stub
	}
	public static String setLevel(String level) {
	  return level;
	}
	/**
	 * Initializes the dice images:
	 * 		- by getting the images from file,
	 * 		- adding them to the dices array.
	 * @param color
	 */
	private void initImages() {
	    String colorString = parseColor(color);
	    int minDice = LevelController.level.equals("Hard") ? -3 : 1; // Hard starts at -3, Normal starts at 1
	    int maxDice = 6; // The maximum dice value

	    // Initialize the dices array to the correct size
	    dices = new Image[maxDice - minDice + 1];

	    for (int i = minDice; i <= maxDice; i++) { // Include maxDice in the range
	        try {
	        	if(i==0) {
	        		continue;
	        	}
	            // Construct the file name based on the dice value directly
	            String fileName = i + ".png"; // File names like -3.png, -2.png, ..., 6.png
	            InputStream input = getClass().getResourceAsStream("img/dices2/" + colorString + "/" + fileName);

	            if (input == null) {
	                throw new IOException("Image file not found for dice: " + fileName);
	            }

	            // Map dice values directly to the array
	            dices[i - minDice] = new Image(input); // Map i to the array index
	            input.close();
	        } catch (IOException e) {
	            System.err.println("Failed to load dice image for dice: " + i + ".png");
	            e.printStackTrace();
	        }
	    }
	}


	/**
	 * Get the roll dice result (i.e. number from 1 to 6).
	 * @return the roll dice result.
	 */
	public int roll() {
		Random rand = new Random();
		int res = rand.nextInt(MAX_DICE_SIZE) + 1;
		return res;
	}
	public static int roll2() {
		Random rand = new Random();
		int res = rand.nextInt(MAX_DIFF_DICE_SIZE) + 1;
		return res;
	}
	public int rollhard() {
	    Random rand = new Random();
	    int min = -3; // Minimum value
	    int max = 6;  // Maximum value
	    int res;

	    do {
	        res = rand.nextInt((max - min) + 1) + min; // Generate random number in range [-3, 6]
	    } while (res == 0); // Re-roll if the result is 0

	    return res;
	}

	/**
	 * Set the image of dice based on result.
	 * i.e. If result is 1, show image with dice at 1.
	 * @param result of dice roll.
	 */
	public Dice draw(int result) {
	    diceRollResult = result;

	    // Adjust the index to account for the hard level range
	    int arrayIndex;
	    if (LevelController.level.equals("Hard")) {
	        arrayIndex = result + 3; // Shift range [-3, 6] to [0, 9]
	    } else {
	        arrayIndex = result - 1; // Default for [1, 6] range
	    }

	    // Set the image based on the adjusted index
	    if (arrayIndex >= 0 && arrayIndex < dices.length) {
	        setImage(dices[arrayIndex]);
	    } else {
	        System.err.println("Invalid dice result: " + result);
	    }

	    setEffect(null);
	    rotate();
	    return this;
	}
	/*public Dice draw(int result) {
		diceRollResult = result;
		setImage(dices[result-1]);
		setEffect(null);
		rotate();
		return this;
	}*/
	
	/**
	 * Rotate the dice image.
	 */
	private void rotate() {
	    // Random final rotation between -15 and 15
	    Random rand = new Random();
	    int rotation = rand.nextInt(30) - 15 + 1;

	    // Rotation animation
	    Timeline rotateTimeline = new Timeline(
	        new KeyFrame(Duration.ZERO, new KeyValue(rotateProperty(), 0)),
	        new KeyFrame(Duration.seconds(0.5), new KeyValue(rotateProperty(), 720)), // Two full rotations
	        new KeyFrame(Duration.seconds(1), new KeyValue(rotateProperty(), rotation)) // Final random rotation
	    );

	    // Bounce animation
	    Timeline bounceTimeline = new Timeline(
	        new KeyFrame(Duration.ZERO, new KeyValue(translateYProperty(), 0)),
	        new KeyFrame(Duration.seconds(0.25), new KeyValue(translateYProperty(), -20)), // Bounce up
	        new KeyFrame(Duration.seconds(0.5), new KeyValue(translateYProperty(), 0)) // Back to original position
	    );

	    // Play animations together
	    rotateTimeline.play();
	    bounceTimeline.play();
	}


	
	public void setUsed() {
		// darken image.
		setEffect(colorAdjust);
	}
	
	public void setNotUsed() {
		setEffect(null);
	}
	
	public int getDiceResult() {
		return diceRollResult;
	}
	
	public Color getColor() {
		return color;
	}
	
	public boolean equalsValueOf(Dice otherDice) {
		return diceRollResult == otherDice.getDiceResult();
	}

}
