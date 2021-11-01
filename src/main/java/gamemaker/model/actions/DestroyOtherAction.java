/**
 * @Author: Pete Fyffe
 * @CreationDate: Oct 12, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.model.actions;

import com.google.gson.JsonObject;
import gamemaker.Constants;
import gamemaker.Constants.CollisionSide;
import gamemaker.model.sprite.Sprite;
import javafx.geometry.Point2D;

import java.io.File;
import java.util.HashMap;

/**
 * This Action holds a reference to the Sprite that does the destroying (i.e. the destroyer).
 * But, it is responsible for destroying *other* Sprites (i.e. the destroyees/victims).
 * When a Sprite is destroyed from the game play, it is removed from the JavaFX UI container
 * and all the other places it's stored in throughout the game (such as the tick observers).
 * Thus, the Sprite will no longer be rendered, experience time events, collisions, key events, etc..
 */
public class DestroyOtherAction extends Action {

	public DestroyOtherAction() {
		setSprite(null);
		setSpriteId(-99);
		setSoundFXFile(null);
	}

	public DestroyOtherAction(Sprite sprite, File soundFXFile) {
		setSprite(sprite);
		setSpriteId(sprite.getId());
		setSoundFXFile(soundFXFile);
	}

	/**
	 * This destroys a Sprite whose ID is held in actionParams.
	 * @param actionParams a HashMap that contains the following pair:
	 *                     {Constants.SPRITE_ID_KEY, id of Sprite to destroy}.
	 */
	@Override
	public void execute(HashMap<String, Object> actionParams) {
		super.execute(actionParams);

		//Check who the collider is. Its ID should not be equal to the destroyer's ID.
		Integer idOfSpriteToDestroy = (Integer) actionParams.get(Constants.SPRITE_ID_KEY);
		if (idOfSpriteToDestroy != null && idOfSpriteToDestroy != spriteId)
		{
			//Remove the Sprite from the UI
			//TODO
			//Remove the Sprite from the GameEngine's observers
			//TODO
		}
	}

	@Override
	public String getUIInfo() {
		return Constants.DESTROY_OTHER;
	}

	/************************************
	 * 
	 * Savable Implementations
	 *
	 ************************************/

	@Override
	public String save(boolean encloseMyself) {
		StringBuilder sb = new StringBuilder();

		if (encloseMyself) {
			sb.append("{");
		}

		sb.append(super.save(false));
		
		if (encloseMyself) {
			sb.append("}");
		}

		return sb.toString();
	}

	@Override
	public void load(JsonObject jsonObject) {
		super.load(jsonObject);
	}
}
