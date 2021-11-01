/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Sep 29, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.model.event;

import com.google.gson.JsonObject;

import gamemaker.Constants;
import gamemaker.model.actions.Action;
import gamemaker.model.actions.DoNothingAction;
import gamemaker.utilities.JsonHelper;
import javafx.scene.input.KeyCode;

public class KeyCodeEvent extends Event {

	private KeyCode inputTrigger;

	public KeyCodeEvent() {
		setSpriteId(-99);
		setInputTrigger(null);
		setAction(new DoNothingAction());
	}

	public KeyCodeEvent(int spriteId, KeyCode inputTrigger, Action action) {
		setSpriteId(spriteId);
		setInputTrigger(inputTrigger);
		setAction(action);
	}
	
	@Override
	public String getUIInfo() {
		return "KeyCode Event";
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

		sb.append(JsonHelper.Serializer.serializeInt(Constants.SPRITE_ID_KEY, spriteId));
		sb.append(",");
		sb.append(JsonHelper.Serializer.serializeString(Constants.INPUT_TRIGGER_KEY, inputTrigger.toString()));
		sb.append(",\"" + Constants.ACTION_KEY + "\":{");
		sb.append(action.save(false));
		sb.append("}");
		if (encloseMyself) {
			sb.append("}");
		}

		return sb.toString();
	}

	@Override
	public void load(JsonObject jsonObject) {
		setSpriteId(JsonHelper.Deserializer.deserializeInt(Constants.SPRITE_ID_KEY, jsonObject));
		setInputTrigger(
				KeyCode.valueOf(JsonHelper.Deserializer.deserializeString(Constants.INPUT_TRIGGER_KEY, jsonObject)));

		JsonObject actionJson = jsonObject.get(Constants.ACTION_KEY).getAsJsonObject();
		setAction(JsonHelper.Deserializer.deserializeAction(Constants.ACTION_CLASS_KEY, actionJson));
		getAction().load(actionJson);
	}

	/************************************
	 * 
	 * Getters/Setters
	 *
	 ************************************/

	public KeyCode getInputTrigger() {
		return inputTrigger;
	}

	public void setInputTrigger(KeyCode inputTrigger) {
		this.inputTrigger = inputTrigger;
	}
}
