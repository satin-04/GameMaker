/**
 * @Author: Maazin Jawad
 * @CreationDate: 9/30/2021
 * @Editors:
 * @EditedDate:
 **/
package gamemaker.view.components;

import javafx.scene.layout.HBox;

public class TimeBehaviorConfigObject {

	private String interval;
	private boolean isContinuous;
	private String action;
	private String selectedProperty;
	private HBox input;

	public String getPath() {
		return path;
	}

	private String path;

	public TimeBehaviorConfigObject(String interval, boolean isContinuous, String action, String selectedProperty,
			HBox input, String path) {
		this.interval = interval;
		this.isContinuous = isContinuous;
		this.action = action;
		this.selectedProperty = selectedProperty;
		this.input = input;
		this.path = path;
	}

	public String getInterval() {
		return interval;
	}

	public boolean isContinuous() {
		return isContinuous;
	}

	public String getAction() {
		return action;
	}

	public String getSelectedProperty() {
		return selectedProperty;
	}

	public HBox getInput() {
		return input;
	}
}
