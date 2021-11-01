/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Sep 29, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.model.event;

import com.google.gson.JsonObject;

import gamemaker.Constants;
import gamemaker.Constants.MouseButtonCode;
import gamemaker.model.actions.Action;
import gamemaker.model.actions.DoNothingAction;
import gamemaker.utilities.JsonHelper;

public class MouseCodeEvent extends Event {

	private MouseButtonCode inputTrigger;

	public MouseCodeEvent() {
		setSpriteId(-99);
		setInputTrigger(null);
		setAction(new DoNothingAction());
	}

	public MouseCodeEvent(int spriteId, MouseButtonCode inputTrigger, Action action) {
		setSpriteId(spriteId);
		setInputTrigger(inputTrigger);
		setAction(action);
	}
	
	@Override
	public String getUIInfo() {
		return "MouseCode Event";
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
		setInputTrigger(JsonHelper.Deserializer.deserializeMouseButtonCode(Constants.INPUT_TRIGGER_KEY, jsonObject));

		JsonObject actionJson = jsonObject.get(Constants.ACTION_KEY).getAsJsonObject();
		setAction(JsonHelper.Deserializer.deserializeAction(Constants.ACTION_CLASS_KEY, actionJson));
		getAction().load(actionJson);
	}

	/************************************
	 * 
	 * Getters/Setters
	 *
	 ************************************/

	public MouseButtonCode getInputTrigger() {
		return inputTrigger;
	}

	public void setInputTrigger(MouseButtonCode inputTrigger) {
		this.inputTrigger = inputTrigger;
	}
}
