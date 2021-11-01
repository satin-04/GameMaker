/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Sep 30, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.model.event;

import com.google.gson.JsonObject;

import gamemaker.Constants;
import gamemaker.Constants.CollisionType;
import gamemaker.model.actions.Action;
import gamemaker.utilities.JsonHelper;

public class CollisionEvent extends Event {

	private CollisionType collisionType;

	public CollisionEvent() {
		setSpriteId(-99);
	}

	public CollisionEvent(CollisionType type, Action action) {
		setCollisionType(type);
		setAction(action);
		setSpriteId(action.getSpriteId());
	}
	
	@Override
	public String getUIInfo() {
		return "Collision Event";
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
		sb.append(JsonHelper.Serializer.serializeString(Constants.COLLISION_TYPE_KEY, collisionType.toString()));
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
		setCollisionType(CollisionType
				.valueOf(JsonHelper.Deserializer.deserializeString(Constants.COLLISION_TYPE_KEY, jsonObject)));

		JsonObject actionJson = jsonObject.get(Constants.ACTION_KEY).getAsJsonObject();
		setAction(JsonHelper.Deserializer.deserializeAction(Constants.ACTION_CLASS_KEY, actionJson));
		getAction().load(actionJson);
	}

	/************************************
	 * 
	 * Getters/Setter
	 *
	 ************************************/

	public CollisionType getCollisionType() {
		return collisionType;
	}

	public void setCollisionType(CollisionType collisionType) {
		this.collisionType = collisionType;
	}
}