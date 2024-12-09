package game;


import java.io.IOException;
import java.io.InputStream;

import constants.GameConstants;
import interfaces.ColorParser;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;

/**
 * This class represents the Pip object in Backgammon.
 * This class helps BoardComponents class to initialize the checkers for each pip object.
 * This class also adds the checkers objects to the pip object, to be drawn to the stage.
 * 
 * @teamname TeaCup
 * @author Bryan Sng, 17205050
 * @author @LxEmily, 17200573
 * @author Braddy Yeoh, 17357376
 *
 */
public class Pip extends CheckersStorer implements ColorParser {
	private Background normalBG;
	private Background highlightedBG; 
	private int pipNum;
	private boolean isQuestionStation=false; // Flag to mark this Pip as a Question Station
    private boolean isSurpriseStation= false ; // Flag to mark this Pip as a Surprise Station


	
	/**
	 * Default Constructor
	 * 		- Initialize the img and imgHighlighteded instance variable of the pip.
	 * 		- Set this pip's transformation, alignment, size, etc.
	 * 		- Set that img to be the background of this pip.
	 * 		- Initialize this pip's listeners.
	 * 
	 * @param color of the pip.
	 * @param rotation either 0 or 180. 0 = pointing upwards. 180 = pointing downwards. 
	 */
    public Pip(Color color, double rotation, int pipNum) {
        super();
        this.pipNum = pipNum;
        // Load normal and highlighted background images
        String colorString = parseColor(color);
        InputStream input1 = getClass().getResourceAsStream("img/board/" + colorString + "_point.png");
        InputStream input2 = getClass().getResourceAsStream("img/board/" + colorString + "_point_highlighted.png");
        
        normalBG = new Background(new BackgroundImage(new Image(input1), null, null, null, null));
        highlightedBG = new Background(new BackgroundImage(new Image(input2), null, null, null, null));
        try {
            input1.close();
            input2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream questionImageStream = getClass().getResourceAsStream("/img/question.png");
        if (questionImageStream == null) {
            System.err.println("Question image not found!");
        }

        InputStream surpriseImageStream = getClass().getResourceAsStream("/img/gift-box-benefits.png");
        if (surpriseImageStream == null) {
            System.err.println("Surprise image not found!");
        }

        setRotate(rotation);
        setAlignment(Pos.BOTTOM_CENTER);
        setMinSize(GameConstants.getPipSize().getWidth(), GameConstants.getPipSize().getHeight());
        setNormalImage();
       
        
    }

	
	
	
    /**
     * Updates the visual representation of the pip based on its type.
     */
    public void updateVisualRepresentation() {
        String imagePath;
        if (isQuestionStation) {
            System.out.println("Updating Pip " + pipNum + " as Question Station");
            imagePath = pipNum % 2 == 0 ? "/game/img/board/black_point_question.png"
                    : "/game/img/board/white_point_question.png";
        } else if (isSurpriseStation) {
            System.out.println("Updating Pip " + pipNum + " as Surprise Station");
            imagePath = pipNum % 2 == 0 ? "/game/img/board/black_point_surprise.png"
                    : "/game/img/board/white_point_surprise.png";
        } else {
            // Reset to default visuals
            System.out.println("Resetting Pip " + pipNum + " to default visuals");
            setNormalImage();
            return;
        }

        // Update background with the selected image
        try (InputStream imageStream = getClass().getResourceAsStream(imagePath)) {
            if (imageStream != null) {
                normalBG = new Background(new BackgroundImage(new Image(imageStream), null, null, null, null));
                setBackground(normalBG);
            } else {
                System.err.println("Failed to load image for Pip " + pipNum + " from path: " + imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    /**
     * Marks this pip as a Question Station.
     * 
     * @param isQuestionStation true to mark it as a Question Station, false otherwise.
     */
	public void setQuestionStation(boolean isQuestionStation) {
        this.isQuestionStation = isQuestionStation;
        updateVisualRepresentation();
    }

    /**
     * Checks if this pip is a Question Station.
     * 
     * @return true if this pip is a Question Station, false otherwise.
     */
    public boolean isQuestionStation() {
        return isQuestionStation;
    }

    /**
     * Marks this pip as a Surprise Station.
     * 
     * @param isSurpriseStation true to mark it as a Surprise Station, false otherwise.
     */
    public void setSurpriseStation(boolean isSurpriseStation) {
        this.isSurpriseStation = isSurpriseStation;
        updateVisualRepresentation();
    }

    /**
     * Checks if this pip is a Surprise Station.
     * 
     * @return true if this pip is a Surprise Station, false otherwise.
     */
    public boolean isSurpriseStation() {
        return isSurpriseStation;
    }

    /**
	 * Use the highlighted image.
	 */
	public void setHighlightImage() {
		setBackground(highlightedBG);
	}

	/**
	 * Use the normal image (i.e. image that is not highlighted).
	 */
	public void setNormalImage() {
		setBackground(normalBG);
	}
	
	/**
	 * Returns the pointNum instance variable (the number the point represents).
	 * @return the pointNum instance variable.
	 */
	public int getPipNumber() {
		return pipNum;
	}

    
    


}