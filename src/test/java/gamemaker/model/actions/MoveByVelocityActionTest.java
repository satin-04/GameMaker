package gamemaker.model.actions;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;

import gamemaker.Constants;
import gamemaker.model.sprite.RectangleSprite;
import de.saxsys.javafx.test.JfxRunner;
import javafx.geometry.Point2D;

@RunWith(JfxRunner.class)
public class MoveByVelocityActionTest {
	
	RectangleSprite testSprite = new RectangleSprite(100);
	MoveByVelocityAction mva = new MoveByVelocityAction(testSprite,null);
	
	@Test
	public void getUIInfoTest() {
		assertEquals("Move", mva.getUIInfo());
	}
	
	@Test
	public void executeTest() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		testSprite.setVelocity(new Point2D(Constants.ONE, Constants.ONE));
		mva.execute(params);
		
		assertEquals(251.0,testSprite.getPosition().getX(),0.02);
		assertEquals(151.0,testSprite.getPosition().getY(),0.02);
		
	}

}
