package model.game_elements;

import model.Vector2;
import model.interfaces.InterfaceBallModel.LastAction;
import model.interfaces.InterfaceRacketController.State;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TestBallModel {

    // Tests

    /**
     * Test the constructor of the {@code BallModel} class.
     */
    @Test
    void testBallModel() {
        System.out.println("Testing BallModel constructor");
        BallModel ball = new BallModel(new Vector2(0, 0), 200, 600, 10.0);

        assertEquals(0, ball.getSpeedDirectionX());
        assertEquals(0, ball.getSpeedDirectionY());
    }

    /**
     * Test the {@code isOutside} method of the {@code BallModel} class.
     */
    @Test
    void testIsOutside() {
        System.out.println("Testing isOutside");
        int width = 1000;
        BallModel ball = new BallModel(new Vector2(0, 0), 200, 600, 10.0);

        assertEquals(false, ball.isOutside(width));
        ball.setCoordX(1000);
        assertEquals(false, ball.isOutside(width));
        ball.setCoordX(1100);
        assertEquals(true, ball.isOutside(width));
        ball.setCoordX(-100);
        assertEquals(true, ball.isOutside(width));
    }

    /**
     * Test the {@code computeRacketBounce} method of the {@code BallModel} class.
     */
    @Test
    void testComputeRacketBounce() {
        System.out.println("Testing computeRacketBounce");
        BallModel ball = new BallModel(new Vector2(0, 100), 200, 600, 10.0);

        // Test left racket bounce
        PlayerModel player = new PlayerModel(new RacketModel(new Vector2(0, 250), 0, 200, 10.0), 0);

        // Test racket going up
        ball.setSpeedDirection(new Vector2(-1, 0));
        player.setState(State.GOING_UP);
        testBounceRacket(ball, player, -1);

        // Test racket going down
        ball.setSpeedDirection(new Vector2(-1, 0));
        player.setState(State.GOING_DOWN);
        testBounceRacket(ball, player, 1);

        // Test racket not moving
        ball.setSpeedDirection(new Vector2(-1, 0));
        player.setState(State.IDLE);
        testBounceRacket(ball, player, 0);

        // Test right racket bounce
        player = new PlayerModel(new RacketModel(new Vector2(1000, 250), 0, 200, 10.0), 0);

        // Test racket going up
        ball.setSpeedDirection(new Vector2(1, 0));
        player.setState(State.GOING_UP);
        testBounceRacket(ball, player, -1);

        // Test racket going down
        ball.setSpeedDirection(new Vector2(1, 0));
        player.setState(State.GOING_DOWN);
        testBounceRacket(ball, player, -1);

        // Test racket not moving
        ball.setSpeedDirection(new Vector2(1, 0));
        player.setState(State.IDLE);
        testBounceRacket(ball, player, 0);
    }

    // /**
    // * Test the {@code update} method of the {@code BallModel} class.
    // */
    // @Test
    // void testUpdate() {
    // BallModel ball = new BallModel(new Vector2(0, 0), 200, 10.0);

    // ball.setCoordY(1000);
    // ball.reset(500, 500);
    // ball.update(0.1, 500, new PlayerModel[] {});

    // // Test if the ball is moving
    // assertNotEquals(250, ball.getCoordX());
    // assertNotEquals(250, ball.getCoordY());

    // // Test if the ball correctly computes a bounce with the racket
    // PlayerModel player = new PlayerModel(new Vector2(0, 250), 200, 10.0);
    // ball.setCoordX(1);
    // ball.setCoordY(300);
    // ball.setSpeedDirection(new Vector2(-50, 0));
    // assertEquals(LastAction.RACKET_HIT, ball.update(0.1, 500, new PlayerModel[] {
    // player }));

    // // Test if the ball correctly computes a bounce with the top
    // ball.setCoordX(200);
    // ball.setCoordY(3);
    // ball.setSpeedDirection(new Vector2(50, -50));
    // assertEquals(LastAction.WALL_HIT, ball.update(0.1, 500, new PlayerModel[] {
    // player }));

    // // Test if the ball correctly computes a bounce with the bottom
    // ball.setCoordX(200);
    // ball.setCoordY(497);
    // ball.setSpeedDirection(new Vector2(50, 50));
    // assertEquals(LastAction.WALL_HIT, ball.update(0.1, 500, new PlayerModel[] {
    // player }));
    // }

    /**
     * Test the {@code reset} method of the {@code BallModel} class.
     */
    @Test
    void testReset() {
        System.out.println("Testing reset");
        BallModel ball = new BallModel(new Vector2(0, 0), 200, 600, 10.0);
        ball.setCoordX(1000);
        ball.setCoordY(1000);
        ball.reset(500, 500);
        // Test if the ball is un the center of the screen
        assertEquals(250, ball.getCoordX());
        assertTrue(ball.getCoordY() > 80 && ball.getCoordY() < 420);
        // Test if the ball is moving in a random direction
        assertTrue(isBetween(ball.getSpeedAngle(), -2 * Math.PI, 2 * Math.PI));
    }

    // Helper functions

    /**
     * Returns true if the value is between {@code min} and {@code max}
     * 
     * @param value value to test
     * @param min   minimum value
     * @param max   maximum value
     * @return true if the value is between {@code min} and {@code max}
     */
    private boolean isBetween(double value, double min, double max) {
        return value >= min && value <= max;
    }

    /**
     * Test if the ball is moving in the correct direction after a bounce on a
     * racket.
     * 
     * @param ball            the ball to test
     * @param player          player containing the racket
     * @param comparisonValue {@code 1} if the angle should be bigger than the
     *                        previous one, {@code -1} if it should be smaller and
     *                        {@code 0} if it should be equal
     */
    private void testBounceRacket(BallModel ball, PlayerModel player, int comparisonValue) {
        double angleBeforeBounce = new Vector2(-ball.getSpeedDirectionX(), ball.getSpeedDirectionY()).getAngle();

        ball.computeRacketBounce(new Vector2(0, 0), 0.1, player);

        double angleAfterBounce = ball.getSpeedAngle();

        // Test if the ball is moving on a correct angle
        assertTrue((comparisonValue == 1 && angleAfterBounce >= angleBeforeBounce)
                || (comparisonValue == -1 && angleAfterBounce <= angleBeforeBounce)
                || (comparisonValue == 0 && angleAfterBounce == angleBeforeBounce));
    }
}
