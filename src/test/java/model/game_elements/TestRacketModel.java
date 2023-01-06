package model.game_elements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import model.Vector2;
import model.interfaces.InterfaceRacketController.State;

public class TestRacketModel {

    /**
     * Test the {@code update} method of the {@code RacketModel} class.
     */
    @Test
    void testUpdate() {
        System.out.println("Testing update");

        RacketModel r = new RacketModel(new Vector2(300, 300), 10, 50, 10);
        r.update(0.10, 500, State.GOING_UP);
        assertTrue(r.getSpeed() < 0);
        double d = Math.abs(r.getSpeed());
        r.update(0.05, 500, State.IDLE);
        assertTrue(Math.abs(r.getSpeed()) < d);
        r.update(0.15, 500, State.GOING_DOWN);
        assertTrue(r.getSpeed() > 0);
        r.update(1000, 500, State.GOING_UP);
        r.update(1000, 500, State.GOING_DOWN);
        assertTrue(r.getCoordY() <= 500);
    }

    /**
     * Test the {@code reset} method of the {@code RacketModel} class.
     */
    @Test
    void testReset() {
        System.out.println("Testing reset");

        RacketModel r = new RacketModel(new Vector2(100, 100), 10, 50, 10);
        r.reset(500);
        assertEquals(r.getCoordY(), 225.);
        assertEquals(r.getSpeed(), 0);
    }

    // /**
    // * Test the {@code hitBall} method of the {@code RacketModel} class.
    // */
    // @Test
    // void testHitBall() {
    // System.out.println("Testing hitBall");

    // RacketModel r = new RacketModel(new Vector2(98, 98), 10, 10, 2);
    // // Not in the passage of the ball
    // assertTrue(!r.hitBall(new Vector2(0, 0), new Vector2(1, 1), 10));
    // // Ball going right
    // assertTrue(r.hitBall(new Vector2(96, 100), new Vector2(100, 100), 2));
    // // Ball going up
    // assertTrue(!r.hitBall(new Vector2(96, 100), new Vector2(96, 102), 2));
    // // Ball going left
    // // assertTrue(r.hitBall(new Vector2(100, 100), new Vector2(96, 100), 2));
    // // Ball going down
    // assertTrue(!r.hitBall(new Vector2(96, 100), new Vector2(96, 102), 2));
    // }
}
