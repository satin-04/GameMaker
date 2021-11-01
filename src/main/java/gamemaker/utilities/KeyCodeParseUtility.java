package gamemaker.utilities;

import gamemaker.Constants;
import gamemaker.GameMakerApplication;
import javafx.scene.input.KeyCode;


public class KeyCodeParseUtility
{
    //Prevent instantiation
    private KeyCodeParseUtility() {
    }

    /**
     * Converts the key in 'input' to a KeyCode.
     * Only the first character will be considered.
     * @param input a string containing a single upper/lowercase char
     * @return a KeyCode representing the first char
     * (ex: "A" yields KeyCode.A). Default is KeyCode.W if
     * there is a parsing problem.
     */
    public static KeyCode parseToKeyCode(String input)
    {
        try
        {
            //Only include first character
            input = input.substring(0, 1);
            //Convert to KeyCode
            return KeyCode.valueOf(input);
        }
        //Parsing error: Return default KeyCode
        catch (IllegalArgumentException | NullPointerException | IndexOutOfBoundsException ex)
        {
            GameMakerApplication.logger.info("No key could be parsed from "+input+". Returning the default key, "+
                    Constants.DEFAULT_KEY_EVENT_CODE);
            return Constants.DEFAULT_KEY_EVENT_CODE;
        }
    }
}
