/**
 * @Author: Maazin Jawad
 * @CreationDate: 10/1/2021
 * @Editors:
 * @EditedDate:
 **/
package gamemaker.view.components;

import gamemaker.Constants;
import gamemaker.utilities.FileManager;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;


public class MouseInputPane extends VBox {
    private  MenuButton mouseInput;
    private String selectedProperty;
    private HBox input;
    MenuButton inputBehavior;


    private Label path;

    public MouseInputPane(){
        super();
        this.setLayoutX(Constants.BEHAVIOR_PANE_MARGIN);
        this.setPrefWidth(Constants.BEHAVIOR_PANE_WIDTH);
        this.setPrefHeight(Constants.BEHAVIOR_PANE_HEIGHT);
        HBox selectKeyConfig = new HBox();
        selectKeyConfig.setSpacing(10);

        Label whenKey = new Label("On mouse ");
        mouseInput = new MenuButton();
        mouseInput.getItems().addAll(new MenuItem("Clicked"), new MenuItem("Moved"));
        mouseInput.getItems().forEach(choice->choice.setOnAction(e->mouseInput.setText(choice.getText())));


        selectKeyConfig.getChildren().addAll(whenKey, mouseInput);

        inputBehavior = new MenuButton("Choose Action");
        configureActionChoices();

        this.setSpacing(15);
        this.getChildren().addAll(selectKeyConfig, inputBehavior);


    }

    private void configureActionChoices() {
        MenuItem choice1 = new MenuItem("Set Property");
        MenuItem choice2 = new MenuItem("Translate Property");
        // MenuItem choice3 = new MenuItem("Reflect");

        HBox propSelectorAndInput = new HBox();
        propSelectorAndInput.setSpacing(10);
        input = new HBox();

        choice1.setOnAction(e -> {
            propSelectorAndInput.getChildren().clear();
            input.getChildren().clear();
            inputBehavior.setText(choice1.getText());
            // this.getChildren().add(new PropertySelector(hbox));
            SettablePropertySelector settablePropertySelector = new SettablePropertySelector(input);
            selectedProperty = settablePropertySelector.getText();
            propSelectorAndInput.getChildren().addAll(settablePropertySelector, input);
            try {
                this.getChildren().add(propSelectorAndInput);
            } catch (IllegalArgumentException ignored) {

            }
        });

        choice2.setOnAction(e -> {
            propSelectorAndInput.getChildren().clear();
            input.getChildren().clear();
            inputBehavior.setText(choice2.getText());
            // this.getChildren().add(new PropertySelector(hbox));
            TranslateAblePropertySelector settablePropertySelector = new TranslateAblePropertySelector(input);
            selectedProperty = settablePropertySelector.getText();
            propSelectorAndInput.getChildren().addAll(settablePropertySelector, input);
            try {
                this.getChildren().add(propSelectorAndInput);
            } catch (IllegalArgumentException ignored) {

            }
        });

        inputBehavior.getItems().addAll(choice1, choice2);

    }

    // commented out to disable warning
//    private VBox getSoundSelectionPane() {
//
//        Label playSound = new Label("Sound FX: ");
//        Button chooseFile = new Button("Choose File...");
//
//        HBox soundSelectionPane = new HBox();
//        soundSelectionPane.setSpacing(25);
//        soundSelectionPane.getChildren().addAll(playSound,chooseFile);
//        path = new Label("No file selected");
//        path.setWrapText(true);
//        path.setPrefWidth(300);
//        VBox box = new VBox(soundSelectionPane, path);
//        box.setPrefWidth(300);
//        box.setSpacing(15);
//        chooseFile.setOnAction(e->
//        {
//            FileChooser.ExtensionFilter audioFilter = new FileChooser.ExtensionFilter("Audio Files", Constants.AUDIO_TYPES);
//
//            boolean successful = FileManager.getInstance().getLoadFile(this.getScene().getWindow(), audioFilter);
//            if (successful) {
//                path.setText(FileManager.getInstance().getSelectedFile().getAbsolutePath());
//            }
//        });
//
//        soundSelectionPane.setAlignment(Pos.CENTER_LEFT);
//
//        return box;
//    }


    public void setPath(Label path) {
        this.path = path;
    }
    public MouseInputBehaviorConfigObject export(){
        return new MouseInputBehaviorConfigObject(mouseInput.getText(),inputBehavior.getText(),selectedProperty,input,path.getText());
    }



}
