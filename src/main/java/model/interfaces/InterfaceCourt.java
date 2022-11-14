package model.interfaces;

import gui.interfaces.UpdatableGui;

import java.util.List;

/**
 * InterfaceCourt
 */
public interface InterfaceCourt {

    // Getters

    /**
     * @return the Court's width
     */
    public double getWidth();

    /**
     * @return the Court's height
     */
    public double getHeight();

    /**
     * @return list of all court's objects to update
     */
    public List<UpdatableGui> getListObjects();

    // Methods

    /**
     * Updates the Court
     * 
     * @param deltaT
     */
    public void update(double deltaT);

    /**
     * Resets the Court
     */
    public void reset();
}