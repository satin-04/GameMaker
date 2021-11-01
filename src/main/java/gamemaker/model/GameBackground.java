/**
 * @author: Ethan Taylor Behar
 * @CreationDate: Sep 25, 2021
 * @editors:
 **/
package gamemaker.model;

import java.io.File;

import com.google.gson.JsonObject;

import gamemaker.Constants;
import gamemaker.model.interfaces.Savable;
import gamemaker.utilities.JsonHelper;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class GameBackground implements Savable {

	// Background object
	private Pane gameCanvas;
	// JavaFX background object for Pane
	private Background background;
	// JavaFX background Fill object for Background
	private BackgroundFill backgroundFill;
	private Color backgroundColor;
	private File gameTrackFile;

	public GameBackground() {
		// Create Canvas
		gameCanvas = new Pane();

		// Force to our game resolution
		gameCanvas.setMinWidth(Constants.GAME_CANVAS_WIDTH);
		gameCanvas.setMinHeight(Constants.GAME_CANVAS_HEIGHT);
		gameCanvas.setPrefWidth(Integer.MAX_VALUE); //makes editor canvas to auto-resize

		// Fill background with default
		backgroundColor = Constants.DEFAULT_BACKGROUND_COLOR;
		backgroundFill = new BackgroundFill(backgroundColor, null, null);
		background = new Background(backgroundFill);
		gameCanvas.setBackground(background);

		// For drag controller so we can ignore dragging this guy
		gameCanvas.setId(Constants.GAME_CANVAS_ID);

		gameTrackFile = null;
	}

	public Pane getGameCanvas() {
		return gameCanvas;
	}

	public File getGameTrackFile() {
		return gameTrackFile;
	}

	public void setGameTrackFile(File gameTrackFile) {
		this.gameTrackFile = gameTrackFile;
	}

	public Color getColor() {
		return backgroundColor;
	}

	public void setColor(Color color) {
		backgroundColor = color;
		backgroundFill = new BackgroundFill(backgroundColor, null, null);
		background = new Background(backgroundFill);
		gameCanvas.setBackground(background);
	}

	@Override
	public String save(boolean encloseMyself) {
		StringBuilder sb = new StringBuilder();

		if (encloseMyself) {
			sb.append("{");
		}

		sb.append(JsonHelper.Serializer.serializeString(Constants.COLOR_KEY, backgroundColor.toString()));
		sb.append(",");
		if (gameTrackFile != null) {
			sb.append(JsonHelper.Serializer.serializeString(Constants.AUDIO_PATH_KEY,
					gameTrackFile.getAbsolutePath().replace("\\", "\\\\")));
		} else {
			sb.append(JsonHelper.Serializer.serializeString(Constants.AUDIO_PATH_KEY, Constants.EMPTY_STRING));
		}

		if (encloseMyself) {
			sb.append("}");
		}

		return sb.toString();
	}

	@Override
	public void load(JsonObject jsonObject) {
		setColor(JsonHelper.Deserializer.deserializeColor(Constants.COLOR_KEY, jsonObject));
		String gameTrackFilePath = JsonHelper.Deserializer.deserializeString(Constants.AUDIO_PATH_KEY, jsonObject);
		if (gameTrackFilePath.compareToIgnoreCase(Constants.EMPTY_STRING) != 0) {
			setGameTrackFile(new File(gameTrackFilePath));
		}
	}
}
