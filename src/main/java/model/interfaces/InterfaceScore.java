package model.interfaces;

import javafx.scene.control.Label;

/**
 * Interface for the the score object.
 */
public interface InterfaceScore {

    // Getters

    /**
     * Returns the amount of points
     * 
     * @return int points
     */
    public int getPoints();

    /**
     * Returns the associated Label
     * 
     * @return Label
     */
    public Label getLabel();

    // Methods

    /**
     * Increments the amount of points by 1
     * and updates the Label accordingly
     */
    public void incrementScore();

    /**
     * Resets the amount of points to 0
     * and updates the Label accordingly
     */
    public void resetScore();

}
