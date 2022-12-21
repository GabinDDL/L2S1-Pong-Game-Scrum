package model.interfaces;

import model.Vector2;
import model.interfaces.InterfaceRacketController.State;
import model.interfaces.InterfaceRacketModel.HitType;

/**
 * Interface for the the player model object.
 */
public interface InterfacePlayerModel {

    /**
     * Returns true if the ball hits the racket
     * 
     * @param Vector2 ballPosition
     * @param Vector2 nextPosition
     * @param double  ballRadius
     */
    HitType hitBall(Vector2 ballPosition, Vector2 nextPosition, double ballRadius, Vector2 vitesseball);

    /**
     * Updates the position and the speed of the racket
     */
    void update(double deltaT, double height, State state);

    /**
     * Resets the position of the racket
     * 
     * @param height
     */
    void reset(double height);

    /**
     * Increments the amount of points by 1
     */
    void incrementPoints();

    /**
     * Resets the amount of points to 0
     */
    void resetPoints();
}
