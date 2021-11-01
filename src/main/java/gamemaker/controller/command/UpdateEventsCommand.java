/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Oct 2, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.controller.command;

import java.util.LinkedList;

import gamemaker.model.Model;
import gamemaker.model.event.CollisionEvent;
import gamemaker.model.event.KeyCodeEvent;
import gamemaker.model.event.MouseCodeEvent;
import gamemaker.model.event.TimeEvent;

public class UpdateEventsCommand extends Command {

	private Model model;
	private LinkedList<TimeEvent> timeEvents;
	private LinkedList<KeyCodeEvent> keyEvents;
	private LinkedList<MouseCodeEvent> mouseEvents;
	private LinkedList<CollisionEvent> collisionEvents;

	public UpdateEventsCommand(Model model, LinkedList<TimeEvent> timeEvents, LinkedList<KeyCodeEvent> keyEvents,
			LinkedList<MouseCodeEvent> mouseEvents, LinkedList<CollisionEvent> collisionEvents) {
		this.model = model;
		this.timeEvents = timeEvents;
		this.keyEvents = keyEvents;
		this.mouseEvents = mouseEvents;
		this.collisionEvents = collisionEvents;
	}

	@Override
	public void execute() {
		model.updateSelectedSpritesEvents(timeEvents, keyEvents, mouseEvents, collisionEvents);
	}

	@Override
	public void undo() {
	}

	@Override
	public void redo() {
	}
	
	
}
