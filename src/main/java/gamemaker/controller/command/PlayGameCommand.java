/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Sep 29, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.controller.command;

import gamemaker.model.Model;
import gamemaker.view.CanvasInputHandler;

public class PlayGameCommand extends Command {

	private Model model;
	private CanvasInputHandler canvasInputHandler;
	
	public PlayGameCommand(Model model, CanvasInputHandler canvasInputHandler) {
		setPushToUndoStack(false);
		this.model = model;
		this.canvasInputHandler = canvasInputHandler;
	}
	
	@Override
	public void execute() {
		canvasInputHandler.setIsDraggable(false);
		model.deselectCurrentSelectedSprite();
		model.playGame();
	}

	@Override
	public void undo() {
		// Nothing!	
	}

	@Override
	public void redo() {
		// Nothing!
	}
}