package gamemaker.model.events;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

import de.saxsys.javafx.test.JfxRunner;
import gamemaker.Constants;
import gamemaker.Constants.CollisionType;
import gamemaker.model.actions.DisplayTimeAction;
import gamemaker.model.actions.ReflectAction;
import gamemaker.model.event.CollisionEvent;
import gamemaker.model.sprite.RectangleSprite;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import static org.junit.Assert.*;


public class CollisionEventTest {
	CollisionEvent collisionEventTest = new CollisionEvent();
	ReflectAction ra = new ReflectAction(new RectangleSprite(100),null);
	CollisionEvent collisionEventParamTest = new CollisionEvent(CollisionType.OBJECT,ra);
	
	
	@Test
	public void getUIInfoTest() {
		assertEquals(collisionEventTest.getUIInfo(), "Collision Event");
	}
	
	@Test
	public void executeTest() {
		assertNotNull(collisionEventParamTest.getCollisionType());
		collisionEventParamTest.setCollisionType(CollisionType.SCREEN);
		assertNotNull(collisionEventParamTest.getCollisionType());
	}
	
}
