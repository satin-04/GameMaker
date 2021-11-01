/**
 * @author: Ethan Taylor Behar
 * @CreationDate: Sep 25, 2021
 * @editors:
 **/
package gamemaker.controller.command;

import java.util.Stack;

public class CommandInvoker {

	private Command currentCommand;
	private Stack<Command> undoCommandList;
	private Stack<Command> redoCommandList;

	public CommandInvoker() {
		currentCommand = new NoCommand();
		undoCommandList = new Stack<Command>();
		redoCommandList = new Stack<Command>();
	}

	public void executeCurrentCommand(Command command) {
		currentCommand = command;
		currentCommand.execute();
		if (currentCommand.getPushToUndoStack()) {
			undoCommandList.push(currentCommand);
		}
	}

	public Command getCurrentCommand() {
		return currentCommand;
	}

	public boolean undosAvailable() {
		return !undoCommandList.isEmpty();
	}

	public void undoCommand() {
		Command undoCommand = undoCommandList.pop();
		redoCommandList.push(undoCommand);
		undoCommand.undo();
	}

	public boolean redosAvailable() {
		return !redoCommandList.isEmpty();
	}

	public void redoCommand() {
		Command redoCommand = redoCommandList.pop();
		undoCommandList.push(redoCommand);
		redoCommand.redo();
	}
}
