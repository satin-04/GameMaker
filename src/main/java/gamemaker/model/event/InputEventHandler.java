/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Sep 29, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.model.event;

import java.util.*;

import gamemaker.Constants.MouseButtonCode;
import gamemaker.GameMakerApplication;
import gamemaker.model.actions.Action;
import gamemaker.model.interfaces.Dumpable;
import gamemaker.observer.pattern.Observer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class InputEventHandler implements Observer, Dumpable {

	// Key Listening
	private Pane gameCanvas;
	private HashSet<Integer> pressedKeys = new HashSet<>();

	// Mouse Listening
	private HashSet<MouseButtonCode> pressedMouseButtons = new HashSet<>();

	// Event Invoking
	private HashMap<Integer, LinkedList<Action>> keyToActionMap;
	private HashMap<MouseButtonCode, LinkedList<Action>> mouseToActionMap;

	public InputEventHandler() {
		keyToActionMap = new HashMap<Integer, LinkedList<Action>>();
		mouseToActionMap = new HashMap<MouseButtonCode, LinkedList<Action>>();
	}

	public InputEventHandler(Pane gameCanvas, Iterator<Integer> keyCodeKeys,
			HashMap<Integer, LinkedList<KeyCodeEvent>> spriteIdToKeyCodeEventsMap, Iterator<Integer> mouseCodeKeys,
			HashMap<Integer, LinkedList<MouseCodeEvent>> spriteIdToMouseCodeEventsMap) {

		this.gameCanvas = gameCanvas;
		//Attach key/mouse listeners to the entire Scene
		Scene scene = GameMakerApplication.getScene();
		scene.setOnKeyPressed(this::keyPressed);
		scene.setOnKeyReleased(this::keyReleased);
		scene.setOnMousePressed(this::mousePressed);
		scene.setOnMouseReleased(this::mouseReleased);

		keyToActionMap = new HashMap<Integer, LinkedList<Action>>();
		buildKeyCodeHashMap(keyCodeKeys, spriteIdToKeyCodeEventsMap);
		mouseToActionMap = new HashMap<MouseButtonCode, LinkedList<Action>>();
		buildMouseCodeHashMap(mouseCodeKeys, spriteIdToMouseCodeEventsMap);
	}

	/************************************
	 * 
	 * Key Listening/Recording
	 *
	 ************************************/

	public void keyPressed(KeyEvent event) {
		GameMakerApplication.logger.info("Key " + event.getCode() + " ("+ event.getCode().getCode() +") was pressed during the game.");
		pressedKeys.add(event.getCode().getCode());
	}

	public void keyReleased(KeyEvent event) {
		GameMakerApplication.logger.info("Key " + event.getCode() + " ("+ event.getCode().getCode() +") was released during the game.");
		pressedKeys.remove(event.getCode().getCode());
	}

	/************************************
	 * 
	 * Mouse Listening/Recording
	 *
	 ************************************/

	public void mousePressed(MouseEvent event) {
		MouseButtonCode code = getMouseCode(event);
		GameMakerApplication.logger.info("Mouse " + code + " was pressed during the game.");
		pressedMouseButtons.add(code);

	}

	public void mouseReleased(MouseEvent event) {
		MouseButtonCode code = getMouseCode(event);
		GameMakerApplication.logger.info("Mouse " + code + " was released during the game.");
		pressedMouseButtons.remove(code);
	}

	public MouseButtonCode getMouseCode(MouseEvent event) {
		if (event.isPrimaryButtonDown()) {
			return MouseButtonCode.PRIMARY;
		} else if (event.isSecondaryButtonDown()) {
			return MouseButtonCode.SECONDARY;
		} else if (event.isMiddleButtonDown()) {
			return MouseButtonCode.MIDDLE;
		} else {
			return MouseButtonCode.IGNORE;
		}
	}

	/************************************
	 * 
	 * Input Event Invoking
	 *
	 ************************************/

	private void buildKeyCodeHashMap(Iterator<Integer> keyCodeKeys,
			HashMap<Integer, LinkedList<KeyCodeEvent>> spriteIdToKeyCodeEventsMap) {
		while (keyCodeKeys.hasNext()) {
			Integer key = keyCodeKeys.next();

			Iterator<KeyCodeEvent> aSpritesEvents = spriteIdToKeyCodeEventsMap.get(key).iterator();
			while (aSpritesEvents.hasNext()) {
				KeyCodeEvent currentEvent = aSpritesEvents.next();

				// KeyCode is already a key
				if (keyToActionMap.containsKey(currentEvent.getInputTrigger().getCode())) {
					LinkedList<Action> actionList = keyToActionMap.get(currentEvent.getInputTrigger().getCode());
					actionList.add(currentEvent.getAction());
				}
				// KeyCode is not a key yet
				else {
					LinkedList<Action> actionList = new LinkedList<Action>();
					actionList.add(currentEvent.getAction());
					keyToActionMap.put(currentEvent.getInputTrigger().getCode(), actionList);
				}
				GameMakerApplication.logger.info("Mapped key code "+currentEvent.getInputTrigger()+" to action " +
						currentEvent.getAction());
			}
		}
	}

	private void buildMouseCodeHashMap(Iterator<Integer> mouseCodeKeys,
			HashMap<Integer, LinkedList<MouseCodeEvent>> spriteIdToMouseCodeEventsMap) {

		while (mouseCodeKeys.hasNext()) {
			Integer key = mouseCodeKeys.next();

			Iterator<MouseCodeEvent> aSpritesEvents = spriteIdToMouseCodeEventsMap.get(key).iterator();
			while (aSpritesEvents.hasNext()) {
				MouseCodeEvent currentEvent = aSpritesEvents.next();

				// MouseCode is already a key
				if (mouseToActionMap.containsKey(currentEvent.getInputTrigger())) {
					LinkedList<Action> actionList = mouseToActionMap.get(currentEvent.getInputTrigger());
					GameMakerApplication.logger.info("mouseCodeToActionMap mouse button "
							+ currentEvent.getInputTrigger() + "'s length pre add: " + actionList.size());
					actionList.add(currentEvent.getAction());
					GameMakerApplication.logger.info("mouseCodeToActionMap mouse button "
							+ currentEvent.getInputTrigger() + "'s action list length post add: " + actionList.size());
				}
				// MouseCode is not a key yet
				else {
					LinkedList<Action> actionList = new LinkedList<Action>();
					actionList.add(currentEvent.getAction());
					mouseToActionMap.put(currentEvent.getInputTrigger(), actionList);
				}
			}
		}
	}

	/************************************
	 * 
	 * Observer Implementation
	 *
	 ************************************/

	@Override
	public void update(double totalTime, double timeDelta) {
		processKeyEvents(totalTime, timeDelta);
		processMouseEvents(totalTime, timeDelta);
	}

	/**
	 * Every tick, run every Action associated with each key.
	 */
	private void processKeyEvents(double totalTime, double timeDelta)
	{
		//Iterate through every key that the user is currently pressing
		try
		{
			Iterator<Integer> keysPressedIterator = pressedKeys.iterator();
			while (keysPressedIterator.hasNext())
			{
				Integer keyCode = keysPressedIterator.next();

				//If the key being pressed has Actions...
				if (keyToActionMap.containsKey(keyCode))
				{
					//Perform each Action
					for (Action action : keyToActionMap.get(keyCode))
					{
						action.execute(null);
					}
				}
			}
		}
		catch (ConcurrentModificationException ex)
		{
			GameMakerApplication.logger.error("Had to stop executing Actions in "+this.getClass().getName()+" because there was a ConcurrentModificationException!" +
					" This could be because a Sprite was added/removed during execution");
		}

		//Testing purposes...
		/*System.out.println("These mappings are present: ");
		Iterator<Integer> keyIterator = keyToActionMap.keySet().iterator();
		while (keyIterator.hasNext())
		{
			int key = keyIterator.next();
			System.out.println(" - "+key);
			for (Action action : keyToActionMap.get(key))
			{
				System.out.println("    > "+action.getClass());
			}
		}*/
	}

	private void processMouseEvents(double totalTime, double timeDelta) {
		// Get Key Iterator
		Iterator<MouseButtonCode> mouseCodeIterator = mouseToActionMap.keySet().iterator();

		while (mouseCodeIterator.hasNext()) {
			MouseButtonCode mouseButtonCoude = mouseCodeIterator.next();

			// Signifiyes every tick!
			if (pressedMouseButtons.contains(mouseButtonCoude)) {
				Iterator<Action> actionIterator = mouseToActionMap.get(mouseButtonCoude).iterator();
				while (actionIterator.hasNext()) {
					Action action = actionIterator.next();
					action.execute(null);
				}
			}
		}
	}

	/**
	 * Adds the Actions contained in the eventList so that they will be subject to key presses.
	 * This method will add more Actions even if the ones being added are duplicates.
	 * @param eventList a list of KeyCodeEvents that each have Actions associated with the target Sprite.
	 */
	public void addSprite(List<KeyCodeEvent> eventList)
	{
		//Exit when given null list input
		if (eventList == null)
		{
			GameMakerApplication.logger.info("Could not add a null eventList in "+this.getClass().getName());
			return;
		}

		//Store the Actions found in the KeyCodeEvents
		for (KeyCodeEvent keyCodeEvent : eventList)
		{
			int inputTriggerCode = keyCodeEvent.getInputTrigger().getCode();
			//Check if there are existing Actions for the event's time interval
			if (keyToActionMap.containsKey(inputTriggerCode))
			{
				//Add to the existing list
				LinkedList<Action> actionList = keyToActionMap.get(inputTriggerCode);
				actionList.add(keyCodeEvent.getAction());
			}
			else
			{
				//Create a new list of actions
				LinkedList<Action> actionList = new LinkedList<Action>();
				actionList.add(keyCodeEvent.getAction());
				//Store the list
				keyToActionMap.put(inputTriggerCode, actionList);
			}
			GameMakerApplication.logger.info("Action added: " + inputTriggerCode +
					" maps to " + keyCodeEvent.getAction().getClass().getName() +
					" for sprite with ID" + keyCodeEvent.getAction().getSpriteId());
		}
		GameMakerApplication.logger.info("Added "+ eventList.size() + " KeyCodeEvents to "+this.getClass().getName());
	}



	/************************************
	 * 
	 * Dumpable Implementation
	 *
	 ************************************/

	@Override
	public void dump() {
		// Clear listeners
		// How do we properly remove these?
		Scene scene = GameMakerApplication.getScene();
		scene.setOnKeyPressed(null);
		scene.setOnKeyReleased(null);
		scene.setOnMousePressed(null);
		scene.setOnMouseReleased(null);
		gameCanvas = null;

		// Clear all key info
		pressedKeys.clear();
		pressedKeys = null;
		keyToActionMap.clear();
		keyToActionMap = null;

		// Clear all mouse info
		pressedMouseButtons.clear();
		pressedMouseButtons = null;
		mouseToActionMap.clear();
		mouseToActionMap = null;
	}
}
