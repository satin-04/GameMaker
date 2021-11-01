/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Oct 2, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.controller.command;

import java.io.File;

import gamemaker.model.Model;
import javafx.scene.paint.Color;

public class UpdateBackgroundPropertiesCommand extends Command {

	private Model model;
	private Color color;
	private File backgroundTrack;

	public UpdateBackgroundPropertiesCommand(Model model, Color color, File backgroundTrack) {
		setPushToUndoStack(false);
		this.model = model;
		this.color = color;
		this.backgroundTrack = backgroundTrack;
	}

	@Override
	public void execute() {
		model.setBackgroundProperties(color, backgroundTrack);
	}

	@Override
	public void undo() {
	}

	@Override
	public void redo() {
	}
}