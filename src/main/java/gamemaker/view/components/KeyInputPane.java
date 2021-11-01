/**
 * @Author: Maazin Jawad
 * @CreationDate: 10/1/2021
 * @Editors: Pete Fyffe
 * @EditedDate: 10/10/2021
 **/
package gamemaker.view.components;

import gamemaker.Constants;
import gamemaker.GameMakerApplication;
import gamemaker.model.event.Event;
import gamemaker.model.event.KeyCodeEvent;
import gamemaker.utilities.FileManager;
import gamemaker.utilities.KeyCodeParseUtility;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.util.ArrayList;


public class KeyInputPane extends VBox {
    private TextField keyInput;
    private String selectedProperty = Constants.CHOOSE_ACTION; //e.g. "Change Property" or "Move the Sprite"
    private HBox input;
    private MenuButton keyBehavior;
    private Label path;
    private VBox soundSelectionPane;
    private MenuButton propertySelector;


    public KeyInputPane() {
        super();
        this.setLayoutX(Constants.BEHAVIOR_PANE_MARGIN);
        this.setPrefWidth(Constants.BEHAVIOR_PANE_WIDTH);
        this.setPrefHeight(Constants.BEHAVIOR_PANE_HEIGHT);
        HBox selectKeyConfig = new HBox();
        selectKeyConfig.setSpacing(10);

        Label whenKey = new Label("On pressing key, ");

        keyInput = new TextField();
        //Restrict the text field to 1 char
        keyInput.textProperty().addListener((observable, oldValue, newValue) -> {
            //If there is more than one character in the input, only include the first char
            if (newValue.length() > 1)
                keyInput.setText("" + newValue.charAt(0));
            //Capitalize the key
            keyInput.setText(keyInput.getText().toUpperCase());
        });

        selectKeyConfig.getChildren().addAll(whenKey, keyInput);

        keyBehavior = new MenuButton(Constants.CHOOSE_ACTION);
        configureActionChoices();

        this.setSpacing(15);
        this.getChildren().addAll(selectKeyConfig, keyBehavior);

        soundSelectionPane = getSoundSelectionPane();
    }

    HBox propSelectorAndInput;

    MenuItem choice1;
    MenuItem choice2;
    MenuItem choice3;

    private void configureActionChoices() {

        ArrayList<MenuItem> keyActions = new ArrayList<>();
        for(String action: Constants.KEY_ACTIONS){
            MenuItem basicAction = new MenuItem(action);
            basicAction.setOnAction(e->{
                keyBehavior.setText(basicAction.getText());
                propSelectorAndInput.getChildren().clear();
                try {
                    this.getChildren().add(soundSelectionPane);
                } catch (IllegalArgumentException duplicate) {

                }
            });
            keyActions.add(basicAction);
        }
        propSelectorAndInput = new HBox();
        propSelectorAndInput.setSpacing(10);
        input = new HBox();

        choice1 = keyActions.get(0);
        choice2 = keyActions.get(1);
        choice3 = keyActions.get(2);
        choice1.setOnAction(this::choice1SetOnAction);
        choice2.setOnAction(this::choice2SetOnAction);
        choice3.setOnAction(this::choice3SetOnAction);

        keyBehavior.getItems().addAll(keyActions);

    }

    private VBox getSoundSelectionPane() {

        Label playSound = new Label("Sound FX: ");
        Button chooseFile = new Button("Choose File...");

        HBox soundSelectionPane = new HBox();
        soundSelectionPane.setSpacing(25);
        soundSelectionPane.getChildren().addAll(playSound, chooseFile);
        path = new Label(Constants.NO_AUDIO_FILE_SELECTED);
        path.setWrapText(true);
        path.setPrefWidth(300);
        VBox box = new VBox(soundSelectionPane, path);
        box.setPrefWidth(300);
        box.setSpacing(15);
        chooseFile.setOnAction(e -> {
            FileChooser.ExtensionFilter audioFilter = new FileChooser.ExtensionFilter("Audio Files",
                    Constants.AUDIO_TYPES);

            boolean successful = FileManager.getInstance().getLoadFile(this.getScene().getWindow(), audioFilter);
            if (successful) {
                // FileManager.getInstance().getSelectedFile();
                path.setText(FileManager.getInstance().getSelectedFile().getAbsolutePath());
            }
        });

        soundSelectionPane.setAlignment(Pos.CENTER_LEFT);

        return box;
    }
    public void choice1SetOnAction(ActionEvent value) {
        propSelectorAndInput.getChildren().clear();
        input.getChildren().clear();
        keyBehavior.setText(choice1.getText());
        propertySelector = new SettablePropertySelector(input);

        propSelectorAndInput.getChildren().addAll(propertySelector, input);
        try {

            this.getChildren().addAll(propSelectorAndInput, soundSelectionPane);

        } catch (IllegalArgumentException ignored) {

        }
    }


    /**
     * When the user wants to translate the Sprite when the event triggers, show
     * options for changing velocity, position, etc.
     */
    public void choice2SetOnAction(ActionEvent value) {
        propSelectorAndInput.getChildren().clear();
        input.getChildren().clear();
        keyBehavior.setText(choice2.getText());
        propertySelector = new TranslateAblePropertySelector(input);

        propSelectorAndInput.getChildren().addAll(propertySelector, input);
        try {
            this.getChildren().addAll(propSelectorAndInput, soundSelectionPane);
        } catch (IllegalArgumentException ignored) {

        }
    }

    public void choice3SetOnAction(ActionEvent value) {
        propSelectorAndInput.getChildren().clear();
        keyBehavior.setText(choice3.getText());
        Label label = new Label("ID: ");
        TextField id = new TextField();
        input.getChildren().addAll(new HBox(id));
        propSelectorAndInput.getChildren().addAll(label,input);

        try {
            this.getChildren().addAll(propSelectorAndInput, soundSelectionPane);
        } catch (IllegalArgumentException duplicate) {

        }
    }


    /**
     * Reads the menu button text for choosing the precise property/action
     */
    private String getPropertySelectorText() {
        //Return a default value if the view is null
        if (propertySelector == null) {
            GameMakerApplication.logger.fatal("Error: propertySelector was null inside KeyInputPane");
            return Constants.VELOCITY_PROPERTY;
        }

        return propertySelector.getText();
    }

    /**
     * Reads all information from this view and packages it into a
     * KeyInputBehaviorConfigObject.
     */
    public KeyInputBehaviorConfigObject export() {

        return new KeyInputBehaviorConfigObject(
                readKeyInput(), //which key should trigger the key event
                keyBehavior.getText(), //"Change Property", "Movement", etc.
                getPropertySelectorText(), //"Velocity" etc.
                input, //container for other input fields
                path.getText() //sound file
        );
    }

    public KeyCode readKeyInput() {
        //If view is null, return default code
        if (keyInput == null) {
            GameMakerApplication.logger.error("Error: the keyInput view inside KeyInputPane was null. Assuming default key code.");
            return Constants.DEFAULT_KEY_EVENT_CODE;
        }
        //Parse the key code inside the text field
        return KeyCodeParseUtility.parseToKeyCode(keyInput.getText());
    }


    public void rebuildFrom(KeyCodeEvent keyCodeEvent) {

        String key = String.valueOf(keyCodeEvent.getInputTrigger().getChar());
        keyInput.setText(key);

        populateOtherFields(keyCodeEvent);

        if (keyCodeEvent.getAction().getSoundFXFile() != null) {
            path.setText(keyCodeEvent.getAction().getSoundFXFile().getAbsolutePath());
        } else {
            path.setText(Constants.NO_AUDIO_FILE_SELECTED);
        }


    }

    public String populateOtherFields(Event event) {
        String actionUIName = event.getAction().getUIInfo();
        String[] components = actionUIName.split("-");
        String behavior = components[0];
        for (MenuItem menuItem : this.keyBehavior.getItems()) {
            if (menuItem.getText().equals(behavior)) {
                menuItem.fire();
                break;
            }
        }
        if (behavior.equals(Constants.SET_PROPERTY) || behavior.equals(Constants.TRANSLATE_PROPERTY)) {
            String selectedProperty = components[1];

            MenuButton mb = (MenuButton) propSelectorAndInput.getChildren().get(0);
            for (MenuItem menuItem : mb.getItems()) {
                if (menuItem.getText().equals(selectedProperty)) {
                    menuItem.fire();
                    break;
                }
            }
            if (selectedProperty.equals(Constants.VELOCITY_PROPERTY) || selectedProperty.equals(Constants.POSITION_PROPERTY)
                    || selectedProperty.equals(Constants.DIMENSIONS_PROPERTY)) {
                String x = components[2];
                String y = components[3];


                HBox inputContents = (HBox) input.getChildren().get(0);
                TextField fieldX = (TextField) inputContents.getChildren().get(0);
                TextField fieldY = (TextField) inputContents.getChildren().get(1);

                fieldX.setText(x);
                fieldY.setText(y);


            } else if (selectedProperty.equals(Constants.COLOR_PROPERTY)) {
                HBox inputContents = (HBox) input.getChildren().get(0);
                ColorPicker colorPicker = (ColorPicker) inputContents.getChildren().get(0);
                colorPicker.setValue(Color.web(components[3]));
            } else if (selectedProperty.equals(Constants.SHAPE_PROPERTY)) {
                HBox inputContents = (HBox) input.getChildren().get(0);
                ((MenuButton) inputContents.getChildren().get(0)).setText(components[3]);
            } else if (selectedProperty.equals(Constants.IS_VISIBLE_PROPERTY)) {
                HBox inputContents = (HBox) input.getChildren().get(0);
                ((CheckBox) inputContents.getChildren().get(0)).setSelected(Boolean.parseBoolean(components[3]));
            }

        } else if (behavior.equals(Constants.CREATE_SPRITE)) {
            HBox inputContents = (HBox) input.getChildren().get(0);
            TextField fieldID = (TextField) inputContents.getChildren().get(0);
            fieldID.setText(components[1]);
        }

        return behavior;
    }
}