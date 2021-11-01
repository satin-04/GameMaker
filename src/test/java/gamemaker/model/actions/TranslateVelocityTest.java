package gamemaker.model.actions;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

import gamemaker.model.sprite.RectangleSprite;
import javafx.geometry.Point2D;

public class TranslateVelocityTest {
	
	TranslateVelocityAction tvaTest = new TranslateVelocityAction(new RectangleSprite(100), null,2.0,1.0);
	
	@Test
	public void getUIInfoTest() {
		assertEquals("Shift Property-Velocity" + "-" + tvaTest.getTranslateX() + "-" + tvaTest.getTranslateY(), tvaTest.getUIInfo());
	}
	
	@Test
	public void executeTest() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		tvaTest.getSprite().setVelocity(new Point2D(2.0,1.0));
		tvaTest.execute(params);
		assertEquals(4.0,tvaTest.getSprite().getVelocity().getX(),0.02);
		assertEquals(2.0,tvaTest.getSprite().getVelocity().getY(),0.02);
	}
}
