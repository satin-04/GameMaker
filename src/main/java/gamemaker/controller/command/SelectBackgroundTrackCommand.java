/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Oct 2, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.controller.command;

import java.io.File;

import gamemaker.Constants;
import gamemaker.utilities.FileManager;
import gamemaker.view.View;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

public class SelectBackgroundTrackCommand extends Command {

	private View view;
	private Window appWindow;
	private File selectedTrackFile;

	public SelectBackgroundTrackCommand(View view, Window appWindow) {
		setPushToUndoStack(false);
		this.view = view;
		this.appWindow = appWindow;
	}

	@Override
	public void execute() {
		ExtensionFilter audioFilter = new ExtensionFilter("Audio Files", Constants.AUDIO_TYPES);

		boolean successful = FileManager.getInstance().getLoadFile(appWindow, audioFilter);
		if (successful) {
			selectedTrackFile = FileManager.getInstance().getSelectedFile();
			view.backgroundTrackSelected(selectedTrackFile);
		} else {
			view.backgroundTrackSelected(null);
		}
	}

	@Override
	public void undo() {
	}

	@Override
	public void redo() {
	}
}