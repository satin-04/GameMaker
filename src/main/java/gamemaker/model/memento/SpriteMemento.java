/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Oct 1, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.model.memento;

import java.util.HashMap;
import java.util.Iterator;

import gamemaker.model.interfaces.Dumpable;
import gamemaker.model.sprite.Sprite;

public class SpriteMemento implements Dumpable {

	private HashMap<Integer, SpriteState> spriteIdToStateMap;

	public SpriteMemento() {
		spriteIdToStateMap = new HashMap<Integer, SpriteState>();
	}

	public void storeSpritesState(Iterator<Sprite> allSprites) {
		while (allSprites.hasNext()) {
			SpriteState state = new SpriteState();
			Sprite next = allSprites.next();
			state.storeSpriteState(next);
			spriteIdToStateMap.put((Integer) next.getId(), state);
		}
	}

	public void restoreSpritesState(Iterator<Sprite> allSprites) {
		while (allSprites.hasNext()) {
			Sprite next = allSprites.next();
			SpriteState state = spriteIdToStateMap.get(next.getId());
			state.restoreSpriteState(next);
		}
	}

	@Override
	public void dump() {
		spriteIdToStateMap.clear();
		spriteIdToStateMap = null;
	}
}
