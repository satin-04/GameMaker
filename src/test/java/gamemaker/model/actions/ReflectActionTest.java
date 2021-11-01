package gamemaker.model.actions;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;



import gamemaker.Constants;
import gamemaker.model.sprite.RectangleSprite;
import javafx.geometry.Point2D;

public class ReflectActionTest {
	
	ReflectAction raTest = new ReflectAction(new RectangleSprite(100),null);
	
	@Test
	public void getUIInfoTest() {
		assertEquals("Reflect", raTest.getUIInfo());
	}
	
	@Test
	public void executeTestForLeft() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.COLLISION_SIDE_KEY, Constants.CollisionSide.LEFT);
		params.put(Constants.KISS_POSITION_KEY, new Point2D(2.0,1.0));
		
		raTest.execute(params);
		assertEquals(2.0,raTest.getSprite().getPosition().getX(),0.02);
		assertEquals(1.0,raTest.getSprite().getPosition().getY(),0.02);
	}
	
	@Test
	public void executeTestForRight() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.COLLISION_SIDE_KEY, Constants.CollisionSide.RIGHT);
		params.put(Constants.KISS_POSITION_KEY, new Point2D(2.0,1.0));
		
		raTest.execute(params);
		assertEquals(2.0,raTest.getSprite().getPosition().getX(),0.02);
		assertEquals(1.0,raTest.getSprite().getPosition().getY(),0.02);
	}
	
	@Test
	public void executeTestForTop() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.COLLISION_SIDE_KEY, Constants.CollisionSide.TOP);
		params.put(Constants.KISS_POSITION_KEY, new Point2D(2.0,1.0));
		
		raTest.execute(params);
		assertEquals(2.0,raTest.getSprite().getPosition().getX(),0.02);
		assertEquals(1.0,raTest.getSprite().getPosition().getY(),0.02);
	}
	
	@Test
	public void executeTestForBottom() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.COLLISION_SIDE_KEY, Constants.CollisionSide.BOTTOM);
		params.put(Constants.KISS_POSITION_KEY, new Point2D(2.0,1.0));
		
		raTest.execute(params);
		assertEquals(2.0,raTest.getSprite().getPosition().getX(),0.02);
		assertEquals(1.0,raTest.getSprite().getPosition().getY(),0.02);
	}

}
