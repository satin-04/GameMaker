/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Sep 29, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.controller.command;

import gamemaker.model.Model;
import gamemaker.view.CanvasInputHandler;

public class StopGameCommand extends Command {

	private Model model;
	private CanvasInputHandler canvasInputHandler;
	
	public StopGameCommand(Model model, CanvasInputHandler canvasInputHandler) {
		setPushToUndoStack(false);
		this.model = model;
		this.canvasInputHandler = canvasInputHandler;
	}
	
	@Override
	public void execute() {
		model.reselectCurrentSelectedSprite();
		model.stopGame();
		canvasInputHandler.setIsDraggable(true);
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