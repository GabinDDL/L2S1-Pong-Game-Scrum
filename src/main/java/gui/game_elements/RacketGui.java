package gui.game_elements;

import gui.interfaces.InterfaceRacketGui;

import model.Vector2;
import model.game_elements.RacketController;
import model.interfaces.InterfaceRacketController.State;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * This class represents a racket in the GUI.
 * It has no knowledge of the game logic.
 * It is only used to display the racket.
 */
public class RacketGui implements InterfaceRacketGui {

    private Rectangle racket;
    private RacketController racketController;

    // Constructors

    public RacketGui(Vector2 coords, double racketWidth, double racketHeight) {
        racket = new Rectangle(racketWidth, racketHeight);
        racket.setX(coords.getXdir());
        racket.setY(coords.getYdir());

        racketController = new RacketController();
    }

    public RacketGui() {
        this.racketController = new RacketController();
    }

    // Getters

    @Override
    public double getCoordX() {
        return racket.getX();
    }

    @Override
    public double getCoordY() {
        return racket.getY();
    }

    @Override
    public double getSize() {
        return racket.getHeight();
    }

    @Override
    public Shape getShape() {
        return racket;
    }

    public RacketController getRacketController() {
        return racketController;
    }

    @Override
    public State getState() {
        return racketController.getState();
    }

    // Setters

    @Override
    public void setState(State state) {
        racketController.setState(state);
    }

    @Override
    public void setCoord(Vector2 coords) {
        racket.setX(coords.getXdir());
        racket.setY(coords.getYdir());
    }

}
