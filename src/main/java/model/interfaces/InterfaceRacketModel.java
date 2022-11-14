package model.interfaces;

import model.Vector2;

/**
 * Interface for the racket model.
 */
public interface InterfaceRacketModel {

    /**
     * Returns true if the ball hits the racket
     * 
     * @param Vector2 ballPosition
     * @param Vector2 nextPosition
     * @param double  ballRadius
     */
    boolean hitBall(Vector2 ballPosition, Vector2 nextPosition, double ballRadius);
}
