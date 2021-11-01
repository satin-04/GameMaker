package gamemaker.model.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import gamemaker.model.actions.DoNothingAction;
import gamemaker.model.event.TimeEvent;

public class TimeEventTest {
	
	TimeEvent t = new TimeEvent();
	TimeEvent tParam = new TimeEvent(1,5,new DoNothingAction());
	
	@Test
	public void getUIInfoTest() {
		assertEquals(t.getUIInfo(), "Time Event");
	}
	
	@Test
	public void executeTest() {
		assertNotNull(tParam.getInterval());
		tParam.setInterval(10);
		assertNotNull(tParam.getInterval());
	}

}
