package model.interfaces;

import javafx.scene.control.Label;

/**
 * Interface for the the score object.
 */
public interface InterfaceScore {

    // Getters

    /**
     * Returns the number of points
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
     * Increments the number of points by 1
     * and updates the Label
     */
    public void incrementScore();

}
