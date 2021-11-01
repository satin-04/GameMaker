/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Oct 2, 2021
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

import static gamemaker.Constants.TELEPORT_OTHER;

public class TeleportOtherAction extends Action {

	public TeleportOtherAction() {
		setSprite(null);
		setSpriteId(-99);
		setSoundFXFile(null);
	}

	public TeleportOtherAction(Sprite sprite, File soundFXFile) {
		setSprite(sprite);
		setSpriteId(sprite.getId());
		setSoundFXFile(soundFXFile);
	}

	@Override
	public void execute(HashMap<String, Object> actionParams) {
		super.execute(actionParams);

		Sprite other =  (Sprite) actionParams.get(Constants.COLLISION_IMPACTEE_KEY);
		other.setPosition(new Point2D(sprite.getPosition().getX(), sprite.getPosition().getY() + sprite.getDimensions().getY()));
	}

	@Override
	public String getUIInfo() {
		return TELEPORT_OTHER;
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
