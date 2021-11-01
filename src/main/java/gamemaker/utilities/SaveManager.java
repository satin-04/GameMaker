/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Oct 1, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import gamemaker.Constants.LoadState;
import gamemaker.Constants.SaveState;
import gamemaker.GameMakerApplication;
import gamemaker.model.Model;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

public class SaveManager {

	private File saveFile;
	private File loadFile;

	// Singleton Pattern
	private static SaveManager instance;

	private SaveManager() {
		saveFile = null;
	}

	public static SaveManager getInstance() {
		if (instance == null) {
			instance = new SaveManager();
		}
		return instance;
	}

	public boolean hasSaveFile() {
		if (saveFile != null) {
			return true;
		}
		return false;
	}

	public SaveState saveAs(Model model, Window appWindow) {
		// Makes it so only our file types are selectable
		ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Game Maker Application File", "*.gmaf");
			
		if (FileManager.getInstance().getSaveFile(appWindow, extensionFilter) == false) {
			// Meaning user failed to pick a file.
			// Quit the save process;
			return SaveState.NOFILE;
		}
		saveFile = FileManager.getInstance().getSelectedFile();

		// Build json string from SavableContainer
		String saveJson = model.save(true);
		GameMakerApplication.logger.info("Save Json Data:\n " + saveJson.toString());

		// Save string to file
		try {
			Files.write(saveFile.toPath(), saveJson.getBytes());
			return SaveState.SUCCESSFUL;
		} catch (IOException e) {
			GameMakerApplication.logger.fatal("Save action failed!");
			return SaveState.FAILURE;
		}
	}

	public SaveState save(Model model) {
		if (saveFile == null) {
			GameMakerApplication.logger.warn("Attempted to use Save without a saveFile. Use SaveAs or load first.");
			return SaveState.NOFILE;
		}

		// Build json string from SavableContainer
		String saveJson = model.save(true);

		// Save string to file
		try {
			Files.write(saveFile.toPath(), saveJson.getBytes());
			return SaveState.SUCCESSFUL;
		} catch (IOException e) {
			GameMakerApplication.logger.fatal("Save action failed!");
			return SaveState.FAILURE;
		}
	}

	public LoadState load(Model model, Window appWindow) {
		// Makes it so only our file types are selectable
		ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Game Maker Application File", "*.gmaf");
		
		if (FileManager.getInstance().getLoadFile(appWindow, extensionFilter) == false) {
			// Meaning user failed to pick a file.
			// Quit the save process;
			return LoadState.NOFILE;
		}
		loadFile = FileManager.getInstance().getSelectedFile();
		// So user can click save now
		saveFile = loadFile;
		
		byte[] jsonBytes;
		try {
			// Read in bytes and convert to string
			jsonBytes = Files.readAllBytes(loadFile.toPath());
			String jsonString = new String(jsonBytes);

			// Convert String to Json
			JsonObject loadedJson = JsonParser.parseString(jsonString).getAsJsonObject();

			// Give to model to load
			model.load(loadedJson);

			return LoadState.SUCCESSFUL;
		} catch (IOException e) {
			GameMakerApplication.logger.fatal("Load action failed!");
			return LoadState.FAILURE;
		}
	}
}
