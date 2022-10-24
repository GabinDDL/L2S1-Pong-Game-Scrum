package model.interfaces;

import javafx.scene.control.Label;

public interface InterfaceScore {

    /**
     * Returns the number of points
     * 
     * @return int points
     */
    public int getPoints();

    /**
     * Increments the number of points by 1
     * and updates the Label
     */
    public void incrementScore();

    /**
     * Returns the associated Label
     * 
     * @return Label
     */
    public Label getLabel();

}
