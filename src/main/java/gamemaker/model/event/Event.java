/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Oct 2, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.model.event;

import gamemaker.GameMakerApplication;
import gamemaker.model.actions.Action;
import gamemaker.model.interfaces.Savable;

public abstract class Event implements Savable {

	protected int spriteId;
	protected Action action;
	
	public Action getAction() {
		return action;
	}
	
	public void setAction(Action action) {
		this.action = action;
	}
	
	public int getSpriteId() {
		return spriteId;
	}
	
	public void setSpriteId(int newSpriteId) {
		if (newSpriteId != getSpriteId()) {
			GameMakerApplication.logger.warn("Setting sprite in " + getClass().getTypeName()
					+ " but new sprite id and variable spriteId are mismatched. This could be a issue! New sprite id: "
					+ newSpriteId + ". Current sprite id: " + getSpriteId());
		}
		this.spriteId = newSpriteId;
	}
	public abstract String getUIInfo();
}
