/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Sep 29, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.controller.command;

import gamemaker.model.Model;

public class SpriteDraggedCommand extends Command {

	private Model model;
	private double translateX, translateY;

	public SpriteDraggedCommand(Model model, double translateX, double translateY) {
		setPushToUndoStack(false);
		this.model = model;
		this.translateX = translateX;
		this.translateY = translateY;

	}

	@Override
	public void execute() {
		model.dragCurrentSelectedSprite(translateX, translateY);
	}

	@Override
	public void undo() {
	}

	@Override
	public void redo() {
	}
}