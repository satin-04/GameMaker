package gamemaker.model.actions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DoNothingActionTest {
	
	DoNothingAction dnaTest = new DoNothingAction();
	
	@Test
	public void getUIInfoTest() {
		assertEquals(dnaTest.getUIInfo(), "Do Nothing");
	}

}
