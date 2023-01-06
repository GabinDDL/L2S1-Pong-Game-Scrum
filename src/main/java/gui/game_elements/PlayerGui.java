package gui.game_elements;

import gui.interfaces.InterfacePlayerGui;

import model.Vector2;
import model.game_elements.RacketController;

import javafx.scene.shape.Shape;

/**
 * This class represents a player in the GUI.
 * It has no knowledge of the game logic.
 * It is only used to display the player.
 */
public class PlayerGui implements InterfacePlayerGui {

    private RacketGui racket;
    private Score score;

    // Constructors
    public PlayerGui(RacketGui racket, Score score) {
        this.racket = racket;
        this.score = score;
    }

    public PlayerGui(Vector2 coords, double racketWidth, double racketHeight, Score score) {
        this(new RacketGui(coords, racketWidth, racketHeight), score);
    }

    public PlayerGui() {
        this.racket = new RacketGui();
    }

    // Getters

    @Override
    public RacketGui getRacketGui() {
        return racket;
    }

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public Shape getShape() {
        return racket.getShape();
    }

    @Override
    public RacketController getRacketController() {
        return racket.getRacketController();
    }

    public Vector2 getCoord() {
        return new Vector2(racket.getCoordX(), racket.getCoordY());
    }

    // Setters

    public void setRacketGui(RacketGui racket) {
        this.racket = racket;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public void setPoints(int points) {
        score.setPoints(points);
    }

    public void setCoord(Vector2 coords) {
        racket.setCoord(coords);
    }

    public void setRacketWidth(double width) {
        racket.setRacketWidth(width);
    }

    public void setRacketHeight(double height) {
        racket.setRacketHeight(height);
    }

    // Methods

    // Overrides from InterfacePlayerGui

    @Override
    public void initDisplayRacket(double scale, double xMargin, double racketThickness) {
        racket.initDisplay(scale, xMargin, racketThickness);
    }

    /**
     * Implements the score by 1 
     */
    @Override
    public void incrementScore() {
        score.incrementScore();
    }

    @Override
    public void resetScore() {
        score.resetScore();
    }

}
