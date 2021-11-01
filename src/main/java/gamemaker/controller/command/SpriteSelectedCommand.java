/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Sep 29, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.controller.command;

import gamemaker.model.Model;
import gamemaker.model.sprite.Sprite;

public class SpriteSelectedCommand extends Command {

	private Model model;
	private Sprite selectedSprite;
	private int selectedSpriteId;
	private int previousSelectedSpriteId;

	public SpriteSelectedCommand(Model model, Sprite selectedSprite) {
		setPushToUndoStack(true);
		this.model = model;
		this.selectedSprite = selectedSprite;
		this.selectedSpriteId = selectedSprite.getId();
	}

	@Override
	public void execute() {
		Sprite previousSelectedSprite = model.getCurrentSelectedSprite();
		if (previousSelectedSprite != null) {
			previousSelectedSpriteId = previousSelectedSprite.getId();
		}

		model.updateCurrentSelectedSprite(selectedSprite);
	}

	@Override
	public void undo() {
		model.updateCurrentSelectedSprite(model.getSpriteById(previousSelectedSpriteId));
	}

	@Override
	public void redo() {
		model.updateCurrentSelectedSprite(model.getSpriteById(selectedSpriteId));
	}
}