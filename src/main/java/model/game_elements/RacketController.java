package model.game_elements;

import model.interfaces.InterfaceRacketController;

/**
 * This class represents the racket controller of the game. It handles the
 * racket's movement.
 */
public class RacketController implements InterfaceRacketController {
    private State state = State.IDLE;

    // Getter
    public State getState() {
        return state;
    }

    // Setter
    public void setState(State state) {
        this.state = state;
    }

}
