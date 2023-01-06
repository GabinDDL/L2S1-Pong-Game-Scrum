package gui.interfaces;

import gui.game_elements.Score;

import model.game_elements.RacketController;
import model.interfaces.InterfaceRacketController.State;

import javafx.scene.shape.Rectangle;

/**
 * This interface represents a player GUI element.
 * It takes care of the update of both the score and the racket of the player
 */
public interface InterfacePlayerGui extends UpdatableGui, ChangeableImage {

    UpdatableGui getRacketGui();

    Score getScore();

    RacketController getRacketController();

    void incrementScore();

    void resetScore();

    /**
     * Initialize the racket of the player
     * 
     * @param scale
     * @param xMargin
     * @param racketThickness
     */
    void initDisplayRacket(double scale, double xMargin, double racketThickness);

    // Defaults

    // Getters
    default State getState() {
        return getRacketController().getState();
    }

    default Rectangle getRectangle() {
        return ((Rectangle) getShape());
    }

    // Setters
    default void setState(State state) {
        getRacketController().setState(state);
    }

    /**
     * Updates the position and the speed of the racket on the screen
     */
    default void updateDisplay(double scale, double[] args) {
        getRacketGui().updateDisplay(scale, args);
    }

    /**
     * Updates the position and the speed of the racket on the screen
     */
    default void updateDisplay(double scale) {
        getRacketGui().updateDisplay(scale, null);
    }
}
