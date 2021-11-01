package gamemaker.model.events;

import gamemaker.model.event.KeyCodeEvent;
import gamemaker.model.actions.DoNothingAction;
import javafx.scene.input.KeyCode;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import static org.junit.Assert.*;


public class KeyCodeEventTest {
	KeyCodeEvent k = new KeyCodeEvent();
	KeyCodeEvent kParam = new KeyCodeEvent(1, KeyCode.UP , new DoNothingAction());
	
	
	@Test
	public void getUIInfoTest() {
		assertEquals(k.getUIInfo(), "KeyCode Event");
	}
	
	@Test
	public void executeTest() {
		assertNotNull(kParam.getInputTrigger());
		kParam.setInputTrigger(KeyCode.UP);
		assertNotNull(kParam.getInputTrigger());
	}
}
