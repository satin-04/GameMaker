/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Sep 29, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.controller.command;

import gamemaker.GameMakerApplication;
import gamemaker.model.Model;

public class SpriteReleasedCommand extends Command {

	private Model model;
	private double layoutX, layoutY;//, initialX, initialY;

	public SpriteReleasedCommand(Model model, double layoutX, double layoutY) { //, double initialX, double initialY) {
		setPushToUndoStack(true);
		this.model = model;
		this.layoutX = layoutX;
		this.layoutY = layoutY;
//		this.initialX = initialX;
//		this.initialY = initialY;
	}

	@Override
	public void execute() {
		GameMakerApplication.logger.info("Release sprite at: " + layoutX + " " + layoutY);
		model.releaseSelectedSprite(layoutX, layoutY);
	}

	@Override
	public void undo() {
	}

	@Override
	public void redo() {
	}
}