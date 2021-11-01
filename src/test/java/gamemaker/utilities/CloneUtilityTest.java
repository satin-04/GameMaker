package gamemaker.utilities;

import gamemaker.model.sprite.CircleSprite;
import gamemaker.model.sprite.RectangleSprite;
import gamemaker.model.sprite.Sprite;
import gamemaker.model.sprite.TextSprite;
import org.junit.Test;
import static org.junit.Assert.*;

public class CloneUtilityTest
{
    /**
     * Ensures that a default CircleSprite and its clone are equal.
     */
    @Test
    public void testCloneCircleSprite()
    {
        //Clone the Sprite
        CircleSprite circleSprite = new CircleSprite(32);
        Sprite clone = CloneUtility.cloneSprite(circleSprite);

        //Check if the original and clone are equal.
        assertEquals(circleSprite.getId(), clone.getId());
        assertTrue(clone instanceof CircleSprite);
        assertEquals(circleSprite, clone);
    }

    /**
     * Ensures that a default RectangleSprite and its clone are equal.
     */
    @Test
    public void testCloneRectangleSprite()
    {
        //Clone the Sprite
        RectangleSprite original = new RectangleSprite(32);
        Sprite clone = CloneUtility.cloneSprite(original);

        //Check if the original and clone are equal.
        assertEquals(original.getId(), clone.getId());
        assertTrue(clone instanceof RectangleSprite);
        assertEquals(original, clone);
    }

    /**
     * Ensures that a default TextSprite and its clone are equal.
     */
    @Test
    public void testCloneTextSprite()
    {
        //Clone the Sprite
        TextSprite original = new TextSprite(32);
        Sprite clone = CloneUtility.cloneSprite(original);

        //Check if the original and clone are equal.
        assertEquals(original.getId(), clone.getId());
        assertTrue(clone instanceof TextSprite);
        assertEquals(original, clone);
    }
}
