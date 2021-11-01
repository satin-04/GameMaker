/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Sep 30, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.model.actions;

import com.google.gson.JsonObject;
import gamemaker.Constants;
import gamemaker.model.sprite.Sprite;

import java.io.File;
import java.util.HashMap;

public class EndGameVictoryAction extends Action {

	public EndGameVictoryAction() {
		setSprite(null);
		setSpriteId(-99);
		setSoundFXFile(null);
	}

	public EndGameVictoryAction(Sprite sprite, File soundFXFile) {
		setSprite(sprite);
		setSpriteId(sprite.getId());
		setSoundFXFile(soundFXFile);
	}
	
	@Override
	public void execute(HashMap<String, Object> actionParams) {
	}

	@Override
	public String getUIInfo() {
		return Constants.END_GAME_VICTORY;
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
	}
}
