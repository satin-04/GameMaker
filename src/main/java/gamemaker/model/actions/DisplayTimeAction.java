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

public class DisplayTimeAction extends Action {

	public DisplayTimeAction() {
		setSprite(null);
		setSpriteId(-99);
		setSoundFXFile(null);
	}
	
	public DisplayTimeAction(Sprite sprite, File soundFXFile) {
		setSprite(sprite);
		setSpriteId(sprite.getId());
		setSoundFXFile(soundFXFile);
	}

	
	@Override
	public void execute(HashMap<String, Object> actionParams) {
		double finalTime = (double) actionParams.get(Constants.TIME_ELAPSED_KEY);
		
		int finalMins = (int) (finalTime / 60);
		int finalSecs = (int) (finalTime % 60);
		String formattedTime = String.format("Time: %02d:%02d", (finalMins), (finalSecs));

		getSprite().setText(formattedTime);
	}

	@Override
	public String getUIInfo() {
		return "Display Time";
	}

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
