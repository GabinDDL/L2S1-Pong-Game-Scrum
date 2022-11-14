package model.interfaces;

import model.Vector2;
import model.game_elements.PlayerModel;

/**
 * Interface for the the ball model object.
 */
public interface InterfaceBallModel {

    enum LastAction {
        NONE, RACKET_HIT, WALL_HIT
    }

    /**
     * Computes the new direction of the ball after it bounces off a racket.
     * It adds an angle bonus of pi/12 or -pi/12 to the direction of the ball
     * if the racket is either GOING_UP(pi/12) of GOING_DOWN(-pi/12). The speed of
     * the racket is also added to the ball.
     * 
     * @param nextPosition the next position of the ball.
     * @param deltaT       the time between the current frame and the previous one.
     * @param player       The player who owns the racket.
     */
    void computeRacketBounce(Vector2 nextPosition, double deltaT, PlayerModel player);

    /**
     * Updates the position of the ball
     * 
     * @param deltaT  the time between the current frame and the previous one.
     * @param height  height of the window.
     * @param players list of all players.
     */
    LastAction update(double deltaT, double height, PlayerModel[] players);
}