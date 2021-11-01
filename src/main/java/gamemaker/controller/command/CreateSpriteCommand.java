/**
 * @author: Ethan Taylor Behar
 * @CreationDate: Sep 25, 2021
 * @editors:
 **/
package gamemaker.controller.command;

import gamemaker.model.Model;
import gamemaker.model.sprite.Sprite;

public class CreateSpriteCommand extends Command {

	private Model model;
	private Sprite sprite; // do we need this?
	private int spriteId;
	// Sprite options(?)
	
	public CreateSpriteCommand(Model model) {
		this.model = model;
		spriteId = -1;
	}

	public Sprite getSprite() {
		return sprite;
	}

	@Override
	public void execute() {
		this.sprite = model.createSprite();
				
		// Make it the selected sprite
		model.updateCurrentSelectedSprite(this.sprite);
		
		spriteId = this.sprite.getId();
	}

	@Override
	public void undo() {
		// Delete the created sprite.
		model.deleteSprite(spriteId);
	}

	@Override
	public void redo() {
		// Recreate the sprite
		this.sprite = model.createSprite(spriteId);
		
		// Make it the selected sprite
		model.updateCurrentSelectedSprite(sprite);
	}
}
