package model.game_elements;

import gui.game_elements.PlayerGui;
import gui.game_elements.RacketGui;
import gui.game_elements.Score;
import gui.interfaces.InterfacePlayerGui;
import gui.interfaces.UpdatableGui;

import model.Vector2;
import model.interfaces.InterfaceRacketController.State;

import javafx.scene.shape.Shape;

/**
 * This class represents the player of the game. It includes the graphical part
 * of the player and the model of the player.
 */
public class Player extends PlayerModel implements InterfacePlayerGui {

    private PlayerGui playerGui;

    // Constructors

    public Player(Racket racket, Score score) {
        super((RacketModel) racket, score.getPoints());
        this.playerGui = new PlayerGui(racket.getRacketGui(), score);
    }

    public Player(Vector2 coords, double speed, double racketWidth, double racketHeight, Score score) {
        this(new Racket(coords, speed, racketWidth, racketHeight), score);
    }

    public Player() {
        this.playerGui = new PlayerGui();
    }

    // Getters

    @Override
    public UpdatableGui getRacketGui() {
        return playerGui.getRacketGui();
    }

    @Override
    public Shape getShape() {
        return playerGui.getShape();
    }

    @Override
    public Score getScore() {
        return playerGui.getScore();
    }

    public State getState() {
        return playerGui.getState();
    }

    @Override
    public RacketController getRacketController() {
        return playerGui.getRacketController();
    }

    // Setters

    public void setRacket(Racket racket) {
        playerGui.setRacketGui(new RacketGui(racket.getCoord(), racket.getRacketWidth(), racket.getRacketHeight()));
        super.setRacketModel((RacketModel) racket);
    }

    public void setState(State state) {
        playerGui.setState(state);
    }

    public void setScore(Score score) {
        super.setPoints(score.getPoints());
        playerGui.setScore(score);
    }

    public void setPoints(int n) {
        super.setPoints(n);
        playerGui.getScore().setPoints(n);
    }

    // Methods

    public void update(double deltaT, double height) {
        super.update(deltaT, height, playerGui.getState());
        playerGui.getRacketGui().setCoordY(getRacket().getCoordY());
    }

    // Overrides of InterfacePlayerGui

    @Override
    public void initDisplayRacket(double scale, double xMargin, double racketThickness) {
        playerGui.initDisplayRacket(scale, xMargin, racketThickness);
    }

    @Override
    public void incrementScore() {
        super.incrementPoints();
        playerGui.incrementScore();
    }

    @Override
    public void resetScore() {
        super.resetPoints();
        playerGui.resetScore();
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
    public boolean isDraw(Player other) {
        // Criss-cross to check
        // if the Model and the Score's
        // integer value representing
        // the amount of points is in sync

        return this.playerGui.getScore().getPoints() == other.getPoints() &&
                this.getPoints() == other.playerGui.getScore().getPoints();

    }

    /**
     * Tests if this Player's amount of points is equal to n
     * 
     * @param n
     * @return true if the Player's amount of points
     *         is equal to n; false otherwise
     */
    public boolean pointsEqualTo(int n) {
        return playerGui.getScore().getPoints() == n &&
                this.getPoints() == n;

    }
}
