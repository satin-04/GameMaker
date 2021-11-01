/**
 * @Author: Pete Fyffe
 * @CreationDate: Oct 13, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.model.actions;

import com.google.gson.JsonObject;
import gamemaker.Constants;
import gamemaker.GameMakerApplication;
import gamemaker.model.Model;
import gamemaker.model.sprite.Sprite;
import gamemaker.utilities.CloneUtility;
import gamemaker.utilities.JsonHelper;

import java.io.File;
import java.util.HashMap;

/**
 * When executed, this clones a Sprite represented by the target ID.
 * The clone is added to the UI, given behaviors, and made subject to events.
 */
public class CloneSpriteAction extends Action {

    private Model model;
    private int idOfSpriteToClone;

    public CloneSpriteAction() {
        setSprite(null);
        setSpriteId(-99);
        setSoundFXFile(null);
        model = GameMakerApplication.getModel();
    }

    /**
     * Creates an action that, when executed, can clone a Sprite and place it into an active game.
     * @param sprite the Sprite that will appear to create the clone. The clone will be placed
     *               at this Sprite's location.
     * @param soundFXFile path to a sound file
     * @param model where the Sprite should be added to
     * @param idOfSpriteToClone the ID of the Sprite to clone. The user decides if this is different
     *                          from the ID of the `sprite` parameter.
     */
    public CloneSpriteAction(Sprite sprite, File soundFXFile, Model model, int idOfSpriteToClone) {
        setSprite(sprite);
        setSpriteId(sprite.getId());
        setSoundFXFile(soundFXFile);
        this.model = model;
        this.idOfSpriteToClone = idOfSpriteToClone;
    }

    public int getIdOfSpriteToClone() {
        return idOfSpriteToClone;
    }

    public void setIdOfSpriteToClone(int idOfSpriteToClone) {
        this.idOfSpriteToClone = idOfSpriteToClone;
    }




    /**
     * Creates a clone of the Sprite indicated by the idOfSpriteToClone at the location
     * of this
     * This adds the clone to the UI, assigns its behaviors, and makes it subject to events.
     * @param actionParams unused for cloning
     */
    @Override
    public void execute(HashMap<String, Object> actionParams) {
        super.execute(actionParams);

        if (model == null)
        {
            GameMakerApplication.logger.fatal("Could not clone because the Model given to CloneSpriteAction is null");
            return;
        }

        //Get the Sprite to clone
        Sprite original = model.getSpriteById(idOfSpriteToClone);
        if (original == null)
        {
            GameMakerApplication.logger.error("Could not clone because there is no Sprite with ID " + idOfSpriteToClone);
            return;
        }

        //Create a copy of the Sprite to clone
        Sprite clone = CloneUtility.cloneSprite(original);
        //Assign a new ID to the clone
        int newId = model.nextSpriteId();
        clone.setId(newId);

        //Move the clone to the creator Sprite
        clone.setPosition(sprite.getPosition());

        //Add the clone to the game
        //(This adds the clone to the UI, assigns its behaviors, and makes it subject to events)
        model.addCloneDuringGame(clone, idOfSpriteToClone);
    }

    @Override
    public String getUIInfo() {
        return "Clone Sprite";
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
        sb.append("\"" + Constants.SPRITE_KEY + "\":" + sprite.save(true));
        sb.append(",");
        sb.append(JsonHelper.Serializer.serializeInt(Constants.OTHER_SPRITE_ID_KEY, idOfSpriteToClone));

        if (encloseMyself) {
            sb.append("}");
        }

        System.out.println("SAVED CloneSpriteAction AS "+sb.toString());

        return sb.toString();
    }

    @Override
    public void load(JsonObject jsonObject) {
        super.load(jsonObject);
    }
}