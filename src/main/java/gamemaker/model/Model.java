/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Sep 25, 2021
 * @Editors:
 **/
package gamemaker.model;

import java.io.File;
import java.util.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import gamemaker.Constants;
import gamemaker.GameMakerApplication;
import gamemaker.model.actions.Action;
import gamemaker.model.actions.MoveByVelocityAction;
import gamemaker.model.event.*;
import gamemaker.model.interfaces.Dumpable;
import gamemaker.model.interfaces.Savable;
import gamemaker.model.memento.SpriteMemento;
import gamemaker.model.sprite.CircleSprite;
import gamemaker.model.sprite.RectangleSprite;
import gamemaker.model.sprite.Sprite;
import gamemaker.model.sprite.TextSprite;
import gamemaker.utilities.CloneUtility;
import gamemaker.utilities.JsonHelper;
import gamemaker.view.View;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class Model implements Savable, Dumpable {

	// Main data
	private GameBackground background;
	private ArrayList<Sprite> allSprites;
	private Sprite currentSelectedSprite;
	private Sprite previousSelectedSprite;

	// Time Events
	private HashMap<Integer, LinkedList<TimeEvent>> timeEventsToSpriteMap;
	private TimeEventHandler timeEventHandler;

	// Input Events
	private HashMap<Integer, LinkedList<KeyCodeEvent>> keyEventsToSpriteMap;
	private HashMap<Integer, LinkedList<MouseCodeEvent>> mouseEventsToSpriteMap;
	private InputEventHandler inputEventHandler;

	// Collision Events
	private HashMap<Integer, LinkedList<CollisionEvent>> collisionEventsToSpriteMap;
	private WallCollisionEventHandler wallCollisionEventHandler;
	private SpriteCollisionEventHandler spriteCollisionEventHandler;

	// Like the sequencizer in the Book
	private GameEngine gameEngine;
	private SpriteMemento memento;

	// For tracking objects
	// Start at zero and count up for every created sprite
	// Generally a sprite id isn't reused if a sprite is deleted
	private int currentSpriteId = Constants.ZERO;

	// So model can tell view how/when to update
	private View view;

	public Model() {
		background = new GameBackground();
		init();
	}

	public Model(View view) {
		this.view = view;
		background = new GameBackground();
		init();
	}

	private void init() {
		memento = new SpriteMemento();
		allSprites = new ArrayList<Sprite>();
		timeEventsToSpriteMap = new HashMap<Integer, LinkedList<TimeEvent>>();
		keyEventsToSpriteMap = new HashMap<Integer, LinkedList<KeyCodeEvent>>();
		mouseEventsToSpriteMap = new HashMap<Integer, LinkedList<MouseCodeEvent>>();
		collisionEventsToSpriteMap = new HashMap<Integer, LinkedList<CollisionEvent>>();
	}

	/************************************
	 *
	 * Background Modification
	 *
	 ************************************/

	public void setBackgroundProperties(Color color, File backgroundTrack) {
		background.setColor(color);
		background.setGameTrackFile(backgroundTrack);
	}

	/************************************
	 * 
	 * Sprite Creation/Modification
	 * 
	 ************************************/

	// For 'Create Sprite' btn
	public Sprite createSprite() {
		if (currentSelectedSprite == null) {
			// Create default sprite
			Sprite newSprite = new CircleSprite(currentSpriteId);

			// Build Event Lists (empty for now)
			buildSpriteEventLists(newSprite);

			// Incrememt id counter
			currentSpriteId++;

			// Officially add sprite to our model
			addSpriteToModel(newSprite);

			// Return it
			return newSprite;
		} else {
			// Create sprite with currentSelectedSprite
			// This handles copying events too
			Sprite newSprite = createSpriteWithTemplate(currentSpriteId, currentSelectedSprite);

			// Build Event Lists (empty for now)
			buildSpriteEventLists(newSprite);

			// Incrememt id counter
			currentSpriteId++;

			// Officially add sprite to our model
			addSpriteToModel(newSprite);

			// Return it
			return newSprite;
		}
	}

	public Sprite createSpriteWithTemplate(int newSpriteId, Sprite templateSprite) {
		// Build new sprite based on template's shape
		Sprite newSprite = getSpriteBasedOnShape(newSpriteId, templateSprite.getShapeAsString());

		// Transfer info
		newSprite.transferProperties(templateSprite);
		// transferEvents(newSprite, templateSprite);

		// Override Label and Position
		// This could be really smart and do "templateSprite + labelInstances"
		newSprite.setLabel(Constants.DEFAULT_LABEL + newSpriteId);
		newSprite.setPosition(
				new Point2D(Constants.GAME_CANVAS_WIDTH / Constants.TWO, Constants.GAME_CANVAS_HEIGHT / Constants.TWO));

		return newSprite;
	}

	public Sprite getSpriteBasedOnShape(int newSpriteId, String shape) {
		Sprite newSprite;
		if (shape.compareToIgnoreCase(Constants.RECTANGLE) == Constants.ZERO) {
			newSprite = new RectangleSprite(newSpriteId);
		} else if (shape.compareToIgnoreCase(Constants.CIRCLE) == Constants.ZERO) {
			newSprite = new CircleSprite(newSpriteId);
		} else if (shape.compareToIgnoreCase(Constants.TEXT) == Constants.ZERO) {
			newSprite = new TextSprite(newSpriteId);
		} else {
			throw new UnsupportedOperationException(
					"No logic in model to handle converting shape " + shape + " into a valid sprite.");
		}
		return newSprite;
	}

	public void updateSelectedSpriteProperties(String label, Point2D position, Point2D dimensions, Point2D velocity,
			Color color, boolean visible, String shape, String text) {
		if (currentSelectedSprite != null) {

			// Change shape, if need be
			if (shape.compareToIgnoreCase(currentSelectedSprite.getShapeAsString()) != Constants.ZERO) {
				changeSpriteShape(shape);
			}

			// Apply new property values
			currentSelectedSprite.updateProperties(label, position, dimensions, velocity, color, visible, text);
		} else {
			GameMakerApplication.logger.fatal(
					"Trying to update properties of a sprite but currentSelectedSprite is null! Who are we updating?!");
			throw new UnsupportedOperationException(
					"Trying to update properties of a sprite but currentSelectedSprite is null! Who are we updating?!");
		}
	}

	public Sprite createSprite(int id) {
		// Create Sprite w/ specific id
		Sprite newSprite = new CircleSprite(id);

		// Build Event Lists (empty for now)
		buildSpriteEventLists(newSprite);

		// Do we want to do this?
		// Incrememt id counter
		// currentSpriteId++;

		// Officially add sprite to our model
		addSpriteToModel(newSprite);

		// Return it
		return newSprite;
	}

	public void changeSpriteShape(String shape) {
		// Make the new sprite based on the new shape
		// We'll recycle the oldSprite's id and use it in the newSprite
		Sprite oldSprite = currentSelectedSprite;
		Sprite newSprite = getSpriteBasedOnShape(oldSprite.getId(), shape);

		// Transfer info
		newSprite.transferProperties(oldSprite);

		// Is this necessary?
		transferEvents(newSprite, oldSprite);

		// Officially remove sprite to our model
		removeSpriteFromModel(oldSprite);

		// Officially add sprite to our model
		addSpriteToModel(newSprite);

		// Make 'em the selected sprite since the old one was!
		setSelectedSprite(newSprite);
	}

	public void deleteSprite(int id) {
		// Find Sprite
		Sprite deleteSprite = getSpriteById(id);

		// Delete
		if (deleteSprite != null) {
			// Officially remove sprite to our model
			removeSpriteFromModel(deleteSprite);
		} else {
			// Log not found as error.
			GameMakerApplication.logger.error("Attempted to delete a sprite that doesn't exist.");
		}

	}

	public void addSpriteToModel(Sprite addSprite) {
		// Be proactive and check for potential erros
		// Prevents exceptions from being thrownl
		boolean allSpriteContains = allSprites.contains(addSprite);
		boolean gameCanvasContains = background.getGameCanvas().getChildren().contains(addSprite.getShape());
		if (allSpriteContains && gameCanvasContains) {
			GameMakerApplication.logger
					.fatal("Trying to add a sprite to the model but the model and game canvas already have it!");
		} else if (allSpriteContains && !gameCanvasContains) {
			// Log sync issue as fatal
			GameMakerApplication.logger.fatal("MODEL SYNC ISSUE - See below message");
			GameMakerApplication.logger.fatal(
					"Trying to add a sprite to the model but the model already has it and the game canvas does not!");
			GameMakerApplication.logger.fatal("Removing sprite from allSprites list.");

			// Get rid of it.
			allSprites.remove(addSprite);
		} else if (!allSpriteContains && gameCanvasContains) {
			// Log sync issue as fatal
			GameMakerApplication.logger.fatal("MODEL SYNC ISSUE - See below message");
			GameMakerApplication.logger.fatal(
					"Trying to add a sprite to the model but the model does not have it and the game canvas does!");
			GameMakerApplication.logger.fatal("Removing sprite from game canvas.");

			// Get rid of it.
			background.getGameCanvas().getChildren().remove(addSprite.getShape());
		} else {
			// ALL GOOD proceed as normal

			// Add to allSprites
			allSprites.add(addSprite);
			// Add to gameCanvas
			background.getGameCanvas().getChildren().add(addSprite.getShape());
		}
	}

	public void addCloneDuringGame(Sprite clone, int originalId)
	{
		//IDs cannot be the same between the two Sprites
		int cloneId = clone.getId();
		if (cloneId == originalId)
		{
			throw new IllegalArgumentException("The clone's ID cannot be the same as the ID of the original Sprite." +
					" Refusing to add the cloned Sprite to the Model." +
					" Issue the clone a new ID to resolve this problem.");
		}

		//Add to gameCanvas
		background.getGameCanvas().getChildren().add(clone.getShape());

		//Add actions to every handler...
		//Clone all TimeEvents from the original Sprite but adjust the target ID to the cloneId
		List<TimeEvent> originalTimeEventList = timeEventsToSpriteMap.get(originalId);
		List<TimeEvent> clonedTimeEventList = new LinkedList<>();
		for (TimeEvent originalEvent : originalTimeEventList)
		{
			//Clone the Action
			Action clonedAction = CloneUtility.cloneAction(originalEvent.getAction(), clone);

			//Clone the TimeEvent
			TimeEvent clonedTimeEvent = new TimeEvent(cloneId, originalEvent.getInterval(), clonedAction);
			clonedTimeEvent.setSpriteId(cloneId);
			clonedTimeEventList.add(clonedTimeEvent);
		}
		timeEventHandler.addSprite(clonedTimeEventList);

		//Clone all KeyCodeEvents
		List<KeyCodeEvent> originalKeyCodeEventList = keyEventsToSpriteMap.get(originalId);
		List<KeyCodeEvent> clonedKeyCodeEventList = new LinkedList<>();
		for (KeyCodeEvent originalEvent : originalKeyCodeEventList)
		{
			//Clone the Action
			Action clonedAction = CloneUtility.cloneAction(originalEvent.getAction(), clone);
			clonedAction.setSpriteId(cloneId);
			clonedAction.setSprite(clone);

			//Clone the Event
			KeyCodeEvent clonedEvent = new KeyCodeEvent(cloneId, originalEvent.getInputTrigger(), clonedAction);
			clonedEvent.setSpriteId(cloneId);
			clonedKeyCodeEventList.add(clonedEvent);
		}
		inputEventHandler.addSprite(clonedKeyCodeEventList);

		//Clone all CollisionEvents
		List<CollisionEvent> originalCollisionEventList = collisionEventsToSpriteMap.get(originalId);
		List<CollisionEvent> clonedCollisionEventList = new LinkedList<>();
		for (CollisionEvent originalEvent : originalCollisionEventList)
		{
			//Clone the Action
			Action clonedAction = CloneUtility.cloneAction(originalEvent.getAction(), clone);
			clonedAction.setSpriteId(cloneId);
			clonedAction.setSprite(clone);

			//Clone the Event
			CollisionEvent clonedEvent = new CollisionEvent(originalEvent.getCollisionType(), clonedAction);
			clonedEvent.setSpriteId(cloneId);
			clonedCollisionEventList.add(clonedEvent);
		}
		spriteCollisionEventHandler.addSprite(clone, clonedCollisionEventList);
		wallCollisionEventHandler.addSprite(clonedCollisionEventList);
	}






	public void removeSpriteFromModel(Sprite removeSprite) {
		// Be proactive and check for potential erros
		// Prevents exceptions from being thrownl
		boolean allSpriteContains = allSprites.contains(removeSprite);
		boolean gameCanvasContains = background.getGameCanvas().getChildren().contains(removeSprite.getShape());
		if (!allSpriteContains && !gameCanvasContains) {
			GameMakerApplication.logger
					.fatal("Trying to remove a sprite from the model but the model has no record of it.");
		} else if (allSpriteContains && !gameCanvasContains) {
			// Log sync issue as fatal
			GameMakerApplication.logger.fatal("MODEL SYNC ISSUE - See below message");
			GameMakerApplication.logger.fatal(
					"Trying to remove a sprite from the model but the model has it and the game canvas does not!");
			GameMakerApplication.logger.fatal("Removing sprite from allSprites list.");

			// Get rid of it.
			allSprites.remove(removeSprite);
		} else if (!allSpriteContains && gameCanvasContains) {
			// Log sync issue as fatal
			GameMakerApplication.logger.fatal("MODEL SYNC ISSUE - See below message");
			GameMakerApplication.logger.fatal(
					"Trying to remove a sprite from the model but the model does not have it and the game canvas does!");
			GameMakerApplication.logger.fatal("Removing sprite from game canvas.");

			// Get rid of it.
			background.getGameCanvas().getChildren().remove(removeSprite.getShape());
		} else {
			// ALL GOOD proceed as normal

			GameMakerApplication.logger.info("Removing sprite from allSprites and game canvas.");

			// Add to allSprites
			allSprites.remove(removeSprite);
			// Add to gameCanvas
			background.getGameCanvas().getChildren().remove(removeSprite.getShape());
		}
	}

	/************************************
	 * 
	 * Event Handling/Modification
	 * 
	 ************************************/

	private void buildSpriteEventLists(Sprite newSprite) {
		timeEventsToSpriteMap.put(newSprite.getId(), new LinkedList<TimeEvent>());

		timeEventsToSpriteMap.get(newSprite.getId())
				.add(new TimeEvent(newSprite.getId(), -1, new MoveByVelocityAction(newSprite, null)));

		keyEventsToSpriteMap.put(newSprite.getId(), new LinkedList<KeyCodeEvent>());
		mouseEventsToSpriteMap.put(newSprite.getId(), new LinkedList<MouseCodeEvent>());
		collisionEventsToSpriteMap.put(newSprite.getId(), new LinkedList<CollisionEvent>());
	}

	private void transferEvents(Sprite receiver, Sprite sender) {
		if (receiver.getId() == sender.getId()) {
			GameMakerApplication.logger.warn("Receiver and sender have same ids. Is this by design?");
		}

		Iterator<TimeEvent> senderTimeEventsIterator = timeEventsToSpriteMap.get(sender.getId()).iterator();
		while (senderTimeEventsIterator.hasNext()) {
			TimeEvent next = senderTimeEventsIterator.next();

			next.setSpriteId(receiver.getId());
			next.getAction().setSprite(receiver);
			next.getAction().setSpriteId(receiver.getId());
		}

		Iterator<KeyCodeEvent> senderKeyEventsIterator = keyEventsToSpriteMap.get(sender.getId()).iterator();
		while (senderKeyEventsIterator.hasNext()) {
			KeyCodeEvent next = senderKeyEventsIterator.next();

			next.setSpriteId(receiver.getId());
			next.getAction().setSprite(receiver);
			next.getAction().setSpriteId(receiver.getId());
		}

		Iterator<MouseCodeEvent> senderMouseEventsIterator = mouseEventsToSpriteMap.get(sender.getId()).iterator();
		while (senderMouseEventsIterator.hasNext()) {
			MouseCodeEvent next = senderMouseEventsIterator.next();

			next.setSpriteId(receiver.getId());
			next.getAction().setSprite(receiver);
			next.getAction().setSpriteId(receiver.getId());
		}

		Iterator<CollisionEvent> senderCollisionEventsIterator = collisionEventsToSpriteMap.get(sender.getId())
				.iterator();
		while (senderCollisionEventsIterator.hasNext()) {
			CollisionEvent next = senderCollisionEventsIterator.next();

			next.setSpriteId(receiver.getId());
			next.getAction().setSprite(receiver);
			next.getAction().setSpriteId(receiver.getId());
		}
	}

	public void updateSelectedSpritesEvents(LinkedList<TimeEvent> timeEvents, LinkedList<KeyCodeEvent> keyEvents,
			LinkedList<MouseCodeEvent> mouseEvents, LinkedList<CollisionEvent> collisionEvents) {
		timeEventsToSpriteMap.put(currentSelectedSprite.getId(), timeEvents);

		timeEventsToSpriteMap.get(currentSelectedSprite.getId())
				.add(new TimeEvent(currentSelectedSprite.getId(), -1, new MoveByVelocityAction(currentSelectedSprite, null)));

		keyEventsToSpriteMap.put(currentSelectedSprite.getId(), keyEvents);
		mouseEventsToSpriteMap.put(currentSelectedSprite.getId(), mouseEvents);
		collisionEventsToSpriteMap.put(currentSelectedSprite.getId(), collisionEvents);
	}

	/************************************
	 * 
	 * Application Handling
	 * 
	 ************************************/

	public void deselectCurrentSelectedSprite() {
		if (currentSelectedSprite != null) {
			currentSelectedSprite.setSelected(false);
		}
		// else nothing to deselect
	}

	public void reselectCurrentSelectedSprite() {
		if (currentSelectedSprite != null) {
			currentSelectedSprite.setSelected(true);
		}
		// else nothing to reselect
	}

	public boolean isSpriteTheSelectedSprite(Sprite newSelectedSprite) {
		// Can't be if current is null
		if (currentSelectedSprite == null) {
			return false;
			// Obviously not
		} else if (currentSelectedSprite != newSelectedSprite) {
			return false;
			// Must be the same sprite
		} else {
			return true;
		}
	}

	public void dragCurrentSelectedSprite(double translateX, double translateY) {
		Shape currentShape = getCurrentSelectedSprite().getShape();

		// Move based on translation
		currentShape.setTranslateX(translateX);
		currentShape.setTranslateY(translateY);

		// Update view with new position
		view.updatePositionBasedOnDrag(currentSelectedSprite);
	}

	public void releaseSelectedSprite(double layoutX, double layoutY) {
		Shape target = getCurrentSelectedSprite().getShape();

		// Commit changes to position
		getCurrentSelectedSprite().setPosition(new Point2D(layoutX, layoutY));
//		target.setLayoutY(layoutY);

		// Clear changes from TranslateX and TranslateY
		target.setTranslateX(Constants.ZERO);
		target.setTranslateY(Constants.ZERO);

		// Update view with new position
		view.updatePosition(currentSelectedSprite);
	}

	/************************************
	 * 
	 * GameEngine Controls
	 * 
	 ************************************/

	// Maybe some parameters here?
	public void playGame() {
		// Save sprite state
		memento = new SpriteMemento();
		memento.storeSpritesState(allSprites.iterator());

		// Create engine
		gameEngine = new GameEngine(background);

		// Create event handlers
		timeEventHandler = new TimeEventHandler(timeEventsToSpriteMap.keySet().iterator(), timeEventsToSpriteMap);
		gameEngine.registerObserver(timeEventHandler);

		inputEventHandler = new InputEventHandler(background.getGameCanvas(), keyEventsToSpriteMap.keySet().iterator(),
				keyEventsToSpriteMap, mouseEventsToSpriteMap.keySet().iterator(), mouseEventsToSpriteMap);
		gameEngine.registerObserver(inputEventHandler);

		wallCollisionEventHandler = new WallCollisionEventHandler(collisionEventsToSpriteMap.keySet().iterator(),
				collisionEventsToSpriteMap, background);
		gameEngine.registerObserver(wallCollisionEventHandler);

		spriteCollisionEventHandler = new SpriteCollisionEventHandler(collisionEventsToSpriteMap, allSprites);
		gameEngine.registerObserver(spriteCollisionEventHandler);

		// Run the game engine
		gameEngine.start();
		gameEngine.play();
	}

	public void stopGame() {
		// Stop the game engine
		gameEngine.stop();
		gameEngine = null;

		// Restore sprite state
		memento.restoreSpritesState(allSprites.iterator());
		memento.dump();
		//Restore visual appearance
		background.getGameCanvas().getChildren().clear();
		for (Sprite restoredSprite : allSprites) {
			background.getGameCanvas().getChildren().add(restoredSprite.getShape());
		}

		// Clear the handlers and null them out
		timeEventHandler.dump();
		timeEventHandler = null;
		inputEventHandler.dump();
		inputEventHandler = null;
		wallCollisionEventHandler.dump();
		wallCollisionEventHandler = null;
		spriteCollisionEventHandler.dump();
		spriteCollisionEventHandler = null;
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

		// Saving background
		sb.append("\"" + Constants.BACKGROUND_INFO_KEY + "\":{");
		sb.append(background.save(false));
		sb.append("},");

		// Saving all sprites
		sb.append("\"" + Constants.ALL_SPRITES_KEY + "\":[");
		Iterator<Sprite> spriteIterator = allSprites.iterator();
		while (spriteIterator.hasNext()) {
			Sprite currentSprite = spriteIterator.next();

			sb.append(currentSprite.save(true));
			GameMakerApplication.logger.info("Saving sprite: " + currentSprite.getLabel() + " with color: "
					+ currentSprite.getColor().toString());
			if (spriteIterator.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("],");

		// Saving all Time Events
		sb.append("\"" + Constants.ALL_TIME_EVENTS_KEY + "\":[");
		Iterator<Integer> timeKeysIterator = timeEventsToSpriteMap.keySet().iterator();
		while (timeKeysIterator.hasNext()) {
			Integer nextKey = timeKeysIterator.next();
			Iterator<TimeEvent> timeEventIterator = timeEventsToSpriteMap.get(nextKey).iterator();
			while (timeEventIterator.hasNext()) {
				TimeEvent next = timeEventIterator.next();

				sb.append(next.save(true));
				if (timeEventIterator.hasNext()) {
					sb.append(",");
				}
			}

			if (timeKeysIterator.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("],");

		// Saving all Key Events
		sb.append("\"" + Constants.ALL_KEY_EVENTS_KEY + "\":[");
		// This is totally not confusing...
		// KeyCode as in a pressed keyboard key
		// KeysIterator as in the keys of a HashMap specfically keyEventsToSpriteMap's
		// keys
		Iterator<Integer> keyCodeKeysIterator = keyEventsToSpriteMap.keySet().iterator();

		boolean firstKey = true;
		while (keyCodeKeysIterator.hasNext()) {
			Integer nextKey = keyCodeKeysIterator.next();
			if (keyEventsToSpriteMap.get(nextKey).size() > 0) {
				if (firstKey) {
					firstKey = false;
				} else {
					if (keyCodeKeysIterator.hasNext()) {
						sb.append(",");
					}
				}

				Iterator<KeyCodeEvent> keyEventIterator = keyEventsToSpriteMap.get(nextKey).iterator();
				while (keyEventIterator.hasNext()) {
					KeyCodeEvent next = keyEventIterator.next();

					sb.append(next.save(true));
					if (keyEventIterator.hasNext()) {
						sb.append(",");
					}
				}

				if (keyCodeKeysIterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("],");

		// Saving all Mouse Events
		sb.append("\"" + Constants.ALL_MOUSE_EVENTS_KEY + "\":[");
		Iterator<Integer> mouseKeysIterator = mouseEventsToSpriteMap.keySet().iterator();

		firstKey = true;
		while (mouseKeysIterator.hasNext()) {
			Integer nextKey = mouseKeysIterator.next();
			if (mouseEventsToSpriteMap.get(nextKey).size() > 0) {
				if (firstKey) {
					firstKey = false;
				} else {
					if (mouseKeysIterator.hasNext()) {
						sb.append(",");
					}
				}

				Iterator<MouseCodeEvent> mouseEventIterator = mouseEventsToSpriteMap.get(nextKey).iterator();
				while (mouseEventIterator.hasNext()) {
					MouseCodeEvent next = mouseEventIterator.next();

					sb.append(next.save(true));
					if (mouseEventIterator.hasNext()) {
						sb.append(",");
					}
				}

				if (mouseKeysIterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("],");

		// Saving all Collision Events
		sb.append("\"" + Constants.ALL_COLLISION_EVENTS_KEY + "\":[");
		Iterator<Integer> collisionKeysIterator = collisionEventsToSpriteMap.keySet().iterator();

		firstKey = true;
		while (collisionKeysIterator.hasNext()) {

			Integer nextKey = -1;

			if (firstKey) {
				firstKey = false;
				nextKey = collisionKeysIterator.next();
			} else {
				if (collisionKeysIterator.hasNext()) {
					nextKey = collisionKeysIterator.next();
					if (collisionEventsToSpriteMap.get(nextKey - 1).size() > 0 && collisionEventsToSpriteMap.get(nextKey).size() > 0) {
						GameMakerApplication.logger.info(nextKey + " Comma 1");
						sb.append(",");
					}
				}
			}

			if (collisionEventsToSpriteMap.get(nextKey).size() > 0) {
				Iterator<CollisionEvent> collisionEventIterator = collisionEventsToSpriteMap.get(nextKey).iterator();
				while (collisionEventIterator.hasNext()) {
					CollisionEvent next = collisionEventIterator.next();

					sb.append(next.save(true));
					if (collisionEventIterator.hasNext()) {
						GameMakerApplication.logger.info(nextKey + " Comma 1");
						sb.append(",");
					}
				}
			}
		}
		sb.append("]");

		if (encloseMyself) {
			sb.append("}");
		}

		return sb.toString();
	}

	@Override
	public void load(JsonObject jsonObject) {
		try {
			view.updateBasedOnSelectedSprite(null, null, null, null, null);
		}catch (NullPointerException e){
			GameMakerApplication.logger.fatal("ERROR: Could not load Sprite event handlers inside Model.load() because the View was null");
		}
		dump();

		timeEventsToSpriteMap = new HashMap<Integer, LinkedList<TimeEvent>>();
		keyEventsToSpriteMap = new HashMap<Integer, LinkedList<KeyCodeEvent>>();
		mouseEventsToSpriteMap = new HashMap<Integer, LinkedList<MouseCodeEvent>>();
		collisionEventsToSpriteMap = new HashMap<Integer, LinkedList<CollisionEvent>>();

		JsonElement backgroundJson = jsonObject.get(Constants.BACKGROUND_INFO_KEY);
		background.load(backgroundJson.getAsJsonObject());

		// Rebuild each sprite
		for (JsonElement item : jsonObject.get(Constants.ALL_SPRITES_KEY).getAsJsonArray()) {
			// Cast JsonElement to JsonObject - easier to work with.
			JsonObject spriteJson = item.getAsJsonObject();

			// Load the sprite
			Sprite loadedSprite = JsonHelper.Deserializer.deserializeSprite(Constants.CLASS_KEY, spriteJson);

			// Load info into sprite
			loadedSprite.load(spriteJson);

			// Create it's event lists
			buildSpriteEventLists(loadedSprite);

			addSpriteToModel(loadedSprite);

			currentSpriteId = loadedSprite.getId() + Constants.ONE;
		}

		for (JsonElement item : jsonObject.get(Constants.ALL_TIME_EVENTS_KEY).getAsJsonArray()) {
			// Cast JsonElement to JsonObject - easier to work with.
			JsonObject timeJson = item.getAsJsonObject();

			TimeEvent event = new TimeEvent();
			event.load(timeJson);
			Sprite eventSprite = getSpriteById(event.getSpriteId());
			event.getAction().setSprite(eventSprite);

			timeEventsToSpriteMap.get(eventSprite.getId()).add(event);
		}

		for (JsonElement item : jsonObject.get(Constants.ALL_KEY_EVENTS_KEY).getAsJsonArray()) {
			// Cast JsonElement to JsonObject - easier to work with.
			JsonObject keyJson = item.getAsJsonObject();

			KeyCodeEvent event = new KeyCodeEvent();
			event.load(keyJson);
			Sprite eventSprite = getSpriteById(event.getSpriteId());
			event.getAction().setSprite(eventSprite);

			keyEventsToSpriteMap.get(eventSprite.getId()).add(event);
		}

		for (JsonElement item : jsonObject.get(Constants.ALL_MOUSE_EVENTS_KEY).getAsJsonArray()) {
			// Cast JsonElement to JsonObject - easier to work with.
			JsonObject mouseJson = item.getAsJsonObject();

			MouseCodeEvent event = new MouseCodeEvent();
			event.load(mouseJson);
			Sprite eventSprite = getSpriteById(event.getSpriteId());
			event.getAction().setSprite(eventSprite);

			mouseEventsToSpriteMap.get(eventSprite.getId()).add(event);
		}

		for (JsonElement item : jsonObject.get(Constants.ALL_COLLISION_EVENTS_KEY).getAsJsonArray()) {
			// Cast JsonElement to JsonObject - easier to work with.
			JsonObject collisionJson = item.getAsJsonObject();

			CollisionEvent event = new CollisionEvent();
			event.load(collisionJson);
			Sprite eventSprite = getSpriteById(event.getSpriteId());
			event.getAction().setSprite(eventSprite);

			collisionEventsToSpriteMap.get(eventSprite.getId()).add(event);
		}
	}

	/**
	 * Returns the current Sprite ID and increments the counter
	 * to prepare for the next Sprite.
	 * @return a unique Sprite ID from 0 to MAX_INT.
	 */
	public int nextSpriteId() {
		int output = currentSpriteId;
		currentSpriteId++;
		return output;
	}

	/************************************
	 * 
	 * Getters/Setters
	 * 
	 ************************************/

	/**
	 * @return the ID of the next Sprite that would be created.
	 * Run nextSpriteId() if you wish to also change the Model's currentSpriteId.
	 */
	public int getCurrentSpriteId() {
		return currentSpriteId;
	}

	public Pane getGameCanvas() {
		return background.getGameCanvas();
	}

	public Sprite getSpriteById(int id) {
		return allSprites.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
	}

	public Sprite getCurrentSelectedSprite() {
		return currentSelectedSprite;
	}

	public void updateCurrentSelectedSprite(Sprite sprite) {
		previousSelectedSprite = currentSelectedSprite;

		if (previousSelectedSprite != null) {
			previousSelectedSprite.setSelected(false);
		}
		if (sprite != null) {
			setSelectedSprite(sprite);

			GameMakerApplication.logger
					.info("Newly selected sprite is Sprite with id " + sprite.getId() + ", " + sprite.toString());

			// Send view Sprite info
			view.updateBasedOnSelectedSprite(currentSelectedSprite,
					timeEventsToSpriteMap.get(currentSelectedSprite.getId()).iterator(),
					keyEventsToSpriteMap.get(currentSelectedSprite.getId()).iterator(),
					mouseEventsToSpriteMap.get(currentSelectedSprite.getId()).iterator(),
					collisionEventsToSpriteMap.get(currentSelectedSprite.getId()).iterator());
		} else {
			view.updateBasedOnSelectedSprite(null, null, null, null, null);
		}
	}

	private void setSelectedSprite(Sprite sprite) {
		currentSelectedSprite = sprite;
		currentSelectedSprite.setSelected(true);
	}

	public HashMap<Integer, LinkedList<TimeEvent>> getTimeEventsToSpriteMap() {
		return timeEventsToSpriteMap;
	}

	public HashMap<Integer, LinkedList<KeyCodeEvent>> getKeyEventsToSpriteMap() {
		return keyEventsToSpriteMap;
	}

	public HashMap<Integer, LinkedList<MouseCodeEvent>> getMouseEventsToSpriteMap() {
		return mouseEventsToSpriteMap;
	}

	public HashMap<Integer, LinkedList<CollisionEvent>> getCollisionEventsToSpriteMap() {
		return collisionEventsToSpriteMap;
	}

	public TimeEventHandler getTimeEventHandler() {
		return timeEventHandler;
	}

	public void setTimeEventHandler(TimeEventHandler timeEventHandler) {
		this.timeEventHandler = timeEventHandler;
	}

	public InputEventHandler getInputEventHandler() {
		return inputEventHandler;
	}

	public void setInputEventHandler(InputEventHandler inputEventHandler) {
		this.inputEventHandler = inputEventHandler;
	}

	public WallCollisionEventHandler getWallCollisionEventHandler() {
		return wallCollisionEventHandler;
	}

	public void setWallCollisionEventHandler(WallCollisionEventHandler wallCollisionEventHandler) {
		this.wallCollisionEventHandler = wallCollisionEventHandler;
	}

	public SpriteCollisionEventHandler getSpriteCollisionEventHandler() {
		return spriteCollisionEventHandler;
	}

	public void setSpriteCollisionEventHandler(SpriteCollisionEventHandler spriteCollisionEventHandler) {
		this.spriteCollisionEventHandler = spriteCollisionEventHandler;
	}

	/************************************
	 * 
	 * Dumpable
	 * 
	 ************************************/

	@Override
	public void dump() {
		Iterator<Sprite> spriteIterator = allSprites.iterator();
		while (spriteIterator.hasNext()) {
			Sprite next = spriteIterator.next();
			background.getGameCanvas().getChildren().remove(next.getShape());
		}
		allSprites.clear();
		allSprites = null;
		currentSelectedSprite = null;

		timeEventsToSpriteMap.clear();
		timeEventsToSpriteMap = null;

		keyEventsToSpriteMap.clear();
		keyEventsToSpriteMap = null;

		mouseEventsToSpriteMap.clear();
		mouseEventsToSpriteMap = null;

		collisionEventsToSpriteMap.clear();
		collisionEventsToSpriteMap = null;

		init();
	}
}
