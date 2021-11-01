/**
 * @Author: Saurabh Gulati
 * @CreationDate: September 30, 2021
 * @Editors: Ethan Taylor Behar
 * @EditDate: Oct 2, 2021
 **/
package gamemaker.model.sound;

import java.io.File;

import gamemaker.Constants;
import javafx.application.Platform;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

public class SoundEngine {

	private MediaPlayer mediaPlayer;
	private Media backgroundTrackMedia;
	private File backgroundTrackFile;
	private boolean trackLoaded = false;
	private double volumne = 0.1f;

	// Singleton Pattern
	private static SoundEngine instance;

	private SoundEngine() {
	}

	public static SoundEngine getInstance() {
		if (instance == null) {
			instance = new SoundEngine();
		}
		return instance;
	}

	public void loadBackgroundTrack(File backgroundTrackFile) {
		if (backgroundTrackFile.getPath().compareToIgnoreCase(Constants.EMPTY_STRING) != 0) {
			this.backgroundTrackFile = backgroundTrackFile;
			this.backgroundTrackMedia = new Media(this.backgroundTrackFile.toURI().toString());
			this.mediaPlayer = new MediaPlayer(backgroundTrackMedia);
			this.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
			this.mediaPlayer.setVolume(getVolumne());
			this.trackLoaded = true;
		}
	}

	public void unloadBackgroundTrack() {
		if (backgroundTrackFile != null) {
			mediaPlayer = null;
			backgroundTrackMedia = null;
			backgroundTrackFile = null;
			trackLoaded = false;
		}
	}

	// For playing short sounds
	public void playAudio(File audioFile) {
		try
		{
			//Perform sound on a new thread
			Platform.runLater(() -> {
				AudioClip buzzer = new AudioClip(audioFile.toURI().toString());
				buzzer.setVolume(getVolumne());
				buzzer.play();
			});
		}
		catch (MediaException ex)
		{
			//Do nothing -- just print the exception
			ex.printStackTrace();
		}
	}

	// For playing sounds for longer duration
	public void play() {
		if (trackLoaded) {
			mediaPlayer.play();
		}
	}

	public void stop() {
		if (trackLoaded) {
			mediaPlayer.stop();
		}
	}

	public double getVolumne() {
		return volumne;
	}

	public void setVolumne(double volumne) {
		this.volumne = volumne;
	}

}
