package gamemaker.model.actions;

import java.io.File;
import java.util.HashMap;

import com.google.gson.JsonObject;

import gamemaker.Constants;
import gamemaker.GameMakerApplication;
import gamemaker.model.interfaces.Savable;
import gamemaker.model.sound.SoundEngine;
import gamemaker.model.sprite.Sprite;
import gamemaker.utilities.JsonHelper;

public abstract class Action implements Savable {

	protected Sprite sprite;
	protected int spriteId;
	protected File soundFXFile;

	public void execute(HashMap<String, Object> actionParams) {
		//Play the sound
		if (soundFXFile != null) {
			GameMakerApplication.logger.info(sprite.toString() + " playing sound fx " + soundFXFile.getAbsolutePath());
			SoundEngine.getInstance().playAudio(soundFXFile);
		}
	}

	public abstract String getUIInfo();

	@Override
	public String save(boolean encloseMyself) {
		StringBuilder sb = new StringBuilder();

		if (encloseMyself) {
			sb.append("{");
		}

		sb.append(JsonHelper.Serializer.serializeString(Constants.ACTION_CLASS_KEY, this.getClass().getTypeName()));
		sb.append(",");
		sb.append(JsonHelper.Serializer.serializeInt(Constants.SPRITE_ID_KEY, getSprite().getId()));
		sb.append(",");
		if (soundFXFile != null) {
			sb.append(JsonHelper.Serializer.serializeString(Constants.AUDIO_PATH_KEY,
					soundFXFile.getAbsolutePath().replace("\\", "\\\\")));
		} else {
			sb.append(JsonHelper.Serializer.serializeString(Constants.AUDIO_PATH_KEY, Constants.EMPTY_STRING));
		}

		if (encloseMyself) {
			sb.append("}");
		}

		return sb.toString();
	}

	@Override
	public void load(JsonObject jsonObject) {
		setSpriteId(JsonHelper.Deserializer.deserializeInt(Constants.SPRITE_ID_KEY, jsonObject));
		String soundFXFilePath = JsonHelper.Deserializer.deserializeString(Constants.AUDIO_PATH_KEY, jsonObject);
		if (soundFXFilePath.compareToIgnoreCase(Constants.EMPTY_STRING) != 0) {
			setSoundFXFile(new File(soundFXFilePath));
		}
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite newSprite) {
//		if (newSprite != null && newSprite.getId() != getSpriteId()) {
//			GameMakerApplication.logger.warn("Setting sprite in " + getClass().getTypeName()
//					+ " but new sprite id and variable spriteId are mismatched. This could be a issue! New sprite id: "
//					+ newSprite.getId() + ". Current sprite id: " + getSpriteId());
//		}
		this.sprite = newSprite;
	}

	public int getSpriteId() {
		return spriteId;
	}

	public void setSpriteId(int spriteId) {
		this.spriteId = spriteId;
	}

	public File getSoundFXFile() {
		return soundFXFile;
	}

	public void setSoundFXFile(File soundFXFile) {
		this.soundFXFile = soundFXFile;
	}
}
