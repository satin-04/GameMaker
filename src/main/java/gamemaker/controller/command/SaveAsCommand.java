/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Oct 1, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.controller.command;

import gamemaker.Constants.SaveState;
import gamemaker.model.Model;
import gamemaker.utilities.SaveManager;
import gamemaker.view.View;
import javafx.stage.Window;

public class SaveAsCommand extends Command {

	private Model model;
	private View view;
	private Window appWindow;
	
	public SaveAsCommand(Model model, View view, Window appWindow) {
		setPushToUndoStack(false);
		this.model = model;
		this.view = view;
		this.appWindow = appWindow;
	}

	@Override
	public void execute() {
		SaveState saveState = SaveManager.getInstance().saveAs(model, appWindow);
		if(saveState == SaveState.SUCCESSFUL) {
			view.enableButton(view.saveBtn);
		}
	}

	@Override
	public void undo() {
	}

	@Override
	public void redo() {
	}
}
