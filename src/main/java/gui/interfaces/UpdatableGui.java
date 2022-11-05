package gui.interfaces;

/**
 * This interface represents a GUI element that can be updated.
 */
public interface UpdatableGui {

    /**
     * Met à jour l'affichage de la balle dans GameView
     * 
     * @param double scale
     * @param args   arguments for the graphical update
     */
    void updateDisplay(double scale, double[] args);

}
