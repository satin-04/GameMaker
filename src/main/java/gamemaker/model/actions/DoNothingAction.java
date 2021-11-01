/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Sep 30, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.model.actions;

import java.util.HashMap;

import com.google.gson.JsonObject;

import gamemaker.Constants;

public class DoNothingAction extends Action {

	public DoNothingAction() { }
	
	@Override
	public void execute(HashMap<String, Object> actionParams) {
	}

	@Override
	public String getUIInfo() {
		return "Do Nothing";
	}
	
	/************************************
	 * 
	 * Savable Implementations
	 *
	 ************************************/
	
	@Override
	public String save(boolean encloseMyself) {
		return Constants.EMPTY_STRING;
	}

	@Override
	public void load(JsonObject jsonObject) {
	}
}
