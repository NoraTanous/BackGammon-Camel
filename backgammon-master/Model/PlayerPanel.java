package Model;

import constants.GameConstants;

import java.io.InputStream;

import Control.ColorParser;
import Control.Player;
import Control.Settings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import musicplayer.MusicPlayer;

/**
 * This class represents the panel that displays player info at the top/bottom of the board.
 * 
 * @teamname TeaCup
 * @author Bryan Sng, 17205050
 * @author @LxEmily, 17200573
 * @author Braddy Yeoh, 17357376
 *
 */
public class PlayerPanel extends HBox implements ColorParser {
	
	public static boolean flag = false;
	/**
	 * This class represents labels that stores player info.
	 */
	private static class PlayerInfo extends Label {
		public PlayerInfo(String string, ImageView icon) {
			super(string, icon);
			initStyle();
		}
		
		public void initStyle() {
			setFont(Font.loadFont(GameConstants.getFontInputStream(), GameConstants.FONT_SIZE_PLAYER_PANEL));
			setTextFill(Color.WHITE);
			setMaxWidth(GameConstants.getScreenSize().getWidth() * 0.15);
			setWrapText(true);
		}
		
		// used by instance variable playerColor.
		public Checker getChecker() {
			return (getGraphic() instanceof Checker) ? (Checker) getGraphic() : null;
		}
		
		// used by instance variable playerName.
		public Emoji getEmoji() {
			return (getGraphic() instanceof Emoji) ? (Emoji) getGraphic() : null;
		}
	}
	
	private Player player;
	private PlayerInfo playerName;
	private PlayerInfo playerColor;
	private PlayerInfo playerScore;
	private GameplayTimer timer;
    private HBox musicControls; // HBox to hold all music icons
    private MusicPlayer musicPlayer;


	
	public PlayerPanel(double width, Player player) {
		super();
		this.player = player;
        this.musicPlayer = new MusicPlayer(); // Initialize MusicPlayer directly
		style(width);
		initComponents();
		initLayout();
	}
	
	private void style(double width) {
		setMinSize(width, GameConstants.getPlayerPanelHeight());
		setAlignment(Pos.CENTER);
		setSpacing(GameConstants.getPlayerLabelSpacing());
	}
	
	 private void initComponents() {
	        // Player Name
	        playerName = new PlayerInfo("", new Emoji());
	        setPlayerName(player, player.getName());
	        playerName.setContentDisplay(ContentDisplay.LEFT);
	        playerName.setGraphicTextGap(10);

	        // Player Color
	        playerColor = new PlayerInfo("", new Checker(player.getColor(), true));

	        // Player Score
	        playerScore = new PlayerInfo("", null);
	        setPlayerScore(player, player.getScore());
	        playerScore.setFont(Font.loadFont(GameConstants.getFontInputStream(true, true), 24));

	        // Timer
	        timer = new GameplayTimer();
	        if(flag == true) {
	        // Music Controls
	        musicControls = new HBox(10); // Spacing between icons
	        musicControls.setAlignment(Pos.CENTER_RIGHT); // Align icons to the right
	        musicControls.setPadding(new Insets(0, -290, 0, 0)); // Add padding to push the icons to the right

	        musicControls.getChildren().addAll(
	                createMusicIcon("Model/img/icons/play.png", "Play", () -> {
	                    System.out.println("Play button clicked!");
	                    musicPlayer.play();
	                    System.out.println(musicPlayer.getStatus("play"));
	                }),
	                createMusicIcon("Model/img/icons/pause.png", "Pause", () -> {
	                    System.out.println("Pause button clicked!");
	                    musicPlayer.pause();
	                    System.out.println(musicPlayer.getStatus("pause"));
	                }),
	                createMusicIcon("Model/img/icons/next.png", "Next", () -> {
	                    System.out.println("Next button clicked!");
	                    musicPlayer.next();
	                    System.out.println(musicPlayer.getStatus("next"));
	                }),
	                createMusicIcon("Model/img/icons/prev.png", "Previous", () -> {
	                    System.out.println("Previous button clicked!");
	                    musicPlayer.prev();
	                    System.out.println(musicPlayer.getStatus("prev"));
	                }),
	                createMusicIcon("Model/img/icons/mute.png", "Mute", () -> {
	                    System.out.println("Mute button clicked!");
	                    MusicPlayer.muteVolume(true);
	                    System.out.println(musicPlayer.getStatus("mute"));
	                }),
	                createMusicIcon("Model/img/icons/unmute.png", "Unmute", () -> {
	                    System.out.println("Unmute button clicked!");
	                    MusicPlayer.muteVolume(false);
	                    System.out.println(musicPlayer.getStatus("unmute"));
	                }),
	                createMusicIcon("Model/img/icons/repeat.png", "Repeat", () -> {
	                    System.out.println("Repeat button clicked!");
	                    musicPlayer.repeat();
	                    System.out.println(musicPlayer.getStatus("repeat"));
	                }),
	                createMusicIcon("Model/img/icons/shuffle.png", "Shuffle", () -> {
	                    System.out.println("Shuffle button clicked!");
	                    musicPlayer.random();
	                    System.out.println(musicPlayer.getStatus("random"));
	                })
	            );
	        flag = false;
	        }
	    }
	
	    private void initLayout() {
	        // Add components to the player panel, ensuring no null child is added
	        if (playerName != null) getChildren().add(playerName);
	        if (playerColor != null) getChildren().add(playerColor);
	        if (playerScore != null) getChildren().add(playerScore);
	        if (timer != null) getChildren().add(timer);
	        if (musicControls != null && !musicControls.getChildren().isEmpty()) getChildren().add(musicControls);
	    }

	
	public void setPlayerName(Player player, String name) {
		player.setName(name);
		playerName.setText(player.getName());
	}
	 private ImageView createMusicIcon(String path, String tooltipText, Runnable action) {
	        InputStream input = getFile(path);
	        if (input == null) {
	            System.err.println("Error: Icon file not found - " + path);
	            return new ImageView(); // Return empty ImageView
	        }

	        Image iconImage = new Image(input);
	        ImageView iconView = new ImageView(iconImage);
	        iconView.setFitWidth(24); // Set icon width
	        iconView.setFitHeight(24); // Set icon height
	        iconView.setOnMouseClicked(e -> action.run()); // Execute action on click

	        try {
	            input.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return iconView;
	    }

	    private InputStream getFile(String path) {
	        InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
	        if (stream == null) {
	            System.err.println("Error: Resource not found - " + path);
	        }
	        return stream;
	    }
	public void setPlayerScore(Player player, int score) {
		player.setScore(score);
		playerScore.setText(player.getScore() + " / " + Settings.TOTAL_GAMES_IN_A_MATCH);
	}
	
	public void updateTotalGames() {
		setPlayerScore(player, player.getScore());
	}
	
	public void highlightChecker() {
		playerColor.getChecker().setHighlightImage();
	}
	public void unhighlightChecker() {
		playerColor.getChecker().setNormalImage();
	}
	
	public Emoji getEmoji() {
		return playerName.getEmoji();
	}
	
	public GameplayTimer getTimer() {
		return timer;
	}
	
	public void reset() {
		setPlayerName(player, player.getName());
		playerName.getEmoji().reset();
		unhighlightChecker();
		setPlayerScore(player, player.getScore());
	}
	public void resetTimer() {
		timer.reset();
	}
}
