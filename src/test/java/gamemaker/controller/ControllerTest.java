package gamemaker.controller;

import de.saxsys.javafx.test.JfxRunner;
import gamemaker.Constants;
import gamemaker.controller.Controller;
import gamemaker.controller.command.*;
import gamemaker.model.Model;
import gamemaker.model.actions.Action;
import gamemaker.model.actions.TranslateVelocityAction;
import gamemaker.model.event.CollisionEvent;
import gamemaker.model.event.KeyCodeEvent;
import gamemaker.model.event.MouseCodeEvent;
import gamemaker.model.event.TimeEvent;
import gamemaker.model.sprite.Sprite;
import gamemaker.view.View;
import gamemaker.view.components.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import static org.junit.Assert.*;


@RunWith(JfxRunner.class) //allows testing with JavaFX components
public class ControllerTest
{
    /**
     * Creates and returns a mock View. It overrides some methods
     * to do nothing so that we can run tests without the UI itself.
     */
    private View mockView()
    {
        return new View(){
            @Override
            public void enableButton(Button btn) {
                //Do nothing
            }
            @Override
            public void updateBasedOnSelectedSprite(Sprite selectedSprite, Iterator<TimeEvent> spritesTimeEvents, Iterator<KeyCodeEvent> spritesKeyEvents, Iterator<MouseCodeEvent> spritesMouseEvents, Iterator<CollisionEvent> spritesCollisionEvents) {
                //Do nothing
            }
        };
    }

    /**
     * Creates and returns a mock Controller that contains a mock Model, mock View,
     * and CommandInvoker.
     */
    private Controller mockController()
    {
        //Mock the view
        View view = mockView();
        //Mock the model
        Model model = new Model(view);

        //Set up a test controller
        Controller controller = new Controller(model, view);

        return controller;
    }

    /**
     * Ensures that a CreateSpriteCommand is created
     * and that it has a non-null Sprite.
     */
    @Test
    public void testIssueCreateSpriteCommand()
    {
        //Create a mock controller
        Controller controller = mockController();
        //Get its invoker
        CommandInvoker invoker = controller.getCommandInvoker();

        //Test issueCreateSpriteCommand()
        controller.issueCreateSpriteCommand(new Pane());

        //Check if most recent command is the command is the CreateSpriteCommand
        Command mostRecentCommand = invoker.getCurrentCommand();
        assertTrue(mostRecentCommand instanceof CreateSpriteCommand);

        //Check if mostRecentCommand has a new Sprite
        assertNotNull(((CreateSpriteCommand) mostRecentCommand).getSprite());
    }


    /**
     * Emsures the correctness of a UpdateSelectedSpritePropertiesCommand that is created
     * when updating the properties of a Sprite.
     */
    @Test
    public void testIssueUpdateSelectedSpritePropertiesCommand()
    {
        //Create a mock controller
        Controller controller = mockController();
        //Get its invoker
        CommandInvoker invoker = controller.getCommandInvoker();

        //Test issueUpdateSelectedSpritePropretiesCommand() with blank HashMap
        HashMap<String, String> propertyToInputValue = new HashMap<>();
        controller.issueUpdateSelectedSpritePropretiesCommand(propertyToInputValue);

        //Ensure a new command has not been created
        Command mostRecentCommand = invoker.getCurrentCommand();
        assertTrue(mostRecentCommand instanceof NoCommand);

        //Now test with data gathered from the view
        propertyToInputValue.put(Constants.LABEL_KEY, "This is my Sprite's label");
        propertyToInputValue.put(Constants.POSITION_X_KEY, "10");
        propertyToInputValue.put(Constants.POSITION_Y_KEY, "3");
        propertyToInputValue.put(Constants.VELOCITY_X_KEY, "1");
        propertyToInputValue.put(Constants.VELOCITY_Y_KEY, "2");
        propertyToInputValue.put(Constants.DIMENSIONS_X_KEY, "42");
        propertyToInputValue.put(Constants.DIMENSIONS_Y_KEY, "72");
        propertyToInputValue.put(Constants.COLOR_KEY, Color.RED.toString());
        propertyToInputValue.put(Constants.SHAPE_KEY, Constants.CIRCLE);
        propertyToInputValue.put(Constants.VISIBLE_KEY, "true");
        propertyToInputValue.put(Constants.TEXT_KEY, "This is my circular Sprite.");

        //Create a UpdateSelectedSpritePropertiesCommand using the data above
        UpdateSelectedSpritePropertiesCommand testCommand = new UpdateSelectedSpritePropertiesCommand(new Model(), propertyToInputValue);

        //Test issueCreateSpriteCommand() with the data above
        controller.issueUpdateSelectedSpritePropretiesCommand(propertyToInputValue);

        //Check if most recent command is the command is the UpdateSelectedSpritePropertiesCommand
        mostRecentCommand = invoker.getCurrentCommand();
        assertTrue(mostRecentCommand instanceof UpdateSelectedSpritePropertiesCommand);

        //Check if mostRecentCommand has the properties we put into testCommand
        assertEquals(mostRecentCommand, testCommand);
    }

    /**
     * Ensures that a Command is not created when trying to customize a
     * Sprite with invalid property inputs.
     */
    @Test
    public void testIssueUpdateSelectedSpritePropretiesCommand_InvalidInput()
    {
        //Create a mock controller
        Controller controller = mockController();
        //Get its invoker
        CommandInvoker invoker = controller.getCommandInvoker();

        //Create a map that stores user input data
        HashMap<String, String> propertyToInputValue = new HashMap<>();
        propertyToInputValue.put(Constants.LABEL_KEY, "This is my Sprite's label");
        propertyToInputValue.put(Constants.POSITION_X_KEY, "10");
        propertyToInputValue.put(Constants.POSITION_Y_KEY, "3");
        propertyToInputValue.put(Constants.VELOCITY_X_KEY, "1");
        propertyToInputValue.put(Constants.VELOCITY_Y_KEY, "2");
        propertyToInputValue.put(Constants.DIMENSIONS_X_KEY, "42");
        propertyToInputValue.put(Constants.DIMENSIONS_Y_KEY, "72");
        propertyToInputValue.put(Constants.SHAPE_KEY, Constants.CIRCLE);
        propertyToInputValue.put(Constants.VISIBLE_KEY, "true");
        propertyToInputValue.put(Constants.TEXT_KEY, "This is my circular Sprite.");

        //Test issueCreateSpriteCommand() using null data
        propertyToInputValue.put(Constants.COLOR_KEY, null);
        controller.issueUpdateSelectedSpritePropretiesCommand(propertyToInputValue);
        //Ensure no command has been created
        Command mostRecentCommand = invoker.getCurrentCommand();
        assertTrue(mostRecentCommand instanceof NoCommand);

        //Test issueCreateSpriteCommand() using non-parse-able data
        propertyToInputValue.put(Constants.POSITION_X_KEY, "3.000000000000000000000000000000000000000000015");
        controller.issueUpdateSelectedSpritePropretiesCommand(propertyToInputValue);
        //Ensure no command has been created
        mostRecentCommand = invoker.getCurrentCommand();
        assertTrue(mostRecentCommand instanceof NoCommand);
    }

    @Test
    public void testIssueCreateEventsCommand_VelocityInput()
    {
        //Test valid inputs
        testIssueCreateEventsCommand_VelocityInput("2", 2, "5", "0", 5, 0);
        testIssueCreateEventsCommand_VelocityInput("0", 0, "2  ", "-4", 2, -4);
        //Test invalid inputs
        testIssueCreateEventsCommand_VelocityInput("", Constants.DEFAULT_TIME_INTERVAL, "", "", Constants.DEFAULT_VELOCITY, Constants.DEFAULT_VELOCITY);
    }

    /**
     * Test setting one time event for a Sprite from the Controller.
     * This tests for:
     *  -When the selected Sprite is null
     *  -When the selected Sprite exists
     * It only provides valid user inputs.
     * This uses a mock view to enter user input.
     */
    public void testIssueCreateEventsCommand_VelocityInput(
            String intervalInput, int expectedInterval,
            String velocityInputX, String velocityInputY, int expectedVelocityX, int expectedVelocityY)
    {
        //Mock the view
        View view = mockView();
        //Mock the model
        Model model = new Model(view);

        //Create a mock controller
        Controller controller = new Controller(model, view);
        //Get its invoker
        CommandInvoker invoker = controller.getCommandInvoker();

        //Mock the HBox where the user will enter velocity
        TextField textField = new TextField();
        TimeBehaviorPane timeBehaviorPane = new TimeBehaviorPane();
        HBox velocityBox = timeBehaviorPane.getInput();
        HBox entryFields = new HBox();
        velocityBox.getChildren().add(entryFields);
        //Add text fields for the Controller to dissect
        TextField translateXInputField = new TextField();
        translateXInputField.setText(velocityInputX); //set x-velocity text field
        entryFields.getChildren().add(translateXInputField);
        TextField translateYInputField = new TextField();
        translateYInputField.setText(velocityInputY); //set y-velocity text field
        entryFields.getChildren().add(translateYInputField);

        //Create one config object manually
        List<TimeBehaviorConfigObject> timeBehaviorConfigObjectList = new ArrayList<>();
        TimeBehaviorConfigObject timeConfig1 = new TimeBehaviorConfigObject(
                intervalInput,
                false,
                Constants.TRANSLATE_PROPERTY,
                Constants.VELOCITY_PROPERTY,
                velocityBox,
                Constants.NO_AUDIO_FILE_SELECTED
        );
        timeBehaviorConfigObjectList.add(timeConfig1);
        //Other config lists will be empty
        List<KeyInputBehaviorConfigObject> keyInputBehaviorConfigObjects = new ArrayList<>();
        List<MouseInputBehaviorConfigObject> mouseInputBehaviorConfigObjects = new ArrayList<>();
        List<CollisionBehaviorConfigObject> collisionBehaviorConfigObjects = new ArrayList<>();

        //Set the Sprite's events for a null sprite --> should cause failure
        controller.issueCreateEventsCommand(
                timeBehaviorConfigObjectList,
                keyInputBehaviorConfigObjects,
                mouseInputBehaviorConfigObjects,
                collisionBehaviorConfigObjects
        );
        //Check if most recent command is a NoCommand
        Command mostRecentCommand = invoker.getCurrentCommand();
        assertTrue(mostRecentCommand instanceof NoCommand);

        //Try again with a non-null sprite --> should be successful
        Sprite defaultSprite = model.createSprite(); //create new default sprite
        model.updateCurrentSelectedSprite(defaultSprite); //set selected sprite
        controller.issueCreateEventsCommand(
                timeBehaviorConfigObjectList,
                keyInputBehaviorConfigObjects,
                mouseInputBehaviorConfigObjects,
                collisionBehaviorConfigObjects
        );
        //Check if most recent command is a UpdateEventsCommand
        mostRecentCommand = invoker.getCurrentCommand();
        assertTrue(mostRecentCommand instanceof UpdateEventsCommand);

        //Check if the Sprite that was edited now has a time event
        Sprite sprite = model.getCurrentSelectedSprite();
        int spriteId = sprite.getId();
        HashMap<Integer, LinkedList<TimeEvent>> map = model.getTimeEventsToSpriteMap();
        LinkedList<TimeEvent> timeEventList = map.get(spriteId);
        assertNotNull(timeEventList);
        TimeEvent timeEvent = timeEventList.getFirst();
        assertNotNull(timeEvent);

        //Ensure that time event is what we specified above
        assertEquals(timeEvent.getInterval(), expectedInterval);
        //Cast to timeEvent's action to TranslateVelocityAction
        Action action = timeEvent.getAction();
        assertTrue(action instanceof TranslateVelocityAction);
        TranslateVelocityAction translateVelocityAction = (TranslateVelocityAction) action;
        //Check fields of TranslateVelocityAction
        assertEquals(spriteId, translateVelocityAction.getSpriteId());
        assertNull(translateVelocityAction.getSoundFXFile()); //consider null object pattern
        //Ensure velocities set equal the input velocities
        assertEquals(expectedVelocityX, translateVelocityAction.getTranslateX(), 0.05);
        assertEquals(expectedVelocityY, translateVelocityAction.getTranslateY(), 0.05);
    }

    /**
     * Tests setting Sprite event handlers from the Controller when the handler lists are empty.
     */
    @Test
    public void testIssueCreateEventsCommand_EmptyInput()
    {
        //Mock the view
        View view = mockView();
        //Mock the model
        Model model = new Model(view);

        //Create a mock controller
        Controller controller = new Controller(model, view);
        //Get its invoker
        CommandInvoker invoker = controller.getCommandInvoker();

        //Set selected Sprite
        Sprite defaultSprite = model.createSprite(); //create new default sprite
        model.updateCurrentSelectedSprite(defaultSprite); //set selected sprite

        //Call the method using empty lists
        List<TimeBehaviorConfigObject> timeBehaviorConfigObjectList = new ArrayList<>();
        List<KeyInputBehaviorConfigObject> keyInputBehaviorConfigObjects = new ArrayList<>();
        List<MouseInputBehaviorConfigObject> mouseInputBehaviorConfigObjects = new ArrayList<>();
        List<CollisionBehaviorConfigObject> collisionBehaviorConfigObjects = new ArrayList<>();
        controller.issueCreateEventsCommand(
                timeBehaviorConfigObjectList,
                keyInputBehaviorConfigObjects,
                mouseInputBehaviorConfigObjects,
                collisionBehaviorConfigObjects
        );

        //Check if most recent command is a UpdateEventsCommand
        Command mostRecentCommand = invoker.getCurrentCommand();
        assertTrue(mostRecentCommand instanceof UpdateEventsCommand);
    }

    //TODO Pete -- Still need more test cases
}