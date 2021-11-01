/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Sep 30, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.model.actions;

import java.io.File;
import java.util.HashMap;

import com.google.gson.JsonObject;
import gamemaker.Constants;
import gamemaker.model.sprite.Sprite;
import gamemaker.utilities.JsonHelper;
import javafx.geometry.Point2D;

public class TranslateVelocityAction extends Action {

	private double translateX, translateY;

	public TranslateVelocityAction() {
		setSprite(null);
		setSpriteId(-99);
		setSoundFXFile(null);
		setTranslateX(-99);
		setTranslateY(-99);
	}

	public TranslateVelocityAction(Sprite sprite, File soundFXFile, double translateX, double translateY) {
		setSprite(sprite);
		setSpriteId(sprite.getId());
		setSoundFXFile(soundFXFile);
		setTranslateX(translateX);
		setTranslateY(translateY);
	}

	@Override
	public void execute(HashMap<String, Object> actionParams) {
		super.execute(actionParams);
		
		double currentVelocityX = sprite.getVelocity().getX();
		double currentVelocityY = sprite.getVelocity().getY();

		double newVelocityX = currentVelocityX > 0 ? currentVelocityX + translateX : currentVelocityX - translateX;
		double newVelocityY = currentVelocityY > 0 ? currentVelocityY + translateY : currentVelocityY - translateY;

		sprite.setVelocity(new Point2D(newVelocityX, newVelocityY));
	}

	@Override
	public String getUIInfo() {
		return Constants.TRANSLATE_PROPERTY +"-Velocity-" + translateX + "-" + translateY;
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
		sb.append(JsonHelper.Serializer.serializeDouble(Constants.TRANSLATE_X_KEY, getTranslateX()));
		sb.append(",");
		sb.append(JsonHelper.Serializer.serializeDouble(Constants.TRANSLATE_Y_KEY, getTranslateY()));

		if (encloseMyself) {
			sb.append("}");
		}

		return sb.toString();
	}

	@Override
	public void load(JsonObject jsonObject) {
		super.load(jsonObject);
		setTranslateX(JsonHelper.Deserializer.deserializeDouble(Constants.TRANSLATE_X_KEY, jsonObject));
		setTranslateY(JsonHelper.Deserializer.deserializeDouble(Constants.TRANSLATE_Y_KEY, jsonObject));
	}

	/************************************
	 * 
	 * Getters/Setter
	 *
	 ************************************/

	public double getTranslateX() {
		return translateX;
	}

	public void setTranslateX(double translateX) {
		this.translateX = translateX;
	}

	public double getTranslateY() {
		return translateY;
	}

	public void setTranslateY(double translateY) {
		this.translateY = translateY;
	}
}
