package model.game_elements;

import gui.game_elements.PlayerGui;
import gui.game_elements.RacketGui;
import gui.game_elements.Score;
import gui.interfaces.UpdatableGui;

import model.Vector2;
import model.interfaces.InterfacePlayer;
import model.interfaces.InterfaceRacketController.State;

import javafx.scene.shape.Shape;

/**
 * This class represents the player of the game. It includes the graphical part
 * of the player and the model of the player.
 */
public class Player implements InterfacePlayer {

    private PlayerGui playerGui;
    private PlayerModel playerModel;

    // Constructors

    public Player(Racket racket, Score score) {
        playerModel = new PlayerModel(racket.getRacketModel(), score.getPoints());
        this.playerGui = new PlayerGui(racket.getRacketGui(), score);
    }

    public Player(Vector2 coords, double speed, double racketWidth, double racketHeight, Score score) {
        this(new Racket(coords, speed, racketWidth, racketHeight), score);
    }

    public Player() {
        this.playerGui = new PlayerGui();
        this.playerModel = new PlayerModel();
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

    public int getPoints() {
        return playerModel.getPoints();
    }

    public State getState() {
        return playerGui.getState();
    }

    public PlayerModel getPlayerModel() {
        return playerModel;
    }

    @Override
    public RacketController getRacketController() {
        return playerGui.getRacketController();
    }

    // Setters

    public void setRacket(Racket racket) {
        playerGui.setRacketGui(new RacketGui(racket.getCoord(), racket.getRacketWidth(), racket.getRacketHeight()));
        playerModel.setRacketModel(racket.getRacketModel());
    }

    public void setState(State state) {
        playerGui.setState(state);
    }

    public void setScore(Score score) {

        playerModel.setPoints(score.getPoints());
        playerGui.setScore(score);
    }

    public void setPoints(int n) {
        playerModel.setPoints(n);
        playerGui.getScore().setPoints(n);
    }

    // Methods

    // Overrides of InterfacePlayer

    @Override
    public void update(double deltaT, double height) {
        playerModel.update(deltaT, height, playerGui.getState());
        playerGui.getRacketGui().setCoordY(playerModel.getRacket().getCoordY());
    }

    @Override
    public void initDisplayRacket(double scale, double xMargin, double racketThickness) {
        playerGui.initDisplayRacket(scale, xMargin, racketThickness);
    }

    /**
     * Adds a point to the player's score, both on the graphical and the model part
     */
    @Override
    public void incrementScore() {
        playerModel.incrementPoints();
        playerGui.incrementScore();
    }

    @Override
    public void resetScore() {
        playerModel.resetPoints();
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

    @Override
    public void resetRacket(double height) {
        playerModel.reset(height);
    }
}
