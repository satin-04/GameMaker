/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Oct 2, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.model.actions;

import java.io.File;
import java.util.HashMap;

import com.google.gson.JsonObject;

import gamemaker.Constants;
import gamemaker.Constants.CollisionSide;
import gamemaker.model.sprite.Sprite;
import javafx.geometry.Point2D;

public class ReflectAction extends Action {

	public ReflectAction() {
		setSprite(null);
		setSpriteId(-99);
		setSoundFXFile(null);
	}

	public ReflectAction(Sprite sprite, File soundFXFile) {
		setSprite(sprite);
		setSpriteId(sprite.getId());
		setSoundFXFile(soundFXFile);
	}

	@Override
	public void execute(HashMap<String, Object> actionParams) {
		super.execute(actionParams);

		CollisionSide side = (CollisionSide) actionParams.get(Constants.COLLISION_SIDE_KEY);
		if (side == Constants.CollisionSide.LEFT || side == Constants.CollisionSide.RIGHT) {
			sprite.setPosition((Point2D) actionParams.get(Constants.KISS_POSITION_KEY));
			sprite.setVelocity(
					new Point2D((sprite.getVelocity().getX() * -Constants.ONE), sprite.getVelocity().getY()));
		}

		if (side == Constants.CollisionSide.TOP || side == Constants.CollisionSide.BOTTOM) {
			sprite.setPosition((Point2D) actionParams.get(Constants.KISS_POSITION_KEY));
			sprite.setVelocity(
					new Point2D(sprite.getVelocity().getX(), (sprite.getVelocity().getY() * -Constants.ONE)));
		}
	}

	@Override
	public String getUIInfo() {
		return "Reflect";
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
