package gamemaker.view;

import gamemaker.Constants;
import gamemaker.GameMakerApplication;
import gamemaker.controller.Controller;
import gamemaker.model.event.CollisionEvent;
import gamemaker.model.event.KeyCodeEvent;
import gamemaker.model.event.MouseCodeEvent;
import gamemaker.model.event.TimeEvent;
import gamemaker.model.sprite.Sprite;
import gamemaker.utilities.SaveManager;
import gamemaker.view.components.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.io.File;
import java.net.URL;
import java.util.*;

public class View implements Initializable {

    // Parent of all
    @FXML
    public VBox window;
    //public BorderPane window;

    // CreateSprite btn/properties area

    @FXML
    public SplitMenuButton createSpriteBtn;

    // Application btns - right side
    @FXML
    public Pane appBtnContainer;
    @FXML
    public Button playBtn;
    @FXML
    public Button stopBtn;
    @FXML
    public Button saveBtn;
    @FXML
    public Button saveAsBtn;
    @FXML
    public Button loadBtn;
    @FXML
    public Button undoBtn;
    @FXML
    public Button redoBtn;

    // Event/Action building area
    @FXML
    public Pane eventActionBar;
    @FXML
    public ScrollPane eventScrollPane;
    @FXML
    public MenuButton modifyEventBtn;
    @FXML
    public Button addEventBtn;

    // Game Canvas Placeholder
    @FXML
    public HBox mockGameCanvas;
    //public Pane mockGameCanvas;
    public TabPane configurationTabPane;

    // pertain to currently selected Sprite: primitive properties
    public TextField propLabel, propPosX, propPosY, propVelocityX, propVelocityY, propText, propDimensionX,
            propDimensionY;
    public MenuButton propShape;
    public ColorPicker propColor, propBackgroundColor;
    public Button submitPropertiesBtn;
    public CheckBox propVisible;
    public VBox propertyVBox;
    public Tab propertiesTab;
    public MenuItem editBackgroundMenuItem;
    public MenuItem createSpriteMenuItem;
    public Tab backgroundTab;
    public Tab eventsTab;
    public Label trackFilePath;


    // User Interface Vars
    // Event/Action building area
    private VBox timeBehaviorList = new VBox();
    private VBox collisionBehaviorList = new VBox();
    private VBox keyInputBehaviorList = new VBox();
    private VBox mouseInputBehaviorList = new VBox();

    // Other Vars
    public CanvasInputHandler canvasInputHandler;
    private Controller controller;
    private Pane gameCanvas; // owned by Model

    // Audio Files?
    private File backgroundTrack;

    /************************************
     *
     * Setup/Tear Down
     *
     ************************************/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Get screen size (Cannot be located in Constants file because this causes problems with testing).
        final int SCREEN_WIDTH = (int) ((int) Screen.getPrimary().getBounds().getWidth() * 0.9);
        final int SCREEN_HEIGHT = (int) (Screen.getPrimary().getBounds().getHeight() * 0.9);
        //Limit the window size to be as large as the screen size
        window.setPrefWidth(SCREEN_WIDTH);
        window.setPrefHeight(SCREEN_HEIGHT);

        undoBtn.setVisible(false);
        redoBtn.setVisible(false);
        // add a border to the tab pane
        configurationTabPane.setVisible(false);
        configurationTabPane.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        configurationTabPane.getTabs().remove(backgroundTab);
        // configure behavior lists. These are the vbox's that go in eventScrollPane
        // when event tab is selected
        for (VBox behaviorList : Arrays.asList(timeBehaviorList, collisionBehaviorList, keyInputBehaviorList,
                mouseInputBehaviorList)) {

            behaviorList.setPadding(new Insets(15));
            behaviorList.setLayoutY(modifyEventBtn.getLayoutY() + 50);
        }

        // default to time behavior display
        eventScrollPane.setContent(timeBehaviorList);

        // configure default color property
        propColor.setValue(Constants.DEFAULT_SPRITE_COLOR);

        // configure shape dropdown to update menuButton text
        propShape.getItems().forEach(shape -> shape.setOnAction(e -> {
            propShape.setText(shape.getText());
            if (shape.getText().equals("Text")) {
                propText.setDisable(false);
            } else {
                propText.setDisable(true);
            }
        }));

        // createSprite dropdown conf
        // TODO THIS CAUSES AND EXCEPTION
        editBackgroundMenuItem.setOnAction(e -> {
            createSpriteBtn.setText(editBackgroundMenuItem.getText());
            configurationTabPane.getTabs().add(backgroundTab);
            configurationTabPane.getTabs().removeAll(propertiesTab, eventsTab);
//			propBackgroundColor.setValue(Constants.DEFAULT_BACKGROUND_COLOR);
            configurationTabPane.setVisible(true);

        });
        createSpriteMenuItem.setOnAction(e -> {
            createSpriteBtn.setText(createSpriteMenuItem.getText());
            configurationTabPane.getTabs().remove(backgroundTab);
            configurationTabPane.getTabs().addAll(eventsTab, propertiesTab);

        });

        // Disable until Play pressed
        disableButton(stopBtn);
        // Disable until Save As used
        disableButton(saveBtn);
        // Disable until command is issued
        disableButton(undoBtn);
        // Disable until command is undone
        disableButton(redoBtn);

        configureModifyBehaviorsSelector();

    }

    public void setupDragController() {
        // Setup Drag Controller
        canvasInputHandler = new CanvasInputHandler(this);
    }

    /************************************
     *
     * Getters/Setters
     *
     ************************************/

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Pane getGameCanvas() {
        return gameCanvas;
    }

    public void setGameCanvas(Pane gameCanvas) {
        this.gameCanvas = gameCanvas;
        mockGameCanvas.getChildren().add(this.gameCanvas);
        this.gameCanvas.setLayoutX(Constants.ZERO);
        this.gameCanvas.setLayoutY(Constants.ZERO);
    }

    /************************************
     *
     * User Interface Handling
     *
     ************************************/

    // This probably should also sent TimeEvents/InputEvents tied to this sprite
    // B/c Sprite does not hold this information. The model does.
    // Example:
    public void updateBasedOnSelectedSprite(Sprite selectedSprite, Iterator<TimeEvent> spritesTimeEvents,
                                            Iterator<KeyCodeEvent> spritesKeyEvents, Iterator<MouseCodeEvent> spritesMouseEvents,
                                            Iterator<CollisionEvent> spritesCollisionEvents) {
        if (selectedSprite != null) {
            configurationTabPane.setVisible(true);
            updatePropertiesPaneBasedOn(selectedSprite);
        } else {
            configurationTabPane.setVisible(false);
        }

        if (spritesTimeEvents != null) {
            timeBehaviorList.getChildren().clear();
            while (spritesTimeEvents.hasNext()) {
                TimeEvent timeEvent = spritesTimeEvents.next();

                TimeBehaviorPane timeBehaviorPane = new TimeBehaviorPane();
                String builtBehavior = timeBehaviorPane.rebuildFrom(timeEvent);
                if (!builtBehavior.startsWith("Move"))
                    timeBehaviorList.getChildren().add(timeBehaviorPane);
            }
        }
        if (spritesCollisionEvents != null) {

            collisionBehaviorList.getChildren().clear();
            while (spritesCollisionEvents.hasNext()) {
                CollisionEvent collisionEvent = spritesCollisionEvents.next();

                CollisionBehaviorPane collisionBehaviorPane = new CollisionBehaviorPane();
                collisionBehaviorPane.rebuildFrom(collisionEvent);

                collisionBehaviorList.getChildren().add(collisionBehaviorPane);
            }
        }

        if (spritesKeyEvents != null){
            keyInputBehaviorList.getChildren().clear();
            while (spritesKeyEvents.hasNext()) {
                KeyCodeEvent keyCodeEvent = spritesKeyEvents.next();
                KeyInputPane keyInputPane = new KeyInputPane();
                keyInputPane.rebuildFrom(keyCodeEvent);
                keyInputBehaviorList.getChildren().add(keyInputPane);
            }
        }

    }

    public void updatePropertiesPaneBasedOn(Sprite selectedSprite) {
        propLabel.setText(selectedSprite.getLabel());
        updatePositionFields((int) selectedSprite.getPosition().getX(), (int) selectedSprite.getPosition().getY());
        propVelocityX.setText(String.valueOf((int) selectedSprite.getVelocity().getX()));
        propVelocityY.setText(String.valueOf((int) selectedSprite.getVelocity().getY()));
        propDimensionX.setText(String.valueOf((int) selectedSprite.getDimensions().getX()));
        propDimensionY.setText(String.valueOf((int) selectedSprite.getDimensions().getY()));
        propShape.setText(selectedSprite.getShapeAsString());
        propColor.setValue(selectedSprite.getColor());
        propText.setText(selectedSprite.getText());
    }

    public void updatePosition(Sprite selectedSprite) {
        updatePositionFields((int) selectedSprite.getPosition().getX(), (int) selectedSprite.getPosition().getY());
    }

    public void updatePositionBasedOnDrag(Sprite selectedSprite) {
        updatePositionFields((int) (selectedSprite.getPosition().getX() + selectedSprite.getShape().getTranslateX()),
                (int) (selectedSprite.getPosition().getY() + selectedSprite.getShape().getTranslateY()));
    }

    private void updatePositionFields(int x, int y) {
        propPosX.setText(String.valueOf(x));
        propPosY.setText(String.valueOf(y));
    }

    public void disableButton(Button btn) {
        btn.setDisable(true);
    }

    public void enableButton(Button btn) {
        btn.setDisable(false);
    }

    private void disableMenuButton(MenuButton btn) {
        btn.setDisable(true);
    }

    private void enableMenuButton(MenuButton btn) {
        btn.setDisable(false);
    }

    /************************************
     *
     * Controller Communication
     *
     ************************************/

    public void onStopBtnClick(MouseEvent actionEvent) {
        // Should this be captured in the command?
        // Probably?!
        createSpriteBtn.setDisable(false);
        enableButton(addEventBtn);
        enableMenuButton(modifyEventBtn);
        enableButton(playBtn);
        if (SaveManager.getInstance().hasSaveFile()) {
            enableButton(saveBtn);
        }
        enableButton(saveAsBtn);
        enableButton(loadBtn);
        enableButton(undoBtn);
        enableButton(redoBtn);
        disableButton(stopBtn);
        controller.issueStopGameCommand(canvasInputHandler);
        enableConfig();
    }

    public void onPlayBtnClick(MouseEvent actionEvent) {
        // Should this be captured in the command?
        // Probably?!
        createSpriteBtn.setDisable(true);
        disableButton(addEventBtn);
        disableMenuButton(modifyEventBtn);
        disableButton(playBtn);
        disableButton(saveBtn);
        disableButton(saveAsBtn);
        disableButton(loadBtn);
        disableButton(undoBtn);
        disableButton(redoBtn);
        enableButton(stopBtn);
        controller.issuePlayGameCommand(canvasInputHandler);
        disableConfig();
    }

    public void onSaveBtnClick(MouseEvent actionEvent) {
        controller.issueSaveCommand();
    }

    public void onSaveAsBtnClick(MouseEvent actionEvent) {
        controller.issueSaveAsCommand(window.getScene().getWindow());
    }

    public void onLoadBtnClick(MouseEvent actionEvent) {
        controller.issueLoadCommand(window.getScene().getWindow());
    }

    public void onUndoBtnClick(MouseEvent actionEvent) {
        controller.undoLastCommand();
    }

    public void onRedoBtnClick(MouseEvent actionEvent) {
        controller.redoLastCommand();
    }

    public void onCreateSpriteClick(MouseEvent mouseEvent) {
        if (createSpriteBtn.getText().equals("Create Sprite")) {
            controller.issueCreateSpriteCommand(gameCanvas);
        }
    }

    public void setSelectedTarget(Node target) {

        controller.issueSpriteSelectedCommand(target);

    }

    public void dragTarget(double translateX, double translateY) {
        controller.issueSpriteDragCommand(translateX, translateY);
    }

    public void releaseTarget(double layoutX, double layoutY, double initialX, double initialY) {
        controller.issueSpriteReleasedCommand(layoutX, layoutY, initialX, initialY);
    }

    public void disableConfig() {
        configurationTabPane.setDisable(true);
    }

    public void enableConfig() {
        configurationTabPane.setDisable(false);
    }

    /************************************
     *
     * Unfiled / Not sure where they belong - Ethan Please organize Maazin! <3
     *
     ************************************/

    private void displayTextBasedOnChoice(MenuButton parent, MenuItem choice) {
        parent.setText(choice.getText());

    }

    public void onAddBehaviorBtnClick(MouseEvent mouseEvent) {
        /**
         * if (timeElapsedConfigPanesPointer < timeElapsedConfigPanes.size()) {
         * timeElapsedConfigPanes.get(timeElapsedConfigPanesPointer).setVisible(true);
         * timeElapsedConfigPanesPointer++; if (timeElapsedConfigPanesPointer ==
         * timeElapsedConfigPanes.size()) { addBehaviorBtn.setVisible(false); } }
         **/
        if (modifyEventBtn.getText().equals("Time-Based Events")) {
            timeBehaviorList.getChildren().add(new TimeBehaviorPane());
        } else if (modifyEventBtn.getText().equals("Collision Events")) {
            collisionBehaviorList.getChildren().add(new CollisionBehaviorPane());
        } else if (modifyEventBtn.getText().equals("Key Input Events")) {
            keyInputBehaviorList.getChildren().add(new KeyInputPane());
        } else {
            mouseInputBehaviorList.getChildren().add(new MouseInputPane());
        }

    }

    private void configureModifyBehaviorsSelector() {
        modifyEventBtn.getItems().clear();
        MenuItem timeChoice = new MenuItem("Time-Based Events");
        MenuItem collisionChoice = new MenuItem("Collision Events");
        MenuItem keyInputChoice = new MenuItem("Key Input Events");
        MenuItem mouseInputChoice = new MenuItem("Mouse Input Events");

        timeChoice.setOnAction(e -> {
            displayTextBasedOnChoice(modifyEventBtn, timeChoice);
            eventScrollPane.setContent(timeBehaviorList);
        });

        collisionChoice.setOnAction(e -> {
            displayTextBasedOnChoice(modifyEventBtn, collisionChoice);
            eventScrollPane.setContent(collisionBehaviorList);
        });

        keyInputChoice.setOnAction(e -> {
            displayTextBasedOnChoice(modifyEventBtn, keyInputChoice);
            eventScrollPane.setContent(keyInputBehaviorList);

        });

        mouseInputChoice.setOnAction(e -> {
            displayTextBasedOnChoice(modifyEventBtn, mouseInputChoice);
            eventScrollPane.setContent(mouseInputBehaviorList);

        });
        //Add the choices to the drop-down
        modifyEventBtn.getItems().addAll(timeChoice, collisionChoice, keyInputChoice);
    }

    public void onSubmitPropertiesClicked(MouseEvent mouseEvent) {
        HashMap<String, String> propertyToInputValue = packageProperties();
        GameMakerApplication.logger.info(propertyToInputValue);
        controller.issueUpdateSelectedSpritePropretiesCommand(propertyToInputValue);

    }

    /**
     * Reads all data from the View and sends it to the Controller for processing.
     * @param mouseEvent unused
     */
    public void onSubmitEventsClicked(MouseEvent mouseEvent) {
        //Wrap all user-supplied data into lists of ConfigObjects
        List<TimeBehaviorConfigObject> timeBehaviorConfigObjects = packageTimeBehaviors();
        List<KeyInputBehaviorConfigObject> keyBehaviorConfigObjects = packageKeyInputBehaviors();
        List<MouseInputBehaviorConfigObject> mouseBehaviorConfigObjects = packageMouseInputBehaviors();
        List<CollisionBehaviorConfigObject> collisionBehaviorConfigObjects = packageCollisionBehaviors();

        //Send all user-supplied data to the Controller
        controller.issueCreateEventsCommand(timeBehaviorConfigObjects, keyBehaviorConfigObjects,
                mouseBehaviorConfigObjects, collisionBehaviorConfigObjects);
    }

    public HashMap<String, String> packageProperties() {
        HashMap<String, String> propToInput = new HashMap<String, String>();
        propToInput.put(Constants.LABEL_KEY, propLabel.getText());
        propToInput.put(Constants.POSITION_X_KEY, propPosX.getText());
        propToInput.put(Constants.POSITION_Y_KEY, propPosY.getText());
        propToInput.put(Constants.VELOCITY_X_KEY, propVelocityX.getText());
        propToInput.put(Constants.VELOCITY_Y_KEY, propVelocityY.getText());
        propToInput.put(Constants.DIMENSIONS_X_KEY, propDimensionX.getText());
        propToInput.put(Constants.DIMENSIONS_Y_KEY, propDimensionY.getText());
        propToInput.put(Constants.COLOR_KEY, propColor.getValue().toString());
        propToInput.put(Constants.SHAPE_KEY, propShape.getText());
        propToInput.put(Constants.VISIBLE_KEY, String.valueOf(propVisible.isSelected()));
        propToInput.put(Constants.TEXT_KEY, propText.getText());
        return propToInput;
    }

    public List<TimeBehaviorConfigObject> packageTimeBehaviors() {
        List<TimeBehaviorConfigObject> timeBehaviorConfigObjects = new ArrayList<>();
        timeBehaviorList.getChildren().forEach(node -> {
            TimeBehaviorPane timeBehaviorPane = (TimeBehaviorPane) node;
            timeBehaviorConfigObjects.add(timeBehaviorPane.export());
        });
        return timeBehaviorConfigObjects;
    }

    public List<KeyInputBehaviorConfigObject> packageKeyInputBehaviors() {
        List<KeyInputBehaviorConfigObject> keyInputBehaviorConfigObjects = new ArrayList<>();
        keyInputBehaviorList.getChildren().forEach(node -> {
            KeyInputPane keyInputPane = (KeyInputPane) node;
            keyInputBehaviorConfigObjects.add(keyInputPane.export());
        });
        return keyInputBehaviorConfigObjects;
    }

    public List<CollisionBehaviorConfigObject> packageCollisionBehaviors() {
        List<CollisionBehaviorConfigObject> collisionBehaviorConfigObjects = new ArrayList<>();
        collisionBehaviorList.getChildren().forEach(node -> {
            CollisionBehaviorPane collisionBehaviorPane = (CollisionBehaviorPane) node;
            collisionBehaviorConfigObjects.add(collisionBehaviorPane.export());
        });
        return collisionBehaviorConfigObjects;
    }

    public List<MouseInputBehaviorConfigObject> packageMouseInputBehaviors() {
        List<MouseInputBehaviorConfigObject> mouseInputBehaviorConfigObjects = new ArrayList<>();
        mouseInputBehaviorList.getChildren().forEach(node -> {
            MouseInputPane mouseInputPane = (MouseInputPane) node;
            mouseInputBehaviorConfigObjects.add(mouseInputPane.export());
        });
        return mouseInputBehaviorConfigObjects;
    }

    public void onChooseBackgroundTrackClicked(MouseEvent mouseEvent) {
        controller.issueGetBackgroundTrackCommand(window.getScene().getWindow());
    }

    public void backgroundTrackSelected(File backgroundTrack) {
        if (backgroundTrack != null) {
            this.backgroundTrack = backgroundTrack;
            trackFilePath.setText(this.backgroundTrack.getAbsolutePath());
        } else {
            trackFilePath.setText("No file selected. Select a *.wav or *.mp3 file.");
        }
    }

    public void onApplyChangesClicked(MouseEvent mouseEvent) {
        GameMakerApplication.logger.info("Confirm background edits: ");

        HashMap<String, String> propToInput = new HashMap<String, String>();
        propToInput.put(Constants.COLOR_KEY, propBackgroundColor.getValue().toString());
        if (backgroundTrack != null) {
            propToInput.put(Constants.AUDIO_PATH_KEY, backgroundTrack.getAbsolutePath());
        } else {
            propToInput.put(Constants.AUDIO_PATH_KEY, Constants.EMPTY_STRING);
        }

        controller.issueUpdateBackgroundProperties(propToInput);
    }
}