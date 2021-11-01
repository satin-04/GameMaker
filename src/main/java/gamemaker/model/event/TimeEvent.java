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

public class TimeEvent extends Event {

	// Must be Integer b/c HashMap doesn't work with primitives
	// See TimeEventHandler
	private Integer interval;

	public TimeEvent() {
		setSpriteId(-99);
		setInterval(-99);
		setAction(new DoNothingAction());
	}

	public TimeEvent(int spriteId, int interval, Action action) {
		setSpriteId(spriteId);
		setInterval(interval);
		setAction(action);
	}
	
	@Override
	public String getUIInfo() {
		return "Time Event";
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
		sb.append(JsonHelper.Serializer.serializeInt(Constants.INTERVAL_KEY, (int) interval));
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
		setInterval((Integer) JsonHelper.Deserializer.deserializeInt(Constants.INTERVAL_KEY, jsonObject));

		JsonObject actionJson = jsonObject.get(Constants.ACTION_KEY).getAsJsonObject();
		setAction(JsonHelper.Deserializer.deserializeAction(Constants.ACTION_CLASS_KEY, actionJson));
		getAction().load(actionJson);
	}

	/************************************
	 * 
	 * Getters/Setters
	 *
	 ************************************/

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}
}
