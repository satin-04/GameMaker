package gamemaker.view.components;

import gamemaker.Constants;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.util.Arrays;
import java.util.List;

public class SettablePropertySelector extends MenuButton {


	public SettablePropertySelector(HBox container) {
		super("Choose Property");
		Constants.SETTABLE_PROPERTIES.forEach(property -> {
			MenuItem choice = new MenuItem(property);
			choice.setOnAction(e -> {
				container.getChildren().clear();
				this.setText(choice.getText());
				container.getChildren().add(processSelection(property));
			});
			this.getItems().add(choice);
		});
	}

	/**
	 * Based on the
	 * 
	 * @param choice
	 * @return
	 */
	private Node processSelection(String choice) {
		if (choice.equals(Constants.TEXT_PROPERTY)) {
			return new TextField();
		} else if (choice.equals(Constants.COLOR_PROPERTY)) {
			ColorPicker cp = new ColorPicker();
			cp.setPrefHeight(45);
			return cp;
		} else if (choice.equals(Constants.SHAPE_PROPERTY)) {
			MenuButton shapeSelector = new MenuButton("Select Shape");
			for (String s : Arrays.asList("Rectangle", "Circle", "Text")) {
				MenuItem shape = new MenuItem(s);
				shape.setOnAction(e -> shapeSelector.setText(shape.getText()));
				shapeSelector.getItems().add(shape);
			}
			return shapeSelector;
		} else if (choice.equals(Constants.IS_VISIBLE_PROPERTY)) {
			CheckBox cb = new CheckBox("Visible");
			cb.setSelected(true);
			return cb;
		}else if(choice.equals(Constants.IS_BULLET_PROPERTY)){
			CheckBox cb = new CheckBox("Is Bullet");
			cb.setSelected(false);
			return cb;
		}
		else
		 {
			HBox dualInput = new HBox();
			dualInput.setSpacing(5);
			TextField inputX = new TextField();
			inputX.setTooltip(new Tooltip("Enter X"));
			inputX.setPrefWidth(50);
			TextField inputY = new TextField();
			inputY.setPrefWidth(50);
			inputY.setTooltip(new Tooltip("Enter Y"));
			dualInput.getChildren().addAll(inputX, inputY);
			return dualInput;
		}
	}

}
