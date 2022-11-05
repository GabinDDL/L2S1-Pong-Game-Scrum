package gui.interfaces;

import model.Vector2;
import model.interfaces.InterfaceRacketController.State;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This interface represents a racket in the GUI.
 */
public interface InterfaceRacketGui extends UpdatableGui, ChangeableImage {

    // Getters
    public double getCoordX();

    public double getCoordY();

    public double getSize();

    State getState();

    default Rectangle getRectangle() {
        return ((Rectangle) getShape());
    }

    // Setters

    void setState(State state);

    public void setCoord(Vector2 coords);

    default void setCoordX(double x) {
        setCoord(new Vector2(x, getCoordY()));
    }

    default void setCoordY(double y) {
        setCoord(new Vector2(getCoordX(), y));
    }

    // Methods

    /**
     * Initialises the display of a Racket
     * 
     * @param double scale
     * @param double xMargin
     * @param double thickness
     */
    default void initDisplay(double scale, double xMargin, double thickness) {
        getRectangle().setHeight(getSize() * scale);
        getRectangle().setWidth(thickness);

        ChangeableImage.super.changeColor(Color.RED);

        if (getCoordX() == 0)
            getRectangle().setX(xMargin - thickness);
        else
            getRectangle().setX(getCoordX() * scale + xMargin);

        getRectangle().setY(getCoordY() * scale);
    }

    // Override of UpdatableGui

    /**
     * Updates the display of the racket
     * 
     * @param double scale
     */
    @Override
    default void updateDisplay(double scale, double[] args) {
        getRectangle().setY(getCoordY() * scale);

    }

}
