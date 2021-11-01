package gamemaker.view.components;

import gamemaker.Constants;
import gamemaker.model.actions.MoveByForceAction;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.util.Arrays;
import java.util.List;

public class TranslateAblePropertySelector extends MenuButton {

    public static final String INITIAL_TEXT = "Choose Property";



    public TranslateAblePropertySelector(HBox container) {
        super(INITIAL_TEXT);
        Constants.TRANSLATABLE_PROPERTIES.forEach(property -> {
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
     * @param choice
     * @return
     */
    private Node processSelection(String choice) {
            HBox dualInput = new HBox();
            dualInput.setSpacing(5);
            TextField inputX = new TextField();
            inputX.setTooltip(new Tooltip("Enter X"));
            inputX.setPrefWidth(50);
            TextField inputY = new TextField();
            inputY.setPrefWidth(50);
            inputY.setTooltip(new Tooltip("Enter Y"));
            dualInput.getChildren().addAll(inputX,inputY);
            return dualInput;

    }
}
