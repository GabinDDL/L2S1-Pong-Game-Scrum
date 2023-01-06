package model.game_elements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TestPlayerModel {

    /**
     * Test the constructor of the {@code PlayerModel} class.
     */
    @Test
    void testPlayerModel() {
        System.out.println("Testing PlayerModel constructor");
        PlayerModel p = new PlayerModel(10);
        assertEquals(10, p.getPoints());
        System.out.println(p.getSpeed());
        assertEquals(0., p.getSpeed());
    }

    /**
     * Test the {@code incrementsPoints} method of the {@code PlayerModel} class.
     */
    @Test
    void testIncrementPoints() {
        System.out.println("Testing IncrementPoints");
        PlayerModel p = new PlayerModel(0);
        assertEquals(0, p.getPoints());
        p.incrementPoints();
        assertEquals(1, p.getPoints());
        p.incrementPoints();
        assertEquals(2, p.getPoints());
    }
}
