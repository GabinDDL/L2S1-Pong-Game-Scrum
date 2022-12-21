package model.game_elements;

import model.Vector2;
import model.interfaces.InterfacePlayerModel;
import model.interfaces.InterfaceRacketController.State;
import model.interfaces.InterfaceRacketModel.HitType;

/**
 * This class represents the model of the player. It doesn't include the gui of
 * the player
 */
public class PlayerModel implements InterfacePlayerModel {

    private RacketModel racket;

    private int points;

    // Constructors

    // Player constructor if we want to associate the Player with his points later
    public PlayerModel(int points) {
        this.points = points;
    }

    public PlayerModel() {
        this(0);
        racket = new RacketModel(new Vector2(0, 0), 0, 0, 0);
    }

    public PlayerModel(RacketModel racket, int points) {
        this(points);
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
     * Returns the Player's amount of points
     */
    public int getPoints() {
        return points;
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

    public void setPoints(int points) {
        this.points = points;
    }

    // Methods

    /**
     * @return Returns true if the player is on the left side of the screen.
     */
    public boolean isPlayerLeft() {
        return getRacket().isRacketLeft();
    }

    // Overrides

    // From InterfacePlayerModel

    @Override
    public HitType hitBall(Vector2 ballPosition, Vector2 nextPosition, double ballRadius, Vector2 speedDirectionBall) {
        return getRacket().hitBall(ballPosition, nextPosition, ballRadius, speedDirectionBall);
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
    public void incrementPoints() {
        points += 1;
    }

    @Override
    public void resetPoints() {
        points = 0;
    }

}