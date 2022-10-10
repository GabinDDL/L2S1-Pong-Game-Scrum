package model;

import model.interfaces.PlayerInterface;
import model.interfaces.RacketController;

public class Player implements RacketController, PlayerInterface {
    // Default state of the palyer's racket is IDLE
    private State state = State.IDLE;
    private Score score;

    // Player constructor if we want to associate the Player with his Score later
    public Player(Score score) {
        this.score = score;
    }

    public Player() {
        this(null);
    }

    // We define the State getter for the player.
    @Override
    public State getState() {
        return state;
    }

    /**
     * Sets the Player's state with the given parameter
     * @param state
     */
    public void setState(State state) {
        this.state = state;
    }

    /* (non-Javadoc)
     * @see model.PlayerInterface#getScore()
     * Returns the Player's Score
     */
    @Override
    public Score getScore() {
        return score;
    }


}

