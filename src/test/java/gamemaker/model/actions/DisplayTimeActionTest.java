package gamemaker.model.actions;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

import gamemaker.Constants;
import gamemaker.model.sprite.Sprite;
import gamemaker.model.sprite.TextSprite;


public class DisplayTimeActionTest {
	
	DisplayTimeAction dtaTest = new DisplayTimeAction();
	DisplayTimeAction dtaParamTest = new DisplayTimeAction(new TextSprite(),null);
	
	@Test
	public void getUIInfoTest() {
		assertEquals(dtaTest.getUIInfo(), "Display Time");
	}
	
	@Test
	public void executeTest() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.TIME_ELAPSED_KEY, 15.008165456);
		dtaParamTest.execute(params);
		assertEquals("Time: 00:15",dtaParamTest.getSprite().getText());
	}

}
