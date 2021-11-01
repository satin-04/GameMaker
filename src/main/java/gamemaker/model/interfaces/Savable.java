/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Sep 25, 2021
 * @Editors:
 **/

package gamemaker.model.interfaces;

import com.google.gson.JsonObject;

public interface Savable {
	public String save(boolean encloseMyself);
	public void load(JsonObject jsonObject);
}
