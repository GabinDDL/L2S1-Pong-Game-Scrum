package model.interfaces;

import gui.interfaces.InterfacePlayerGui;

/**
 * Interface for the player class.
 */
public interface InterfacePlayer extends InterfacePlayerGui {

    /**
     * Updates the position and the speed of the racket
     */
    void update(double deltaT, double height);

    /**
     * Resets the position of the racket
     * 
     * @param height
     */
    void resetRacket(double height);

}
