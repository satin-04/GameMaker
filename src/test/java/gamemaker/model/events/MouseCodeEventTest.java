package gamemaker.model.events;

import gamemaker.model.event.MouseCodeEvent;
import gamemaker.Constants.MouseButtonCode;
import gamemaker.Constants;
import gamemaker.model.actions.DoNothingAction;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import static org.junit.Assert.*;

public class MouseCodeEventTest {
	MouseCodeEvent m = new MouseCodeEvent();
	MouseCodeEvent mParam = new MouseCodeEvent(1,MouseButtonCode.MIDDLE,new DoNothingAction());
	
	@Test
	public void getUIInfoTest() {
		assertEquals(m.getUIInfo(), "MouseCode Event");
	}
	
	@Test
	public void executeTest() {
		assertNotNull(mParam.getInputTrigger());
		mParam.setInputTrigger(MouseButtonCode.MIDDLE);
		assertNotNull(mParam.getInputTrigger());
	}
}
