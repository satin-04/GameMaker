/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Sep 30, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.model.actions;

import com.google.gson.JsonObject;
import gamemaker.Constants;
import gamemaker.model.sprite.Sprite;
import gamemaker.utilities.JsonHelper;
import javafx.geometry.Point2D;

import java.io.File;
import java.util.HashMap;

public class SetVelocityAction extends Action {

	private double x, y;

	public SetVelocityAction() {
		setSprite(null);
		setSpriteId(-99);
		setSoundFXFile(null);
		setX(-99);
		setY(-99);
	}

	public SetVelocityAction(Sprite sprite, File soundFXFile, double x, double y) {
		setSprite(sprite);
		setSpriteId(sprite.getId());
		setSoundFXFile(soundFXFile);
		setX(x);
		setY(y);
	}

	@Override
	public void execute(HashMap<String, Object> actionParams) {
		super.execute(actionParams);

		sprite.setVelocity(new Point2D(x, y));
	}

	@Override
	public String getUIInfo() {
		return Constants.SET_PROPERTY +"-" + Constants.VELOCITY_PROPERTY + "-" + x + "-" + y;
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
		sb.append(",");
		sb.append(JsonHelper.Serializer.serializeDouble(Constants.VELOCITY_X_KEY, getX()));
		sb.append(",");
		sb.append(JsonHelper.Serializer.serializeDouble(Constants.VELOCITY_Y_KEY, getY()));

		if (encloseMyself) {
			sb.append("}");
		}

		return sb.toString();
	}

	@Override
	public void load(JsonObject jsonObject) {
		super.load(jsonObject);
		setX(JsonHelper.Deserializer.deserializeDouble(Constants.VELOCITY_X_KEY, jsonObject));
		setY(JsonHelper.Deserializer.deserializeDouble(Constants.VELOCITY_Y_KEY, jsonObject));
	}

	/************************************
	 * 
	 * Getters/Setter
	 *
	 ************************************/

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}
