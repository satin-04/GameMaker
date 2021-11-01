/**
 * @Author: Maazin Jawad
 * @CreationDate: 10/1/2021
 * @Editors:
 * @EditedDate:
 **/
package gamemaker.view.components;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;

public class KeyInputBehaviorConfigObject
{
    private KeyCode keyInput;
    private String action, selectedProperty;
    private HBox input;
    private String path;


    public KeyInputBehaviorConfigObject(KeyCode keyInput, String action, String selectedProperty, HBox input, String path) {
        this.keyInput = keyInput;
        this.action = action;
        this.selectedProperty = selectedProperty;
        this.input = input;
        this.path = path;
    }

    public KeyCode getKeyInput() {
        return keyInput;
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

    public String getPath() {
        return path;
    }
}
