package gamemaker.view.components;

import gamemaker.Constants;
import gamemaker.GameMakerApplication;
import gamemaker.model.event.Event;
import gamemaker.model.event.TimeEvent;
import gamemaker.utilities.FileManager;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.util.ArrayList;

public class TimeBehaviorPane extends VBox {

    private CheckBox continuous;
    Label every;
    TextField numSeconds;
    Label seconds;

    Label path;
    HBox input;
    MenuButton timeBehavior;
    MenuButton propertySelector;
    VBox soundSelectionPane;
    public HBox getInput() {
        return input;
    }
    public TimeBehaviorPane() {
        super();
        this.setLayoutX(Constants.BEHAVIOR_PANE_MARGIN);
        this.setPrefWidth(Constants.BEHAVIOR_PANE_WIDTH);
        this.setPrefHeight(Constants.BEHAVIOR_PANE_HEIGHT);
        every = new Label("Every");
        numSeconds = new TextField();
        numSeconds.setPrefWidth(30);
        seconds = new Label("second(s): ");
        continuous = new CheckBox("Continuous");
        continuous
                .setTooltip(new Tooltip("Selected action will process every frame. Input for seconds will be ignored"));
        continuous.setLayoutX(60);

        timeBehavior = new MenuButton("Choose Action");
        configureActionChoices();

        HBox everyXseconds = new HBox(every, numSeconds, seconds, continuous);
        everyXseconds.setSpacing(10);

        this.setSpacing(15);
        this.getChildren().addAll(everyXseconds, timeBehavior);

        soundSelectionPane = getSoundSelectionPane();


    }

    public HBox propSelectorAndInput;
    MenuItem choice1;
    MenuItem choice2;
    MenuItem choice3;

    private void configureActionChoices() {

		ArrayList<MenuItem> timeActions = new ArrayList<>();
		for(String action: Constants.TIME_ACTIONS){
			MenuItem basicAction = new MenuItem(action);
			basicAction.setOnAction(e->{
				timeBehavior.setText(basicAction.getText());
				propSelectorAndInput.getChildren().clear();
				try {
					this.getChildren().add(soundSelectionPane);
				} catch (IllegalArgumentException duplicate) {

				}
			});
			timeActions.add(basicAction);
		}
        propSelectorAndInput = new HBox();
        propSelectorAndInput.setSpacing(10);
        input = new HBox();

		choice1 = timeActions.get(0);
		choice2 = timeActions.get(1);
		choice3 = timeActions.get(2);
        choice1.setOnAction(this::choice1SetOnAction);
        choice2.setOnAction(this::choice2SetOnAction);
        choice3.setOnAction(this::choice3SetOnAction);

		timeBehavior.getItems().addAll(timeActions);

    }

    public void choice1SetOnAction(ActionEvent value) {
        propSelectorAndInput.getChildren().clear();
        input.getChildren().clear();
        timeBehavior.setText(choice1.getText());
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
        timeBehavior.setText(choice2.getText());
        propertySelector = new TranslateAblePropertySelector(input);

        propSelectorAndInput.getChildren().addAll(propertySelector, input);
        try {
            this.getChildren().addAll(propSelectorAndInput, soundSelectionPane);
        } catch (IllegalArgumentException ignored) {

        }
    }

    //Display unique UI for Spawn Sprite action
    public void choice3SetOnAction(ActionEvent value) {
        timeBehavior.setText(choice3.getText());
        propSelectorAndInput.getChildren().clear();


        Label label = new Label("ID: ");
        TextField id = new TextField();
       	input.getChildren().addAll(new HBox(id));
		propSelectorAndInput.getChildren().addAll(label,input);

        try {
            this.getChildren().addAll(propSelectorAndInput, soundSelectionPane);
        } catch (IllegalArgumentException duplicate) {

        }
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
    public TimeBehaviorConfigObject export() {

        return new TimeBehaviorConfigObject(numSeconds.getText(), continuous.isSelected(), timeBehavior.getText(),
                getPropertySelectorText(), input, path.getText());
    }

    /**
     *
     * @param timeEvent
     * @return string of the time behavior that was recreated
     */
    public String rebuildFrom(TimeEvent timeEvent) {

        String interval = String.valueOf(timeEvent.getInterval());
        if (interval.equals("-1")) {
            continuous.setSelected(true);
        } else {
            numSeconds.setText(interval);
        }

        String timeBehavior =  populateOtherFields(timeEvent);
        if (timeEvent.getAction().getSoundFXFile() != null) {
            path.setText(timeEvent.getAction().getSoundFXFile().getAbsolutePath());
        }

        return timeBehavior;
    }


    public String populateOtherFields(Event event) {
        String actionUIName = event.getAction().getUIInfo();
        String[] components = actionUIName.split("-");
        String behavior = components[0];
        for (MenuItem menuItem : this.timeBehavior.getItems()) {
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
