package gamemaker.view.components;

import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import java.util.Arrays;
import java.util.List;

public class CollisionBehaviorSelector extends MenuButton {
	private static List<String> properties = Arrays.asList("Reflect", "De-Spawn", "End Game");

	public CollisionBehaviorSelector() {
		super("Choose Action");
		properties.forEach(property -> {
			MenuItem choice = new MenuItem(property);
			choice.setOnAction(e -> {
				this.setText(choice.getText());
			});
			this.getItems().add(choice);
		});
	}
}
