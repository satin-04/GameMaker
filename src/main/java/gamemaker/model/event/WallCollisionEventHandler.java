/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Sep 26, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.model.event;

import java.util.*;

import gamemaker.Constants;
import gamemaker.Constants.CollisionSide;
import gamemaker.Constants.CollisionType;
import gamemaker.GameMakerApplication;
import gamemaker.model.GameBackground;
import gamemaker.model.actions.Action;
import gamemaker.model.interfaces.Dumpable;
import gamemaker.model.sprite.Sprite;
import gamemaker.observer.pattern.Observer;
import javafx.geometry.Point2D;

/**
 * This class holds a HashMap that maps where the collisions may occur to user-specified actions.
 */
public class WallCollisionEventHandler implements Observer, Dumpable {

	private HashMap<CollisionType, LinkedList<CollisionEvent>> collisionToActionMap;
	private GameBackground gameBackground;

	public WallCollisionEventHandler(Iterator<Integer> collisionKeys,
									 HashMap<Integer, LinkedList<CollisionEvent>> spriteIdToCollisionEventsMap,
									 GameBackground background) {

		this.gameBackground = background;
		collisionToActionMap = new HashMap<CollisionType, LinkedList<CollisionEvent>>();
		buildCollisionHashMap(collisionKeys, spriteIdToCollisionEventsMap);
	}

	// LATE NIGHT NOTES
	// THINK WE NEED OBJECT VS GETHITOBJECT COLLISION TYPE!
	private void buildCollisionHashMap(Iterator<Integer> collisionKeys,
			HashMap<Integer, LinkedList<CollisionEvent>> spriteIdToCollisionEventsMap) {
		//LinkedList<CollisionEvent> objectCollisionActions = new LinkedList<CollisionEvent>();
		LinkedList<CollisionEvent> screenCollisionActions = new LinkedList<CollisionEvent>();

		while (collisionKeys.hasNext()) {
			Integer key = collisionKeys.next();

			Iterator<CollisionEvent> aSpritesEvents = spriteIdToCollisionEventsMap.get(key).iterator();

			while (aSpritesEvents.hasNext()) {
				CollisionEvent event = aSpritesEvents.next();

				if (event.getCollisionType() == CollisionType.SCREEN) {
					screenCollisionActions.add(event);
				} /*else {
					objectCollisionActions.add(event);
				}*/
			}
		}
		collisionToActionMap.put(CollisionType.SCREEN, screenCollisionActions);
		//collisionToActionMap.put(CollisionType.OBJECT, objectCollisionActions);
	}


	/**
	 * Adds the Actions contained in the collisionEventList so that they will be subject to wall collisions.
	 * This method will add more Actions even if the ones being added are duplicates.
	 * @param eventList a list of TimeEvents that each have Actions associated with the target Sprite.
	 */
	public void addSprite(List<CollisionEvent> eventList)
	{
		//Exit when given null list input
		if (eventList == null)
		{
			GameMakerApplication.logger.info("Could not add a null eventList in "+this.getClass().getName());
			return;
		}

		//Store the Actions found in the TimeEvents
		for (CollisionEvent collisionEvent : eventList)
		{
			CollisionType collisionType = collisionEvent.getCollisionType();
			//Check if there are existing Actions for the event's time interval
			if (collisionToActionMap.containsKey(collisionType))
			{
				//Add to the existing list
				LinkedList<CollisionEvent> existingList = collisionToActionMap.get(collisionType);
				existingList.add(collisionEvent);
			}
			else
			{
				//Create a new list
				LinkedList<CollisionEvent> newList = new LinkedList<>();
				newList.add(collisionEvent);
				//Store the list
				collisionToActionMap.put(collisionType, newList);
			}
		}
		GameMakerApplication.logger.info("Added "+ eventList.size() + " CollisionEvents to "+this.getClass().getName());
	}




	private void processCollisions() {
		checkScreenBoundsCollision();
	}

	/**
	 * Uses the Sprites contained within the actions of the collisionToActionMap to check
	 * for collisions with the edges of the window. If a Sprite will move outside the bounds,
	 * adjusts its position back to the inside and calls the associated Action.
	 */
	private void checkScreenBoundsCollision()
	{
		//Get the current width/height of the game area
		final double GAME_CANVAS_WIDTH = gameBackground.getGameCanvas().getWidth();
		final double GAME_CANVAS_HEIGHT = gameBackground.getGameCanvas().getHeight();

		try
		{
			Iterator<CollisionEvent> screenCollisionActions = collisionToActionMap.get(CollisionType.SCREEN).iterator();
			while (screenCollisionActions.hasNext()) {
				CollisionEvent event = screenCollisionActions.next();
				Sprite sprite = event.getAction().getSprite();

				// Horizontal screen bound check
				// if moving left and beyond pixel 0 - force inside bounds
				if (sprite.getVelocity().getX() < Constants.ZERO && sprite.getPosition().getX() < Constants.ZERO) {
					HashMap<String, Object> actionParams = new HashMap<String, Object>();
					actionParams.put(Constants.KISS_POSITION_KEY, new Point2D(Constants.ZERO, sprite.getPosition().getY()));
					actionParams.put(Constants.COLLISION_SIDE_KEY, CollisionSide.LEFT);
					event.action.execute(actionParams);
				}
				// if moving right and beyond scene width - force inside bounds
				else if (sprite.getVelocity().getX() > 0
						&& (sprite.getPosition().getX() + sprite.getDimensions().getX()) >= GAME_CANVAS_WIDTH) {
					HashMap<String, Object> actionParams = new HashMap<String, Object>();
					actionParams.put(Constants.KISS_POSITION_KEY, new Point2D(
							(GAME_CANVAS_WIDTH - sprite.getDimensions().getX()), sprite.getPosition().getY()));
					actionParams.put(Constants.COLLISION_SIDE_KEY, CollisionSide.RIGHT);
					event.action.execute(actionParams);
				}

				// Vertical Scene Check
				// if moving up and beyound pixel 0 - force inside bounds
				if (sprite.getVelocity().getY() < 0 && sprite.getPosition().getY() < Constants.ZERO) {
					HashMap<String, Object> actionParams = new HashMap<String, Object>();
					actionParams.put(Constants.KISS_POSITION_KEY, new Point2D(sprite.getPosition().getX(), Constants.ZERO));
					actionParams.put(Constants.COLLISION_SIDE_KEY, CollisionSide.TOP);
					event.action.execute(actionParams);
				}

				// if moving down and beyond scene height - force inside bounds
				else if (sprite.getVelocity().getY() > 0
						&& (sprite.getPosition().getY() + sprite.getDimensions().getY()) >= GAME_CANVAS_HEIGHT) {
					HashMap<String, Object> actionParams = new HashMap<String, Object>();
					actionParams.put(Constants.KISS_POSITION_KEY, new Point2D(sprite.getPosition().getX(),
							GAME_CANVAS_HEIGHT - sprite.getDimensions().getY()));
					actionParams.put(Constants.COLLISION_SIDE_KEY, CollisionSide.BOTTOM);
					event.action.execute(actionParams);
				}
			}
		}
		catch (ConcurrentModificationException ex)
		{
			GameMakerApplication.logger.error("Had to stop executing Actions in "+this.getClass().getName()+" because there was a ConcurrentModificationException!" +
					" This could be because a Sprite was added/removed during execution");
		}
	}

	@Override
	public void dump() {
		collisionToActionMap.clear();
		collisionToActionMap = null;
	}

	/**
	 * Every tick, check for collisions and perform the Sprites' actions when necessary.
	 */
	@Override
	public void update(double totalTime, double timeDelta) {
		processCollisions();
	}

}
