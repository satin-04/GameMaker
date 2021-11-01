/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Oct 2, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.utilities;

import java.io.File;

import gamemaker.GameMakerApplication;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

public class FileManager {

	private File selectedFile;

	// Singleton Pattern
	private static FileManager instance;

	private FileManager() {
	}

	public static FileManager getInstance() {
		if (instance == null) {
			instance = new FileManager();
		}
		return instance;
	}

	public boolean getSaveFile(Window appWindow, ExtensionFilter extensionFilter) {
		// Build FileChooser and ExtensionFilter
		FileChooser fileChooser = new FileChooser();

		// Apply the filter
		fileChooser.getExtensionFilters().add(extensionFilter);

		// Attempt to get a file from user.
		setSelectedFile(fileChooser.showSaveDialog(appWindow));

		// If nothing was selected return false
		if (getSelectedFile() == null) {
			GameMakerApplication.logger.info("No file was selected.");
			return false;
		}

		GameMakerApplication.logger.info("Selected save file: " + getSelectedFile().getAbsolutePath());
		return true;
	}

	public boolean getLoadFile(Window appWindow, ExtensionFilter extensionFilter) {
		// Build FileChooser and ExtensionFilter
		FileChooser fileChooser = new FileChooser();

		// Apply the filter
		fileChooser.getExtensionFilters().add(extensionFilter);

		// Attempt to get a file from user.
		setSelectedFile(fileChooser.showOpenDialog(appWindow));

		// If nothing was selected return false
		// Otherwise we selected a file so save it and return true.
		if (getSelectedFile() == null) {
			GameMakerApplication.logger.info("No file was selected.");
			return false;
		}

		GameMakerApplication.logger.info("Selected load file: " + getSelectedFile().getAbsolutePath());
		return true;
	}

	public void setSelectedFile(File selectedFile) {
		this.selectedFile = selectedFile;
	}

	public File getSelectedFile() {
		return selectedFile;
	}
}
