package model;

import model.Objects.Racket;
import model.interfaces.InterfacePlayer;
import model.interfaces.RacketController;

public class Player implements RacketController, InterfacePlayer {
    // Default state of the player's racket is IDLE
    private State state = State.IDLE;
    private Racket racket;
    private Score score;

    public Player(Racket racket, Score score) {
        this.racket = racket;
        this.score = score;
    }

    public Player() {
        this(null, null);
    }

    // We define the State getter for the player.
    @Override
    public State getState() {
        return state;
    }

    /**
     * Sets the Player's state with the given parameter
     * 
     * @param state
     */
    public void setState(State state) {
        this.state = state;
    }

    public Score getScore() {
        return score;
    }

    public void setRacket(Racket racket) {
        this.racket = racket;
    }

    public Racket getRacket() {
        return racket;
    }

}
