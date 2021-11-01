/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Sep 25, 2021
 * @Editors:
 **/
package gamemaker.controller;

import gamemaker.Constants;
import gamemaker.GameMakerApplication;
import gamemaker.controller.command.*;
import gamemaker.model.Model;
import gamemaker.model.actions.*;
import gamemaker.model.event.CollisionEvent;
import gamemaker.model.event.KeyCodeEvent;
import gamemaker.model.event.MouseCodeEvent;
import gamemaker.model.event.TimeEvent;
import gamemaker.model.sprite.Sprite;
import gamemaker.view.CanvasInputHandler;
import gamemaker.view.View;
import gamemaker.view.components.CollisionBehaviorConfigObject;
import gamemaker.view.components.KeyInputBehaviorConfigObject;
import gamemaker.view.components.MouseInputBehaviorConfigObject;
import gamemaker.view.components.TimeBehaviorConfigObject;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Window;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Controller {

    private Model model;
    private View view;
    private CommandInvoker commandInvoker;

    public Controller() {
        model = new Model();
        commandInvoker = new CommandInvoker();
    }

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        this.commandInvoker = new CommandInvoker();
    }

    public CommandInvoker getCommandInvoker() {
        return commandInvoker;
    }

    private void sendCommandToCommandInvoker(Command command, boolean enableUndoButton) {
        if (enableUndoButton) {
            view.enableButton(view.undoBtn);
        }

        // Call the command
        commandInvoker.executeCurrentCommand(command);
    }

    /************************************
     *
     * Sprite Related...
     * The below methods each create one Command, immediately execute it,
     * then store it in the CommandInvoker.
     *
     ************************************/

    public void issueCreateSpriteCommand(Pane gameCanvas) {
        // Build command
        CreateSpriteCommand createSpriteCommand = new CreateSpriteCommand(model);

        sendCommandToCommandInvoker(createSpriteCommand, false);
    }

    public void issueUpdateSelectedSpritePropretiesCommand(HashMap<String, String> propertyToInputValue) {
        //Check if map is null or empty
        if (propertyToInputValue == null || propertyToInputValue.isEmpty())
            return; //Don't create a Command

        // Build command
        try {
            UpdateSelectedSpritePropertiesCommand updateSelectedSpritePropertiesCommand = new UpdateSelectedSpritePropertiesCommand(
                    model, propertyToInputValue);

            sendCommandToCommandInvoker(updateSelectedSpritePropertiesCommand, false);
        }
        //Catch invalid input errors
        catch (NumberFormatException ex) {
            //Parse error
            GameMakerApplication.logger.error("One or more inputs for editing the Sprite could not be parsed");
        } catch (NullPointerException ex) {
            //Color is null error
            GameMakerApplication.logger.error("One or more inputs for editing the Sprite could not be parsed. " +
                    "Perhaps the Color was null?");
        }
    }

    public void issueCreateEventsCommand(List<TimeBehaviorConfigObject> timeBehaviorConfigObjects,
                                         List<KeyInputBehaviorConfigObject> keyBehaviorConfigObjects,
                                         List<MouseInputBehaviorConfigObject> mouseBehaviorConfigObjects,
                                         List<CollisionBehaviorConfigObject> collisionBehaviorConfigObjects) {

        Sprite target = model.getCurrentSelectedSprite();

        //Sprite cannot be null
        if (target == null) {
            GameMakerApplication.logger.fatal("Selected Sprite was null while trying to call controller.issueCreateEventsCommand()");
            return;
        }

        LinkedList<TimeEvent> timeEvents = buildTimeEvents(target, timeBehaviorConfigObjects);
        LinkedList<KeyCodeEvent> keyEvents = buildKeyCodeEvents(target, keyBehaviorConfigObjects);
        LinkedList<MouseCodeEvent> mouseEvents = buildMouseCodeEvents(target, mouseBehaviorConfigObjects);
        LinkedList<CollisionEvent> collisionEvents = buildCollisionEvents(target, collisionBehaviorConfigObjects);

        UpdateEventsCommand updateEventsCommand = new UpdateEventsCommand(model, timeEvents, keyEvents, mouseEvents,
                collisionEvents);

        sendCommandToCommandInvoker(updateEventsCommand, false);
    }

    private LinkedList<TimeEvent> buildTimeEvents(Sprite target,
                                                  List<TimeBehaviorConfigObject> timeBehaviorConfigObjects) {
        LinkedList<TimeEvent> timeEvents = new LinkedList<TimeEvent>();

        for (TimeBehaviorConfigObject timeBehavior : timeBehaviorConfigObjects) {
            timeEvents.add(buildTimeEvent(target, timeBehavior));
        }

        return timeEvents;
    }

    private TimeEvent buildTimeEvent(Sprite target, TimeBehaviorConfigObject timeEventParams) {
        String interval = timeEventParams.isContinuous() ? "-1" : timeEventParams.getInterval();
        String action = timeEventParams.getAction();
        String selectedProperty = timeEventParams.getSelectedProperty();
        File soundFxFile;
        if (timeEventParams.getPath().compareToIgnoreCase(Constants.NO_AUDIO_FILE_SELECTED) != 0) {
            soundFxFile = new File(timeEventParams.getPath());
        } else {
            soundFxFile = null;
        }

        HBox input = timeEventParams.getInput();
        Action actionToMake;

        if (action.equals(Constants.TRANSLATE_PROPERTY)) {
            actionToMake = makeTranslatePropertyAction(target, selectedProperty, soundFxFile, input);
        } else if (action.equals(Constants.SET_PROPERTY)) {
            actionToMake = makeSetPropertyAction(target, selectedProperty, soundFxFile, input);
        } else if (action.equals(Constants.CREATE_SPRITE)) {
            HBox idInput = ((HBox) (input.getChildren().get(0)));

            int id = Integer.parseInt(((TextField) idInput.getChildren().get(0)).getText());
            //Sprite toCreate = model.getSpriteById(id);
            actionToMake = new CloneSpriteAction(target, soundFxFile, model, id);

        } else {
            throw new UnsupportedOperationException("Unknown Action");
        }

        return new TimeEvent(target.getId(), Integer.parseInt(interval), actionToMake);
    }

    private LinkedList<KeyCodeEvent> buildKeyCodeEvents(Sprite target,
                                                        List<KeyInputBehaviorConfigObject> keyBehaviorConfigObjects) {
        LinkedList<KeyCodeEvent> keyEvents = new LinkedList<KeyCodeEvent>();

        for (KeyInputBehaviorConfigObject keyBehavior : keyBehaviorConfigObjects) {
            keyEvents.add(buildKeyEvent(target, keyBehavior));
        }

        return keyEvents;
    }

    private KeyCodeEvent buildKeyEvent(Sprite target, KeyInputBehaviorConfigObject keyEventParams) {

        KeyCode inputTrigger = keyEventParams.getKeyInput();
        String action = keyEventParams.getAction();
        String selectedProperty = keyEventParams.getSelectedProperty();
        File soundFxFile;
        if (keyEventParams.getPath().compareToIgnoreCase(Constants.NO_AUDIO_FILE_SELECTED) != 0) {
            soundFxFile = new File(keyEventParams.getPath());
        } else {
            soundFxFile = null;
        }

        HBox input = keyEventParams.getInput();

        Action actionToMake;

        if (action.equals(Constants.TRANSLATE_PROPERTY)) {
            actionToMake = makeTranslatePropertyAction(target, selectedProperty, soundFxFile, input);
        } else if (action.equals(Constants.SET_PROPERTY)) {
            actionToMake = makeSetPropertyAction(target, selectedProperty, soundFxFile, input);
        } else if (action.equals(Constants.CREATE_SPRITE)) {
            HBox idInput = ((HBox) (input.getChildren().get(0)));

            int id = Integer.parseInt(((TextField) idInput.getChildren().get(0)).getText());
            //Sprite toCreate = model.getSpriteById(id);
            actionToMake = new CloneSpriteAction(target, soundFxFile, model, id);

        } else {
            throw new UnsupportedOperationException("Unknown Action");
        }
        return new KeyCodeEvent(target.getId(), inputTrigger, actionToMake);
    }

    private Action makeSetPropertyAction(Sprite target, String selectedProperty, File soundFxFile, HBox input) {
        boolean isXYInput = selectedProperty.equals(Constants.VELOCITY_PROPERTY) || selectedProperty.equals(Constants.POSITION_PROPERTY) ||
                selectedProperty.equals(Constants.DIMENSIONS_PROPERTY);
        if (isXYInput) {

            //Read the x/y velocity inputs from the text fields
            HBox entryFields = (HBox) (input.getChildren().get(0));
            TextField setXInput = (TextField) (entryFields.getChildren().get(0));
            TextField setYInput = (TextField) (entryFields.getChildren().get(1));

            double setX = Double.parseDouble(setXInput.getText());
            double setY = Double.parseDouble(setYInput.getText());


            //Create velocity action
            if (selectedProperty.equals(Constants.VELOCITY_PROPERTY)) {
                //Create a SetVelocityAction that will hard-set the velocity of the Sprite
                return new SetVelocityAction(target, soundFxFile, setX, setY);
            }
            //Create move action
            else if (selectedProperty.equals(Constants.POSITION_PROPERTY)) {
                //Create a SetPositionAction that will actually move the Sprite
                return new SetPositionAction(target, soundFxFile, setX, setY);
            }
        }

        throw new UnsupportedOperationException("Controller could not build SetPropertyAction from input");
    }

    private Action makeTranslatePropertyAction(Sprite target, String selectedProperty, File soundFxFile, HBox input) {

        boolean isXYInput = selectedProperty.equals(Constants.VELOCITY_PROPERTY) || selectedProperty.equals(Constants.POSITION_PROPERTY) ||
                selectedProperty.equals(Constants.DIMENSIONS_PROPERTY);
        //Read the views for moving or changing the velocity of the Sprite by user-specified amounts
        if (isXYInput) {
            //Read the x/y velocity inputs from the text fields
            HBox entryFields = (HBox) (input.getChildren().get(0));
            TextField translateXInput = (TextField) (entryFields.getChildren().get(0));
            TextField translateYInput = (TextField) (entryFields.getChildren().get(1));

            double translateX = Double.parseDouble(translateXInput.getText());
            double translateY = Double.parseDouble(translateYInput.getText());

            Action newAction;
            //Create velocity action
            if (selectedProperty.equals(Constants.VELOCITY_PROPERTY)) {
                //Create a TranslateVelocityAction that will customize the velocity of the Sprite
                return new TranslateVelocityAction(target, soundFxFile, translateX, translateY);
            }
            //Create move action
            else if (selectedProperty.equals(Constants.POSITION_PROPERTY)) {
                //Create a MoveByForceAction that will actually move the Sprite
                return new MoveByForceAction(target, soundFxFile, translateX, translateY);
            }

        }

        throw new UnsupportedOperationException("MakeTranslateAction did not know what to create");
    }

    private LinkedList<MouseCodeEvent> buildMouseCodeEvents(Sprite target,
                                                            List<MouseInputBehaviorConfigObject> mouseBehaviorConfigObjects) {
        LinkedList<MouseCodeEvent> mouseEvents = new LinkedList<MouseCodeEvent>();

        for (MouseInputBehaviorConfigObject mouseBehavior : mouseBehaviorConfigObjects) {
            mouseEvents.add(buildMouseEvent(target, mouseBehavior));
        }

        return mouseEvents;
    }

    private MouseCodeEvent buildMouseEvent(Sprite target, MouseInputBehaviorConfigObject mouseEventParams) {
        GameMakerApplication.logger.fatal("Do not know how to build an mouse event");
        throw new UnsupportedOperationException("Do not know how to build an mouse event");
    }

    private LinkedList<CollisionEvent> buildCollisionEvents(Sprite target,
                                                            List<CollisionBehaviorConfigObject> collisionBehaviorConfigObjects) {
        LinkedList<CollisionEvent> collisionEvents = new LinkedList<CollisionEvent>();

        for (CollisionBehaviorConfigObject collisionBehavior : collisionBehaviorConfigObjects) {
            collisionEvents.add(buildCollisionEvent(target, collisionBehavior));
        }

        return collisionEvents;
    }

    private CollisionEvent buildCollisionEvent(Sprite target, CollisionBehaviorConfigObject collisionEventParams) {
        CollisionEvent collisionEvent;

        HashMap<String, Constants.CollisionType> conditionToType = new HashMap<>();
        conditionToType.put(Constants.ON_HIT_GAMEPLAY_BOUNDS, Constants.CollisionType.SCREEN);
        conditionToType.put(Constants.ON_GET_HIT, Constants.CollisionType.GET_HIT);
        conditionToType.put(Constants.ON_HIT_OTHER_SPRITE, Constants.CollisionType.OBJECT);
        String condition = collisionEventParams.getCondition();
        String action = collisionEventParams.getAction();
        File soundFxFile;
        if (collisionEventParams.getPath().compareToIgnoreCase(Constants.NO_AUDIO_FILE_SELECTED) != 0) {
            soundFxFile = new File(collisionEventParams.getPath());
        } else {
            soundFxFile = null;
        }

        if (action.compareToIgnoreCase(Constants.REFLECT) == 0) {
            ReflectAction reflectAction = new ReflectAction(target, soundFxFile);
            return new CollisionEvent(conditionToType.get(condition), reflectAction);
        }
		else if (action.compareToIgnoreCase(Constants.DESTROY_SELF) == 0) {
            DestroySelfAction destroySelfAction = new DestroySelfAction(target, soundFxFile);
            return new CollisionEvent(conditionToType.get(condition), destroySelfAction);
        }
		else if (action.compareToIgnoreCase(Constants.DESTROY_OTHER) == 0) {
            DestroyOtherAction destroyOtherAction = new DestroyOtherAction(target, soundFxFile);
            return new CollisionEvent(conditionToType.get(condition), destroyOtherAction);
        }
		else if (action.compareToIgnoreCase(Constants.END_GAME_DEFEAT) == 0) {
            EndGameDefeatAction endGameDefeatAction = new EndGameDefeatAction(target, soundFxFile);
            return new CollisionEvent(conditionToType.get(condition), endGameDefeatAction);
        }
		else if (action.compareToIgnoreCase(Constants.END_GAME_VICTORY) == 0) {
            EndGameVictoryAction endGameVictoryAction = new EndGameVictoryAction(target, soundFxFile);
            return new CollisionEvent(conditionToType.get(condition), endGameVictoryAction);
        }


        GameMakerApplication.logger.fatal("Do not know how to build " + condition + " condition with action " + action);
        throw new UnsupportedOperationException(
                "Do not know how to build " + condition + " condition with action " + action);
    }

    /************************************
     *
     * Background
     *
     ************************************/

    public void issueUpdateBackgroundProperties(HashMap<String, String> propToInput) {
        // Unbox input for command
        Color color = Color.web(propToInput.get(Constants.COLOR_KEY));
        String backgroundTrackPath = propToInput.get(Constants.AUDIO_PATH_KEY);
        File backgroundTrack;
        if (backgroundTrackPath.compareToIgnoreCase(Constants.EMPTY_STRING) == Constants.ZERO) {
            backgroundTrack = null;
        } else {
            backgroundTrack = new File(backgroundTrackPath);
        }

        UpdateBackgroundPropertiesCommand updateBackgroundProperties = new UpdateBackgroundPropertiesCommand(model,
                color, backgroundTrack);

        sendCommandToCommandInvoker(updateBackgroundProperties, false);
    }

    /************************************
     *
     * Audio Selecting
     *
     ************************************/

    public void issueGetBackgroundTrackCommand(Window appWindow) {
        SelectBackgroundTrackCommand selectBackgroundTrackCommand = new SelectBackgroundTrackCommand(view, appWindow);

        sendCommandToCommandInvoker(selectBackgroundTrackCommand, false);
    }

    /************************************
     *
     * Right Side Buttons
     *
     ************************************/

    public void issuePlayGameCommand(CanvasInputHandler canvasInputHandler) {
        // Build command
        PlayGameCommand playGameCommand = new PlayGameCommand(model, canvasInputHandler);

        sendCommandToCommandInvoker(playGameCommand, false);
    }

    public void issueStopGameCommand(CanvasInputHandler canvasInputHandler) {
        // Build command
        StopGameCommand stopGameCommand = new StopGameCommand(model, canvasInputHandler);

        sendCommandToCommandInvoker(stopGameCommand, false);
    }

    public void issueSaveCommand() {
        SaveCommand saveCommand = new SaveCommand(model);

        sendCommandToCommandInvoker(saveCommand, false);
    }

    public void issueSaveAsCommand(Window appWindow) {
        SaveAsCommand saveAsCommand = new SaveAsCommand(model, view, appWindow);

        sendCommandToCommandInvoker(saveAsCommand, false);
    }

    public void issueLoadCommand(Window appWindow) {
        LoadCommand loadCommand = new LoadCommand(model, view, appWindow);

        sendCommandToCommandInvoker(loadCommand, false);
    }

    public void undoLastCommand() {
        if (commandInvoker.undosAvailable()) {
            commandInvoker.undoCommand();
            view.enableButton(view.redoBtn);
            if (!commandInvoker.undosAvailable()) {
                view.disableButton(view.undoBtn);
            }
        } else {
            view.disableButton(view.undoBtn);
        }
    }

    public void redoLastCommand() {
        if (commandInvoker.redosAvailable()) {
            commandInvoker.redoCommand();
            view.enableButton(view.undoBtn);
            if (!commandInvoker.redosAvailable()) {
                view.disableButton(view.redoBtn);
            }
        } else {
            view.disableButton(view.redoBtn);
        }
    }

    /************************************
     *
     * Non-Button Invoked i.e. Selecting/Dragging a shape
     *
     ************************************/

    public void issueSpriteSelectedCommand(Node target) {
        // Each shape's id is set to the sprite id
        String id = target.getId();
        int spriteId = Integer.parseInt(id);

        // Get sprite based on id
        Sprite newSelectedSprite = model.getSpriteById(spriteId);

        // Null check in case
        if (newSelectedSprite == null) {
            GameMakerApplication.logger.error("The selected node cannot be cast to sprite!");
            return;
        }

        // Ignore request if sprite is already selected
        if (model.isSpriteTheSelectedSprite(newSelectedSprite)) {
            GameMakerApplication.logger
                    .info("CommandInvoker denying SpriteSelectedCommand b/c selected sprite is already the target.");
            return;
        }

        // Build Command
        SpriteSelectedCommand shapeSelectedCommand = new SpriteSelectedCommand(model, newSelectedSprite);

        sendCommandToCommandInvoker(shapeSelectedCommand, false);
    }

    public void issueSpriteDragCommand(double translateX, double translateY) {
        // Build Command
        SpriteDraggedCommand spriteDraggedCommand = new SpriteDraggedCommand(model, translateX, translateY);

        sendCommandToCommandInvoker(spriteDraggedCommand, false);
    }

    public void issueSpriteReleasedCommand(double layoutX, double layoutY, double initialX, double initialY) {
        // Build Command
        SpriteReleasedCommand spriteReleasedCommand = new SpriteReleasedCommand(model, layoutX, layoutY);
        // , initialX, initialY);

        sendCommandToCommandInvoker(spriteReleasedCommand, false);
    }
}
