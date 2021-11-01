/**
 * @Author: Maazin Jawad
 * @CreationDate: 10/2/2021
 * @Editors:
 * @EditedDate:
 **/
package gamemaker.model.actions;

import java.io.File;
import java.util.HashMap;

import com.google.gson.JsonObject;

import gamemaker.model.sprite.Sprite;

/**
 * Updates a Sprite's position using its current velocity
 */
public class MoveByVelocityAction extends Action {

    public MoveByVelocityAction() {
        setSprite(null);
        setSpriteId(-99);
        setSoundFXFile(null);
    }

    public MoveByVelocityAction(Sprite sprite, File soundFXFile) {
        setSprite(sprite);
        setSoundFXFile(soundFXFile);
    }

    @Override
    public void execute(HashMap<String, Object> actionParams) {
        super.execute(actionParams);

        //Update Sprite's position using its current velocity
        sprite.setPositionX(sprite.getPosition().getX() + sprite.getVelocity().getX());
        sprite.setPositionY(sprite.getPosition().getY() + sprite.getVelocity().getY());

    }

    @Override
    public String getUIInfo() {
        return "Move";
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