package gui.interfaces;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.Vector2;

/**
 * This interface represents a ball GUI element.
 */
public interface InterfaceBallGui extends UpdatableGui, ChangeableImage {

    // Getters
    double getCoordX();

    double getCoordY();

    default Circle getCircle() {
        return ((Circle) getShape());
    }

    // Setters
    void setRadius(double radius);

    void setCoord(Vector2 coords);

    /**
     * Initialise l'affichage de la balle dans GameView
     * 
     * @param double scale
     * @param double xMargin
     */
    default void initDisplay(double scale, double xMargin) {
        getCircle().setCenterX(getCoordX() * scale + xMargin);
        getCircle().setCenterY(getCoordY() * scale);
        ChangeableImage.super.changeColor(Color.PINK);
    }

    /**
     * Met à jour l'affichage de la balle dans GameView
     * 
     * @param double scale
     * @param args   [xMargin,radius]
     */
    default void updateDisplay(double scale, double args[]) {

        double xMargin = args[0];
        double radius = args[1];

        setRadius(radius);

        getCircle().setCenterX(getCoordX() * scale + xMargin);
        getCircle().setCenterY(getCoordY() * scale);
    }

    /**
     * Met à jour l'affichage de la balle dans GameView
     * 
     * @param double scale
     * @param double xMargin
     */

    default void updateDisplay(double scale, double xMargin) {
        updateDisplay(scale, new double[] { xMargin, getCircle().getRadius() });
    }

    /**
     * Met à jour l'affichage de la balle dans GameView
     * 
     * @param double scale
     * @param double xMargin
     * @param radius ball radius
     */
    default void updateDisplay(double scale, double xMargin, double radius) {
        updateDisplay(scale, new double[] { xMargin, radius });

    }

}
