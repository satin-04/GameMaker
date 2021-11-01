/**
 * @Author: Maazin Jawad
 * @CreationDate: 10/1/2021
 * @Editors:
 * @EditedDate:
 **/
package gamemaker.view.components;

import javafx.scene.layout.HBox;

public class CollisionBehaviorConfigObject {

    private String condition,action;
    private HBox input;
    private String path;
    private String selectedProperty;
    public CollisionBehaviorConfigObject(String condition, String action, String selectedProperty, HBox input, String path) {
        this.condition = condition;
        this.action = action;
        this.input = input;
        this.path = path;
        this.selectedProperty = selectedProperty;
    }

    public String getCondition() {
        return condition;
    }

    public String getAction() {
        return action;
    }

    public HBox getInput() {
        return input;
    }

    public String getPath() {
        return path;
    }

    public String getSelectedProperty(){
        return selectedProperty;
    }
}
