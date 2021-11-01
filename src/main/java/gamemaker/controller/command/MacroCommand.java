/**
 * @author: Ethan Taylor Behar
 * @CreationDate: Sep 25, 2021
 * @editors:
 **/
package gamemaker.controller.command;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class MacroCommand extends Command {

	private Queue<Command> commands;

	public MacroCommand() {
		commands = new LinkedList<Command>();
	}

	public Iterator<Command> getCommandIterator() {
		return commands.iterator();
	}

	public void addCommand(Command command) {
		commands.add(command);
	}

	@Override
	public void execute() {
		for (Command command : commands) {
			command.execute();
		}
	}

	@Override
	public void undo() {
		for (Command command : commands) {
			command.undo();
		}
	}

	@Override
	public void redo() {
		for (Command command : commands) {
			command.redo();
		}
	}
}
