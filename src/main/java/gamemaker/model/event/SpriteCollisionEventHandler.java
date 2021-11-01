package gamemaker.model.event;

import gamemaker.Constants;
import gamemaker.GameMakerApplication;
import gamemaker.model.actions.Action;
import gamemaker.model.interfaces.Dumpable;
import gamemaker.model.sprite.Sprite;
import gamemaker.observer.pattern.Observer;
import gamemaker.utilities.CollisionDetectionUtility;

import java.util.*;

/**
 * Every tick, this detects collisions between Sprites and calls their respective Actions.
 * This class needs to hold all Sprites susceptible to collisons as well as a
 * SpriteID to CollisionEvent map. CollisionEvents are needed because they actually hold the Actions.
 */
public class SpriteCollisionEventHandler implements Observer, Dumpable
{
    //A HashMap that maps Sprite IDs to the list of actions (stored in CollisionEvents)
    private HashMap<Integer, LinkedList<CollisionEvent>> spriteIdToCollisionEventsMap;
    //A list of all Sprites that use collision detection
    private ArrayList<Sprite> allSpritesList = new ArrayList<>();

    /**
     *
     * @param spriteIdToCollisionEventsMap this maps Sprite IDs to lists of behaviors that occur upon collision.
     *                                     Every behavior in the list will have its Action called when the target Sprite
     *                                     experiences a collision.
     * @param allSpritesList all Sprites susceptible to collisions
     */
    public SpriteCollisionEventHandler(HashMap<Integer, LinkedList<CollisionEvent>> spriteIdToCollisionEventsMap, ArrayList<Sprite> allSpritesList)
    {
        //Clone the data structures provided so that they do not interfere with the game design
        this.spriteIdToCollisionEventsMap = (HashMap<Integer, LinkedList<CollisionEvent>>) spriteIdToCollisionEventsMap.clone();
        this.allSpritesList = (ArrayList<Sprite>) allSpritesList.clone();
    }



    /**
     * Adds the Actions contained in the eventList so that they will be subject to sprite collisions.
     * This method will add more Actions even if the ones being added are duplicates.
     * @param spriteToAdd the Sprite that needs to use the eventList when it experiences a collision.
     * @param eventList a list of CollisionEvent that each have Actions associated with the target Sprite.
     */
    public void addSprite(Sprite spriteToAdd, List<CollisionEvent> eventList)
    {
        //Exit when given null list input
        if (eventList == null)
        {
            GameMakerApplication.logger.info("Could not add a null eventList in "+this.getClass().getName());
            return;
        }

        int spriteId = spriteToAdd.getId();
        allSpritesList.add(spriteToAdd);

        //Store the Actions found in the CollisionEvents
        for (CollisionEvent collisionEvent : eventList)
        {
            //Check if there are existing Actions for the event's time interval
            if (spriteIdToCollisionEventsMap.containsKey(spriteId))
            {
                //Add to the existing list
                LinkedList<CollisionEvent> existingEventList = spriteIdToCollisionEventsMap.get(spriteId);
                existingEventList.add(collisionEvent);
            }
            else
            {
                //Create a new list of actions
                LinkedList<CollisionEvent> newEventList = new LinkedList<>();
                newEventList.add(collisionEvent);
                //Store the list
                spriteIdToCollisionEventsMap.put(spriteId, newEventList);
            }
            GameMakerApplication.logger.info("Action added: sprite ID " + spriteId +
                    " maps to " + collisionEvent.getAction().getClass().getName() +
                    " for sprite with ID " + spriteId);
        }
        GameMakerApplication.logger.info("Added "+ eventList.size() + " CollisionEvents to "+this.getClass().getName());
    }



    /**
     * Every tick, check for collisions and perform the Sprites' actions when necessary.
     */
    @Override
    public void update(double totalTime, double timeDelta)
    {
        //Do nothing if one or more aggregates is null.
        //This is basically a quick fix for when dump() is performed but the game starts again.
        if (spriteIdToCollisionEventsMap == null)
        {
            GameMakerApplication.logger.error("Cannot check for collisions because spriteIdToCollisionEventsMap is null inside SpriteCollisionEventHandler.");
            return;
        }
        if (allSpritesList == null)
        {
            GameMakerApplication.logger.error("Cannot check for collisions because allSpritesList is null inside SpriteCollisionEventHandler.");
            return;
        }

        //Compare every Sprite's position to every other Sprite's position
        try
        {
            Iterator<Sprite> colliderIterator = allSpritesList.listIterator();
            int i = 0; //index of collider
            int j = 0; //index of impactee
            while (colliderIterator.hasNext()) {
                //Pull one object from the iterator
                Sprite collider = colliderIterator.next();

                Iterator<Sprite> impacteeIterator = allSpritesList.listIterator();
                while (impacteeIterator.hasNext()) {
                    Sprite impactee = impacteeIterator.next();
                    //Make sure collider != impactee, then check for collisions
                    if (i != j) {
                        //Determine where the collision occurred and where the collider Sprite could move to
                        HashMap<String, Object> actionParams = new HashMap<String, Object>();
                        CollisionDetectionUtility.checkForCollision(collider, impactee, actionParams);

                        //Execute the Action
                        LinkedList<CollisionEvent> eventHandlersForCollider = spriteIdToCollisionEventsMap.get(collider.getId());
                        if (eventHandlersForCollider != null)
                        {
                            for (CollisionEvent collisionEvent : spriteIdToCollisionEventsMap.get(collider.getId()))
                            {
                                //The collision will only happen if the user wants Sprite collisions
                                if (collisionEvent.getCollisionType() == Constants.CollisionType.OBJECT)
                                {
                                    Action action = collisionEvent.action;
                                    action.execute(actionParams);
                                }
                            }
                        }
                    }
                    j++; //Update impactee index
                }
                i++; //Update collider index
            }
        }
        catch (ConcurrentModificationException ex)
        {
            GameMakerApplication.logger.error("Had to stop executing Actions in "+this.getClass().getName()+" because there was a ConcurrentModificationException!" +
                    " This could be because a Sprite was added/removed during execution");
        }
    }

    /**
     * This adds another Sprite so that it can experience collision events.
     * @param sprite the Sprite that needs to have collision detection
     * @param behaviors a list of behaviors that should occur when the input
     *                  Sprite experiences a collision.
     */
    public void registerSprite(Sprite sprite, LinkedList<CollisionEvent> behaviors)
    {
        //Let the Sprite be hit
        allSpritesList.add(sprite);
        //Add behavior for the Sprite
        spriteIdToCollisionEventsMap.put(sprite.getId(), behaviors);
    }

    /**
     * This causes the Sprite to no longer experience
     * collision detections. It will no longer hit other Sprites
     * or get hit.
     * @param sprite the Sprite to remove
     */
    public void unregisterSprite(Sprite sprite)
    {
        allSpritesList.remove(sprite);
    }


    @Override
    public void dump() {
        //Clear and garbage-collect the collisionToActionMap
        spriteIdToCollisionEventsMap.clear();
        spriteIdToCollisionEventsMap = null;
        allSpritesList.clear();
        allSpritesList = null;
    }
}
