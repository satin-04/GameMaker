package gamemaker.model.actions;

import gamemaker.model.Model;
import gamemaker.model.event.*;
import gamemaker.model.sprite.CircleSprite;
import gamemaker.model.sprite.Sprite;
import gamemaker.view.View;
import javafx.scene.control.Button;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;


public class CloneActionTest
{
    /**
     * Tests using a CloneAction to clone a CircleSprite.
     * This checks:
     *  - If the clone and original have different IDs.
     *  - If exactly one sprite was added to the UI.
     *  - If the clone was created at the position of the creator (CircleSprite in this case)
     */
    @Test
    public void testCloneCircleSprite()
    {
        //Create a Circle Sprite and assign it a TranslateVelocityAction that will be run after the clone is created
        int ID_OF_CIRCLE_SPRITE = 32;
        CircleSprite circleSprite = new CircleSprite(ID_OF_CIRCLE_SPRITE);
        /*final int NEW_VELOCITY_X = 10;
        final int NEW_VELOCITY_Y = 20;
        final int INTERVAL = 30;
        TranslateVelocityAction translateVelocityAction = new TranslateVelocityAction(circleSprite, null, NEW_VELOCITY_X, NEW_VELOCITY_Y);
        TimeEvent translateVelocityTimeEvent = new TimeEvent(ID_OF_CIRCLE_SPRITE, INTERVAL, translateVelocityAction);
        List<TimeEvent> timeEventList = new LinkedList<>();
        timeEventList.add(translateVelocityTimeEvent);*/

        //Form other test subjects
        View view = new View() {
            @Override
            public void enableButton(Button btn) {
                //Do nothing
            }
        };
        Model model = new Model(view) {
            @Override
            public void playGame() {
                //Create mock event handlers with empty behavior maps
                TimeEventHandler mockTimeEventHandler = new TimeEventHandler(
                        Collections.<Integer>emptyIterator(),
                        new HashMap<Integer, LinkedList<TimeEvent>>());
                setTimeEventHandler(mockTimeEventHandler);
            }
        };
        model.addSpriteToModel(circleSprite);
        model.playGame(); //start --> create time event handler

        /*GameMakerApplication.setScene(new Scene(new Parent() {
            //Dummy parent
        }));
        model.playGame(); //start --> create time event handler
        model.getTimeEventHandler().addSprite(ID_OF_CIRCLE_SPRITE, timeEventList);*/ //assign time event behavior
        //Record the current count of Sprites

        int spriteCountInPane = model.getGameCanvas().getChildren().size();


        //Create and execute a CloneAction
        CloneSpriteAction cloneSpriteAction = new CloneSpriteAction(circleSprite, null, model, ID_OF_CIRCLE_SPRITE);
        HashMap<String, Object> params = new HashMap<String, Object>(); //no parameters
        cloneSpriteAction.execute(params);

        //Ensure the clone is now in the Model
        Sprite clone = model.getSpriteById(model.getCurrentSpriteId() - 1);
        assertNotNull(clone);

        //Ensure that the clone's ID is unique to the original's
        assertNotEquals(circleSprite.getId(), clone.getId());

        //Ensure that the positions match
        assertEquals(circleSprite.getPosition(), clone.getPosition());

        //Ensure exactly one Sprite was added to the UI
        assertEquals(spriteCountInPane + 1, model.getGameCanvas().getChildren().size());


        //Run one tick to test the Action
        /*model.getTimeEventHandler().update(INTERVAL, INTERVAL);
        //Ensure the TranslateVelocityAction was processed correctly
        assertEquals(clone.getVelocity().getX(), NEW_VELOCITY_X, 0);
        assertEquals(clone.getVelocity().getY(), NEW_VELOCITY_Y, 0);*/
    }
}
