/**
 * @author: Ethan Taylor Behar
 * @CreationDate: Sep 25, 2021
 * @editors:
 **/
package gamemaker.controller.command;

public class NoCommand extends Command {

	public NoCommand() {
		setPushToUndoStack(false);
	}
	
	@Override
	public void execute() {
		// Nothing!
	}

	@Override
	public void undo() {
		// Nothing!	
	}

	@Override
	public void redo() {
		// Nothing!
	}
}
