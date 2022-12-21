package model.interfaces;

import model.Vector2;

/**
 * Interface for the racket model.
 */
public interface InterfaceRacketModel {

    /**
     * Enum representing the different ways the ball can hit a racket
     */
    public enum HitType {
        NONE, BALL_HIT_HORIZONTAL_AND_VERTICAL, BALL_HIT_HORIZONTAL, BALL_HIT_VERTICAL;
    }

    /**
     * Returns true if the ball hits the racket
     * 
     * @param Vector2 ballPosition
     * @param Vector2 nextPosition
     * @param double  ballRadius
     */
    HitType hitBall(Vector2 ballPosition, Vector2 nextPosition, double ballRadius, Vector2 speedDirectionBall);
}
