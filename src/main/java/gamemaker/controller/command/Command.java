/**
 * @author: Ethan Taylor Behar
 * @CreationDate: Sep 25, 2021
 * @editors:
 **/
package gamemaker.controller.command;

public abstract class Command {

	protected boolean pushToUndoStack = true;

	public boolean getPushToUndoStack() {
		return pushToUndoStack;
	}

	public void setPushToUndoStack(boolean pushToUndoStack) {
		this.pushToUndoStack = pushToUndoStack;
	}

	public abstract void execute();

	public abstract void undo();

	public abstract void redo();
}
