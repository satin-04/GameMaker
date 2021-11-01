package gamemaker.utilities;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gamemaker.Constants;
import gamemaker.GameMakerApplication;
import gamemaker.model.actions.Action;
import gamemaker.model.actions.MoveByForceAction;
import gamemaker.model.actions.MoveByVelocityAction;
import gamemaker.model.sprite.Sprite;
import javafx.geometry.Point2D;

/**
 * A class capable of cloning objects by turning them into GSON and back.
 */
public class CloneUtility
{
    private CloneUtility() {
        //Prevents instantiation
    }

    /**
     * Returns a copy of the target Sprite, leaving the original unaffected.
     * The new Sprite will share the ID of the original. The behaviors (event handlers) will not be cloned.
     * @param sprite the Sprite to clone
     * @return a new Sprite with every property the same as the original
     * @throws NullPointerException if the input Sprite is null
     */
    public static Sprite cloneSprite(Sprite sprite)
    {
        String cloneJson = sprite.save(true);
        Sprite clone = deserializeSprite(cloneJson);
        if (clone == null)
            GameMakerApplication.logger.error("Warning: Failed to clone Sprite "+sprite+". The resulting clone is null.");
        return clone;
    }

    /**
     * Parses a single Sprite from a JSON String.
     * @param spriteJson JSON containing a single Sprite object
     * @return the Sprite contained within the JSON or null if it cannot be read.
     */
    public static Sprite deserializeSprite(String spriteJson)
    {
        //Convert String to Json
        JsonObject jsonObject = JsonParser.parseString(spriteJson).getAsJsonObject();

        //Load the sprite
        Sprite loadedSprite = JsonHelper.Deserializer.deserializeSprite(Constants.CLASS_KEY, jsonObject);

        //Read JSON data and apply it to the new sprite
        loadedSprite.load(jsonObject);

        return loadedSprite;
    }

    public static Action cloneAction(Action action, Sprite newSubject)
    {
        //Convert to JSON String
        String cloneJson = action.save(true);
        //Convert String to JSON
        JsonObject jsonObject = JsonParser.parseString(cloneJson).getAsJsonObject();
        //Load the sprite
        Action loadedAction = JsonHelper.Deserializer.deserializeAction(Constants.ACTION_CLASS_KEY, jsonObject);
        if (loadedAction == null) {
            GameMakerApplication.logger.error("Failed to clone Action " + loadedAction + ". The resulting clone is null.");
            return null;
        }

        //Copy the properties of the original Action (needs improvement to be less hard-coded)
        if (loadedAction instanceof MoveByForceAction)
        {
            MoveByForceAction moveByForceAction = (MoveByForceAction) loadedAction;
            MoveByForceAction originalAction = (MoveByForceAction) action;
            moveByForceAction.setTranslateX(originalAction.getTranslateX());
            moveByForceAction.setTranslateY(originalAction.getTranslateY());
        }

        //Assign the action a Sprite
        loadedAction.setSprite(newSubject);
        if (newSubject != null) {
            loadedAction.setSpriteId(newSubject.getId());
            loadedAction.setSprite(newSubject);
        }

        return loadedAction;
    }
}
