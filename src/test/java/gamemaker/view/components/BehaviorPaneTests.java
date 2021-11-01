/**
 * @Author: Maazin Jawad
 * @CreationDate: 10/9/2021
 * @Editors:
 * @EditedDate:
 **/
package gamemaker.view.components;

import de.saxsys.javafx.test.JfxRunner;
import gamemaker.Constants;
import gamemaker.model.actions.ReflectAction;
import gamemaker.model.actions.TranslateVelocityAction;
import gamemaker.model.event.CollisionEvent;
import gamemaker.model.event.KeyCodeEvent;
import gamemaker.model.event.TimeEvent;
import gamemaker.model.sprite.CircleSprite;
import gamemaker.model.sprite.Sprite;
import gamemaker.view.components.*;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JfxRunner.class)
public class BehaviorPaneTests {

    TimeBehaviorPane timeBehaviorPane = new TimeBehaviorPane();
    CollisionBehaviorPane collisionBehaviorPane = new CollisionBehaviorPane();
    KeyInputPane keyInputPane = new KeyInputPane();


    @Test
    public void timeBehaviorPaneTest(){
        Sprite testSprite = new CircleSprite();

        TimeEvent timeEvent = new TimeEvent(4,5,new TranslateVelocityAction(testSprite,null,5,5));
        timeBehaviorPane.rebuildFrom(timeEvent);
        TimeBehaviorConfigObject timeBehaviorConfigObject = timeBehaviorPane.export();
        Assert.assertEquals("5", timeBehaviorConfigObject.getInterval());
        Assert.assertEquals(false, timeBehaviorConfigObject.isContinuous());
        Assert.assertEquals("Shift Property", timeBehaviorConfigObject.getAction());
        Assert.assertEquals("Velocity", timeBehaviorConfigObject.getSelectedProperty());
        HBox inputContainer = timeBehaviorConfigObject.getInput();
        Assert.assertNotNull(inputContainer);
        HBox input = (HBox) inputContainer.getChildren().get(0);
        Assert.assertEquals(2, input.getChildren().size());
        Assert.assertEquals("5.0", ((TextField) (input.getChildren().get(0))).getText());
        Assert.assertEquals(Constants.NO_AUDIO_FILE_SELECTED, timeBehaviorConfigObject.getPath());

    }


    @Test
    public void collisionBehaviorPaneTest(){
        CollisionEvent collisionEvent = new CollisionEvent(Constants.CollisionType.SCREEN, new ReflectAction());
        collisionBehaviorPane.rebuildFrom(collisionEvent);
        CollisionBehaviorConfigObject collisionBehaviorConfigObject = collisionBehaviorPane.export();

        Assert.assertEquals("Reflect", collisionBehaviorConfigObject.getAction());
        Assert.assertEquals("On Hit Gameplay Bounds", collisionBehaviorConfigObject.getCondition());
        Assert.assertEquals(Constants.NO_AUDIO_FILE_SELECTED, collisionBehaviorConfigObject.getPath());
        Assert.assertEquals(0, collisionBehaviorConfigObject.getInput().getChildren().size());

    }

    @Test
    public void keyBehaviorPaneTest(){
        Sprite testSprite = new CircleSprite();

        KeyCodeEvent keyCodeEvent = new KeyCodeEvent(8, KeyCode.W, new TranslateVelocityAction(testSprite,null,4,5));
        keyInputPane.rebuildFrom(keyCodeEvent);
        KeyInputBehaviorConfigObject keyInputBehaviorConfigObject = keyInputPane.export();




    }
}
