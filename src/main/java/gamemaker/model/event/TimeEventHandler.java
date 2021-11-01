/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Sep 30, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.model.event;

import gamemaker.Constants;
import gamemaker.GameMakerApplication;
import gamemaker.model.actions.Action;
import gamemaker.model.interfaces.Dumpable;
import gamemaker.observer.pattern.Observer;

import java.util.*;

public class TimeEventHandler implements Observer, Dumpable {

	// Must be Integer b/c HashMap doesn't work with primitives
	private HashMap<Integer, LinkedList<Action>> intervalToActionMap;
	private int secsElapsed = 0;

	public TimeEventHandler() {
		intervalToActionMap = new HashMap<Integer, LinkedList<Action>>();
	}

	public TimeEventHandler(Iterator<Integer> keys, HashMap<Integer, LinkedList<TimeEvent>> spriteIdToTimeEventsMap) {
		intervalToActionMap = new HashMap<Integer, LinkedList<Action>>();
		buildHashMap(keys, spriteIdToTimeEventsMap);
	}

	/************************************
	 *
	 * Time Event Invoking
	 *
	 ************************************/

	private void buildHashMap(Iterator<Integer> keys, HashMap<Integer, LinkedList<TimeEvent>> spriteIdToTimeEventsMap) {
		while (keys.hasNext()) {
			Integer key = keys.next();

			Iterator<TimeEvent> aSpritesEvents = spriteIdToTimeEventsMap.get(key).iterator();
			while (aSpritesEvents.hasNext()) {
				TimeEvent currentEvent = aSpritesEvents.next();

				// Interval is already a key
				if (intervalToActionMap.containsKey(currentEvent.getInterval())) {
					LinkedList<Action> actionList = intervalToActionMap.get(currentEvent.getInterval());
					GameMakerApplication.logger.info("intervalToActionMap interval " + currentEvent.getInterval()
							+ "'s action list length pre add: " + actionList.size());
					actionList.add(currentEvent.getAction());
					GameMakerApplication.logger.info("intervalToActionMap interval " + currentEvent.getInterval()
							+ "'s action list length post add: " + actionList.size());
				}
				// Interval is not a key yet
				else {
					LinkedList<Action> actionList = new LinkedList<Action>();
					actionList.add(currentEvent.getAction());
					intervalToActionMap.put(currentEvent.getInterval(), actionList);
				}
			}
		}
	}

	/**
	 * Adds the Actions contained in the timeEventList so that they will be subject to ticks.
	 * This method will add more Actions even if the ones being added are duplicates.
	 * @param timeEventList a list of TimeEvents that each have Actions associated with the target Sprite.
	 */
	public void addSprite(List<TimeEvent> timeEventList)
	{
		//Exit when given null list input
		if (timeEventList == null)
		{
			GameMakerApplication.logger.info("Could not add a null timeEventList in "+this.getClass().getName());
			return;
		}

		//Store the Actions found in the TimeEvents
		for (TimeEvent timeEvent : timeEventList)
		{
			int interval = timeEvent.getInterval();
			//Check if there are existing Actions for the event's time interval
			if (intervalToActionMap.containsKey(timeEvent.getInterval()))
			{
				//Add to the existing list
				LinkedList<Action> actionList = intervalToActionMap.get(interval);
				actionList.add(timeEvent.getAction());
			}
			else
			{
				//Create a new list of actions
				LinkedList<Action> actionList = new LinkedList<Action>();
				actionList.add(timeEvent.getAction());
				//Store the list
				intervalToActionMap.put(interval, actionList);

			}
		}
		GameMakerApplication.logger.info("Added "+ timeEventList.size() + " TimeEvents to TimeEventHandler");
	}

	/************************************
	 *
	 * Observer Implementations
	 *
	 ************************************/

	@Override
	public void update(double totalTime, double timeDelta) {
		// Get Key Iterator
		Iterator<Integer> keyIterator = intervalToActionMap.keySet().iterator();
		HashMap<String, Object> params = new HashMap<String, Object>();

		while (keyIterator.hasNext()) {

			params.put(Constants.TIME_ELAPSED_KEY, totalTime);
			params.put(Constants.TIME_DELTA_KEY, timeDelta);
			int interval = (int) keyIterator.next();

			// Signifies every tick!
			if (interval == -1) {
				executeAllActions(params, interval);
			}
			// Signifies every X seconds
			else if (Math.floor(totalTime) == secsElapsed + 1) {
				secsElapsed++;
				if ((Math.floor(totalTime) % interval) == 0) {
					executeAllActions(params, interval);
				}
			}
		}
	}

	private void executeAllActions(HashMap<String, Object> params, int interval)
	{
		try
		{
			Iterator<Action> actionIterator = intervalToActionMap.get(interval).iterator();
			while (actionIterator.hasNext()) {
				Action action = actionIterator.next();
				action.execute(params);
			}
		}
		catch (ConcurrentModificationException ex)
		{
			GameMakerApplication.logger.error("Had to stop executing Actions in TimeEventHandler because there was a ConcurrentModificationException!" +
					" This could be because a Sprite was added/removed during execution");
		}
	}

	/************************************
	 *
	 * Dumpable Implementation
	 *
	 ************************************/

	@Override
	public void dump() {
		// Clear time event info
		intervalToActionMap.clear();
		intervalToActionMap = null;
	}
}
