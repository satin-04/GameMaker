package gamemaker.model.actions;

import com.google.gson.JsonObject;
import gamemaker.Constants;
import gamemaker.model.sprite.Sprite;
import gamemaker.utilities.JsonHelper;

import java.io.File;
import java.util.HashMap;

/**
 * @Author: Pete Fyffe
 * @CreationDate: Oct 9, 2021
 * @Editors:
 * @EditedDate:
 *
 * Updates a Sprite's position directly without checking or affecting its velocity.
 **/
public class MoveByForceAction extends Action
{
    private double translateX = 0;
    private double translateY = 0;
    public static final String MENU_TEXT = "Move by";

    /**
     * Empty constructor intended for use with deserialization.
     * The sprite and sound file will be null by default.
     */
    public MoveByForceAction() {
        setSprite(null);
        setSpriteId(-99);
        setSoundFXFile(null);
    }

    public MoveByForceAction(Sprite sprite, File soundFXFile, double translateX, double translateY)
    {
        if (sprite == null) throw new NullPointerException("The Sprite of a MoveAction cannot be null."); //TODO move to setSprite()
        setSprite(sprite);
        setSpriteId(sprite.getId());
        setSoundFXFile(soundFXFile);
        setTranslateX(translateX);
        setTranslateY(translateY);
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


    /************************************
     *
     * General methods
     *
     ************************************/

    /**
     * Moves the target Sprite
     */
    @Override
    public void execute(HashMap<String, Object> actionParams) {
        //super.execute(actionParams); //FIXME: When the sound if not found, the game freezes. So, I've commented this.

        //Update the Sprite's current position
        double newX = translateX + sprite.getPosition().getX();
        double newY = translateY + sprite.getPosition().getY();
        sprite.setPositionX(newX);
        sprite.setPositionY(newY);
    }

    @Override
    public String getUIInfo() {
        return Constants.TRANSLATE_PROPERTY + "-Position-" + translateX + "-" + translateY;
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
}