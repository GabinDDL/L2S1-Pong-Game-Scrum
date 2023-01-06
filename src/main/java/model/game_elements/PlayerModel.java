package model.game_elements;

import model.Court;
import model.Vector2;
import model.interfaces.InterfacePlayerModel;
import model.interfaces.InterfaceRacketController.State;
import model.interfaces.InterfaceRacketModel.HitType;

/**
 * This class represents the model of the player. It doesn't include the GUI of
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

    public PlayerModel(boolean isLeft, double width) {
        this(0);
        if (isLeft)
            racket = new RacketModel(new Vector2(0, 0), 0, Court.INITIAL_RACKET_HEIGHT, Court.INITIAL_RACKET_WIDTH);
        else
            racket = new RacketModel(new Vector2(width, 0), 0, Court.INITIAL_RACKET_HEIGHT, Court.INITIAL_RACKET_WIDTH);

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

    public double getCoordX() {
        return getRacket().getCoordX();
    }

    public double getCoordY() {
        return getRacket().getCoordY();
    }

    public double getRacketWidth() {
        return getRacket().getRacketWidth();
    }

    public double getRacketHeight() {
        return getRacket().getRacketHeight();
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

    public void setDecceleration(double d) {
        racket.setDeceleration(d);
    }

    public void setAcceleration(double a) {
        racket.setAcceleration(a);
    }

    public void setMajorSpeed(double mS) {
        racket.setMajorSpeed(mS);
    }

    public void setInitialSpeed(double iS) {
        racket.setInitialSpeed(iS);
    }

    // Methods

    /**
     * @return Returns true if the player is on the left side of the screen.
     */
    public boolean isPlayerLeft() {
        return getRacket().isRacketLeft();
    }

    /**
     * Tests if this Player's amount of points is equal to
     * other Player's amount of points
     * 
     * @param other
     * @return true if the Player's amount of points
     *         is equal to other Player's amount of points;
     *         false otherwise
     */
    public boolean isDraw(PlayerModel other) {
        return points == other.points;
    }

    /**
     * Tests if this Player's amount of points is equal to n
     * 
     * @param n
     * @return true if the Player's amount of points
     *         is equal to n; false otherwise
     */
    public boolean pointsEqualTo(int n) {
        return points == n;
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