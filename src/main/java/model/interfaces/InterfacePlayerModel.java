package model.interfaces;

import model.Vector2;
import model.interfaces.InterfaceRacketController.State;

/**
 * Interface for the the player model object.
 */
public interface InterfacePlayerModel {

    boolean hitBall(Vector2 ballPosition, Vector2 nextPosition, double ballRadius);

    void update(double deltaT, double height, State state);

    void reset(double height);

    void incrementScore();

}
