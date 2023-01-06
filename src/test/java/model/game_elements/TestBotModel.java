package model.game_elements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import model.Vector2;
import model.interfaces.InterfaceRacketController.State;

public class TestBotModel {

    // Tests

    /**
     * Test the {@code updateStateFollow} method of the {@code BotModel} class.
     */
    @Test
    void testUpdateStateFollow() {
        BotModel b = new BotModel();
        b.setCoord(new Vector2(0, 10));

        Ball ball = new Ball(new Vector2(20, 20), 0, 600, 10);
        ball.setSpeedDirection(new Vector2(-1, -1));

        ArrayList<Ball> balls = new ArrayList<>();
        balls.add(ball);

        b.updateStateFollow(500, balls, true);
        assertEquals(b.getState(), State.GOING_DOWN);
    }

    /**
     * Test the {@code updateStatePredict} method of the {@code BotModel} class.
     */
    @Test
    void testUpdateStatePredict() {
        BotModel b = new BotModel();
        b.setCoord(new Vector2(0, 10));

        Ball ball = new Ball(new Vector2(20, 20), 0, 600, 10);

        ball.setSpeedDirection(new Vector2(-1, -1));

        ArrayList<Ball> balls = new ArrayList<>();
        balls.add(ball);

        b.updateStatePredict(500, balls, true);
        b.reset(20);

        // test bounce
        ball.setSpeedDirection(new Vector2(-1, -4));

        b.updateStatePredict(500, balls, true);
        assertEquals(b.getState(), State.GOING_DOWN);
    }
}
