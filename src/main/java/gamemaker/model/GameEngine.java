/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Sep 26, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.model;

import java.io.File;
import java.util.ArrayList;

import gamemaker.GameMakerApplication;
import gamemaker.model.sound.SoundEngine;
import gamemaker.observer.pattern.Observable;
import gamemaker.observer.pattern.Observer;
import javafx.animation.AnimationTimer;

public class GameEngine extends AnimationTimer implements Observable {

	private float totalTime = 0;
	private long lastFrameTimeNanos = 0;
	private double secondsSincePreviousFrame = 0; // AKA TimeDelta
	private boolean isPaused = false;
	private boolean isPauseScheduled = false;
	private boolean isPlayScheduled = false;

	private ArrayList<Observer> observers;

	private GameBackground background;

	public GameEngine(GameBackground background) {
		observers = new ArrayList<Observer>();
		this.background = background;
		File gameTrackFile = this.background.getGameTrackFile();
		if (gameTrackFile != null) {
			SoundEngine.getInstance().loadBackgroundTrack(gameTrackFile);
		}
	}

	/************************************
	 * 
	 * Getters/Setters
	 * 
	 ************************************/

	/************************************
	 * 
	 * Game Loop Functionality
	 * 
	 ************************************/

	public boolean isPaused() {
		return isPaused;
	}

	public void pause() {
		if (!isPaused) {
			isPlayScheduled = false;
			isPauseScheduled = true;
		}
	}

	public void play() {
		if (isPaused) {
			super.start();
			isPlayScheduled = true;
			isPauseScheduled = false;
			SoundEngine.getInstance().play();
		}
	}

	@Override
	public void start() {
		super.start();
		isPlayScheduled = false;
		isPauseScheduled = true;
		isPaused = true;
		SoundEngine.getInstance().play();
	}

	@Override
	public void stop() {
		// Stop the game loop
		super.stop();

		// Restart pause/play flags
		isPaused = false;
		isPauseScheduled = false;
		isPlayScheduled = false;

		SoundEngine.getInstance().stop();
		SoundEngine.getInstance().unloadBackgroundTrack();

		// Clear game components
		observers.clear();
		observers = null;
	}

	@Override
	public void handle(long now) {
		if (isPauseScheduled) {
			isPaused = true;
			isPauseScheduled = false;
			secondsSincePreviousFrame = (now - lastFrameTimeNanos) / 1e9;
		}

		if (isPlayScheduled) {
			lastFrameTimeNanos = now;
			isPaused = false;
			isPlayScheduled = false;
		}

		if (!isPaused) {
			secondsSincePreviousFrame = (now - lastFrameTimeNanos) / 1e9;
			totalTime += secondsSincePreviousFrame;
			lastFrameTimeNanos = now;
			update();
		}
	}

	/************************************
	 * 
	 * Observer Interface
	 * 
	 ************************************/

	@Override
	public void registerObserver(Observer observer) {
		if (observers.contains(observer)) {
			GameMakerApplication.logger
					.error("Game Engine was told to add an SpriteObserver that was already registered.");
			return;
		}
		observers.add(observer);
	}

	@Override
	public void unregisterObserver(Observer observer) {
		if (!observers.contains(observer)) {
			GameMakerApplication.logger
					.error("Game Engine was told to remove an SpriteObserver that wasn't registered.");
			return;
		}
		observers.remove(observer);
	}

	@Override
	public void update() {
		// Update Objects
		for (Observer observer : observers) {
			observer.update(totalTime, secondsSincePreviousFrame);
		}
	}
}
