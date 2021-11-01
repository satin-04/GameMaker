package gamemaker;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;

public class Constants {


    public enum MouseButtonCode {
        PRIMARY, SECONDARY, MIDDLE, IGNORE
    }

    public enum SaveState {
        SUCCESSFUL, FAILURE, NOFILE
    }

    public enum LoadState {
        SUCCESSFUL, FAILURE, NOFILE
    }

    public enum CollisionSide {
        LEFT, RIGHT, TOP, BOTTOM,
        NO_COLLISION
    }

    public enum CollisionType {
        OBJECT, GET_HIT, SCREEN
    }


    // Accepted File Types
    public static final ArrayList<String> AUDIO_TYPES = new ArrayList<String>(Arrays.asList("*.wav", "*.mp3"));

    // Numbers =/ (This too much? ETB)
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;

    // Dimensions
    public static final int GAME_CANVAS_WIDTH = 500;
    public static final int GAME_CANVAS_HEIGHT = 300;

    // Model Data Defaults
    public static final long FRAME_DURATION = 33; // 30 FPS
    public static final int NULL_SPRITE_ID = -99;
    public static final String DEFAULT_LABEL = "New Sprite ";
    public static final int DEFAULT_SPRITE_SIZE = 10;
    public static final Color DEFAULT_SPRITE_COLOR = Color.MAGENTA;
    public static final Color SELECTED_STROKE_COLOR = Color.BLACK;
    public static final Color UNSELECTED_STROKE_COLOR = Color.TRANSPARENT;
    public static final Color DEFAULT_BACKGROUND_COLOR = Color.DARKGREY;
    public static final String GAME_CANVAS_ID = "Game Canvas";
    public static final String DEFAULT_FONT = "Verdana";
    public static final int DEFAULT_FONT_SIZE = 18;
    public static final int DEFAULT_VELOCITY = 0;
    public static final int DEFAULT_TIME_INTERVAL = 1;
    public static final KeyCode DEFAULT_KEY_EVENT_CODE = KeyCode.W;

    // Sprite Types
    public static final String RECTANGLE = "Rectangle";
    public static final String CIRCLE = "Circle";
    public static final String TEXT = "Text";

    // Keys!
    public static final String AUDIO_PATH_KEY = "audioPath";
    public static final String SPRITE_ID_KEY = "spriteId";
    public static final String OTHER_SPRITE_ID_KEY = "otherSpriteId"; //example: saving the ID of the Sprite to clone
    public static final String CLASS_KEY = "class";
    public static final String ACTION_CLASS_KEY = "actionClass";
    public static final String ACTION_KEY = "action";
    public static final String LABEL_KEY = "label";
    public static final String SHAPE_KEY = "shape";
    public static final String SPRITE_KEY = "sprite";
    public static final String POSITION_KEY = "position";
    public static final String POSITION_X_KEY = "positionX";
    public static final String POSITION_Y_KEY = "positionY";
    public static final String DIMENSIONS_KEY = "dimensions";
    public static final String DIMENSIONS_X_KEY = "dimensionsX";
    public static final String DIMENSIONS_Y_KEY = "dimensionsY";
    public static final String RADIUS_KEY = "radius";
    public static final String VELOCITY_KEY = "velocity";
    public static final String VELOCITY_X_KEY = "velocityX";
    public static final String VELOCITY_Y_KEY = "velocityY";
    public static final String COLOR_KEY = "color";
    public static final String VISIBLE_KEY = "visible";
    public static final String TEXT_KEY = "text";
    public static final String TRANSLATE_X_KEY = "translateX";
    public static final String TRANSLATE_Y_KEY = "translateY";
    public static final String COLLISION_TYPE_KEY = "collisionType";
    public static final String COLLISION_SIDE_KEY = "collisionSide";
    public static final String COLLISION_IMPACTEE_KEY = "impactee";
    public static final String KISS_POSITION_KEY = "kissPosition";
    public static final String INTERVAL_KEY = "interval";
    public static final String INPUT_TRIGGER_KEY = "inputTrigger";
    public static final String BACKGROUND_INFO_KEY = "backgroundInfo";
    public static final String ALL_SPRITES_KEY = "allSprites";
    public static final String ALL_TIME_EVENTS_KEY = "allTimeEvents";
    public static final String ALL_KEY_EVENTS_KEY = "allKeyEvents";
    public static final String ALL_MOUSE_EVENTS_KEY = "allMouseEvents";
    public static final String ALL_COLLISION_EVENTS_KEY = "allCollisionEvents";
    public static final String TIME_ELAPSED_KEY = "Time ELapsed";
    public static final String TIME_DELTA_KEY = "Time Delta";

    // User Interface
    public static final int BEHAVIOR_PANE_MARGIN = 15;
    public static final int BEHAVIOR_PANE_WIDTH = 250;
    public static final int BEHAVIOR_PANE_HEIGHT = 135;
    public static final String EMPTY_STRING = "";
    public static final String NO_AUDIO_FILE_SELECTED = "No file selected. Select a *.wav or *.mp3 file.";

    //Menu option text...
    //All possible actions
    public static final String CHOOSE_ACTION = "Choose Action";
    public static final String SET_PROPERTY = "Change Property";
    public static final String TRANSLATE_PROPERTY = "Shift Property";
    public static final String CREATE_SPRITE = "Spawn Sprite";
    public static final String DESTROY_SELF = "De-Spawn";
    public static final String DESTROY_OTHER = "De-Spawn Other";
    public static final String TELEPORT_OTHER = "Teleport Other";
    public static final String REFLECT = "Reflect";
	public static final String DISPLAY_TIME = "Display Time";
	public static final String END_GAME_VICTORY = "End Game-Victory";
	public static final String END_GAME_DEFEAT= "End Game-Defeat";

    //Collision Types
    public static final String ON_HIT_GAMEPLAY_BOUNDS= "On Hit Gameplay Bounds";
    public static final String ON_HIT_OTHER_SPRITE = "On Hit Other Sprite";
    public static final String ON_GET_HIT= "On Get Hit";


    //collections of actions pertaining to their appropriate trigger.
    public static final ArrayList<String> TIME_ACTIONS =
            new ArrayList<String>(Arrays.asList(SET_PROPERTY,
					TRANSLATE_PROPERTY,
					CREATE_SPRITE,
					DISPLAY_TIME));
    public static final ArrayList<String> COLLISION_ACTIONS =
            new ArrayList<String>(Arrays.asList(SET_PROPERTY,
                    TRANSLATE_PROPERTY,
                    REFLECT,
                    CREATE_SPRITE,
                    DESTROY_SELF,
                    DESTROY_OTHER,
                    TELEPORT_OTHER,
                    END_GAME_DEFEAT,
                    END_GAME_VICTORY
            ));

    public static final ArrayList<String> COLLISION_ACTIONS_NON_PARAMETERIZED =
            new ArrayList<String>(Arrays.asList(SET_PROPERTY,
                    TRANSLATE_PROPERTY,
                    REFLECT,
                    CREATE_SPRITE,
                    DESTROY_SELF,
                    DESTROY_OTHER,
                    TELEPORT_OTHER
            ));
    public static final ArrayList<String> KEY_ACTIONS =
            new ArrayList<String>(Arrays.asList(SET_PROPERTY,
					TRANSLATE_PROPERTY,
					CREATE_SPRITE));


    //All Properties
    public static final String VELOCITY_PROPERTY = "Velocity";
    public static final String POSITION_PROPERTY = "Position";
	public static final String SHAPE_PROPERTY = "Shape";
	public static final String COLOR_PROPERTY = "Color";
	public static final String DIMENSIONS_PROPERTY = "Dimensions";
	public static final String IS_VISIBLE_PROPERTY = "Visible";
	public static final String IS_BULLET_PROPERTY = "Bullet";
	public static final String TEXT_PROPERTY = "Text";

	//SETTABLE properties
	public static final ArrayList<String> SETTABLE_PROPERTIES =
			new ArrayList<String>(Arrays.asList(VELOCITY_PROPERTY,
					POSITION_PROPERTY,
					SHAPE_PROPERTY,
					COLOR_PROPERTY,
					DIMENSIONS_PROPERTY,
					IS_VISIBLE_PROPERTY,
					IS_BULLET_PROPERTY,
					TEXT_PROPERTY));
	//TRANSLATABLE properties
	public static final ArrayList<String> TRANSLATABLE_PROPERTIES =
			new ArrayList<String>(Arrays.asList(VELOCITY_PROPERTY,
					POSITION_PROPERTY,
					DIMENSIONS_PROPERTY));
}
