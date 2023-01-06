package model.interfaces;

/**
 * Interface for the racket controller object.
 */
public interface InterfaceRacketController {

    /**
     * Type that can hold up to 3 different states :
     * GOING_UP, IDLE, GOING_DOWN. These states define the set of all
     * possible states of the Racket.
     */
    enum State {
        GOING_UP, IDLE, GOING_DOWN
    }

    public State getState();

    public void setState(State state);
}
