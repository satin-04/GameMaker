/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Oct 1, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.controller.command;

import gamemaker.model.Model;
import gamemaker.utilities.SaveManager;

public class SaveCommand extends Command {

	private Model model;
	
	public SaveCommand(Model model) {
		setPushToUndoStack(false);
		this.model = model;
	}
	
	@Override
	public void execute() {
		SaveManager.getInstance().save(model);
	}

	@Override
	public void undo() {
	}

	@Override
	public void redo() {
	}

}
