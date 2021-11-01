/**
 * @Author: Maazin Jawad
 * @CreationDate: 10/1/2021
 * @Editors:
 * @EditedDate:
 **/
package gamemaker.view.components;

import javafx.scene.layout.HBox;

public class MouseInputBehaviorConfigObject {

    String mouseEvent, action, property;
    HBox input;
    String path;

    public MouseInputBehaviorConfigObject(String mouseEvent, String action, String property, HBox input, String path) {
        this.mouseEvent = mouseEvent;
        this.action = action;
        this.property = property;
        this.input = input;
        this.path = path;
    }

    public String getMouseEvent() {
        return mouseEvent;
    }

    public String getAction() {
        return action;
    }

    public String getProperty() {
        return property;
    }

    public HBox getInput() {
        return input;
    }

    public String getPath() {
        return path;
    }

}
