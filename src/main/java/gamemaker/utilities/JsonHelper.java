/**
 * @author: Ethan Taylor Behar
 * @CreationDate: Sep 25, 2021
 * @editors:
 **/
package gamemaker.utilities;

import com.google.gson.JsonObject;

import gamemaker.Constants.MouseButtonCode;
import gamemaker.Constants;
import gamemaker.GameMakerApplication;
import gamemaker.model.actions.*;
import gamemaker.model.sprite.CircleSprite;
import gamemaker.model.sprite.RectangleSprite;
import gamemaker.model.sprite.Sprite;
import gamemaker.model.sprite.TextSprite;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class JsonHelper {

	public static class Serializer {

		public static String serializeString(String key, String value) {
			//Special case: do not use quotation marks if the value is null
			if (value == null)
				return ("\"" + key + "\":null");
			// Example:
			// "class": "command.pattern.BallMoveCommand"

			return ("\"" + key + "\":\"" + value + "\"");
		}

		public static String serializeInt(String key, int value) {
			// Example:
			// "ball": 63
			return ("\"" + key + "\":" + value);
		}

		public static String serializeDouble(String key, double value) {
			// Example:
			// "ball": 63.000
			return ("\"" + key + "\":" + value);
		}

		public static String serializeFloat(String key, float value) {
			// Example:
			// "ball": 63.000
			return ("\"" + key + "\":" + value);
		}

		public static String serializePoint2D(String key, Point2D value) {
			// Example:
			// "undoPositionX": 588.1476196751464,
			// "undoPositionY": 583.1476196751464
			return ("\"" + key + "X" + "\":" + value.getX() + ",\"" + key + "Y" + "\":" + value.getY());
		}

		public static String serializeBoolean(String key, boolean value) {
			// Example:
			// "wasExecuted": false
			return ("\"" + key + "\":" + value);
		}
	}

	public static class Deserializer {

		public static String deserializeString(String key, JsonObject json) {
			//Special case to return null
			if (json.get(key).isJsonNull())
				return null;
			return json.get(key).getAsString();
		}

		public static int deserializeInt(String key, JsonObject json) {
			return json.get(key).getAsInt();
		}

		public static Double deserializeDouble(String key, JsonObject json) {
			return json.get(key).getAsDouble();
		}

		public static Float deserializeFloat(String key, JsonObject json) {
			return json.get(key).getAsFloat();
		}

		public static Point2D deserializePoint2D(String key, JsonObject json) {
			return new Point2D(json.get(key + 'X').getAsDouble(), json.get(key + 'Y').getAsDouble());
		}

		public static Boolean deserializeBoolean(String key, JsonObject json) {
			return json.get(key).getAsBoolean();
		}

		public static Color deserializeColor(String key, JsonObject json) {
			return Color.web(deserializeString(key, json));
		}

		public static Sprite deserializeSprite(String key, JsonObject json) {
			String shape = JsonHelper.Deserializer.deserializeString(key, json);

			if (shape.compareToIgnoreCase("gamemaker.model.sprite.CircleSprite") == Constants.ZERO) {
				return new CircleSprite();
			} else if (shape.compareToIgnoreCase("gamemaker.model.sprite.RectangleSprite") == Constants.ZERO) {
				return new RectangleSprite();
			} else if (shape.compareToIgnoreCase("gamemaker.model.sprite.TextSprite") == Constants.ZERO) {
				return new TextSprite();
			} else {
				GameMakerApplication.logger.error("Cannot convert class " + shape + " to a Sprite.");
				throw new UnsupportedOperationException("Cannot convert class " + shape + " to a Sprite.");
			}
		}

		public static Action deserializeAction(String key, JsonObject json) {
			String action = JsonHelper.Deserializer.deserializeString(key, json);
			if (action.compareToIgnoreCase("gamemaker.model.actions.DoNothingAction") == Constants.ZERO) {
				return new DoNothingAction();
			} else if (action.compareToIgnoreCase("gamemaker.model.actions.MoveByVelocityAction") == Constants.ZERO) {
				return new MoveByVelocityAction();
			} else if (action.compareToIgnoreCase("gamemaker.model.actions.MoveByForceAction") == Constants.ZERO) {
				return new MoveByForceAction();
			} else if (action.compareToIgnoreCase("gamemaker.model.actions.ReflectAction") == Constants.ZERO) {
				return new ReflectAction();
			} else if (action
					.compareToIgnoreCase("gamemaker.model.actions.TranslateVelocityAction") == Constants.ZERO) {
				return new TranslateVelocityAction();
			} else if (action
					.compareToIgnoreCase("gamemaker.model.actions.DisplayTimeAction") == Constants.ZERO) {
				return new DisplayTimeAction();
			}
			else if (action
					.compareToIgnoreCase("gamemaker.model.actions.CloneSpriteAction") == Constants.ZERO) {
				CloneSpriteAction newAction = new CloneSpriteAction();
				//Deserialize the Sprite and idOfSpriteToClone from the JSON
				Sprite sprite =  deserializeSprite(Constants.CLASS_KEY, (JsonObject) json.get(Constants.SPRITE_KEY));
				newAction.setSprite(sprite);
				newAction.setSpriteId(sprite.getId());
				int idOfSpriteToClone = deserializeInt(Constants.OTHER_SPRITE_ID_KEY, json);
				newAction.setIdOfSpriteToClone(idOfSpriteToClone);
				return newAction;
			}
			else {
				GameMakerApplication.logger.error("Cannot convert class " + action + " to an action.");
				throw new UnsupportedOperationException("Cannot convert class " + action + " to an action.");
			}
		}

		public static MouseButtonCode deserializeMouseButtonCode(String key, JsonObject json) {
			String code = JsonHelper.Deserializer.deserializeString(key, json);

			if (code.compareToIgnoreCase(MouseButtonCode.PRIMARY.toString()) == Constants.ZERO) {
				return MouseButtonCode.PRIMARY;
			} else if (code.compareToIgnoreCase(MouseButtonCode.SECONDARY.toString()) == Constants.ZERO) {
				return MouseButtonCode.SECONDARY;
			} else if (code.compareToIgnoreCase(MouseButtonCode.MIDDLE.toString()) == Constants.ZERO) {
				return MouseButtonCode.MIDDLE;
			} else {
				return MouseButtonCode.IGNORE;
			}
		}
	}
}
