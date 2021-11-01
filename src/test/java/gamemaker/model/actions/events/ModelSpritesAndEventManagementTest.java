/**
 * @Author: Maazin Jawad
 * @CreationDate: 10/9/2021
 * @Editors:
 * @EditedDate:
 **/
package gamemaker.model.actions.events;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gamemaker.model.Model;
import gamemaker.model.event.TimeEvent;
import gamemaker.model.event.TimeEventHandler;
import gamemaker.model.sprite.Sprite;
import gamemaker.utilities.SaveManager;
import gamemaker.view.View;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ModelSpritesAndEventManagementTest {


    @Test
    public void spriteManagement() {
        View view = new View();
        Model model = new Model(view);


        for (int i = 0; i < 30; i++) {
            Sprite sprite = model.createSprite();
            sprite.setVelocity(new Point2D(8, 8));
            sprite.setColor(Color.BLACK);

        }

        //model.updateCurrentSelectedSprite(model.getSpriteById(1));

        //now calling createSprite will create duplicates of Sprite 0

        for (int i = 0; i < 30; i++) {
            Sprite sprite = model.createSpriteWithTemplate(30 + i, model.getSpriteById(1));

            Assert.assertEquals(new Point2D(8, 8), sprite.getVelocity());
            Assert.assertEquals(Color.BLACK, sprite.getColor());


        }

        Assert.assertEquals(30, model.getTimeEventsToSpriteMap().size());


    }

    @Test
    public void defaultTimeEventCreation() {
        View view = new View();
        Model model = new Model(view);

        for (int i = 0; i < 30; i++) {
            model.createSprite();
            model.getSpriteById(i).setVelocity(new Point2D(1, 0));


        }
        HashMap<Integer, LinkedList<TimeEvent>> spriteIdToTimeEventList = model.getTimeEventsToSpriteMap();

        Assert.assertEquals(30, spriteIdToTimeEventList.size());

        for (Map.Entry<Integer, LinkedList<TimeEvent>> entry : spriteIdToTimeEventList.entrySet()) {
            LinkedList<TimeEvent> timeEvents = entry.getValue();
            TimeEvent te = timeEvents.getFirst();

            Assert.assertEquals("For spriteID " + entry.getKey() + ", a default move TimeEvent was not present",
                    te.getAction().getUIInfo(), "Move");
            Assert.assertEquals("For spriteID " + entry.getKey() + ", default  move TimeEvent was not continuous",
                    te.getInterval(), -1);


        }

        TimeEventHandler timeEventHandler = new TimeEventHandler(spriteIdToTimeEventList.keySet().iterator(), spriteIdToTimeEventList);


        //current position of all sprites
        Point2D originalPosition = model.getSpriteById(1).getPosition();
        //calling update should move all circles 1 unit to the right since they all have velocity (1,0)
        timeEventHandler.update(1, 1);

        for (int i = 0; i < 30; i++) {
            Assert.assertEquals(1.0, originalPosition.distance(model.getSpriteById(i).getPosition()), 1);

        }

        timeEventHandler.update(1, 1);

        for (int i = 0; i < 30; i++) {
            Assert.assertEquals(2.0, originalPosition.distance(model.getSpriteById(i).getPosition()), 1);

        }

        for (int i = 0; i < 30; i++) {
            Assert.assertEquals(2.0, originalPosition.distance(model.getSpriteById(i).getPosition()), 1);

        }

    }

    @Test
    public void saveLoad() {
        View view = new View();
        Model model = new Model(view);

        for (int i = 0; i < 30; i++) {
            model.createSprite();
            model.getSpriteById(i).setVelocity(new Point2D(1, 0));


        }

        String json = model.save(true);

        // Convert String to Json
        JsonObject loadedJson = JsonParser.parseString(json).getAsJsonObject();

        // Give to model to load
        model.load(loadedJson);

        for (int i = 0; i < 30; i++) {
            Assert.assertEquals(new Point2D(1, 0), model.getSpriteById(i).getVelocity());

        }


    }

}
