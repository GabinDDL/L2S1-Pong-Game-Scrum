package model.game_elements;

import model.Vector2;
import model.interfaces.InterfacePlayerModel;
import model.interfaces.InterfaceRacketController.State;

/**
 * This class represents the model of the player. It doesn't include the gui of
 * the player
 */
public class PlayerModel implements InterfacePlayerModel {

    private RacketModel racket;

    private int score;

    // Constructors

    // Player constructor if we want to associate the Player with his Score later
    public PlayerModel(int score) {
        this.score = score;
    }

    public PlayerModel() {
        this(0);
        racket = new RacketModel(new Vector2(0, 0), 0, 0, 0);
    }

    public PlayerModel(RacketModel racket, int score) {
        this(score);
        this.racket = racket;
    }

    // Getters
    protected RacketModel getRacket() {
        return racket;
    }

    public double getSpeed() {
        return getRacket().getSpeed();
    }

    /**
     * Returns the Player's Score
     */
    public int getScoreValue() {
        return score;
    }

    public double getMajorSpeed() {
        return getRacket().getMajorSpeed();
    }

    public State getState() {
        return getRacket().getState();
    }

    // Setters

    protected void setRacketModel(RacketModel racket) {
        this.racket = racket;
    }

    public void setState(State state) {
        getRacket().setState(state);
    }

    public void setCoord(Vector2 coord) {
        getRacket().setCoord(coord);
    }

    public void setScore(int score) {
        this.score = score;
    }

    // Methods

    public boolean isPlayerLeft() {
        return getRacket().isRacketLeft();
    }

    // Overrides

    // From InterfacePlayerModel

    @Override
    public boolean hitBall(Vector2 ballPosition, Vector2 nextPosition, double ballRadius) {
        return getRacket().hitBall(ballPosition, nextPosition, ballRadius);
    }

    @Override
    public void update(double deltaT, double height, State state) {
        getRacket().update(deltaT, height, state);
    }

    @Override
    public void reset(double height) {
        getRacket().reset(height);
    }

    @Override
    public void incrementScore() {
        score += 1;
    }

}