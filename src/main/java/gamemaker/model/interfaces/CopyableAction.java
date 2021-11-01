/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Oct 2, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.model.interfaces;

import gamemaker.model.actions.Action;

/**
 ** Designed to return a new instance of T with 'this' T's properties
 **/
public interface CopyableAction {
	public Action copy();
}
