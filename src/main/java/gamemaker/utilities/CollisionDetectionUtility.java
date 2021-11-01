package gamemaker.utilities;

import gamemaker.Constants;
import gamemaker.model.sprite.Sprite;
import javafx.geometry.Point2D;

import java.util.HashMap;

import static gamemaker.Constants.CollisionSide.*;

/**
 * This class can detect collisions betwen Sprites.
 */
public class CollisionDetectionUtility
{
    /**
     * Determines the collision type by checking where the collision happened.
     * No collision occurs if either object is marked as destroyed.
     * @param impactee the object that collider might be hitting/touching since collider object is moving
     * @return any one of the possible CollisionSides.
     * Ex: Return LeftImpact if the collider hits the impactee's left side.
     */
    public static void checkForCollision(Sprite collider, Sprite impactee, HashMap<String, Object> actionParams)
    {
        //Define some abbreviations
        Point2D colliderPosition = collider.getPosition();
        Point2D impacteePosition = impactee.getPosition();
        double colliderX = colliderPosition.getX();
        double colliderY = colliderPosition.getY();
        double impacteeX = impacteePosition.getX();
        double impacteeY = impacteePosition.getY();
        double colliderVelX = collider.getVelocity().getX();
        double colliderVelY = collider.getVelocity().getY();
        //double colliderWidth = ...
        //double colliderHeight =
        double impacteeWidth = impactee.getDimensions().getX();
        double impacteeHeight = impactee.getDimensions().getY();

        //If the collider's y-velocity is so fast that it will speed right through the impactee...
        if (Math.abs(colliderVelY) > impacteeHeight)
        {
            if (colliderX >= impacteeX && colliderX <= impacteeX + impacteeWidth) {
                if (colliderY < impacteeY && colliderY + colliderVelY >= impacteeY)
                    placeTopCollision(collider, impactee, actionParams);
                else if (colliderY > impacteeY && colliderY + colliderVelY <= impacteeY)
                    placeBottomCollision(collider, impactee, actionParams);
            }
        }
        //If the collider's x-velocity is so fast that it will speed right through the impactee...
        else if (Math.abs(colliderVelX) > impacteeWidth)
        {
            if (colliderY >= impacteeY && colliderY <= impacteeY + impacteeHeight) {
                if (colliderX < impacteeX && colliderX + colliderVelX >= impacteeX)
                    placeRightCollision(collider, impactee, actionParams);
                else if (colliderX > impacteeX && colliderX + colliderVelX <= impacteeX)
                    placeLeftCollision(collider, impactee, actionParams);
            }
        }

        //Perform the actual collision checks to see if the Sprites are already touching
        checkForHorizontalCollision(collider, impactee, actionParams);
        checkForVerticalCollision(collider, impactee, actionParams);
    }


    private static void checkForVerticalCollision(Sprite collider, Sprite impactee, HashMap<String, Object> actionParams)
    {
        //Define some abbreviations
        Point2D colliderPosition = collider.getPosition();
        Point2D impacteePosition = impactee.getPosition();
        double colliderX = colliderPosition.getX();
        double colliderY = colliderPosition.getY();
        double impacteeX = impacteePosition.getX();
        double impacteeY = impacteePosition.getY();
        double colliderWidth = collider.getDimensions().getX();
        double colliderHeight = collider.getDimensions().getY();
        double impacteeWidth = impactee.getDimensions().getX();
        double impacteeHeight = impactee.getDimensions().getY();

        //Check if the x-coordinate of the collider is inside the impactee
        if (colliderX >= impacteeX - colliderWidth && colliderX <= impacteeX + impacteeWidth)
        {
            //Check if collider touches the TOP half of the impactee (top collision)
            if (colliderY + colliderHeight >= impacteeY && colliderY + colliderHeight <= impacteeY + (impacteeHeight / 2))
                placeTopCollision(collider, impactee, actionParams);
            //Check if collider touches the BOTTOM half of the impactee (bottom collision)
            else if (colliderY >= impacteeY + (impacteeHeight / 2) && colliderY <= impacteeY + impacteeHeight)
                placeBottomCollision(collider, impactee, actionParams);
        }
    }


    private static void checkForHorizontalCollision(Sprite collider, Sprite impactee, HashMap<String, Object> actionParams)
    {
        //Define some abbreviations
        Point2D colliderPosition = collider.getPosition();
        Point2D impacteePosition = impactee.getPosition();
        double colliderX = colliderPosition.getX();
        double colliderY = colliderPosition.getY();
        double impacteeX = impacteePosition.getX();
        double impacteeY = impacteePosition.getY();
        double colliderWidth = collider.getDimensions().getX();
        double colliderHeight = collider.getDimensions().getY();
        double impacteeWidth = impactee.getDimensions().getX();
        double impacteeHeight = impactee.getDimensions().getY();

        //Check if the y-coordinate of the collider is inside the impactee
        if (colliderY >= impacteeY - colliderHeight && colliderY <= impacteeY + impacteeHeight)
        {
            //Check if collider touches the LEFT half of the impactee (left collision)
            if (colliderX + colliderWidth >= impacteeX && colliderX + colliderWidth <= impacteeX + (impacteeWidth / 2))
                placeLeftCollision(collider, impactee, actionParams);
            //Check if collider touches the RIGHT half of the impactee (right collision)
            else if (colliderX >= impacteeX + (impacteeWidth / 2) && colliderX <= impacteeX + impacteeWidth)
                placeRightCollision(collider, impactee, actionParams);
        }
    }


    /**
     * Records the collider hitting the LEFT edge of the impactee.
     * Determines the new location of the collider so that it does not remain touching
     * the impactee, then places that location into actionParams.
     */
    private static void placeLeftCollision(Sprite collider, Sprite impactee, HashMap<String, Object> actionParams)
    {
        //The collider should be placed on the left edge of the impactee
        double newX = impactee.getPosition().getX() - collider.getDimensions().getX();
        double newY = collider.getPosition().getY();  //keep the y-value
        placeCollisionData(collider, impactee, actionParams, newX, newY, LEFT);
    }

    /**
     * Records the collider hitting the RIGHT edge of the impactee.
     * Determines the new location of the collider so that it does not remain touching
     * the impactee, then places that location into actionParams.
     */
    private static void placeRightCollision(Sprite collider, Sprite impactee, HashMap<String, Object> actionParams)
    {
        //The collider should be placed on the right edge of the impactee
        double newX = impactee.getPosition().getX() + impactee.getDimensions().getX();
        double newY = collider.getPosition().getY(); //keep the y-value
        placeCollisionData(collider,impactee, actionParams, newX, newY, RIGHT);
    }

    /**
     * Records the collider hitting the TOP edge of the impactee.
     * Determines the new location of the collider so that it does not remain touching
     * the impactee, then places that location into actionParams.
     */
    private static void placeTopCollision(Sprite collider, Sprite impactee, HashMap<String, Object> actionParams)
    {
        //The collider should be placed on the top edge of the impactee
        double newX = collider.getPosition().getX(); //keep the x-value
        double newY = impactee.getPosition().getY() - impactee.getDimensions().getY();
        placeCollisionData(collider, impactee, actionParams, newX, newY, TOP);
    }

    /**
     * Records the collider hitting the BOTTOM edge of the impactee.
     * Determines the new location of the collider so that it does not remain touching
     * the impactee, then places that location into actionParams.
     */
    private static void placeBottomCollision(Sprite collider, Sprite impactee, HashMap<String, Object> actionParams)
    {
        //The collider should be placed on the bottom edge of the impactee
        double newX = collider.getPosition().getX(); //keep the x-value
        double newY = impactee.getPosition().getY() + impactee.getDimensions().getY();
        placeCollisionData(collider, impactee, actionParams, newX, newY, BOTTOM);
    }

    /**
     * Writes data to actionParams so that the collider Sprite can be relocated.
     * @param actionParams this records collision data
     * @param newX where the collider Sprite should be placed
     * @param newY where the collider Sprite should be placed
     * @param collisionSide where the collider hit the impactee Sprite (i.e. which edge of the impactee was hit).
     */
    private static void placeCollisionData(Sprite target, Sprite impactee, HashMap<String, Object> actionParams, double newX, double newY, Constants.CollisionSide collisionSide)
    {
        actionParams.put(Constants.SPRITE_ID_KEY, target.getId()); //record the ID of the Sprite so that the Action can check if the ID in the params is the same as the ID it (the Action) is holding
        actionParams.put(Constants.KISS_POSITION_KEY, new Point2D(newX, newY));
        actionParams.put(Constants.COLLISION_SIDE_KEY, collisionSide);
        actionParams.put(Constants.COLLISION_IMPACTEE_KEY, impactee);

    }
}

